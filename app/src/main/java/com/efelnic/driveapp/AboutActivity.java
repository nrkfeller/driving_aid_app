package com.efelnic.driveapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button PrivacyButton = (Button)findViewById(R.id.privacybutton);
        PrivacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, PrivacyActivity.class);
                startActivity(intent);
            }
        });

        Button TermsButton = (Button)findViewById(R.id.termsbutton);
        TermsButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(AboutActivity.this,TermsActivity.class);
             startActivity(intent);
         }
        });


        Button CreditsButton = (Button)findViewById(R.id.creditsbutton);
        CreditsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,CreditsActivity.class);
                startActivity(intent);
            }
        });


    }
}