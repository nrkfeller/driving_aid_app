package com.efelnic.driveapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccelLineGraphActivity extends AppCompatActivity {

    String linAcc, xAcc, yAcc, zAcc;
    List<String>  linAccArray, xAccArray, yAccArray, zAccArray;
    private LineChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accel_line_graph);


        Bundle extras = getIntent().getExtras();
        linAcc = extras.getString("ACCELERATION");
        xAcc = extras.getString("XACCEL");
        yAcc = extras.getString("YACCEL");
        zAcc = extras.getString("ZACCEL");

        linAccArray = createListFromString(linAcc);
        xAccArray = createListFromString(xAcc);
        yAccArray = createListFromString(yAcc);
        zAccArray = createListFromString(zAcc);

        multiLineChart();
    }
    public void multiLineChart() {

        ArrayList<Entry> linAccVals = new ArrayList<>();
        ArrayList<Entry> xAccVals = new ArrayList<>(); // y-values
        ArrayList<Entry> yAccVals = new ArrayList<>(); // y-values
        ArrayList<Entry> zAccVals = new ArrayList<>(); // y-values
        ArrayList<String> xVals = new ArrayList<>(); //x values

        for (int i = 0; i < linAccArray.size(); i++) {

            linAccVals.add(new Entry(Float.valueOf(linAccArray.get(i)), i));
            xAccVals.add(new Entry(Float.valueOf(xAccArray.get(i)), i));
            yAccVals.add(new Entry(Float.valueOf(yAccArray.get(i)), i));
            zAccVals.add(new Entry(Float.valueOf(zAccArray.get(i)), i));
            xVals.add(i + "");
            //labels.add("i");
        }


        LineDataSet line1 = new LineDataSet(linAccVals, "Linear");
        line1.setLineWidth(2.5f);
        line1.setCircleRadius(1.5f);
        line1.setHighLightColor(Color.rgb(244, 117, 117));
        line1.setDrawValues(false);

        LineDataSet line2 = new LineDataSet(xAccVals, "X");
        line2.setLineWidth(1f);
        line2.setCircleRadius(1.5f);
        line2.setHighLightColor(Color.rgb(150, 160, 200));
        line2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        line2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        line2.setDrawValues(false);

        LineDataSet line3 = new LineDataSet(yAccVals, "Y");
        line3.setLineWidth(1f);
        line3.setCircleRadius(1.5f);
        line3.setHighLightColor(Color.rgb(200, 90, 117));
        line3.setColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        line3.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        line3.setDrawValues(false);

        LineDataSet line4 = new LineDataSet(zAccVals, "Z");
        line4.setLineWidth(1f);
        line4.setCircleRadius(1.5f);
        line4.setHighLightColor(Color.rgb(200, 90, 117));
        line4.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        line4.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        line4.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(line1);
        sets.add(line2);
        sets.add(line3);
        sets.add(line4);

        Context context = this;

        LineData accelLines = new LineData(xVals, sets);
        chart = new LineChart(context);
        setContentView(chart);

        chart.setData(accelLines);
        chart.setDescription("Accelerometer Values");
        chart.animateY(2500);
    }

    public List createListFromString(String string){
        //Split accel into arraylist of strings
        String string2 = string.replace("[", ""); // remove [
        String string3 = string2.replace("]", "");// remove ]
        String string4 = string3.replaceAll("\"", ""); // remove QUOTATION marks
        return Arrays.asList((string4.split(",")));//remove COMMAS
    }
}
