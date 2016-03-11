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


        Button readyButton = (Button)findViewById(R.id.readyButton);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrackingActivity.class);
                startActivity(intent);
            }
        });


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
          public  void onClick(View v) {
             Intent intent = new Intent(MainActivity.this, RecordingsActivity.class);
             startActivity(intent);
         }
                                            });

    }
}
