package com.efelnic.driveapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ScriptActivity extends MainActivity {

    HashMap timeAndMessages;

    EditText message;
    EditText time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);

        timeAndMessages = new HashMap();

        message = (EditText)findViewById(R.id.messageText);
        time = (EditText)findViewById(R.id.timeText);
    }

    public void createMessage (View view){

        String stringmessage = message.getText().toString();
        String inttime = time.getText().toString();

        timeAndMessages.put(inttime, stringmessage);

        Toast.makeText(getApplicationContext(), "Will display : " + stringmessage + " At time : " + inttime , Toast.LENGTH_LONG).show();
    }

    public void removeMessage(View view) {
        timeAndMessages = new HashMap();
        Toast.makeText(getApplicationContext(), "Messages Cleared", Toast.LENGTH_LONG).show();
    }

    public void viewAllMessages(View view) {
        Toast.makeText(getApplicationContext(), timeAndMessages.toString(), Toast.LENGTH_LONG).show();
    }

    public void saveToOutputStream(View view) {
//        Gson gson = new Gson();
//        String hashMapString = gson.toJson(timeAndMessages);
//
//        //save in shared prefs
//        SharedPreferences prefs = getSharedPreferences("test", MODE_PRIVATE);
//        prefs.edit().putString("hashString", hashMapString).apply();
//
//        //get from shared prefs

        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            Toast.makeText(getApplicationContext(), timeAndMessages.toString(), Toast.LENGTH_LONG).show();
            JSONObject jsonObject = new JSONObject(timeAndMessages);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map").commit();
            editor.putString("My_map", jsonString);
            editor.commit();
        }

    }
}
