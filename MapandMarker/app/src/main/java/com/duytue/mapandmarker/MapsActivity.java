package com.duytue.mapandmarker;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    MapWrapperLayout mapWrapperLayout;


    View v;
    Button btn;
    OnInterInfoWindowTouchListener buttonClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.mapWrapper);
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
        mapWrapperLayout.init(mMap, this);


        // Add a marker in Sydney and move the camera
        final LatLng hcmus = new LatLng(10.7629472,106.682666);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(hcmus).bearing(90).zoom(15).bearing(0f).build()));

        mMap.addMarker(new MarkerOptions().title("University of Science - 227 Nguyen Van Cu, District 5").position(hcmus));



        v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.info_box, null);
        btn = (Button)v.findViewById(R.id.button);

        buttonClicked = new OnInterInfoWindowTouchListener(btn) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                Log.i("Button clicked", "Clicked");
            }
        };


        btn.setOnTouchListener(buttonClicked);


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {;
                buttonClicked.setMarker(marker);
                TextView nameView = (TextView)v.findViewById(R.id.name);
                TextView locationView = (TextView)v.findViewById(R.id.location);

                nameView.setText(marker.getTitle());
                locationView.setText(Double.toString(hcmus.latitude) + ", " + Double.toString(hcmus.longitude));

                mapWrapperLayout.setMarkerWithInfoWindow(marker, v);
                return v;
            }
        });
    }
}
