package com.kopisenja.floodmonitoring.activity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kopisenja.floodmonitoring.R;

import java.util.ArrayList;

import static com.kopisenja.floodmonitoring.base.FunctionClass.ToastMessage;

public class IndexActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> mLocation = new ArrayList<LatLng>();

    public static final int REQUEST_ACCESS_LOCATION = 10;
    private String mLatitude;
    private String mLongitude;

    private ConstraintLayout mTrueConstraintLayout, mLocationDeniedConstraintLayout, mInternetDeniedConstraintLayout;
    private Button mRetryLocationButton;
    private ImageView mHistoryImageView;
    private BottomSheetDialog mBottomSheetDialog;

    private TextView mCategoryTextview;
    private TextView mLocationTextview;
    private TextView mDateTextview;
    private TextView mTimeTextview;
    private TextView mDebitTextview;
    private TextView mLevelTextview;

    LatLng location1 = new LatLng(-6.975464, 107.633257);
    LatLng location2 = new LatLng(-6.993728, 107.631702);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mTrueConstraintLayout = findViewById(R.id.constraint_index_location_true);
        mLocationDeniedConstraintLayout = findViewById(R.id.constraint_index_location_false);
        mRetryLocationButton = findViewById(R.id.button_index_location_retry);
        mHistoryImageView = findViewById(R.id.imageView_index_history);

        checkLocationPermission();
        intentToHistory();
        initializeBottomSheet();
    }

    private void intentToHistory() {
        mHistoryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(R.layout.bottomsheet_dialog_index);
        mBottomSheetDialog.setCanceledOnTouchOutside(true);

        mCategoryTextview = mBottomSheetDialog.findViewById(R.id.textView_index_category);
        mLocationTextview = mBottomSheetDialog.findViewById(R.id.textView_index_location);
        mDateTextview = mBottomSheetDialog.findViewById(R.id.textView_index_date);
        mTimeTextview = mBottomSheetDialog.findViewById(R.id.textView_index_time);
        mDebitTextview = mBottomSheetDialog.findViewById(R.id.textView_index_debit);
        mLevelTextview = mBottomSheetDialog.findViewById(R.id.textView_index_level);
    }

    private void setLocation() {
        mLocation.add(location1);
        mLocation.add(location2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getCurrentData(int locationDevice) {
        DatabaseReference reference;

        if (locationDevice == 1) {
            reference = FirebaseDatabase.getInstance().getReference().child("Recent").child("Device1");
        } else {
            reference = FirebaseDatabase.getInstance().getReference().child("Recent").child("Device2");
        }

        Query lastQuery = reference.orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey();

                    String date = dataSnapshot.child(key).child("date").getValue(String.class);
                    String time = dataSnapshot.child(key).child("time").getValue(String.class);
                    String location = dataSnapshot.child(key).child("location").getValue(String.class);
                    String debit = dataSnapshot.child(key).child("debit").getValue(String.class);
                    String level = dataSnapshot.child(key).child("level").getValue(String.class);
                    Integer category = dataSnapshot.child(key).child("category").getValue(Integer.class);

                    Log.d("TES_FIREBASE", date + " " + time);

                    if (category == 1) {
                        mCategoryTextview.setText(getString(R.string.category_normal));
                        mCategoryTextview.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else if (category == 2) {
                        mCategoryTextview.setText(getString(R.string.category_standby));
                        mCategoryTextview.setTextColor(getResources().getColor(R.color.colorYellow));
                    } else {
                        mCategoryTextview.setText(getString(R.string.category_danger));
                        mCategoryTextview.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    mLocationTextview.setText(location);
                    mDateTextview.setText(date);
                    mTimeTextview.setText(time);
                    mDebitTextview.setText(debit);
                    mLevelTextview.setText(level);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mBottomSheetDialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < mLocation.size() ; i++) {

            final int indexLocation = i;
            // location0 == 1
            // location1 == 2

            if (indexLocation == 0) {
                mMap.addMarker(new MarkerOptions().position(mLocation.get(i)).title("Bojongsoang"));
            } else {
                mMap.addMarker(new MarkerOptions().position(mLocation.get(i)).title("Radio"));
            }

            int calcutateDistance =  calculateDistance();

            if (calcutateDistance == 1) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 15f));
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location2, 15f));
            }
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng position = marker.getPosition();

                if (position.latitude == location1.latitude && position.longitude == location1.longitude) {
                    getCurrentData(1);
                } else if (position.latitude == location2.latitude && position.longitude == location2.longitude) {
                    getCurrentData(2);
                }

                return false;
            }
        });
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // meminta akses lokasi, karena akses belum ada
            ActivityCompat.requestPermissions(IndexActivity.this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_LOCATION);
        }else{
            // akses sudah ada
            displayIndex(true);
            setLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ACCESS_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // akses disetujui
                    displayIndex(true);
                    setLocation();
                } else {
                    // akses ditolak
                    displayIndex(false);
                }
                break;
        }
    }


    private void displayIndex(boolean isTrue) {
        if (isTrue == true) {
            mTrueConstraintLayout.setVisibility(View.VISIBLE);
            mLocationDeniedConstraintLayout.setVisibility(View.GONE);
            getCurrentLocation();
        } else {
            mTrueConstraintLayout.setVisibility(View.GONE);
            mLocationDeniedConstraintLayout.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location locationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (locationGps != null) {
            double lat = locationGps.getLatitude();
            double longi = locationGps.getLongitude();

            mLatitude = String.valueOf(lat);
            mLongitude = String.valueOf(longi);

            Log.d("CURRENT_LOCATION", mLatitude + " " + mLongitude);
            Log.d("CURRENT_LOCATION", "Jarak Terdekat di Lokasi : " + calculateDistance());

        } else if (locationNetwork != null) {
            double lat = locationNetwork.getLatitude();
            double longi = locationNetwork.getLongitude();

            mLatitude = String.valueOf(lat);
            mLongitude = String.valueOf(longi);

            Log.d("CURRENT_LOCATION", mLatitude + " " + mLongitude);
            Log.d("CURRENT_LOCATION", "Jarak Terdekat di Lokasi : " + calculateDistance());

        } else if (locationPassive != null) {
            double lat = locationPassive.getLatitude();
            double longi = locationPassive.getLongitude();

            mLatitude = String.valueOf(lat);
            mLongitude = String.valueOf(longi);

            Log.d("CURRENT_LOCATION", mLatitude + " " + mLongitude);
            Log.d("CURRENT_LOCATION", "Jarak Terdekat di Lokasi : " + calculateDistance());

        } else {
            Log.e("CURRENT_LOCATION", "cant get current location");
        }
    }

    private int calculateDistance() {
        Location currentLocation = new Location("");
        Location loc1 = new Location("");
        Location loc2 = new Location("");

        currentLocation.setLatitude(Double.parseDouble(mLatitude));
        currentLocation.setLongitude(Double.parseDouble(mLongitude));

        loc1.setLatitude(-6.975464);
        loc1.setLongitude(107.633257);

        loc2.setLatitude(-6.993728);
        loc2.setLongitude(107.631702);

        float distance1 = currentLocation.distanceTo(loc1);
        float distance2 = currentLocation.distanceTo(loc2);

        if (distance1 <= distance2) {
            return 1;
        } else {
            return 2;
        }
    }
}
