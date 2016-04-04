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

/**
 * Created by loic on 4/4/2016.
 */

class CustomAdapter extends ArrayAdapter<String> {
    CustomAdapter(Context context, String[] recordings){
        super(context, R.layout.custom_row, recordings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       LayoutInflater recordinginflator = LayoutInflater.from(getContext());
        View customView = recordinginflator.inflate(R.layout.custom_row, parent, false);

        String singleRecording = getItem(position);
        TextView customText = (TextView) customView.findViewById(R.id.custom_text);
        ImageView customImage = (ImageView) customView.findViewById(R.id.custom_Image);

        customText.setText(singleRecording);
        customImage.setImageResource(R.drawable.save);

        return customView;
    }
}
