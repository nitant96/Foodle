package com.example.nitantsood.buyer_serverapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class PoolLocationSetFinder extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    Marker marker;
    Route mroute;
    ImageButton searchButton;
    EditText searchText;
    String origin,destination;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_location_set_finder);

        searchText=(EditText) findViewById(R.id.mapSearchBar);
        searchButton=(ImageButton) findViewById(R.id.searchMapButton);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin="Lado Sarai";
                destination=searchText.getText().toString();
                try {
                    new DirectionFinder(PoolLocationSetFinder.this, origin, destination,0).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent=new Intent();
                intent.putExtra("address",mroute.endAddress);
                intent.putExtra("lat",mroute.endLocation.latitude);
                intent.putExtra("lng",mroute.endLocation.longitude);
                setResult(RESULT_OK,intent);
                finish();
                return true;
            }
        });
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route, int position) {
        progressDialog.dismiss();
        for(Route route1:route) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route1.endLocation, 14));

            marker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(route1.endAddress)
                    .position(route1.endLocation));
            marker.setTag(route1);
            mroute=route1;
        }
    }
}
