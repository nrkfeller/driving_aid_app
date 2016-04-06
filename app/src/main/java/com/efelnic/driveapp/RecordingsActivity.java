package com.efelnic.driveapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    Button deleteButton;
    Cursor cursor;
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

// Test to see if .getRowcol work (yes it did)
//        String testing = myDb.getRowcol(2, 5);
//        System.out.println(testing);

        //queryButton = (Button) findViewById(R.id.queryButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

//        // testing string manipulation (useful for data retrieval and plotting)
//        String testings_manipulations = "[\"0.1234\",\"5.678\"]";
//        String test1 = testings_manipulations.replace("\"", "");
//        String test2 = test1.replace("[", "");
//        String test3 = test2.replace("]", "");
//
//        String[] recordings = {"1", "2", "3", testings_manipulations, test1, test2, test3 };


//        CustomAdapter recordingsAdapter = new CustomAdapter(this, recordings);
        // Construct the data source
        ArrayList<User> arrayOfUsers = new ArrayList<User>();
        // Create the adapter to convert the array to views
        CustomAdapter recordingsAdapter = new CustomAdapter(this, arrayOfUsers);
        ListView recordingsListView = (ListView) findViewById(R.id.recordingsListView);
        recordingsListView.setAdapter(recordingsAdapter);
        grabAllData(recordingsListView);

        //TODO: OnClickListener
        recordingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                      @Override
                                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                          String recording = String.valueOf(parent.getItemAtPosition(position));
                                                          Toast.makeText(RecordingsActivity.this, recording, Toast.LENGTH_SHORT).show();

                                                          //Send ID as a bundle through intent to next activity
                                                          Bundle b = new Bundle();
                                                          //Then, associate the string data stored in anystring with bundle key "ID"
                                                          long ID = parent.getItemIdAtPosition(position);
                                                          b.putLong("ID", id);
                                                          //Now, create an Intent object
                                                          Intent intent = new Intent(RecordingsActivity.this, DatabaseItemActivity.class);
                                                          //Pass bundle object b to the intent
                                                          intent.putExtras(b);
                                                          // and start second activity
                                                          startActivity(intent);

                                                      }

                                                  }
        );
        //TODO: OnClickListener (LONG CLICK)
        recordingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                                                          @Override
                                                          public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                              //  int recording = (int) parent.getItemAtPosition(position);
                                                              String recording = String.valueOf(parent.getItemAtPosition(position));
                                                              id = parent.getItemIdAtPosition(position);
                                                              myDb.deleteData(recording);
                                                              Toast.makeText(getApplicationContext(), "Nothing For Now", Toast.LENGTH_SHORT).show();
                                                              return true;
                                                          }

                                                      }
        );
    }



    public void deleteEverything(View view){
        myDb.deleteEverything();
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
                databaseEntries.add("Date : " + res.getString(5)); //Only show date of each entry to reduce clutter
            }
        } catch (Exception e) {
            Toast.makeText(RecordingsActivity.this, "Database is Empty", Toast.LENGTH_SHORT).show();
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






}


