package com.kopisenja.floodmonitoring.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.adapter.HistoryAdapter;
import com.kopisenja.floodmonitoring.base.FloodData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kopisenja.floodmonitoring.base.FunctionClass.ToastMessage;

public class IndexActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    public static final int REQUEST_ACCESS_LOCATION = 10;
    public double mLatitude;
    public double mLongitude;

    private ConstraintLayout mTrueConstraintLayout, mLocationDeniedConstraintLayout, mInternetDeniedConstraintLayout, mGetLocationConstraintLayout, mGpsFalseConstrainLayout;
    private Button mRetryLocationButton;
    private Button mRetryConnectionButton;
    private Button mGetCurrentLoationButton;
    private Button mRetryGPSButton;
    private BottomSheetDialog mBottomSheetDialog;

    // bottom sheet initialize
    private TextView mTimeTextview;
    private TextView mDetailTextview;
    private TextView mCategoryTextview;
    private TextView mLocationTextview;
    private TextView mDateTextview;
    private TextView mOtherTextview;
    private TextView mLevelTextview;
    private TextView mFlowTextview;
    private TextView mCategoryMessageTextview;
    private LinearLayout mDateTimeLinear;
    private LinearLayout mDetailLinear;
    private LinearLayout mCategoryLinear;
    private ConstraintLayout mDateTimeConstraint;
    private ConstraintLayout mDetailConstraint;
    private ConstraintLayout mCategoryConstraint;
    private ImageView mDateImageView;
    private ImageView mDetailImageView;
    private ImageView mCategoryImageView;

    LatLng location1 = new LatLng(-6.975464, 107.633257);
    LatLng location2 = new LatLng(-6.993728, 107.631702);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mTrueConstraintLayout = findViewById(R.id.constraint_index_location_true);
        mLocationDeniedConstraintLayout = findViewById(R.id.constraint_index_location_false);
        mInternetDeniedConstraintLayout = findViewById(R.id.recycleView_no_connection);
        mGetLocationConstraintLayout = findViewById(R.id.constraint_get_location_false);
        mGpsFalseConstrainLayout = findViewById(R.id.constraint_gps_false);

        mRetryLocationButton = findViewById(R.id.button_index_location_retry);
        mRetryConnectionButton = findViewById(R.id.button_retry_connection);
        mGetCurrentLoationButton = findViewById(R.id.button_get_location_retry);
        mRetryGPSButton = findViewById(R.id.button_gps_retry);


        if (isConnected() == true) {
            mTrueConstraintLayout.setVisibility(View.VISIBLE);
            mLocationDeniedConstraintLayout.setVisibility(View.GONE);
            mInternetDeniedConstraintLayout.setVisibility(View.GONE);
            mGetLocationConstraintLayout.setVisibility(View.GONE);
            mGpsFalseConstrainLayout.setVisibility(View.GONE);

            checkLocationPermission();
//            initializeBottomSheet();
        } else {
            mTrueConstraintLayout.setVisibility(View.GONE);
            mLocationDeniedConstraintLayout.setVisibility(View.GONE);
            mGetLocationConstraintLayout.setVisibility(View.GONE);
            mGpsFalseConstrainLayout.setVisibility(View.GONE);
            mInternetDeniedConstraintLayout.setVisibility(View.VISIBLE);
        }

        reconnectingInternet();
        reconnectingLocation();
        reconnectingGetCurrentLocation();
    }


    // check internet ---------------------------------------------------------------------------------------------------------------------

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }

    private void reconnectingInternet() {
        mRetryConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    mTrueConstraintLayout.setVisibility(View.VISIBLE);
                    mLocationDeniedConstraintLayout.setVisibility(View.GONE);
                    mInternetDeniedConstraintLayout.setVisibility(View.GONE);
                    mGetLocationConstraintLayout.setVisibility(View.GONE);
                    mGpsFalseConstrainLayout.setVisibility(View.GONE);

                    checkLocationPermission();
//                    initializeBottomSheet();
                } else {
                    mTrueConstraintLayout.setVisibility(View.GONE);
                    mLocationDeniedConstraintLayout.setVisibility(View.GONE);
                    mGetLocationConstraintLayout.setVisibility(View.GONE);
                    mGpsFalseConstrainLayout.setVisibility(View.GONE);
                    mInternetDeniedConstraintLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // check location -------------------------------------------------------------------------------------------------------------------------

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // meminta akses lokasi, karena akses belum ada
            changeDisplayAccessLocation(false);
            ActivityCompat.requestPermissions(IndexActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_LOCATION);
        }else{
            // akses sudah ada
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (statusOfGPS == false) {
                mTrueConstraintLayout.setVisibility(View.GONE);
                mLocationDeniedConstraintLayout.setVisibility(View.GONE);
                mInternetDeniedConstraintLayout.setVisibility(View.GONE);
                mGetLocationConstraintLayout.setVisibility(View.GONE);
                mGpsFalseConstrainLayout.setVisibility(View.VISIBLE);

                mRetryGPSButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
            } else {
                changeDisplayAccessLocation(true);
                geCurrentLocation();
            }
        }
    }

    private void changeDisplayAccessLocation(boolean status) {
        if (status == true) {
            mTrueConstraintLayout.setVisibility(View.VISIBLE);
            mLocationDeniedConstraintLayout.setVisibility(View.GONE);
            mInternetDeniedConstraintLayout.setVisibility(View.GONE);
            mGetLocationConstraintLayout.setVisibility(View.GONE);
            mGpsFalseConstrainLayout.setVisibility(View.GONE);
        } else {
            mTrueConstraintLayout.setVisibility(View.GONE);
            mLocationDeniedConstraintLayout.setVisibility(View.VISIBLE);
            mInternetDeniedConstraintLayout.setVisibility(View.GONE);
            mGetLocationConstraintLayout.setVisibility(View.GONE);
            mGpsFalseConstrainLayout.setVisibility(View.GONE);
        }
    }

    private void reconnectingLocation() {
        mRetryLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermission();
            }
        });
    }

    private void geCurrentLocation() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(IndexActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(IndexActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            changeDisplayGetCurrentLocation(true);
                            int latestLocationIndex = locationResult.getLocations().size() - 1;

                            mLatitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            mLongitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            initializeBottomSheet();

                            //mulai ngoding dari sini karena dari sini dapet current lokasi nta
                            Log.d("TAG_LOCATION", String.valueOf(mLatitude) + "  " + mLongitude);
                        } else {
                            changeDisplayGetCurrentLocation(false);
                        }
                    }

                }, Looper.getMainLooper());
    }

    private String mLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location locationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (locationGps != null) {
            double lat = locationGps.getLatitude();
            double longi = locationGps.getLongitude();

            return String.valueOf(lat) + "," + String.valueOf(longi);

        } else if (locationNetwork != null) {
            double lat = locationNetwork.getLatitude();
            double longi = locationNetwork.getLongitude();

            return String.valueOf(lat) + "," + String.valueOf(longi);

        } else if (locationPassive != null) {
            double lat = locationPassive.getLatitude();
            double longi = locationPassive.getLongitude();

            return String.valueOf(lat) + "," + String.valueOf(longi);

        } else {
            return "";
        }
    }


    private void changeDisplayGetCurrentLocation(boolean status) {
        if (status == true) {
            mTrueConstraintLayout.setVisibility(View.VISIBLE);
            mLocationDeniedConstraintLayout.setVisibility(View.GONE);
            mInternetDeniedConstraintLayout.setVisibility(View.GONE);
            mGetLocationConstraintLayout.setVisibility(View.GONE);
            mGpsFalseConstrainLayout.setVisibility(View.GONE);
        } else {
            mTrueConstraintLayout.setVisibility(View.GONE);
            mLocationDeniedConstraintLayout.setVisibility(View.GONE);
            mInternetDeniedConstraintLayout.setVisibility(View.GONE);
            mGpsFalseConstrainLayout.setVisibility(View.GONE);
            mGetLocationConstraintLayout.setVisibility(View.VISIBLE);
        }
    }

    private void reconnectingGetCurrentLocation() {
        mGetCurrentLoationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geCurrentLocation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (statusOfGPS == false) {
            mTrueConstraintLayout.setVisibility(View.GONE);
            mLocationDeniedConstraintLayout.setVisibility(View.GONE);
            mInternetDeniedConstraintLayout.setVisibility(View.GONE);
            mGetLocationConstraintLayout.setVisibility(View.GONE);
            mGpsFalseConstrainLayout.setVisibility(View.VISIBLE);

            mRetryGPSButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
        } else {
            mTrueConstraintLayout.setVisibility(View.VISIBLE);
            mLocationDeniedConstraintLayout.setVisibility(View.GONE);
            mInternetDeniedConstraintLayout.setVisibility(View.GONE);
            mGetLocationConstraintLayout.setVisibility(View.GONE);
            mGpsFalseConstrainLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ACCESS_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // akses disetujui
                    changeDisplayAccessLocation(true);
                    geCurrentLocation();
                } else {
                    // akses ditolak
                    changeDisplayAccessLocation(false);
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference().child("Marker");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final HashMap<LatLng, String> mapp = new HashMap<LatLng, String>();
                HashMap<LatLng, Double> distance = new HashMap<LatLng, Double>();

                for (final DataSnapshot value : dataSnapshot.getChildren()) {
                    final String key = value.getKey();
                    String name = dataSnapshot.child(key).child("name").getValue(String.class);
                    double longitude = dataSnapshot.child(key).child("longitude").getValue(Double.class);
                    double latitude = dataSnapshot.child(key).child("latitude").getValue(Double.class);
                    int status = dataSnapshot.child(key).child("status").getValue(Integer.class);
                    if (status == 1) {
                        LatLng databaseLocation = new LatLng(latitude, longitude);
                        mapp.put(databaseLocation, key);
                        mMap.addMarker(new MarkerOptions().position(databaseLocation).title(name));

                        distance.put(databaseLocation, calculateDistance(databaseLocation));
                    }
                }

                Map.Entry<LatLng, Double> minDistance = null;

                for (Map.Entry<LatLng, Double> entry : distance.entrySet()) {
                    if (minDistance == null || minDistance.getValue() > entry.getValue()) {
                        minDistance = entry;
                    }
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(minDistance.getKey(), 15f));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        onMarkerMapsClicked(mapp.get(marker.getPosition()), marker.getTitle());
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private double calculateDistance(LatLng latLng) {
        String locationRaw;
        String currentLocation = mLocation();
        String backupLocation = "-6.922108,107.606670";

        if (currentLocation == "") {
            locationRaw = backupLocation;
        } else {
            locationRaw = currentLocation;
        }

        String[] splitRaw = locationRaw.split(",");

        Double lat1 = Double.parseDouble(splitRaw[0]);
        Double long1 = Double.parseDouble(splitRaw[1]);

        Double lat2 = latLng.latitude;
        Double long2 = latLng.longitude;

        double theta = long1 - long2;
        double dist =
                Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));

        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return dist;
    }


//  Botton Sheet Dialog-----------------------------------------------------------------------------------------------------------------------------------------
    
    private void initializeBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(this, R.style.TransparentDialog);
        mBottomSheetDialog.setContentView(R.layout.bottomsheet_dialog_index);
        mBottomSheetDialog.setCanceledOnTouchOutside(true);

        mCategoryTextview = mBottomSheetDialog.findViewById(R.id.bottom_sheet_category_textview);
        mLocationTextview = mBottomSheetDialog.findViewById(R.id.textView_bottom_sheet_location);
        mDateTextview = mBottomSheetDialog.findViewById(R.id.textView_bottom_sheet_date);
        mTimeTextview = mBottomSheetDialog.findViewById(R.id.bottom_sheet_time_textview);
        mDetailTextview = mBottomSheetDialog.findViewById(R.id.bottom_sheet_detail_textview);
        mOtherTextview = mBottomSheetDialog.findViewById(R.id.textView_bottom_sheet_other);
        mLevelTextview = mBottomSheetDialog.findViewById(R.id.textView_bottom_sheet_level);
        mFlowTextview = mBottomSheetDialog.findViewById(R.id.textView_bottom_sheet_flow);
        mCategoryMessageTextview = mBottomSheetDialog.findViewById(R.id.textView_bottom_sheet_category);

        mDateTimeLinear = mBottomSheetDialog.findViewById(R.id.linear_dateTime);
        mDetailLinear = mBottomSheetDialog.findViewById(R.id.linear_detail);
        mCategoryLinear = mBottomSheetDialog.findViewById(R.id.linear_category);

        mDateTimeConstraint = mBottomSheetDialog.findViewById(R.id.bottom_sheet_dateTime);
        mDetailConstraint = mBottomSheetDialog.findViewById(R.id.bottom_sheet_detail);
        mCategoryConstraint = mBottomSheetDialog.findViewById(R.id.bottom_sheet_category);

        mDateImageView = mBottomSheetDialog.findViewById(R.id.bottom_sheet_location_textview);
        mDetailImageView = mBottomSheetDialog.findViewById(R.id.bottom_sheet_imageview_detail);
        mCategoryImageView = mBottomSheetDialog.findViewById(R.id.bottom_sheet_imageview_category);
    }

    private void onMarkerMapsClicked(final String keyMarker, final String titleMaps) {
        final Intent i = new Intent(getApplicationContext(), HistoryActivity.class);
        i.putExtra("CODE_LOCATION", keyMarker);
        i.putExtra("NAME_LOCATION", titleMaps);

        mOtherTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i);
            }
        });

        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("Marker").child(keyMarker).child("recent");
        Query query = reference.orderByChild("status").equalTo(1).limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    String key = value.getKey();

                    String date = dataSnapshot.child(key).child("date").getValue(String.class);
                    String time = dataSnapshot.child(key).child("time").getValue(String.class);
                    String debit = dataSnapshot.child(key).child("debit").getValue(String.class);
                    String level = dataSnapshot.child(key).child("level").getValue(String.class);
                    Integer category = dataSnapshot.child(key).child("category").getValue(Integer.class);

                    mTimeTextview.setText(time + " WIB");
                    mDetailTextview.setText(level + " " + debit);

                    if (category == 1) {
                        mCategoryTextview.setText(getString(R.string.category_normal));
                        mCategoryMessageTextview.setText(getString(R.string.bottom_sheet_text_first) + " " + getString(R.string.category_normal) + "\n" + getString(R.string.bottom_sheet_text_last_normal));
                    } else if (category == 2) {
                        mCategoryTextview.setText(getString(R.string.category_standby));
                        mCategoryMessageTextview.setText(getString(R.string.bottom_sheet_text_first) + " " + getString(R.string.category_standby) + "\n" + getString(R.string.bottom_sheet_text_last_standby));
                    } else {
                        mCategoryTextview.setText(getString(R.string.category_danger));
                        mCategoryMessageTextview.setText(getString(R.string.bottom_sheet_text_first) + " " + getString(R.string.category_danger) + "\n" + getString(R.string.bottom_sheet_text_last_danger));
                    }

                    mLocationTextview.setText(titleMaps);
                    mDateTextview.setText(date);

                    mLevelTextview.setText("Tinggi Air : " + level);
                    mFlowTextview.setText("Debit Air : " + debit);

                    mDateTimeConstraint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDateTimeLinear.setVisibility(View.VISIBLE);
                            mDetailLinear.setVisibility(View.GONE);
                            mCategoryLinear.setVisibility(View.GONE);

                            mDateImageView.setImageResource(R.drawable.ic_time_24dp);
                            mDetailImageView.setImageResource(R.drawable.ic_water_dark_24dp);
                            mCategoryImageView.setImageResource(R.drawable.ic_warning_dark_24dp);

                            mDateTimeConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteBlue));
                            mDetailConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));
                            mCategoryConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));

                            mTimeTextview.setTextColor(getResources().getColor(R.color.colorTextWhite));
                            mDetailTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
                            mCategoryTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
                        }
                    });

                    mDetailConstraint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDateTimeLinear.setVisibility(View.GONE);
                            mDetailLinear.setVisibility(View.VISIBLE);
                            mCategoryLinear.setVisibility(View.GONE);

                            mDateImageView.setImageResource(R.drawable.ic_time_dark_24dp);
                            mDetailImageView.setImageResource(R.drawable.ic_water_24dp);
                            mCategoryImageView.setImageResource(R.drawable.ic_warning_dark_24dp);

                            mDateTimeConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));
                            mDetailConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteBlue));
                            mCategoryConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));

                            mTimeTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
                            mDetailTextview.setTextColor(getResources().getColor(R.color.colorTextWhite));
                            mCategoryTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
                        }
                    });

                    mCategoryConstraint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDateTimeLinear.setVisibility(View.GONE);
                            mDetailLinear.setVisibility(View.GONE);
                            mCategoryLinear.setVisibility(View.VISIBLE);

                            mDateImageView.setImageResource(R.drawable.ic_time_dark_24dp);
                            mDetailImageView.setImageResource(R.drawable.ic_water_dark_24dp);
                            mCategoryImageView.setImageResource(R.drawable.ic_warning_24dp);

                            mDateTimeConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));
                            mDetailConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));
                            mCategoryConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteBlue));

                            mTimeTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
                            mDetailTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
                            mCategoryTextview.setTextColor(getResources().getColor(R.color.colorTextWhite));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBottomSheetDialog.show();
    }

//    Menu-----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
            case R.id.action_full_marker:
                startActivity(new Intent(getApplicationContext(), OtherMarkerActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
