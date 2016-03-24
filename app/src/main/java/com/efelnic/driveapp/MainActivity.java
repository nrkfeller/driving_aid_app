package com.efelnic.driveapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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
            public  void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    public void goToTrackingActivity(View view) {
        Intent i = new Intent(getApplicationContext(), TrackingActivity.class);
        startActivity(i);
    }
}
