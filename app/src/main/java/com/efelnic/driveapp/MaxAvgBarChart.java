package com.efelnic.driveapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MaxAvgBarChart extends AppCompatActivity {

    private BarChart chart;
    String avgSpeed, maxLinAccel, avgLinAcc, maxXAcc, maxyAcc, maxZacc, maxSpeed, avgXAcc, avgYAcc, avgZAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_avg_bar_chart);

        Bundle extras = getIntent().getExtras();
        avgSpeed = extras.getString("AVGSPEED");
        maxLinAccel = extras.getString("MAXLINACC");
        avgLinAcc= extras.getString("AVGLINACC");
        maxXAcc = extras.getString("MAXXACC");
        maxyAcc = extras.getString("MAXYACC");
        maxZacc = extras.getString("MAXZACC");
        maxSpeed = extras.getString("MAXSPEED");
        avgXAcc = extras.getString("AVGXACC");
        avgYAcc = extras.getString("AVGYACC");
        avgZAcc = extras.getString("AVGZACC");

        multiBarChart();

    }
    public void multiBarChart(){

//        ArrayList<BarEntry> avgSpdVal = new ArrayList<>(); // y-values
//        ArrayList<BarEntry> maxSpdVal = new ArrayList<>(); // y-values
//        ArrayList<BarEntry> avgLinAccVal = new ArrayList<>(); // y-values
//        ArrayList<BarEntry> maxLinAccVal = new ArrayList<>(); // y-values
//        ArrayList<BarEntry> avgXAccVal = new ArrayList<>(); // y-values
//        ArrayList<BarEntry> maxXAccVal = new ArrayList<>(); // y-values
//
//        ArrayList<BarEntry> avgYAccVal = new ArrayList<>(); // y-values
//        ArrayList<BarEntry> maxYAccVal = new ArrayList<>(); // y-values
//        ArrayList<BarEntry> avgZAccVal = new ArrayList<>(); // y-values
//        ArrayList<BarEntry> maxZAccVal = new ArrayList<>(); // y-values
//
//
//        avgSpdVal.add(new BarEntry( Float.valueOf(avgSpeed), 0));
//        maxSpdVal.add(new BarEntry( Float.valueOf(maxSpeed), 1));
//
//        avgLinAccVal.add(new BarEntry( Float.valueOf(avgLinAcc), 2));
//        maxLinAccVal.add(new BarEntry( Float.valueOf(maxLinAcc), 3));
//
//        avgXAccVal.add(new BarEntry( Float.valueOf(avgXAcc), 4));
//        maxXAccVal.add(new BarEntry(Float.valueOf(maxXAcc), 5));
//        avgYAccVal.add(new BarEntry(Float.valueOf(avgYAcc), 6));
//        maxYAccVal.add(new BarEntry(Float.valueOf(maxyAcc), 7));
//        avgZAccVal.add(new BarEntry(Float.valueOf(avgZAcc), 8));
//        maxZAccVal.add(new BarEntry(Float.valueOf(maxZacc), 9));

        ArrayList<String> xVals = new ArrayList<>();


        xVals.add("Speed");
        //xVals.add("Avg Speed");
        xVals.add("Lin. Accel.");
        //xVals.add("");
        xVals.add("X");
        //xVals.add("");
        xVals.add("Y");
        //xVals.add("");
        xVals.add("Z");
        //xVals.add("");


        ArrayList<BarEntry> avgValues = new ArrayList<>();
        avgValues.add(new BarEntry(Float.valueOf(avgSpeed), 0));
        avgValues.add(new BarEntry( Float.valueOf(avgLinAcc), 1));
        avgValues.add(new BarEntry( Float.valueOf(avgXAcc), 2));
        avgValues.add(new BarEntry(Float.valueOf(avgYAcc), 3));
        avgValues.add(new BarEntry(Float.valueOf(avgZAcc), 4));

        ArrayList<BarEntry> maxValues = new ArrayList<>();
        maxValues.add(new BarEntry(Float.valueOf(maxSpeed), 0));
        maxValues.add(new BarEntry( Float.valueOf(maxLinAccel), 1));
        maxValues.add(new BarEntry(Float.valueOf(maxXAcc), 2));
        maxValues.add(new BarEntry(Float.valueOf(maxyAcc), 3));
        maxValues.add(new BarEntry(Float.valueOf(maxZacc), 4));

        BarDataSet bar1 = new BarDataSet(avgValues, "Avg");
        //bar1.setColor(Color.rgb(104, 241, 175));
        bar1.setColors(ColorTemplate.COLORFUL_COLORS);
        //bar1.setBarSpacePercent(0.01f);

        BarDataSet bar2 = new BarDataSet(maxValues, "Max");
        //bar2.setColor(Color.rgb(164, 228, 251));
        bar2.setColors(ColorTemplate.COLORFUL_COLORS);
//        BarDataSet bar3 = new BarDataSet(avgLinAccVal, "");
//        bar3.setColor(Color.rgb(104, 241, 175));
//        BarDataSet bar4 = new BarDataSet(maxLinAccVal, "");
//        bar4.setColor(Color.rgb(164, 228, 251));
//        BarDataSet bar5 = new BarDataSet(maxXAccVal, "");
//        bar5.setColor(Color.rgb(104, 241, 175));
//        BarDataSet bar6 = new BarDataSet(avgXAccVal, "Max X Acc");
//        bar6.setColor(Color.rgb(164, 228, 251));
//        BarDataSet bar7 = new BarDataSet(maxYAccVal, "Avg Y Acc");
//        bar7.setColor(Color.rgb(104, 241, 175));
//        BarDataSet bar8 = new BarDataSet(avgYAccVal, "Max Y Acc");
//        bar8.setColor(Color.rgb(164, 228, 251));
//        BarDataSet bar9= new BarDataSet(maxZAccVal, "Avg Z Acc");
//        bar9.setColor(Color.rgb(104, 241, 175));
//        BarDataSet bar10 = new BarDataSet(avgZAccVal, "Max Z Acc");
//        bar10.setColor(Color.rgb(164, 228, 251));
//        bar1.setBarSpacePercent(0f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(bar1);
        dataSets.add(bar2);
//        dataSets.add(bar3);
//        dataSets.add(bar4);
//        dataSets.add(bar5);
//        dataSets.add(bar6);
//        dataSets.add(bar7);
//        dataSets.add(bar8);
//        dataSets.add(bar9);
//        dataSets.add(bar10);




        Context context = this;

        chart = new BarChart(context);
        setContentView(chart);
        BarData data = new BarData(xVals, dataSets);

        data.setValueTextSize(10);
        data.setGroupSpace(25f);
        chart.setData(data);
        chart.setDescription("Average and Max Values for Speed, and Linear & XYZ-Accel");
        chart.setDescriptionTextSize(15);

        YAxis yAxis = chart.getAxisLeft();
        YAxis yAxis1 = chart.getAxisRight();
        yAxis1.setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setSpaceBetweenLabels(0);
        //xAxis.setTextSize(10);

        Legend l = chart.getLegend();
        l.setEnabled(false);

//        mChart.setData(data);
        chart.invalidate();
    }





}

