package com.efelnic.driveapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

public class TrackingActivity extends AppCompatActivity implements LocationListener, SensorEventListener {

    LocationManager locationManager;
    String provider;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    TextView accelView;
    float alpha = (float) 0.8;
    float[] gravity = new float[3];
    float[] linear_acceleration = new float[3];

    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;

    TextView latView, lngView, altView, spdView, xrotView, yrotView, zrotView;

    boolean bPermissionGranted;

    LinearLayout la; // used for charts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bPermissionGranted = checkLocationPermission();
        }


        startChronometer();

        startAccel();

        latView = (TextView) findViewById(R.id.latitudeView);
        lngView = (TextView) findViewById(R.id.longitudeView);
        altView = (TextView) findViewById(R.id.altitudeView);
        spdView = (TextView) findViewById(R.id.speedView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Toast.makeText(getApplicationContext(), "works", Toast.LENGTH_LONG).show();
        }

    }
    public void startChronometer() {
        Chronometer c = (Chronometer) findViewById(R.id.chronometer);
        c.setFormat("%s");
        c.start();
    }

    public void startPositionTracking(){
    }

    public void startAccel() {

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyroscope  = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        accelView = (TextView) findViewById(R.id.accelView);
        xrotView = (TextView) findViewById(R.id.xrotationView);;
        yrotView = (TextView) findViewById(R.id.yrotationView);;
        zrotView = (TextView) findViewById(R.id.zrotationView);;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bPermissionGranted = checkLocationPermission();
        }

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        locationManager.requestLocationUpdates(provider, 400, 0, this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bPermissionGranted = checkLocationPermission();
        }

        mSensorManager.unregisterListener(this);

        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Double alt = location.getAltitude();
        float spd = location.getSpeed();


        altView.setText("Altitude : " + Double.toString(alt));
        latView.setText("Latitude : " + Double.toString(lat));
        lngView.setText("Longitude : " + Double.toString(lng));
        spdView.setText("Speed : " + Float.toString(spd));

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

    public void getLocation(View view) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bPermissionGranted = checkLocationPermission();
        }

        Location location = locationManager.getLastKnownLocation(provider);

        onLocationChanged(location);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        accelView.setText("Accel : " + df.format(Math.sqrt(linear_acceleration[0] * linear_acceleration[0] + linear_acceleration[1] * linear_acceleration[1] + linear_acceleration[2] * linear_acceleration[2])));


        xrotView.setText("Orientation X : " + Float.toString(event.values[2]));
        yrotView.setText("Orientation Y : " + Float.toString(event.values[1]));
        zrotView.setText("Orientation Z : " + Float.toString(event.values[0]));

        // Create Charts
        la = (LinearLayout)findViewById(R.id.lchart);
        drawchart(1, 1, (int)linear_acceleration[0]); //float converted to int
        drawchart(2, 2, (int)linear_acceleration[1]); //float converted to int
        drawchart(3, 3, (int)linear_acceleration[2]); //float converted to int


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    // Chart creation function
    private void drawchart(int count, int color, int hight) {
     System.out.println(count+color+hight);
        if(color == 1) {
            color = Color.RED;
        }
        if(color == 2) {
            color = Color.GREEN;
        }
        if(color == 3) {
            color = Color.BLUE;
        }
        for(int k = 1; k<= count; k++) {
            View view = new View(this);
            view.setBackgroundColor(color);
            view.setLayoutParams(new LinearLayout.LayoutParams(30, hight));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();
            params.setMargins(3, 0, 0, 0);
            view.setLayoutParams(params);
            la.addView(view);
        }
    }
}