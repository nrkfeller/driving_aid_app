package com.efelnic.driveapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseItemActivity extends AppCompatActivity {

    ListView myListView;

    DatabaseHelper myDb;
    String accel, speed, dist;

    List<String> accelArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_item);
        myListView = (ListView) findViewById(R.id.recordingItemListView);
        myDb = new DatabaseHelper(this);

        grabEntryData(myListView);

        radarGraph();
        scatterGraph();


    }

    public void grabEntryData(View view) {
        myListView = (ListView) findViewById(R.id.recordingItemListView);
        final ArrayList<String> databaseEntries = new ArrayList<String>();


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

//                databaseEntries.add("ID: " + res.getString(0));
                databaseEntries.add("distance: " + res.getString(1));
                databaseEntries.add("acceleration: " + res.getString(2));
                databaseEntries.add("duration: " + res.getString(3));
                databaseEntries.add("speed: " + res.getString(4));
                databaseEntries.add("Date : " + res.getString(5));
                dist = res.getString(1);
                accel = res.getString(2);
                speed = res.getString(4);
            }
        } catch (Exception e) {
            Toast.makeText(DatabaseItemActivity.this, "Database is Empty", Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, databaseEntries);
        myListView.setAdapter(arrayAdapter);

        //Split accel into arraylist of strings
        String accel2 = accel.replace("[", ""); // remove [
        String accel3 = accel2.replace("]", "");// remove ]
        String accel4 = accel3.replaceAll("\"", ""); // remove QUOTATION marks
        accelArray = Arrays.asList((accel4.split(",")));//remove COMMAS

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
    public void radarGraph(){





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
