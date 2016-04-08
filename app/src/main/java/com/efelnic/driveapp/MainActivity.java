package com.efelnic.driveapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.opengl.Visibility;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    protected static final int RESULT_SETTINGS = 1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    boolean bPermissionGranted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bPermissionGranted = checkLocationPermission();
        }


        Button ScriptButton = (Button)findViewById(R.id.ScriptButton);
        ScriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScriptActivity.class);
                startActivity(intent);
            }


        });

        Button RecordingsButton = (Button)findViewById(R.id.RecordingsButton);
        RecordingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordingsActivity.class);
                startActivity(intent);
            }
        });

        Button AboutButton = (Button)findViewById(R.id.AboutButton);
        AboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

    }


    public void goToTrackingActivity(View view) {
        Intent i = new Intent(getApplicationContext(), TrackingActivity.class);

        startActivity(i);
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.settings, menu);
//        MenuItem save = menu.findItem(R.id.menu_save);
//        MenuItem play = menu.findItem(R.id.menu_play);
//        MenuItem pause = menu.findItem(R.id.menu_pause);
//        save.setVisible(false);
//        play.setVisible(false);
//        pause.setVisible(false);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_settings:
                Intent i = new Intent(this, UserSettingActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    //What is this for??
//    protected void showUserSettings() {
//        SharedPreferences sharedPrefs = PreferenceManager
//                .getDefaultSharedPreferences(this);
//
//        StringBuilder builder = new StringBuilder();
//
//        //What is this? (only commented part)
////        builder.append("\n Username: "
////                + sharedPrefs.getString("prefUsername", "NULL"));
////
////        builder.append("\n Send report:"
////                + sharedPrefs.getBoolean("prefSendReport", false));
////
////        builder.append("\n Sync Frequency: "
////                + sharedPrefs.getString("prefSyncFrequency", "NULL"));
//        //
//
//        //TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);
//
//        //settingsTextView.setText(builder.toString());
//    }



    //**Settings menu in TRACKING ACTIVITY crashes when this is enabled
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case RESULT_SETTINGS:
//                showUserSettings();
//                break;
//
//        }
//
//    }


}







