package com.efelnic.driveapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RawDataActivity extends AppCompatActivity {

    ListView myListView;
    String date, duration, dist, speed, accel, xAccel, yAccel, zAccel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_data);

        Bundle extras = getIntent().getExtras();
        date = extras.getString("DATE");
        duration = extras.getString("DURATION");
        dist = extras.getString("DISTANCE");
        speed = extras.getString("SPEED");
        accel = extras.getString("ACCELERATION");
        xAccel = extras.getString("XACCEL");
        yAccel = extras.getString("YACCEL");
        zAccel = extras.getString("ZACCEL");

        displayData(myListView);
    }



    public void displayData(View view){
        myListView = (ListView) findViewById(R.id.rawDataItemListView);
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
        myListView.setAdapter(arrayAdapter);
    }
}

