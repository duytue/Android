package com.example.duytue.miniproject1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;


    GoogleApiClient mGoogleApiClient;
    String currentAddress;
    Location currentLocation;
    LocationRequest mLocationRequest;
    boolean mRequestingLocationUpdates = true;

    final Handler handler = new Handler();

    Intent i;
    String action;
    public static final int LOCATION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        i = getIntent();
        action = i.getStringExtra("ACTION");

        createToolbar();


        initiateGoogleApiClient();
        createLocationRequest();
    }

    private void initiateGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(15000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                    startLocationUpdates();
                }
            }
        }, 2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    // location methods
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        currentAddress = "";

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        //require internet connection
        try {
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            int n = listAddress.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < n; ++i) {
                currentAddress += listAddress.get(0).getAddressLine(i);
                /*if (i == n - 1)
                {
                    currentAddress += ".";
                }
                else
                    currentAddress += ", ";*/
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




    private void createToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //return to appropriate activity
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Improve experience by letting Miniproject 1 access to your location!").setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    requestPermission();
                }
            });
            builder.create().show();

        }



        // move to designated position
        switch (action) {
            case "SHOW_LOCATION":
                moveToDesignatedPosition();
                break;


            default:
                break;

        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Some location functions will be disabled!")
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    return;
                                }
                            }).create().show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void moveToDesignatedPosition() {
        LatLng latLng = new LatLng(i.getDoubleExtra("Lat", 0), i.getDoubleExtra("Lng", 0));
        mMap.addMarker(new MarkerOptions().position(latLng).title(i.getStringExtra("Name")));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }


    public void toCurrentLocation(View view) {
        if (currentLocation == null) {
            // TODO: Notify user about missing location settings
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Please enable your location setting").setPositiveButton("OK", null);
            builder.create().show();
        } else {
            LatLng temp = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(temp).title("Your location").alpha(244));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp, 16));
        }
    }

    public void findDirection(View view) {
        if (currentLocation == null) {
            // TODO: Notify user about missing location settings
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Enable your location setting to use Direction Mode").setPositiveButton("OK", null);
            builder.create().show();
        } else
        {
           String originLat, originLng, destLat, destLng;
            originLat = Double.toString(currentLocation.getLatitude());
            originLng = Double.toString(currentLocation.getLongitude());

            destLat = Double.toString(i.getDoubleExtra("Lat", 0));
            destLng = Double.toString(i.getDoubleExtra("Lng", 0));
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+originLat+"," + originLng+"&daddr="+destLat+","+destLng));
            startActivity(intent);
        }
    }
}
