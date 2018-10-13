package com.syscort.nipm;

import android.app.ProgressDialog;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Whats_new extends AppCompatActivity {

    HttpPost httppost;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    HttpResponse response;
    HttpClient httpclient;
    DataObject obj;
    HttpEntity httpentity;
    private RecyclerView.LayoutManager mLayoutManager;
    private final String serverUrl = "http://nipmaurangabad.com/whatsfetch.php";
    JSONObject jsonobj;
    String title,date,id;
    private ArrayList<DataObject> mDataset;
    JSONArray list;
    ArrayList results  = new ArrayList<DataObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backbutton pressed
                Intent i = new Intent(Whats_new.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (!isNetworkAvailable()) {
            Log.d("andro", "not connected");

            AlertDialog.Builder builder = new AlertDialog.Builder(Whats_new.this);
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

            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Fetching What's new for you...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();

            Thread t = new Thread(new Runnable() {
                String result = null;
                InputStream isr = null;

                @Override
                public void run() {
                    try {

                        httpclient = new DefaultHttpClient();
                        httppost = new HttpPost(serverUrl);

                        response = httpclient.execute(httppost);
                        httpentity = response.getEntity();
                        isr = httpentity.getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "UTF-8"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        result = sb.toString();
                        Log.d("bc", result);

                        Whats_new.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                progress.dismiss();

                            }
                        });

                        if (!(result.startsWith("N"))) {
                            try {
                                jsonobj = new JSONObject(result);
                                list = jsonobj.getJSONArray("result");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject c = list.getJSONObject(i);
                                    title = c.getString("title");
                                    id = c.getString("wid");
                                    date = "";


                                    obj = new DataObject(title, date, id);

                                    results.add(obj);


                                }


                            } catch (JSONException j) {
                                j.printStackTrace();
                            }
                        } else {
                            Log.d("bc", "idhar");
                        }

                    } catch (Exception e) {
                    }

                    Whats_new.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mAdapter = new MyRecyclerViewAdapter(results);
                            mRecyclerView.setAdapter(mAdapter);

                        }
                    });
                }
            });
            t.start();

        }

        mRecyclerView.addOnItemTouchListener(    new RecyclerItemClickListener1(getApplicationContext(), mRecyclerView ,new RecyclerItemClickListener1.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {

                mDataset =results;
                String abc = mDataset.get(position).getmText3();

                Log.d("abc","posi"+position);
                Intent i = new Intent(Whats_new.this,New.class);
                i.putExtra("roll",abc);
                startActivity(i);
                finish();

                   // Toast.makeText(Whats_new.this, "Clcked on this id = "+abc, Toast.LENGTH_SHORT).show();
            }

        }));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Whats_new.this, MainActivity.class);
        startActivity(i);
        finish();

    }

}
