package com.syscort.nipm;

/**
 * Created by Ashwin on 17/01/17.
 */
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ashwin on 17/01/17.
 */

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final List web;

    private final Integer[] imageId;
    private final Integer[] back;
    public CustomList(Activity context,
                      List web, Integer[] imageId, Integer[] back) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
        this.back = back;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);

        rowView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));


        TextView timedisp = (TextView)rowView.findViewById(R.id.time);
        // rowView.setMinimumHeight(180);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        txtTitle.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText((CharSequence) web.get(position));
        imageView.setImageResource(imageId[position]);
        rowView.setBackgroundResource(back[position]);
        txtTitle.setTextColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rowView.setElevation(100);
        }
        return rowView;
    }

}
