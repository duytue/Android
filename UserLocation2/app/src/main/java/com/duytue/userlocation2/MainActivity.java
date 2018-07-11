package com.duytue.userlocation2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.Random;


public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener{

    final Handler handler = new Handler();

    GoogleApiClient mGoogleApiClient;
    TextView mLongitudeText, mLatitudeText;
    Location currentLocation;
    //LocationManager locationManager;
    LocationRequest mLocationRequest;
    boolean mRequestingLocationUpdates = false;

    public void changeUpdateState(View view) {
        TextView state = (TextView) findViewById(R.id.stateView);
        if (view.getTag().toString().equals("1")) {
            state.setText("Update ON");
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        }
        else {
            state.setText("Update OFF");
            stopLocationUpdate();
            mRequestingLocationUpdates = false;
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateValuesFromBundle(savedInstanceState);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            Log.i("API Created", "OK");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
        //LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
         //       .addLocationRequest(mLocationRequest);

    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (currentLocation != null) {
            mLatitudeText = (TextView)findViewById(R.id.mLatitudeText);
            mLongitudeText = (TextView)findViewById(R.id.mLongitudeText);
            mLatitudeText.setText(String.valueOf(currentLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(currentLocation.getLongitude()));
        }

        if (mRequestingLocationUpdates) {
            Log.i("Location Request Update", "Yes");
            startLocationUpdates();
        }
        else
        {
            Log.i("Location Request Update", "No");
        }
        Log.i("Location On Connected", "Yes");
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.i("Location Update Start", "Yes");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Location On Resume", "Yes");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                    Log.i("On Resume2", "Yes");
                    startLocationUpdates();
                    //mRequestingLocationUpdates = true;
                }
            }
        }, 2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdate();
        Log.i("Location On Pause", "Yes");
    }

    public void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        //mRequestingLocationUpdates = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        Log.i("Location Changed", "Yes");
        updateUI();
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public int getInverseColor(int color){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, 255-red, 255-green, 255-blue);
    }

    public void updateUI() {
        mLatitudeText = (TextView)findViewById(R.id.mLatitudeText);
        mLongitudeText = (TextView)findViewById(R.id.mLongitudeText);
        mLatitudeText.setText(String.valueOf(currentLocation.getLatitude()));
        mLongitudeText.setText(String.valueOf(currentLocation.getLongitude()));
        int color = getRandomColor();
        mLongitudeText.setBackgroundColor(color);
        mLatitudeText.setBackgroundColor(color);

        color = getInverseColor(color);
        mLongitudeText.setTextColor(color);
        mLatitudeText.setTextColor(color);
    }

    String REQUESTING_LOCATION_UPDATES_KEY, LOCATION_KEY, LAST_UPDATED_TIME_STRING_KEY;
    //This function save the states of the app
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);

        savedInstanceState.putParcelable(LOCATION_KEY, currentLocation);
       // savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and
            // make sure that the Start Updates and Stop Updates buttons are
            // correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                if (mRequestingLocationUpdates)
                    Log.i("Recovered", "Yes");
                else
                    Log.i("Recovered", "No");
                //setButtonsEnabledState();
            }

            // Update the value of mCurrentLocation from the Bundle and update the
            // UI to show the correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that
                // mCurrentLocationis not null.
                currentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            //if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
            //    mLastUpdateTime = savedInstanceState.getString(
            //            LAST_UPDATED_TIME_STRING_KEY);
            //}
            updateUI();
        }
    }
}
