package com.efelnic.driveapp;

import android.content.Intent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        Button GraphButton = (Button)findViewById(R.id.GraphButton);
        GraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraphTest.class);
                startActivity(intent);
            }
        });
    }

    public void goToTrackingActivity(View view) {
        Intent i = new Intent(getApplicationContext(), TrackingActivity.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.settings, menu);
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


    protected void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        //What is this? (only commented part)
//        builder.append("\n Username: "
//                + sharedPrefs.getString("prefUsername", "NULL"));
//
//        builder.append("\n Send report:"
//                + sharedPrefs.getBoolean("prefSendReport", false));
//
//        builder.append("\n Sync Frequency: "
//                + sharedPrefs.getString("prefSyncFrequency", "NULL"));
        //

        TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);

        settingsTextView.setText(builder.toString());
    }



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







