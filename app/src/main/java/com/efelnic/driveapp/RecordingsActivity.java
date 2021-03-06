package com.efelnic.driveapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Arrays;
import java.util.List;

public class RecordingsActivity extends MainActivity {

    DatabaseHelper myDb;




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



//        CustomAdapter recordingsAdapter = new CustomAdapter(this, recordings);
        // Construct the data source
        ArrayList<User> arrayOfUsers = new ArrayList<User>();
        // Create the adapter to convert the array to views
        CustomAdapter recordingsAdapter = new CustomAdapter(this, arrayOfUsers);
        final ListView recordingsListView = (ListView) findViewById(R.id.recordingsListView);
        recordingsListView.setAdapter(recordingsAdapter);
        grabAllData(recordingsListView);



        recordingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                      @Override
                                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                          //String recording = String.valueOf(parent.getItemAtPosition(position));
                                                          //Toast.makeText(RecordingsActivity.this, recording, Toast.LENGTH_SHORT).show();

                                                          //Send ID as a bundle through intent to next activity
                                                          Bundle b = new Bundle();
                                                          //Then, associate the string data stored in id with bundle key "ID"
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
//        //TODO: OnClickListener (LONG CLICK)
//        recordingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
//                                                          @Override
//                                                          public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                                                              //  int recording = (int) parent.getItemAtPosition(position);
//                                                              String recording = String.valueOf(parent.getItemAtPosition(position));
//                                                              id = parent.getItemIdAtPosition(position);
//                                                              myDb.deleteData(recording);
//                                                              Toast.makeText(getApplicationContext(), "Nothing For Now", Toast.LENGTH_SHORT).show();
//                                                              return true;
//                                                          }
//
//                                                      }
//        );
    }

    public void deleteEverything(View view){

        myDb.deleteEverything();
        //refresh list (create new one so screen refreshes)
        ArrayList<User> arrayOfUsers = new ArrayList<User>();
        CustomAdapter recordingsAdapter = new CustomAdapter(this, arrayOfUsers);
        ListView recordingsListView = (ListView) findViewById(R.id.recordingsListView);
        recordingsListView.setAdapter(recordingsAdapter);
        grabAllData(recordingsListView);
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

                String lapTimes = res.getString(9);
                List lapTimeArray = Arrays.asList((lapTimes.split(",")));//remove COMMAS
                Integer numOfLaps = lapTimeArray.size();

                //Round Values for display
                double avgLapTime = Math.round(Float.valueOf(res.getString(11)) * 100.00) / 100.00;
                double maxLinAcc = Math.round(Float.valueOf(res.getString(12)) * 100.00) / 100.00;
                double maxXAcc = Math.round(Float.valueOf(res.getString(13)) * 100.00) / 100.00;
                double maxYAcc = Math.round(Float.valueOf(res.getString(14)) * 100.00) / 100.00;
                double maxZAcc = Math.round(Float.valueOf(res.getString(15)) * 100.00) / 100.00;

                databaseEntries.add("Date : " + res.getString(1)+ "\n" +
                        "            Duration: " + res.getString(2) + "\n" +
                        //"            Distance: " + res.getString(3) +"\n" +
                        "            Number of Laps: " + numOfLaps +"\n" +
                        "            Average Lap Time: " + avgLapTime + "s" + "\n" +
                        "            Average Speed: " + res.getString(10) + " kmh/mph" + "\n" +
                        "            Max Linear Accel: " + maxLinAcc + " m/s/s" + "\n" +
                        "            Max X,Y,Z Accel: " + maxXAcc + ", "+ maxYAcc + ", "+ maxZAcc);

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

    public void grabEntryData(String date) {
        try {
            Cursor res = myDb.getIdData();
//            if (res.getCount() == 0) {
//                return;
//            }

            StringBuffer buffer = new StringBuffer();

            while (res.moveToNext()) {

//                databaseEntries.add("ID: " + res.getString(0));
//                databaseEntries.add("distance: " + res.getString(1));
//                databaseEntries.add("acceleration: " + res.getString(2));
//                databaseEntries.add("duration: " + res.getString(3));
//                databaseEntries.add("speed: " + res.getString(4));
//                databaseEntries.add("Date : " + res.getString(5));
            }
        } catch (Exception e) {
            Toast.makeText(RecordingsActivity.this, "Entry is Empty", Toast.LENGTH_SHORT).show();
        }
    }


    //Creating options menu and items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        MenuItem settings = menu.findItem(R.id.menu_settings);//Remove Settings button
        settings.setVisible(false);
        MenuItem delete = menu.findItem(R.id.menu_delete);//Add Delete Database button
        delete.setVisible(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ListView recordingsListView = (ListView) findViewById(R.id.recordingsListView);
        if (item.getItemId() == R.id.menu_delete){
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    RecordingsActivity.this);
            alert.setTitle("Alert!!");
            alert.setMessage("Are you sure you want to delete the database?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteEverything(recordingsListView);
                    dialog.dismiss();

                }

            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            alert.show();
        }

        return super.onOptionsItemSelected(item);
        }

    public List createListFromString(String string){
        //Split accel into arraylist of strings
        String string2 = string.replace("[", ""); // remove [
        String string3 = string2.replace("]", "");// remove ]
        String string4 = string3.replaceAll("\"", ""); // remove QUOTATION marks
        return Arrays.asList((string4.split(",")));//remove COMMAS
    }

}


