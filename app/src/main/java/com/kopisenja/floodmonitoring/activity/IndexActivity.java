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

import static com.kopisenja.floodmonitoring.base.FunctionClass.ToastMessage;

public class IndexActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private ArrayList<LatLng> mLocation = new ArrayList<LatLng>();

    public static final int REQUEST_ACCESS_LOCATION = 10;
    private double mLatitude;
    private double mLongitude;

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
                            mLatitude += locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            mLongitude += locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            initializeBottomSheet();

                            //mulai ngoding dari sini karena dari sini dapet current lokasi nta
                            Log.d("TAG_LOCATION", String.valueOf(mLatitude) + "  " + mLongitude);
                        } else {
                            changeDisplayGetCurrentLocation(false);
                        }
                    }

                }, Looper.getMainLooper());


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

                for (final DataSnapshot value : dataSnapshot.getChildren()) {
                    final String key = value.getKey();
                    String name = dataSnapshot.child(key).child("name").getValue(String.class);
                    double longitude = dataSnapshot.child(key).child("longitude").getValue(Double.class);
                    double latitude = dataSnapshot.child(key).child("latitude").getValue(Double.class);
                    int status = dataSnapshot.child(key).child("status").getValue(Integer.class);
                    if (status == 1) {
                        LatLng databaseLocation = new LatLng(longitude, latitude);
                        mapp.put(databaseLocation, key);
                        mMap.addMarker(new MarkerOptions().position(databaseLocation).title(name));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(databaseLocation, 13f));
                    }
                }

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

//    private void intentToHistory(int location) {
//        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
//
//        if (location == 1) {
//            intent.putExtra("CODE_LOCATION", "1");
//        } else {
//            intent.putExtra("CODE_LOCATION", "2");
//        }
//
//        startActivity(intent);
//    }
//

//
//    private void setLocation() {
//        mLocation.add(location1);
//        mLocation.add(location2);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    private void getCurrentData(int locationDevice) {
//        DatabaseReference reference;
//
//        final Intent i = new Intent(getApplicationContext(), HistoryActivity.class);
//
//        if (locationDevice == 1) {
//            reference = FirebaseDatabase.getInstance().getReference().child("Recent").child("Device1");
//            i.putExtra("CODE_LOCATION", "1");
//        } else {
//            reference = FirebaseDatabase.getInstance().getReference().child("Recent").child("Device2");
//            i.putExtra("CODE_LOCATION", "2");
//        }
//
//        mOtherTextview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(i);
//            }
//        });
//
//        Query lastQuery = reference.orderByKey().limitToLast(1);
//        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    String key = data.getKey();
//                    String date = dataSnapshot.child(key).child("date").getValue(String.class);
//                    String time = dataSnapshot.child(key).child("time").getValue(String.class);
//                    String location = dataSnapshot.child(key).child("location").getValue(String.class);
//                    String debit = dataSnapshot.child(key).child("debit").getValue(String.class);
//                    String level = dataSnapshot.child(key).child("level").getValue(String.class);
//                    Integer category = dataSnapshot.child(key).child("category").getValue(Integer.class);
//
//                    mTimeTextview.setText(time + " WIB");
//                    mDetailTextview.setText(level + " " + debit);
//
//                    if (category == 1) {
//                        mCategoryTextview.setText(getString(R.string.category_normal));
//                        mCategoryMessageTextview.setText(getString(R.string.bottom_sheet_text_first) + " " + getString(R.string.category_normal) + "\n" + getString(R.string.bottom_sheet_text_last_normal));
//                    } else if (category == 2) {
//                        mCategoryTextview.setText(getString(R.string.category_standby));
//                        mCategoryMessageTextview.setText(getString(R.string.bottom_sheet_text_first) + " " + getString(R.string.category_standby) + "\n" + getString(R.string.bottom_sheet_text_last_standby));
//                    } else {
//                        mCategoryTextview.setText(getString(R.string.category_danger));
//                        mCategoryMessageTextview.setText(getString(R.string.bottom_sheet_text_first) + " " + getString(R.string.category_danger) + "\n" + getString(R.string.bottom_sheet_text_last_danger));
//                    }
//
//                    mLocationTextview.setText(location);
//                    mDateTextview.setText(date);
//
//                    mLevelTextview.setText("Tinggi Air : " + level);
//                    mFlowTextview.setText("Debit Air : " + debit);
//
//
//                    mDateTimeConstraint.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            mDateTimeLinear.setVisibility(View.VISIBLE);
//                            mDetailLinear.setVisibility(View.GONE);
//                            mCategoryLinear.setVisibility(View.GONE);
//
//                            mDateImageView.setImageResource(R.drawable.ic_time_24dp);
//                            mDetailImageView.setImageResource(R.drawable.ic_water_dark_24dp);
//                            mCategoryImageView.setImageResource(R.drawable.ic_warning_dark_24dp);
//
//                            mDateTimeConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteBlue));
//                            mDetailConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));
//                            mCategoryConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));
//
//                            mTimeTextview.setTextColor(getResources().getColor(R.color.colorTextWhite));
//                            mDetailTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
//                            mCategoryTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
//                        }
//                    });
//
//                    mDetailConstraint.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            mDateTimeLinear.setVisibility(View.GONE);
//                            mDetailLinear.setVisibility(View.VISIBLE);
//                            mCategoryLinear.setVisibility(View.GONE);
//
//                            mDateImageView.setImageResource(R.drawable.ic_time_dark_24dp);
//                            mDetailImageView.setImageResource(R.drawable.ic_water_24dp);
//                            mCategoryImageView.setImageResource(R.drawable.ic_warning_dark_24dp);
//
//                            mDateTimeConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));
//                            mDetailConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteBlue));
//                            mCategoryConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));
//
//                            mTimeTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
//                            mDetailTextview.setTextColor(getResources().getColor(R.color.colorTextWhite));
//                            mCategoryTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
//                        }
//                    });
//
//                    mCategoryConstraint.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            mDateTimeLinear.setVisibility(View.GONE);
//                            mDetailLinear.setVisibility(View.GONE);
//                            mCategoryLinear.setVisibility(View.VISIBLE);
//
//                            mDateImageView.setImageResource(R.drawable.ic_time_dark_24dp);
//                            mDetailImageView.setImageResource(R.drawable.ic_water_dark_24dp);
//                            mCategoryImageView.setImageResource(R.drawable.ic_warning_24dp);
//
//                            mDateTimeConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));
//                            mDetailConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteGray));
//                            mCategoryConstraint.setBackgroundColor(getResources().getColor(R.color.colorPaletteBlue));
//
//                            mTimeTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
//                            mDetailTextview.setTextColor(getResources().getColor(R.color.colorTextDark));
//                            mCategoryTextview.setTextColor(getResources().getColor(R.color.colorTextWhite));
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        mBottomSheetDialog.show();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.info_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_about:
//                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
//                return true;
//            case R.id.action_full_marker:
//                startActivity(new Intent(getApplicationContext(), OtherMarkerActivity.class));
//                return true;
//            case R.id.action_other_marker:
//                changeCameraMove();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void changeCameraMove() {
//        LatLng loc1 = new LatLng(-6.975464015049101, 107.63325713574885);
//        LatLng loc2 = new LatLng(-6.993728086127658, 107.63170212507248);
//
//        if (mMap.getCameraPosition().target.equals(loc1)) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location2, 15f));
//            //abis ini di matiin cuma buat espresso doang
//            getCurrentData(2);
//
//        } else {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 15f));
//            //abis ini di matiin cuma buat espresso doang
//            getCurrentData(1);
//        }
//    }





//    private void displayIndex(boolean isTrue) {
//        if (isTrue == true) {
//            mTrueConstraintLayout.setVisibility(View.VISIBLE);
//            mLocationDeniedConstraintLayout.setVisibility(View.GONE);
////            getCurrentLocation();
//        } else {
//            mTrueConstraintLayout.setVisibility(View.GONE);
//            mLocationDeniedConstraintLayout.setVisibility(View.VISIBLE);
//        }
//    }

//    @SuppressLint("MissingPermission")
//    private void getCurrentLocation() {
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        Location locationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//
//        if (locationGps != null) {
//            double lat = locationGps.getLatitude();
//            double longi = locationGps.getLongitude();
//
//            mLatitude = String.valueOf(lat);
//            mLongitude = String.valueOf(longi);
//
//            Log.d("CURRENT_LOCATION", mLatitude + " " + mLongitude);
//            Log.d("CURRENT_LOCATION", "Jarak Terdekat di Lokasi : " + calculateDistance());
//
//        } else if (locationNetwork != null) {
//            double lat = locationNetwork.getLatitude();
//            double longi = locationNetwork.getLongitude();
//
//            mLatitude = String.valueOf(lat);
//            mLongitude = String.valueOf(longi);
//
//            Log.d("CURRENT_LOCATION", mLatitude + " " + mLongitude);
//            Log.d("CURRENT_LOCATION", "Jarak Terdekat di Lokasi : " + calculateDistance());
//
//        } else if (locationPassive != null) {
//            double lat = locationPassive.getLatitude();
//            double longi = locationPassive.getLongitude();
//
//            mLatitude = String.valueOf(lat);
//            mLongitude = String.valueOf(longi);
//
//            Log.d("CURRENT_LOCATION", mLatitude + " " + mLongitude);
//            Log.d("CURRENT_LOCATION", "Jarak Terdekat di Lokasi : " + calculateDistance());
//
//        } else {
//            Log.e("CURRENT_LOCATION", "cant get current location");
//        }
//    }

//    private int calculateDistance() {
//        Location currentLocation = new Location("");
//        Location loc1 = new Location("");
//        Location loc2 = new Location("");
//
////        Log.d("LOCATION", mLatitude);
////        Log.d("LOCATION", mLongitude);
//
////        LatLng location1 = new LatLng(-6.975464, 107.633257);
////        LatLng location2 = new LatLng(-6.993728, 107.631702);
//
////        currentLocation.setLatitude(107.631702);
////        currentLocation.setLongitude(-6.993728);
//
//        currentLocation.setLatitude(Double.parseDouble(mLatitude));
//        currentLocation.setLongitude(Double.parseDouble(mLongitude));
//
//        loc1.setLatitude(-6.975464);
//        loc1.setLongitude(107.633257);
//
//        loc2.setLatitude(-6.993728);
//        loc2.setLongitude(107.631702);
//
//        float distance1 = currentLocation.distanceTo(loc1);
//        float distance2 = currentLocation.distanceTo(loc2);
//
//        if (distance1 <= distance2) {
//            return 1;
//        } else {
//            return 2;
//        }
//    }


}
