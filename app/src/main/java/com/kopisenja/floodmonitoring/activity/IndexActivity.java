package com.kopisenja.floodmonitoring.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kopisenja.floodmonitoring.R;

import java.util.ArrayList;

public class IndexActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> mLocation = new ArrayList<LatLng>();
    public static final int REQUEST_ACCESS_LOCATION = 10;

    private ConstraintLayout mTrueConstraintLayout, mLocationDeniedConstraintLayout, mInternetDeniedConstraintLayout;
    private Button mRetryLocationButton;

    LatLng location1 = new LatLng(-6.975464, 107.633257);
    LatLng location2 = new LatLng(-6.993728, 107.631702);

    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mTrueConstraintLayout = findViewById(R.id.constraint_index_location_true);
        mLocationDeniedConstraintLayout = findViewById(R.id.constraint_index_location_false);
        mRetryLocationButton = findViewById(R.id.button_index_location_retry);

        checkLocationPermission();
    }

    private void setLocation() {
        mLocation.add(location1);
        mLocation.add(location2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < mLocation.size() ; i++) {
            mMap.addMarker(new MarkerOptions().position(mLocation.get(i)).title("Location"));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(new LatLng(location1, 15f)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 15f));
        }
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
        } else {
            mTrueConstraintLayout.setVisibility(View.GONE);
            mLocationDeniedConstraintLayout.setVisibility(View.VISIBLE);
        }
    }
}
