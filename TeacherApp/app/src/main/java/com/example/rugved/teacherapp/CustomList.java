package com.example.rugved.teacherapp;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final List web;


    private final Integer[] back;
    public CustomList(Activity context,
                      List web, Integer[] back) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;

        this.back = back;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        rowView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));


        TextView timedisp = (TextView)rowView.findViewById(R.id.time);
       // rowView.setMinimumHeight(180);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        //txtTitle.setTextColor(R.color.color2);
        txtTitle.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText((CharSequence) web.get(position));

//       imageView.setImageResource(imageId[position]);
        rowView.setBackgroundResource(back[position]);
        return rowView;
    }
}