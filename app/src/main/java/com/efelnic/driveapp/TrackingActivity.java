package com.efelnic.driveapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TrackingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        trackLongitude();
        trackLatitude();
        trackAcceleration();
        trackGyroscope();
        trackTime();

    }

    public void trackLongitude(){
        
    }

    public void trackLatitude(){

    }

    public void trackAcceleration(){

    }

    public void trackGyroscope(){

    }
    public void trackTime(){

    }

}
