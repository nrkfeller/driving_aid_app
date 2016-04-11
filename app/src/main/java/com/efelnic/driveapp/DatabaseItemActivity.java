package com.efelnic.driveapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseItemActivity extends AppCompatActivity {

    ListView myListView;

    DatabaseHelper myDb;
    String date, duration, dist, speed, accel, xAccel, yAccel, zAccel;

    List<String> accelArray, distArray, speedArray, xAccelArray, yAccelArray, zAccelArray;
    ArrayList<ChartItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_database_item);


        myDb = new DatabaseHelper(this);

        grabEntryData();
        //displayData(myListView);
        //radarGraph();
       // scatterGraph();
        myListView = (ListView) findViewById(R.id.chartListView);
        list = new ArrayList<>();
        list.add(new LineChartItem(lineGraph(), getApplicationContext()));
        list.add(new RadarChartItem(radarGraph(), getApplicationContext()));
        list.add(new LineChartItem(multiLineGraph(), getApplicationContext()));
        list.add(new PieChartItem(pieGraph(), getApplicationContext()));
        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        myListView.setAdapter(cda);







        Button RawDataButton = (Button)findViewById(R.id.rawDataButton);
        RawDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatabaseItemActivity.this, RawDataActivity.class);
                Bundle extras = new Bundle();
                extras.putString("DATE", date);
                extras.putString("DURATION", duration);
                extras.putString("DISTANCE", dist );
                extras.putString("SPEED", speed);
                extras.putString("ACCELERATION", accel );
                extras.putString("XACCEL", xAccel );
                extras.putString("YACCEL", yAccel);
                extras.putString("ZACCEL", zAccel);
                intent.putExtras(extras);
                startActivity(intent);
            }


        });
    }



    private LineData lineGraph(){

        //+      TextView accelTest = (TextView) findViewById(R.id.accelString);
//        accelTest.setText(accelArray.get(1));

        ArrayList<Entry> entries = new ArrayList<>(); // y-values
        ArrayList<String> xVals = new ArrayList<String>(); //x values

        for (int i = 0; i< accelArray.size(); i++){

            entries.add(new Entry( Float.valueOf(accelArray.get(i)), i));
            xVals.add(String.valueOf(i));
            //labels.add("i");
        }

        LineDataSet line1 = new LineDataSet(entries, "Accelerometer");

        Context context = this;

        LineChart lineChart = new LineChart(context);
        //setContentView(chart);
        LineData accelLine = new LineData(xVals, line1);
        lineChart.setData(accelLine);
        lineChart.setDescription("Your Accelerometer Values");

        return accelLine;

    }
    private LineData multiLineGraph(){

        //+      TextView accelTest = (TextView) findViewById(R.id.accelString);
//        accelTest.setText(accelArray.get(1));

        ArrayList<Entry> xAccVals = new ArrayList<>(); // y-values
        ArrayList<Entry> yAccVals = new ArrayList<>(); // y-values
        ArrayList<Entry> zAccVals = new ArrayList<>(); // y-values
        ArrayList<String> xVals = new ArrayList<String>(); //x values

        for (int i = 0; i< xAccelArray.size(); i++){

            xAccVals.add(new Entry( Float.valueOf(xAccelArray.get(i)), i));
            yAccVals.add(new Entry(Float.valueOf(yAccelArray.get(i)), i));
            zAccVals.add(new Entry( Float.valueOf(zAccelArray.get(i)), i));
            xVals.add(String.valueOf(i));
            //labels.add("i");
        }


        LineDataSet line1 = new LineDataSet(xAccVals, "X-Accel");
            line1.setLineWidth(2.5f);
            line1.setCircleRadius(4.5f);
            line1.setHighLightColor(Color.rgb(244, 117, 117));
            line1.setDrawValues(false);
        LineDataSet line2 = new LineDataSet(yAccVals, "Y-Accel");
            line2.setLineWidth(2.5f);
            line2.setCircleRadius(4.5f);
            line2.setHighLightColor(Color.rgb(150, 160, 200));
            line2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            line2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            line2.setDrawValues(false);
        LineDataSet line3 = new LineDataSet(zAccVals, "Z-Accel");
            line3.setLineWidth(2.5f);
            line3.setCircleRadius(4.5f);
            line3.setHighLightColor(Color.rgb(200, 90, 117));
            line3.setColor(ColorTemplate.VORDIPLOM_COLORS[1]);
            line3.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[1]);
            line3.setDrawValues(false);




        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(line1);
        sets.add(line2);
        sets.add(line3);
        Context context = this;

        LineChart lineChart = new LineChart(context);
        //setContentView(chart);
        LineData accelLines = new LineData(xVals, sets);
        //lineChart.setData(accelLines);
        lineChart.setDescription("Your Accelerometer Values");

        return accelLines;

    }
    private RadarData radarGraph(){

        //+      TextView accelTest = (TextView) findViewById(R.id.accelString);
//        accelTest.setText(accelArray.get(1));

        ArrayList<Entry> entries = new ArrayList<>(); // y-values
        ArrayList<String> xVals = new ArrayList<String>(); //x values

        for (int i = 0; i< accelArray.size(); i++){

            entries.add(new Entry( Float.valueOf(accelArray.get(i)), i));
            xVals.add(String.valueOf(i));
            //labels.add("i");
        }

        RadarDataSet radar = new RadarDataSet(entries, "Accelerometer");

        Context context = this;

        RadarChart radarChart = new RadarChart(context);
        //setContentView(chart);
        RadarData radarLine = new RadarData(xVals, radar);
        //radarChart.setData(radarLine);
        radarChart.setDescription("Your Accelerometer Values");

        return radarLine;

    }

    private PieData pieGraph(){

        //+      TextView accelTest = (TextView) findViewById(R.id.accelString);
//        accelTest.setText(accelArray.get(1));

        ArrayList<Entry> entries = new ArrayList<>(); // y-values
        ArrayList<String> xVals = new ArrayList<String>(); //x values

            Integer xAccelSize = xAccelArray.size();
            Integer yAccelSize = yAccelArray.size();
            Integer zAccelSize = zAccelArray.size();

            Integer total = xAccelSize+yAccelSize+zAccelSize;

            entries.add(new Entry( Float.valueOf(xAccelSize/total), 1));
            entries.add(new Entry( Float.valueOf(yAccelSize/total), 2));
            entries.add(new Entry( Float.valueOf(zAccelSize/total), 3));

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());



            xVals.add(String.valueOf(1));
            xVals.add(String.valueOf(2));
            xVals.add(String.valueOf(3));
            //labels.add("i");


        PieDataSet pieSet = new PieDataSet(entries, "Accelerometer");
        pieSet.setColors(colors);
        Context context = this;

        PieChart pieChart = new PieChart(context);
        //setContentView(chart);
        PieData pieLine = new PieData(xVals, pieSet);
        pieLine.setValueFormatter(new PercentFormatter());
       // pieChart.setData(radarLine);
        pieChart.setDescription("Your Accelerometer Values");

        return pieLine;

    }











    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }




    public void displayData(View view){
        //myListView = (ListView) findViewById(R.id.recordingItemListView);
        final ArrayList<String> databaseEntries = new ArrayList<String>();
        //display data
        databaseEntries.add("Date : " + date);
        databaseEntries.add("Duration: " + duration);
        databaseEntries.add("Distance: " + dist);
        databaseEntries.add("Speed: " + speed);
        databaseEntries.add("Acceleration: " + accel);
        databaseEntries.add("X-Accel: " + xAccel);
        databaseEntries.add("Y-Accel: " + yAccel);
        databaseEntries.add("Z-Accel: " + zAccel);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, databaseEntries);
        //myListView.setAdapter(arrayAdapter);
    }
    public void grabEntryData() {


        //Now, you need to get the data from the bundle
        Bundle extras = getIntent().getExtras();
        //Finally, get the value of the string data associated with key named "myname"
        long id = extras.getLong("ID") +1; //add 1 to align the id with the correct id in the db, if not the data displayed if for the previous entry.
        String s = String.valueOf(id);

        try {

            Cursor res = myDb.getRow(s);
//            if (res.getCount() == 0) {
//                return;
//            }

            StringBuffer buffer = new StringBuffer();

            while (res.moveToNext()) {
                //save data in variables in order to pass them later
                date = res.getString(1);
                duration = res.getString(2);
                dist = res.getString(3);
                speed = res.getString(4);
                accel = res.getString(5);
                xAccel = res.getString(6);
                yAccel = res.getString(7);
                zAccel = res.getString(8);
            }
//                //display data
//                databaseEntries.add("Date : " + date);
//                databaseEntries.add("Duration: " + duration);
//                databaseEntries.add("Distance: " + dist);
//                databaseEntries.add("Speed: " + speed);
//                databaseEntries.add("Acceleration: " + accel);
//                databaseEntries.add("X-Accel: " + xAccel);
//                databaseEntries.add("Y-Accel: " + yAccel);
//                databaseEntries.add("Z-Accel: " + zAccel);

            //}
        } catch (Exception e) {
            Toast.makeText(DatabaseItemActivity.this, "Database is Empty", Toast.LENGTH_SHORT).show();
        }
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, databaseEntries);
//        myListView.setAdapter(arrayAdapter);

        accelArray = createListFromString(accel);
        speedArray = createListFromString(speed);
        distArray = createListFromString(dist);
        xAccelArray = createListFromString(xAccel);
        yAccelArray = createListFromString(yAccel);
        zAccelArray = createListFromString(zAccel);
    }
    public List createListFromString(String string){
        //Split accel into arraylist of strings
        String string2 = string.replace("[", ""); // remove [
        String string3 = string2.replace("]", "");// remove ]
        String string4 = string3.replaceAll("\"", ""); // remove QUOTATION marks
        return Arrays.asList((string4.split(",")));//remove COMMAS
    }



    public void scatterGraph(){

        //+      TextView accelTest = (TextView) findViewById(R.id.accelString);
//        accelTest.setText(accelArray.get(1));

        ArrayList<Entry> entries = new ArrayList<>(); // y-values
        ArrayList<String> labels = new ArrayList<String>(); //x values

        for (int i = 0; i< accelArray.size(); i++){

            entries.add(new Entry( Float.valueOf(accelArray.get(i)), i));
            labels.add("i");
        }
        ScatterDataSet dataSet = new ScatterDataSet(entries, "Accelerometer");

        Context context = this;

        ScatterChart chart = new ScatterChart(context);
        setContentView(chart);
        ScatterData data = new ScatterData(labels, dataSet);
        chart.setData(data);
        chart.setDescription("Your Accelerometer Values");
    }

    public void radarGraphGeneric(){
        ArrayList<Entry> entries = new ArrayList<>(); // y-values
        ArrayList<String> labels = new ArrayList<String>(); //x values

        for (int i = 0; i < accelArray.size(); i++){

            entries.add(new Entry( Float.valueOf(accelArray.get(i)), i));
            labels.add("i");
        }
        RadarDataSet dataSet = new RadarDataSet(entries, "Accelerometer");

        Context context = this;

        RadarChart chart = new RadarChart(context);
        setContentView(chart);
        RadarData data = new RadarData(labels, dataSet);
        chart.setData(data);
        chart.setDescription("Your Accelerometer Values");
    }


}


