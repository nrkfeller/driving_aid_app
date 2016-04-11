package com.efelnic.driveapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeedChartActivity extends AppCompatActivity {

    String date, duration, dist, speed, accel, xAccel, yAccel, zAccel;
    List<String>  speedArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_speed_chart);


        Bundle extras = getIntent().getExtras();
//        date = extras.getString("DATE");
        duration = extras.getString("DURATION");
//        dist = extras.getString("DISTANCE");
        speed = extras.getString("SPEED");
//        accel = extras.getString("ACCELERATION");
//        xAccel = extras.getString("XACCEL");
//        yAccel = extras.getString("YACCEL");
//        zAccel = extras.getString("ZACCEL");
        speedArray = createListFromString(speed);

    lineGraph();
    }

    public void lineGraph(){

        //+      TextView accelTest = (TextView) findViewById(R.id.accelString);
//        accelTest.setText(accelArray.get(1));

        ArrayList<Entry> entries = new ArrayList<>(); // y-values
        ArrayList<String> labels = new ArrayList<String>(); //x values

        for (int i = 0; i< speedArray.size(); i++){

            entries.add(new Entry( Float.valueOf(speedArray.get(i)), i));
            labels.add("i");
        }
        LineDataSet dataSet = new LineDataSet(entries, "Speed");

        Context context = this;

        LineChart chart = new LineChart(context);
        setContentView(chart);
        LineData data = new LineData(labels, dataSet);
        chart.setData(data);
        chart.setDescription("Your Speed Values");
    }

    public List createListFromString(String string){
        //Split accel into arraylist of strings
        String string2 = string.replace("[", ""); // remove [
        String string3 = string2.replace("]", "");// remove ]
        String string4 = string3.replaceAll("\"", ""); // remove QUOTATION marks
        return Arrays.asList((string4.split(",")));//remove COMMAS
    }
}
