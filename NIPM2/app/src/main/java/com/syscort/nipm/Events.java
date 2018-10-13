package com.syscort.nipm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class Events extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter ;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    private ArrayList<DataObject1> mDataset;
    DataObject1 obj;
    ArrayList results  = new ArrayList<DataObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backbutton pressed
                Intent i = new Intent(Events.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        if (!isNetworkAvailable()) {
            Log.d("andro", "not connected");

            AlertDialog.Builder builder = new AlertDialog.Builder(Events.this);
            builder.setTitle("Connectivity Error.");
            builder.setMessage("Check your Internet Connection.")
                    .setCancelable(false)
                    .setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            final Context ctx = getApplicationContext();
                            Intent i = new Intent(Settings.ACTION_SETTINGS);
                            // i.setClassName("com.android.phone","com.android.phone.NetworkSetting");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ctx.startActivity(i);
                        }
                    });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.show();
        } else {


            obj = new DataObject1("UPCOMING EVENTS", "1");
            results.add(obj);

            obj = new DataObject1("PAST EVENTS", "2");
            results.add(obj);


            mAdapter = new MyRecyclerViewAdapter_Events(results);
            mRecyclerView.setAdapter(mAdapter);


            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener1(getApplicationContext(), mRecyclerView, new RecyclerItemClickListener1.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    mDataset = results;
                    String abc = mDataset.get(position).getmText2();

                    Log.d("abc", "posi" + position);
//                    Intent i = new Intent(Events.this,.class);
//                    i.putExtra("roll",abc);
//                    startActivity(i);
//                    finish();

//                Toast.makeText(Events.this, "Clcked on this id = "+abc, Toast.LENGTH_SHORT).show();

                    if (Integer.parseInt(abc) == 1) {
                        Intent i = new Intent(Events.this, Upcoming.class);
                        startActivity(i);
                        finish();
                    }

                    if (Integer.parseInt(abc) == 2) {
                        Intent i = new Intent(Events.this, Past.class);
                        startActivity(i);
                        finish();
                    }
                }

            }));

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Events.this, MainActivity.class);
        startActivity(i);
        finish();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
