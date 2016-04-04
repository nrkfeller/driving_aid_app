package com.efelnic.driveapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by loic on 4/4/2016.
 */






class CustomAdapter extends ArrayAdapter<User> {
    CustomAdapter(Context context, ArrayList<User> users){
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_row, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
        tvName.setText(user.name);
        tvHome.setText(user.hometown);
        // Return the completed view to render on screen
        return convertView;
    }
//        LayoutInflater recordinginflator = LayoutInflater.from(getContext());
//        View customView = recordinginflator.inflate(R.layout.custom_row, parent, false);
//
//        String singleRecording = getItem(position);
//        TextView customText = (TextView) customView.findViewById(R.id.custom_text);
//        ImageView customImage = (ImageView) customView.findViewById(R.id.custom_Image);
//
//        customText.setText(singleRecording);
//        customImage.setImageResource(R.drawable.save);
//
//        return customView;
//    }

}
