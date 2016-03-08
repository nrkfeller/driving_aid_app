package com.efelnic.driveapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TrackingActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;
    Location location;
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
        latView = (TextView)findViewById(R.id.latitudeView);
        lngView = (TextView)findViewById(R.id.longitudeView);
        altView  =(TextView)findViewById(R.id.altitudeView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                provider = locationManager.getBestProvider(new Criteria(), false);
                location = locationManager.getLastKnownLocation(provider);
            }
            return;
        }

        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Log.i("LocationInfo", "Location Achieved");
        } else {
            Log.i("LocationInfo", "No Location");
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                locationManager.requestLocationUpdates(provider, 400, 1, this);
            }
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                locationManager.removeUpdates(this);
            }
            return;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Double alt = location.getAltitude();

        Log.i("Location", "Latitude : " + lat.toString());
        Log.i("Location", "Longitude : " + lng.toString());
        Log.i("Location", "Altitude : " + alt.toString());
        altView.setText(Double.toString(alt));
        latView.setText(Double.toString(lat));
        lngView.setText(Double.toString(lng));
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
}
