package com.efelnic.driveapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Iterator;
import java.util.Map;

public class ScriptActivity extends MainActivity {

    HashMap timeAndMessages;

    EditText message;
    EditText time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);

        timeAndMessages = (HashMap) loadMap();

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
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            Toast.makeText(getApplicationContext(), "Messages Cleared", Toast.LENGTH_LONG).show();
            JSONObject jsonObject = new JSONObject(timeAndMessages);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map").commit();
            editor.putString("My_map", jsonString);
            editor.commit();
        }
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
            Toast.makeText(getApplicationContext(), "Commited Race Script", Toast.LENGTH_LONG).show();
            JSONObject jsonObject = new JSONObject(timeAndMessages);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map").commit();
            editor.putString("My_map", jsonString);
            editor.commit();
        }

    }

    private Map<String,String> loadMap(){
        Map<String,String> outputMap = new HashMap<String,String>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }


//Creating options menu and items
    //Used to remove settings icon from actionbar(since activity extends MainActivity it is there by default
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        MenuItem settings = menu.findItem(R.id.menu_settings);//Display save option
        settings.setVisible(false);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
//end of Options menu and items
}
