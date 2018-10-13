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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.util.List;

public class Articles extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter ;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    private ArrayList<DataObject> mDataset;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    DataObject obj;
    EditText ip;
    ArrayAdapter<String> adapter;
    JSONArray list;
    AutoCompleteTextView auto;
    String title,date,id;
    ArrayList<String> item1 = new ArrayList<String>();
    AlertDialog.Builder alertDialog;
    JSONObject jsonobj;
    private final String serverUrl = "http://nipmaurangabad.com/articlefetch.php";
    String abc;
    ImageView material;
    JSONArray student = null;
    List<NameValuePair> nameValuePairs;
    HttpEntity httpentity;
    ArrayList results  = new ArrayList<DataObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
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
                Intent i = new Intent(Articles.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });



        if (!isNetworkAvailable()) {
            Log.d("andro", "not connected");

            AlertDialog.Builder builder = new AlertDialog.Builder(Articles.this);
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
        }

        else {

            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Getting Article list ready...");
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

                        Articles.this.runOnUiThread(new Runnable() {
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
                                    id = c.getString("aid");
                                    date = c.getString("writer");





                                    obj = new DataObject(title, date,id);

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

                    Articles.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mAdapter = new MyRecyclerViewAdapter(results);
                            mRecyclerView.setAdapter(mAdapter);

                        }
                    });
                }
            });
            t.start();



//            mAdapter = new MyRecyclerViewAdapter(results);
//            mRecyclerView.setAdapter(mAdapter);

            mRecyclerView.addOnItemTouchListener(    new RecyclerItemClickListener1(getApplicationContext(), mRecyclerView ,new RecyclerItemClickListener1.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {

                    mDataset =results;
                    String abc = mDataset.get(position).getmText3();

                    Log.d("abc","posi"+position);
                    Intent i = new Intent(Articles.this,ArticleDisplay.class);
                    i.putExtra("roll",abc);
                    startActivity(i);
                    finish();

//                    Toast.makeText(Articles.this, "Clcked on this id = "+abc, Toast.LENGTH_SHORT).show();
                }

            }));

        }



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
        Intent i = new Intent(Articles.this, MainActivity.class);
        startActivity(i);
        finish();

    }

}
