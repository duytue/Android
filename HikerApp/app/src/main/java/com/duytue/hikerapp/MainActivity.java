package com.duytue.hikerapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener {


    final Handler handler = new Handler();

    GoogleApiClient mGoogleApiClient;
    TextView mLongitudeText, mLatitudeText, mBearing, mSpeed, mAddress, mAltitude, mAccuracy;
    ProgressBar progressBar;
    String currentAddress;
    Location currentLocation;
    LocationRequest mLocationRequest;
    boolean mRequestingLocationUpdates = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
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
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
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

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        //updateUI();
        startLocationUpdates();
    }

    protected void updateUI() {
        mLongitudeText = (TextView) findViewById(R.id.longtitudeView);
        mLatitudeText = (TextView) findViewById(R.id.latitudeView);
        mBearing = (TextView)findViewById(R.id.bearingView);
        mAltitude = (TextView)findViewById(R.id.altitudeView);
        mSpeed = (TextView)findViewById(R.id.speedView);
        mAccuracy = (TextView)findViewById(R.id.accuracyView);
        mAddress = (TextView)findViewById(R.id.addressView);

        mLongitudeText.setText("Longtitude: " + Double.toString(currentLocation.getLongitude()));
        mLatitudeText.setText("Latitude: " + Double.toString(currentLocation.getLatitude()));
        mBearing.setText("Bearing: " + Float.toString(currentLocation.getBearing()));
        mAltitude.setText("Altitude: " + Double.toString(currentLocation.getAltitude()));
        mSpeed.setText("Speed: " + Float.toString(currentLocation.getSpeed()) + "m/s");
        mAccuracy.setText("Accuracy: " + Float.toString(currentLocation.getAccuracy()) + "m");
        mAddress.setText(currentAddress);

        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        currentAddress = "";

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        //require internet connection
        try {
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddress != null && listAddress.size() > 0) {
                Log.i("Address", listAddress.get(0).toString());
            }

            int n = listAddress.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < n; ++i) {
                currentAddress += listAddress.get(0).getAddressLine(i);
                if (i == n - 1)
                {
                    currentAddress += ".";
                }
                else
                    currentAddress += ", ";
            }

            Log.i("Address", currentAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateUI();
    }


    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
}
