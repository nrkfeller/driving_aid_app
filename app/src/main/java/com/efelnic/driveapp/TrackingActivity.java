/*REFERENCES: Thank you to Philipp Jahoda and Anton Danshin,  who created these libraries for letting us use them in our app!

Using libraries from
https://github.com/PhilJay/MPAndroidChart - Philipp Jahoda
https://github.com/ntoskrnl/AndroidWidgets - Anton Danshin
*/

package com.efelnic.driveapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;


//import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

public class TrackingActivity extends ScriptActivity implements LocationListener, SensorEventListener, OnChartValueSelectedListener {


    //ProximitySensor
    SensorManager sm;
    Sensor proximitySensor;
    int newCount = 0;


    Chronometer c;
    long totalTimeNow = 0;

    //Location & Permission Vars
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10; // in Meters

    boolean bPermissionGranted;
    LocationManager locationManager;
    String provider;

    //Database Vars
    DatabaseHelper myDb;
    ArrayList<String> accelerationList;
    ArrayList<String> XaccelList;
    ArrayList<String> YaccelList;
    ArrayList<String> ZaccelList;
    ArrayList<String> speedList;
    ArrayList<String> lapTimesList;
    String distValue;


    Gson gsonAccel = new Gson();
    Gson gsonXAccel = new Gson();
    Gson gsonYAccel = new Gson();
    Gson gsonZAccel = new Gson();
    Gson gsonSpeed = new Gson();
    Gson gsonLapTimes = new Gson();
    Gson gsonDistance = new Gson();
    Gson gsonAvgSpeed = new Gson();
    Gson gsonAvgLapTime = new Gson();
    Gson gsonMaxLinAccel = new Gson();
    Gson gsonMaxXAccel = new Gson();
    Gson gsonMaxYAccel = new Gson();
    Gson gsonMaxZAccel = new Gson();
    Gson gsonMaxSpeed = new Gson();
    Gson gsonAvgXAcc = new Gson();
    Gson gsonAvgYAcc = new Gson();
    Gson gsonAvgZAcc = new Gson();
    Gson gsonAvgLinAcc = new Gson();


    double dist = 0;
    //    double curLat = 0;
//    double curLng = 0;
//    double lastLat = 0;
//    double lastLng = 0;


    private Location lastLocation;

    double curLat, curLng, lastLat, lastLng;


    //Sensor Vars
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;

    float alpha = (float) 0.8;
    float[] gravity = new float[3];
    float[] linear_acceleration = new float[3];

    //Different View Vars
    TextView gpsTitle, latView, lngView, altView, spdView, //gps + speed
            timerTitle, timerView, previousLapView, currentLapView, lapChronoView, lapView, chronoView, // time + lap
            accTitle, accView, // lin accel
            compAccTitle, xrotView, yrotView, zrotView; // componential accel
    View lineGraphView, speedometerView;//Line graph
    ScrollView mainLayout;

    //Conversion &
    private final static double CONVERSION_METERSPERSECOND_TO_KMH = 3.6; //getSpeed returns the speed in m/s, so multiply by 3.6 to get km/h
    private final static double CONVERSION_KMH_TO_MPH = 0.62137; // KM/H to MPH

    //Settings Vars
    boolean gpsUISetting,digitalSpeedUISetting, accelUISetting, timerUISetting, lineGraphUISetting, speedometerUISetting, speedUnitSetting, backgroundColorSetting;
    String gpsTextSizeSetting, accelTextSizeSetting, chronTextSizeSetting, speedometerTextSizeSetting, digitalSpeedTextSizeSetting, speedometerMaxValueSetting;

    //LineChart and Speedometer Vars.
    private SpeedometerGauge speedometer;
    private LineChart mChart;
    double time = 0; //used to add x-values in addEntry (for line chart)

    //Not Used
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;

//Database Methods
    public void SaveRace(MenuItem item) {

        String inputAccel = gsonAccel.toJson(accelerationList);
        String inputXAccel = gsonXAccel.toJson(XaccelList);
        String inputYAccel = gsonYAccel.toJson(YaccelList);
        String inputZAccel = gsonZAccel.toJson(ZaccelList);
        String inputSpeed = gsonSpeed.toJson(speedList);

        if (inputSpeed == null){
            inputSpeed = 0 + "";
        }

        String inputDist = gsonDistance.toJson(distValue);
        String inputLapTimes = gsonLapTimes.toJson(lapTimesList);



        //Add average speed
        float avgSpeed = calculateAverage(speedList);
        String inputAverageSpeed = gsonAvgSpeed.toJson((avgSpeed));
        float maxSpeed;
        //Add Max speed
        if (speedList.size() == 0)
            maxSpeed = 0;

        else
            maxSpeed = Float.valueOf(Collections.max(speedList));
        String inputMaxSpeed= gsonMaxSpeed.toJson(maxSpeed);


        //Add average laptime
        float avgLapTime = calculateAverage(lapTimesList);
        String inputAverageLapTime = gsonAvgLapTime.toJson(avgLapTime);

        //Add Max Linear Accel Value
        float maxLinAccelValue = Float.valueOf(Collections.max(accelerationList));
        String inputMaxLinAccel= gsonMaxLinAccel.toJson(maxLinAccelValue);

        //Add Max X Accel Value
        float maxXAccelValue = Float.valueOf(Collections.max(XaccelList));
        String inputMaxXAccel= gsonMaxXAccel.toJson(maxXAccelValue);
        //Add Max YAccel Value
        float maxYAccelValue = Float.valueOf(Collections.max(YaccelList));
        String inputMaxYAccel= gsonMaxYAccel.toJson(maxYAccelValue);
        //Add Max Z Accel Value
        float maxZAccelValue = Float.valueOf(Collections.max(ZaccelList));
        String inputMaxZAccel= gsonMaxZAccel.toJson(maxZAccelValue);

        //Add average Lin Acc
        float avgLinAcc = calculateAverage(accelerationList);
        String inputAverageLinAcc = gsonAvgLinAcc.toJson((avgLinAcc));


        //Add average X Acc
        float avgXAcc = calculateAverage(XaccelList);
        String inputAverageXAcc = gsonAvgXAcc.toJson((avgXAcc));

        //Add average Y Acc
        float avgYAcc = calculateAverage(YaccelList);
        String inputAverageYAcc = gsonAvgYAcc.toJson((avgYAcc));
        //Add average Z Acc
        float avgZAcc = calculateAverage(ZaccelList);
        String inputAverageZAcc = gsonAvgZAcc.toJson((avgZAcc));

        boolean isinserted = myDb.insertData(chronoView.getText().toString(), inputDist, inputSpeed, inputAccel, inputXAccel, inputYAccel, inputZAccel, inputLapTimes, inputAverageSpeed, inputAverageLapTime, inputMaxLinAccel, inputMaxXAccel, inputMaxYAccel, inputMaxZAccel, inputMaxSpeed, inputAverageXAcc, inputAverageYAcc, inputAverageZAcc, inputAverageLinAcc);
        if (isinserted) {
            Toast.makeText(TrackingActivity.this, "Race Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(TrackingActivity.this, "Error: Race Not Saved", Toast.LENGTH_SHORT).show();
        }

    }
    public List createListFromString(String string){
        //Split accel into arraylist of strings
        String string2 = string.replace("[", ""); // remove [
        String string3 = string2.replace("]", "");// remove ]
        String string4 = string3.replaceAll("\"", ""); // remove QUOTATION marks
        return Arrays.asList((string4.split(",")));//remove COMMAS
    }
    private float calculateAverage(ArrayList<String> list) {
        float sum = 0;
        if(!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                sum += Float.valueOf(list.get(i));
            }
            return sum / list.size();
        }
        return 0;
    }
//End of Database Methods

    // "ON-" Methods
    //Create, resume, pause
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        //Proximity Sensor
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);

        //Script
        timeAndMessages = (HashMap) loadMap();

        //Permissions
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bPermissionGranted = checkLocationPermission();
        }
        //Location Stuffs
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        Location location = locationManager.getLastKnownLocation(provider);


        //SETTINGS TOGGLE
        checkSettings();
        if (gpsUISetting) {

            if (location == null) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
                Toast.makeText(getApplicationContext(), "One moment for GPS please! - 1", Toast.LENGTH_SHORT).show();
            } else {
                // Toast.makeText(getApplicationContext(), "GPS works", Toast.LENGTH_SHORT).show();
            }
        }
        //For distance function
        lastLocation = location;


        //Start Chrono, accel, speedometer
        startChronometer();
        startAccel();
        startSpeedometer();

        //RealTime line chart
        lineChartFormat();

        //DATABASE
        accelerationList = new ArrayList<String>();
        XaccelList = new ArrayList<String>();
        YaccelList = new ArrayList<String>();
        ZaccelList = new ArrayList<String>();
        speedList = new ArrayList<String>();
        lapTimesList = new ArrayList<String>();
        myDb = new DatabaseHelper(this);

        //If all UI settings are off, display toast
        if (AllUiSettingsOff())
            Toast.makeText(getApplicationContext(), "Make sure to customize your Settings!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sm != null) {
            sm.registerListener(mProximityListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);

        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bPermissionGranted = checkLocationPermission();
        }
        //Check Settings
        checkSettings();
        checkSpeedometerTextSize();
        checkSpeedometerMaxValue();
        checkSpeedometerUnits();
        //If all UI settings are off, display toast
        if (AllUiSettingsOff())
            Toast.makeText(getApplicationContext(), "Make sure to customize your Settings!", Toast.LENGTH_LONG).show();

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        //Check if there is a previous known location and if gps is enabled!
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LocationManager mlocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gpsUISetting) {
            if (location == null) {

                if (!enabled) {
                    showDialogGPS();
                }
                // request location update!!
                else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Toast.makeText(getApplicationContext(), "GPS is loading. One moment please!", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (!enabled) {
                    showDialogGPS();
                } else if (enabled && (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
                } else {
                    Toast.makeText(getApplicationContext(), "GPS is loading. One moment please!", Toast.LENGTH_SHORT).show();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
                }
            }
        }
        //For distance Calc
        lastLocation = location;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (sm != null) {
            sm.unregisterListener(mProximityListener);
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bPermissionGranted = checkLocationPermission();
        }
        mSensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

    //End of Create, resume, pause
    //ON- "Changed"
    @Override
    public void onLocationChanged(Location location) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        speedUnitSetting = sp.getBoolean("prefSpeedUnits", false);

        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            double alt = location.getAltitude();
            double spd = (location.getSpeed()) * CONVERSION_METERSPERSECOND_TO_KMH; //getSpeed returns the speed in m/s, so multiply by 3.6 to get km/h

//For distance
////            dist += dist + lastLocation.distanceTo(location);
//            lastLat = lastLocation.getLatitude();
//            lastLng = lastLocation.getLongitude();
//
//            dist += dist + getDistance(lastLat, lastLng, lat, lng);
            distValue = String.valueOf(dist);
//            lastLocation = location;
//TODO Distance
//FOR DISTANCE
//            curLat = lat;
//            curLng = lng;

//            if(curLat!=lastLat || curLng!=lastLng) {
//                dist += getDistance(lastLat, lastLng, curLat, curLng);
//                //dist += calculateDistance(lastLat, lastLng, curLat, curLng);
//                distValue = String.valueOf(dist);
//                lastLat = curLat;
//                lastLng = curLng;
//
//            }

//            // rounding values to format "#.##"
            double sped = Math.round(spd * 100.00) / 100.00;

            //To avoid concatenating text displayed with setText
            String latt = "Latitude : " + Double.toString(lat);
            String lngg = "Longitude : " + Double.toString(lng);
            String altt = "Altitude : " + Double.toString(alt);
            String spedd = "Speed : " + Double.toString(sped) + " km/h";

            latView.setText(latt);
            lngView.setText(lngg);
            altView.setText(altt);
            spdView.setText(spedd);
            speedList.add(Double.toString(sped));

            if (speedUnitSetting) {
                sped = sped * CONVERSION_KMH_TO_MPH;
                spedd = "Speed : " + Double.toString(sped) + "mph";
                spdView.setText(spedd);
            }
            speedometer.setSpeed(sped);
        } else {
            LocationManager mlocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (gpsUISetting) {
                if (!enabled) {
                    showDialogGPS();
                } else
                    Toast.makeText(getApplicationContext(), "GPS is loading. One moment please!", Toast.LENGTH_SHORT).show();
            }
        }


        //Loic
        // speed_bar.setLayoutParams(new LinearLayout.LayoutParams(90, (int) Math.round(spd)));
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Rounding
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);

        //Gravity filter
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        //Acceleration components after gravity has been filtered
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        //Calculate Linear Acceleration
        double lin_accel = Math.sqrt(linear_acceleration[0] * linear_acceleration[0] + linear_acceleration[1] * linear_acceleration[1] + linear_acceleration[2] * linear_acceleration[2]);

        //Database
        accelerationList.add(String.valueOf(lin_accel));
        XaccelList.add(String.valueOf(linear_acceleration[2]));
        YaccelList.add(String.valueOf(linear_acceleration[1]));
        ZaccelList.add(String.valueOf(linear_acceleration[0]));
        // rounding values to format "#.##"

        double x = Math.round(linear_acceleration[2] * 100.0) / 100.0;
        double y = Math.round(linear_acceleration[1] * 100.0) / 100.0;
        double z = Math.round(linear_acceleration[0] * 100.0) / 100.0;
        double accel = Math.round(lin_accel * 100.0) / 100.0;

        //Send values to txt display including rounding
        accView.setText("Accel: " + accel);
        xrotView.setText("X : " + x);
        yrotView.setText("Y : " + y);
        zrotView.setText("Z : " + z);

        //Send value to entry function for plotting (on REAL time line chart)
        addEntry(lin_accel);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //ProximitySensor
    private SensorEventListener mProximityListener = new SensorEventListener() {

        boolean High = false, Low = false, Highagain = false;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }


        @Override
        public void onSensorChanged(SensorEvent event) {
        previousLapView = (TextView) findViewById(R.id.previousLapView);

            //ProximitySensor
            float[] value = event.values;
            if (value[0] >= proximitySensor.getMaximumRange()) {
                High = true;
            }

            if (High && value[0] <= 5) {
                Low = true;
            }

            if (Low && value[0] >= proximitySensor.getMaximumRange()) {
                Highagain = true;
            }

            if (Highagain) {
                newCount++;
                lapView.setText("Laps Complete : " + newCount + "");

                long previousTotal = totalTimeNow;

                totalTimeNow = (SystemClock.elapsedRealtime() - c.getBase()) / 1000;

                long lapTime = totalTimeNow - previousTotal;
//TODO FIX THIS
                lapTimesList.add(Long.toString(lapTime));

                previousLapView.setText("Previous Lap: " + lapTime + 's');
                High = Low = Highagain = false;

                Chronometer lapc = (Chronometer) findViewById(R.id.lapchronometer);
                lapc.setBase(SystemClock.elapsedRealtime());
                lapc.start();
            }



        }


    };
//End of ON- "Changed"
    //Not yet used
    //@Override
    //public void onStatusChanged(String provider, int status, Bundle extras) {
    //}
    // @Override
    // public void onProviderEnabled(String provider) {
    //}
    // @Override
    // public void onProviderDisabled(String provider) {
    // }

    //End of Not yet used
//End "ON-" Methods

    //Proximity Sensor
    private Map<String, String> loadMap() {
        Map<String, String> outputMap = new HashMap<String, String>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }

    //Creating options menu and items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        MenuItem save = menu.findItem(R.id.menu_save);//Display save option
        save.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                SaveRace(item); // Save race data to database
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//end of Options menu and items

    // Alert dialog and dialog popup in case Location service is Disabled
    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    private void showDialogGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please enable GPS");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean checkLocationPermission() {
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
//End of Alerts

//getLocation, Chronometer, Accel, position tracking methods
    public void getLocation(View view) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bPermissionGranted = checkLocationPermission();
        }
        Location location = locationManager.getLastKnownLocation(provider);

        //If no previous location saved, tell user to wait for gps to load
        if (location == null) {
            // request location update!!
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
            Toast.makeText(getApplicationContext(), "GPS is loading. One moment please!", Toast.LENGTH_SHORT).show();
        }
        onLocationChanged(location);
    }

    public void startChronometer() {
        c = (Chronometer) findViewById(R.id.chronometer);
        c.setBase(SystemClock.elapsedRealtime());
        c.start();
        checkChronoTextSizeSetting();

        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {

                long elapsed = SystemClock.elapsedRealtime() - c.getBase();
                int elapsedsec = (int) (elapsed / 1000);
                String elapsedString = Integer.toString(elapsedsec);

                //ProximitySensor
                //Toast.makeText(getApplicationContext(), String.valueOf(elapsedsec) , Toast.LENGTH_LONG).show();

                if (timeAndMessages.containsKey(elapsedString)) {

                    Toast toast = Toast.makeText(getApplicationContext(), timeAndMessages.get(elapsedString).toString(), Toast.LENGTH_LONG);
                    ViewGroup group = (ViewGroup) toast.getView();
                    TextView messageTextView = (TextView) group.getChildAt(0);
                    messageTextView.setTextSize(25);
                    toast.show();
                }


                handler.postDelayed(this, 1000);
            }
        };

        handler.post(run);
    }

    public void startAccel() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    //Not yet used
    public void startPositionTracking() {
    }
//End of getLocation, Chronometer, Accel, position tracking methods

    //Line Chart Methods
    public void lineChartFormat() {
        //RealTime line chart
        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.setDescription("Linear Accelerometer Data");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        // Typeface tf = Typeface.createFromAsset(getAssets(), "");

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        // l.setTypeface(tf);
        l.setTextColor(Color.WHITE);


        XAxis xl = mChart.getXAxis();
        // xl.setTypeface(tf);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(false);
        xl.setSpaceBetweenLabels(2);

        xl.setEnabled(true);


        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(30f);
        leftAxis.setAxisMinValue(-5f);
        leftAxis.setDrawGridLines(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void addEntry(double value1) {

        time = time + 0.5;
        LineData linedata = mChart.getData();
        float gravity = (float) value1;

        if (linedata != null) {

            ILineDataSet set = linedata.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                linedata.addDataSet(set);
            }

            // add a new x-value first
            linedata.addXValue(time + ""); //name displayed for x-axis value
            linedata.addEntry(new Entry(gravity, set.getEntryCount()), 0);//Y value

            // Add new value
            set.addEntryOrdered(new Entry((float) time, 0)); //x value that corresponds to y value

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(200);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(linedata.getXValCount() - 121);

            // this automatically refreshes the chart (calls invalidate())
            mChart.moveViewTo(linedata.getXValCount() - 7, 55f,
                    YAxis.AxisDependency.LEFT);

        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Real Time Linear Acceleration");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(1f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }
//End of Line Chart Methods

    //Speedometer Method
    public void startSpeedometer() {

        speedometer = (SpeedometerGauge) findViewById(R.id.speedometer);

        speedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });


        //speedometer.setMaxSpeed(45);
        speedometer.setMajorTickStep(5);
        speedometer.setMinorTicks(1);
        speedometer.addColoredRange(0, 15, Color.GREEN);
        speedometer.addColoredRange(15, 30, Color.YELLOW);
        speedometer.addColoredRange(30, 45, Color.RED);


        checkSpeedometerTextSize();
        checkSpeedometerMaxValue();
        checkSpeedometerUnits();

    }

    //Settings Methods
    //Check ALL the settings
    public void checkSettings() {

        //UI Display
        checkGpsUISetting();
        checkAccelUISetting();
        checkTimerUISetting();
        checkLineGraphUISetting();
        checkDigitalSpeedometerUISetting();
        checkAnalogSpeedometerUISetting();

        //Text Sizes
        checkGPSTextSizeSetting();
        checkAccelTextSizeSetting();
        checkDigitalSpeedTextSizeSetting();
        checkChronoTextSizeSetting();

        //Background
        checkBackgroundColorSetting();



    }

    //Individual Settings methods
    public void checkGpsUISetting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gpsUISetting = sp.getBoolean("prefGpsUI", true);
        //GPS
        gpsTitle = (TextView) findViewById(R.id.gpsView);
        latView = (TextView) findViewById(R.id.latitudeView);
        lngView = (TextView) findViewById(R.id.longitudeView);
        altView = (TextView) findViewById(R.id.altitudeView);


        if (gpsUISetting) {
            gpsTitle.setVisibility(View.VISIBLE);
            latView.setVisibility(View.VISIBLE);
            lngView.setVisibility(View.VISIBLE);
            altView.setVisibility(View.VISIBLE);

        } else {
            gpsTitle.setVisibility(View.GONE);
            latView.setVisibility(View.GONE);
            lngView.setVisibility(View.GONE);
            altView.setVisibility(View.GONE);

        }
    }

    public void checkAccelUISetting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        accelUISetting = sp.getBoolean("prefAccelerometerUI", true);

        accTitle = (TextView) findViewById(R.id.accelTitle);
        accView = (TextView) findViewById(R.id.accelView);
        compAccTitle = (TextView) findViewById(R.id.compAccView);
        xrotView = (TextView) findViewById(R.id.xrotationView);
        yrotView = (TextView) findViewById(R.id.yrotationView);
        zrotView = (TextView) findViewById(R.id.zrotationView);

        if (accelUISetting) {
            accTitle.setVisibility(View.VISIBLE);
            accView.setVisibility(View.VISIBLE);
            compAccTitle.setVisibility(View.VISIBLE);
            xrotView.setVisibility(View.VISIBLE);
            yrotView.setVisibility(View.VISIBLE);
            zrotView.setVisibility(View.VISIBLE);
        } else {
            accTitle.setVisibility(View.GONE);
            accView.setVisibility(View.GONE);
            compAccTitle.setVisibility(View.GONE);
            xrotView.setVisibility(View.GONE);
            yrotView.setVisibility(View.GONE);
            zrotView.setVisibility(View.GONE);
        }
    }

    public void checkTimerUISetting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        timerUISetting = sp.getBoolean("prefTimerUI", true);

        timerTitle = (TextView) findViewById(R.id.timerTitle);//timer section title
        lapView = (TextView) findViewById(R.id.lapView);//lap counter
        previousLapView = (TextView) findViewById(R.id.previousLapView);//previous lap time
        currentLapView = (TextView) findViewById(R.id.currentLapView);//current lap time
        lapChronoView = (TextView) findViewById(R.id.lapchronometer);//lap time chrono
        timerView = (TextView) findViewById(R.id.timerView);//total time
        chronoView = (TextView) findViewById(R.id.chronometer);// total time chronometer

        if (timerUISetting) {
            timerTitle.setVisibility(View.VISIBLE);
            timerView.setVisibility(View.VISIBLE);
            chronoView.setVisibility(View.VISIBLE);
            lapView.setVisibility(View.VISIBLE);
            previousLapView.setVisibility(View.VISIBLE);
            currentLapView.setVisibility(View.VISIBLE);
            lapChronoView.setVisibility(View.VISIBLE);
        } else {
            timerTitle.setVisibility(View.GONE);
            lapView.setVisibility(View.GONE);
            previousLapView.setVisibility(View.GONE);
            currentLapView.setVisibility(View.GONE);
            lapChronoView.setVisibility(View.GONE);
            timerView.setVisibility(View.GONE);
            chronoView.setVisibility(View.GONE);
        }
    }

    public void checkLineGraphUISetting() {


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lineGraphUISetting = sp.getBoolean("prefLineGraphUI", true);
        lineGraphView = findViewById(R.id.chart1);

        if (lineGraphUISetting)
            lineGraphView.setVisibility(View.VISIBLE);
        else
            lineGraphView.setVisibility(View.GONE);
    }

    public void checkDigitalSpeedometerUISetting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        digitalSpeedUISetting = sp.getBoolean("prefDigitalSpeedometer", true);
        spdView = (TextView) findViewById(R.id.speedView);


        if (digitalSpeedUISetting)
            spdView.setVisibility(View.VISIBLE);
        else spdView.setVisibility(View.GONE);
    }
    public void checkAnalogSpeedometerUISetting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        speedometerUISetting = sp.getBoolean("prefSpeedometer", true);
        speedometerView = findViewById(R.id.speedometer);

        if (speedometerUISetting)
            speedometerView.setVisibility(View.VISIBLE);
        else speedometerView.setVisibility(View.GONE);
    }

    public void checkGPSTextSizeSetting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gpsTextSizeSetting = sp.getString("prefGPSTextSize", "25");

        switch (gpsTextSizeSetting) {
            case "25":
                gpsTitle.setTextSize(25);
                latView.setTextSize(25);
                lngView.setTextSize(25);
                altView.setTextSize(25);
                break;

            case "40":
                gpsTitle.setTextSize(40);
                latView.setTextSize(40);
                lngView.setTextSize(40);
                altView.setTextSize(40);
                break;

            case "50":
                gpsTitle.setTextSize(50);
                latView.setTextSize(50);
                lngView.setTextSize(50);
                altView.setTextSize(50);
                break;
        }
    }
    public void checkDigitalSpeedTextSizeSetting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        digitalSpeedTextSizeSetting = sp.getString("prefDigitalSpeedometerTextSize", "25");

        switch (digitalSpeedTextSizeSetting) {
            case "25":
                spdView.setTextSize(25);
                break;
            case "40":
                spdView.setTextSize(40);
                break;
            case "50":
                spdView.setTextSize(50);
                break;
        }
    }

    public void checkAccelTextSizeSetting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        accelTextSizeSetting = sp.getString("prefAccelTextSize", "25");
        switch (accelTextSizeSetting) {
            case "25":
                accTitle.setTextSize(25);
                accView.setTextSize(25);
                compAccTitle.setTextSize(25);
                xrotView.setTextSize(25);
                yrotView.setTextSize(25);
                zrotView.setTextSize(25);
                break;

            case "40":
                accTitle.setTextSize(40);
                accView.setTextSize(40);
                compAccTitle.setTextSize(40);
                xrotView.setTextSize(40);
                yrotView.setTextSize(40);
                zrotView.setTextSize(40);
                break;

            case "50":
                accTitle.setTextSize(50);
                accView.setTextSize(50);
                compAccTitle.setTextSize(50);
                xrotView.setTextSize(50);
                yrotView.setTextSize(50);
                zrotView.setTextSize(50);
                break;
        }

    }

    public void checkChronoTextSizeSetting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        chronTextSizeSetting = sp.getString("prefChronoTextSize", "25");

        switch (chronTextSizeSetting) {
            case "25":
                timerTitle.setTextSize(25);
                lapView.setTextSize(25);
                previousLapView.setTextSize(25);
                currentLapView.setTextSize(25);
                lapChronoView.setTextSize(25);
                timerView.setTextSize(25);
                chronoView.setTextSize(25);
                break;

            case "40":
                timerTitle.setTextSize(40);
                lapView.setTextSize(40);
                previousLapView.setTextSize(40);
                currentLapView.setTextSize(40);
                lapChronoView.setTextSize(40);
                timerView.setTextSize(40);
                chronoView.setTextSize(40);
                break;

            case "50":
                timerTitle.setTextSize(50);
                lapView.setTextSize(50);
                previousLapView.setTextSize(50);
                currentLapView.setTextSize(50);
                lapChronoView.setTextSize(40);
                timerView.setTextSize(40);
                chronoView.setTextSize(40);
                break;
        }
    }

    public void checkBackgroundColorSetting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        backgroundColorSetting = sp.getBoolean("prefBackgroundColor", false);

        mainLayout = (ScrollView) findViewById(R.id.scrollView);
        if (backgroundColorSetting) {
            mainLayout.setBackgroundColor(Color.BLACK);
            //GPS
            gpsTitle.setTextColor(Color.WHITE);
            latView.setTextColor(Color.WHITE);
            lngView.setTextColor(Color.WHITE);
            altView.setTextColor(Color.WHITE);
            spdView.setTextColor(Color.WHITE);
            //accel
            accTitle.setTextColor(Color.WHITE);
            accView.setTextColor(Color.WHITE);
            compAccTitle.setTextColor(Color.WHITE);
            xrotView.setTextColor(Color.WHITE);
            yrotView.setTextColor(Color.WHITE);
            zrotView.setTextColor(Color.WHITE);
            //Chrono
            timerTitle.setTextColor(Color.WHITE);
            timerView.setTextColor(Color.WHITE);
            chronoView.setTextColor(Color.WHITE);
            lapView.setTextColor(Color.WHITE);

        } else {
            mainLayout.setBackgroundColor(Color.WHITE);
            //GPS
            gpsTitle.setTextColor(Color.BLACK);
            latView.setTextColor(Color.BLACK);
            lngView.setTextColor(Color.BLACK);
            altView.setTextColor(Color.BLACK);
            spdView.setTextColor(Color.BLACK);
            //accel
            accTitle.setTextColor(Color.BLACK);
            accView.setTextColor(Color.BLACK);
            compAccTitle.setTextColor(Color.BLACK);
            xrotView.setTextColor(Color.BLACK);
            yrotView.setTextColor(Color.BLACK);
            zrotView.setTextColor(Color.BLACK);
            //Chrono
            timerTitle.setTextColor(Color.BLACK);
            timerView.setTextColor(Color.BLACK);
            chronoView.setTextColor(Color.BLACK);
            lapView.setTextColor(Color.BLACK);
        }
    }

    public void checkSpeedometerTextSize() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        speedometerTextSizeSetting = sp.getString("prefSpeedometerTextSize", "40");

        switch (speedometerTextSizeSetting) {
            case "40":
                speedometer.setLabelTextSize(40);
                break;
            case "50":
                speedometer.setLabelTextSize(50);
                break;
            case "60":
                speedometer.setLabelTextSize(50);
                break;
            default:
                speedometer.setLabelTextSize(40);
        }
    }
    public void checkSpeedometerMaxValue() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        speedometerMaxValueSetting = sp.getString("prefSpeedometerMaxValue", "100");

        switch (speedometerMaxValueSetting) {
            case "20":
                speedometer.setMaxSpeed(20);
                break;
            case "30":
                speedometer.setMaxSpeed(30);
                break;
            case "40":
                speedometer.setMaxSpeed(40);
                break;
            case "50":
                speedometer.setMaxSpeed(50);
                break;
            case "100":
                speedometer.setMaxSpeed(100);
                break;
            case "200":
                speedometer.setMaxSpeed(200);
                break;
        }
    }
    public void checkSpeedometerUnits(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        speedUnitSetting = sp.getBoolean("prefSpeedUnits", false);

        if (speedUnitSetting)
            speedometer.setUnitsText("mph");
        else
            speedometer.setUnitsText("km/h");
    }

    public boolean AllUiSettingsOff(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!gpsUISetting && !digitalSpeedUISetting && !accelUISetting && !timerUISetting && !lineGraphUISetting && !speedometerUISetting)
            return true;
        else
            return false;
    }
//End of Settings Methods


    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double latA = Math.toRadians(lat1);
        double lonA = Math.toRadians(lon1);
        double latB = Math.toRadians(lat2);
        double lonB = Math.toRadians(lon2);
        double cosAng = (Math.cos(latA) * Math.cos(latB) * Math.cos(lonB - lonA)) +
                (Math.sin(latA) * Math.sin(latB));
        double ang = Math.acos(cosAng);
        double dist = ang * 6371;

        return dist;
    }

    public static double calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        float[] results = new float[3];
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
        return results[0];
    }

}