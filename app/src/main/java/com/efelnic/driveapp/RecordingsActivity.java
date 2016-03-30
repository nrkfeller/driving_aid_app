package com.efelnic.driveapp;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class RecordingsActivity extends AppCompatActivity   {


    DatabaseHelper myDb;
    Button queryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        myDb = new DatabaseHelper(this);
        queryButton = (Button)findViewById(R.id.queryButton);
    }

    public void grabAllData(View view) {
        ListView myListView = (ListView) findViewById(R.id.listView);
        final ArrayList<String> databaseEntries = new ArrayList<String>();

        try {
            Cursor res = myDb.getAllData();
            if (res.getCount() == 0){
                return;
            }

            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()){
                databaseEntries.add(res.getString(1));
                databaseEntries.add(res.getString(2));
            }
        } catch (Exception e){
            Toast.makeText(RecordingsActivity.this, "Database is Empty", Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, databaseEntries);
        myListView.setAdapter(arrayAdapter);
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


