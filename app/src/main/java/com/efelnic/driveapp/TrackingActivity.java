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

public class TrackingActivity extends AppCompatActivity implements LocationListener, SensorEventListener, OnChartValueSelectedListener {

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

    private LineChart mChart;
    float lin_accel;
    double time = 0;


    //Bar Chart Loic
//    LinearLayout la; // used for charts
//    View bar1, bar2, bar3, lin_acel_bar, speed_bar, time_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bPermissionGranted = checkLocationPermission();
        }

        startChronometer();

        startAccel();
          //Loic
//        //Create Bar chart
//        la = (LinearLayout)findViewById(R.id.barchart);
//        bar1 = drawChart(7,10);
//        bar2 = drawChart(7,10);
//        bar3 = drawChart(7,10);
//        lin_acel_bar = drawChart(8,10);
//        speed_bar = drawChart(3,10);
//        time_bar = drawChart(5,5);

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

        //Dynamic line chart
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

        //Typeface tf = Typeface.createFromAsset(getAssets(), "");

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(tf);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        // xl.setTypeface(tf);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(60f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

    }


    private void addEntry(double value1) {

        time = time + 0.5;
        LineData linedata = mChart.getData();
        float gravity = (float)value1;

        if (linedata != null) {

            ILineDataSet set = linedata.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                linedata.addDataSet(set);
            }

            // add a new x-value first
            linedata.addXValue(time + ""); //name displayed for x-axis value
            linedata.addEntry(new Entry(gravity , set.getEntryCount()), 0);//Y value

            // Add new value
            set.addEntryOrdered(new Entry((float)time, 0)); //x value that corresponds to y value

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(120);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(linedata.getXValCount() - 121);

            // this automatically refreshes the chart (calls invalidate())
            mChart.moveViewTo(linedata.getXValCount()-7, 55f,
                    YAxis.AxisDependency.LEFT);

        }
    }
    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
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

        double  lin_accel = Math.sqrt(linear_acceleration[0] * linear_acceleration[0] + linear_acceleration[1] * linear_acceleration[1] + linear_acceleration[2] * linear_acceleration[2]);
        accelView.setText("Accel : " + df.format(lin_accel*10));

        xrotView.setText("Orientation X : " + Float.toString(event.values[2]));
        yrotView.setText("Orientation Y : " + Float.toString(event.values[1]));
        zrotView.setText("Orientation Z : " + Float.toString(event.values[0]));

        addEntry(lin_accel);


        // Loic
//        // Bar charts
//        int temp_x = Math.round(event.values[2] * 10);
//        int temp_y = Math.round(event.values[1] * 10);
//        int temp_z = Math.round(event.values[0] * 10);
//        int temp_A = (int)Math.round((Math.sqrt(linear_acceleration[0] * linear_acceleration[0] + linear_acceleration[1] * linear_acceleration[1] + linear_acceleration[2] * linear_acceleration[2]))*100);
//
//        bar1.setLayoutParams(new LinearLayout.LayoutParams(90, temp_x));
//        bar2.setLayoutParams(new LinearLayout.LayoutParams(90, temp_y));
//        bar3.setLayoutParams(new LinearLayout.LayoutParams(90, temp_z));
//        lin_acel_bar.setLayoutParams(new LinearLayout.LayoutParams(90, temp_A));
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
      // Loic
//    // Chart creation function
//    private View drawChart(int color, int height) {
//        switch(color) {
//            case 1: color = Color.RED; break;
//            case 2: color = Color.BLUE; break;
//            case 3: color = Color.GREEN; break;
//            case 4: color = Color.BLACK; break;
//            case 5: color = Color.MAGENTA; break;
//            case 6: color = Color.YELLOW; break;
//            case 7: color = Color.GRAY; break;
//            case 8: color = Color.CYAN; break;
//        }
//        View custom_view = new View(this);
//        custom_view.setBackgroundColor(color);
//        custom_view.setLayoutParams(new LinearLayout.LayoutParams(90, height));
//
//        LinearLayout.LayoutParams custom_params = (LinearLayout.LayoutParams)custom_view.getLayoutParams();
//        custom_params.setMargins(3, 0, 0, 0); // left, top, right, bottom
//        custom_view.setLayoutParams(custom_params);
//
//        la.addView(custom_view);
//        return custom_view;
//    }

}