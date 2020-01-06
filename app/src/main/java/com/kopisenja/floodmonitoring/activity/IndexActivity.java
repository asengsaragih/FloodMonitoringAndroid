package com.kopisenja.floodmonitoring.activity;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

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

    LatLng location1 = new LatLng(-6.975464, 107.633257);
    LatLng location2 = new LatLng(-6.993728, 107.631702);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setLocation();
    }

    private void setLocation() {
        mLocation.add(location1);
        mLocation.add(location2);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < mLocation.size() ; i++) {
            mMap.addMarker(new MarkerOptions().position(mLocation.get(i)).title("Location"));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(new LatLng(location1, 15f)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 15f));
        }
    }
}
