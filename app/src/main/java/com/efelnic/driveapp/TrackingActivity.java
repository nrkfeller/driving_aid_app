package com.efelnic.driveapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TrackingActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;

    TextView latView;
    TextView lngView;
    TextView altView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        trackCoordinate();






    }

    public void trackCoordinate(){
        latView = (TextView) findViewById(R.id.latitudeView);
        lngView = (TextView) findViewById(R.id.longitudeView);
        altView = (TextView) findViewById(R.id.altitudeView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        int permissionCheck = ContextCompat.checkSelfPermission(TrackingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {

            Toast.makeText(getApplicationContext(), "works", Toast.LENGTH_LONG).show();

            Log.i("Location Info", "Location achieved!");
        } else {

            Log.i("Location Info", "No location :(");
        }
    }



    public void trackAcceleration(){

    }

    public void trackGyroscope(){

    }
    public void trackTime(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        int permissionCheck = ContextCompat.checkSelfPermission(TrackingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        int permissionCheck = ContextCompat.checkSelfPermission(TrackingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Double alt = location.getAltitude();

        Toast.makeText(getApplicationContext(), lat.toString() +alt.toString() +lng.toString(), Toast.LENGTH_LONG).show();

        /*
        altView.setText(Double.toString(alt));
        latView.setText(Double.toString(lat));
        lngView.setText(Double.toString(lng));
        */
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void getLocation(View view ) {

        int permissionCheck = ContextCompat.checkSelfPermission(TrackingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        Location location = locationManager.getLastKnownLocation(provider);

        onLocationChanged(location);
    }
}
