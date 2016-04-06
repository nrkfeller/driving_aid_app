package com.efelnic.driveapp;

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
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;

public class DatabaseItemActivity extends AppCompatActivity {

    ListView myListView;

    DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_item);
        myListView = (ListView) findViewById(R.id.recordingItemListView);
        myDb = new DatabaseHelper(this);

        grabEntryData(myListView);



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
            }
        } catch (Exception e) {
            Toast.makeText(DatabaseItemActivity.this, "Database is Empty", Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, databaseEntries);
        myListView.setAdapter(arrayAdapter);
    }
}
