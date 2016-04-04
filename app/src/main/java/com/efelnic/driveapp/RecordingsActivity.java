package com.efelnic.driveapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Toolbar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class RecordingsActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    Button queryButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        myDb = new DatabaseHelper(this);

        String testing = myDb.getRowcol(2, 5);
        System.out.println(testing);

        queryButton = (Button) findViewById(R.id.queryButton);

        String[] recordings = {"1", "2", "3"};
        ListAdapter recordingsAdapter = new CustomAdapter(this, recordings);
        ListView recordingsListView = (ListView) findViewById(R.id.recordingsListView);
        recordingsListView.setAdapter(recordingsAdapter);

// TODO: OnClickListener
//        recordingsListView.setOnClickListener(new AdapterView.OnItemClickListener(){
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String recording = String.valueOf(parent.getItemAtPosition(position));
//                        Toast.makeText(RecordingsActivity.this, recording, Toast.LENGTH_LONG).show();
//                    }
//                }
//        );

    }


    public void grabAllData(View view) {
        ListView myListView = (ListView) findViewById(R.id.recordingsListView);
        final ArrayList<String> databaseEntries = new ArrayList<String>();

        try {
            Cursor res = myDb.getAllData();
            if (res.getCount() == 0) {
                return;
            }

            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {

                databaseEntries.add("ID: " + res.getString(0));
                databaseEntries.add("distance: " + res.getString(1));
                databaseEntries.add("acceleration: " + res.getFloat(2));
                databaseEntries.add("duration: " + res.getString(3));
                databaseEntries.add("speed: " + res.getString(4));
                databaseEntries.add("Date : " + res.getString(5));
            }
        } catch (Exception e) {
            Toast.makeText(RecordingsActivity.this, "Database is Empty", Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, databaseEntries);
        myListView.setAdapter(arrayAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }
    /*
    public void viewData(){

        ListView myListView = (ListView)findViewById(R.id.listView);

        final ArrayList<String> databaseEntries = new ArrayList<String>();

        try{
            Cursor res = myDb.getAllData();

            if ( res.getCount() == 0){
                return;
            }

            StringBuffer buffer = new StringBuffer();

            while (res.moveToNext()) {
                databaseEntries.add(res.getString(1) + " " + res.getString(2));
            }
        } catch (Exception e ) {

            Toast.makeText(RecordingsActivity.this, "DB is empty", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, databaseEntries);

        myListView.setAdapter(arrayAdapter);
    } */
}


