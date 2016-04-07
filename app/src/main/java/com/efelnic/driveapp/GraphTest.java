package com.efelnic.driveapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;


import java.util.ArrayList;

public class GraphTest extends AppCompatActivity {

    DatabaseHelper myDb;
    Button graphType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_test);

       // myDb = new DatabaseHelper(this);
       // graphData();
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(12f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));
        RadarDataSet dataSet = new RadarDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");


        Context context = this;

        RadarChart chart = new RadarChart(context);
        setContentView(chart);
        RadarData data = new RadarData(labels, dataSet);
        chart.setData(data);
        chart.setDescription("# of times Alice called Bob");



    }

//        public void graphData() {
//        graphType.setOnClickListener(
//                new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        Cursor res = myDb.getRow("ACCELERATION");
//                        if (res.getCount() == 0){
//                            //showMessage("Error", "No data found");
//                            return;
//                        }
//
//                        StringBuffer buffer = new StringBuffer();
//                        while ( res.moveToNext() ) {
//                            buffer.append("ID : " + res.getString(0) + "\n");
//                            buffer.append("Distance : " + res.getString(1) + "\n");
//                            buffer.append("Speed : " + res.getString(2) + "\n\n");
//                        }
//                        showMessage("Data", buffer.toString());
//                    }
//                }
//        );
//    }
//
//
}
