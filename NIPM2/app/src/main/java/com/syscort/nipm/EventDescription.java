package com.syscort.nipm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EventDescription extends AppCompatActivity {

    HttpPost httppost;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Roll = "rollKey";
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    HttpEntity httpentity;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    EditText rollnum;
    EditText pass;
    int flag = 0;
    String myJSON;
    int id=0;
    JSONArray list1;
    JSONObject jsonobj;
    JSONArray student = null;
    TextView desc1, title1, location1, eventdt1, eventtime1;
    String desc, title, location, eventdt, eventtime;
    TextInputLayout ti;
    String pqr;

    String name, password;
    private final String serverUrl = "http://nipmaurangabad.com/eventclick.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pqr = getIntent().getExtras().getString("roll");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backbutton pressed
                Intent i = new Intent(EventDescription.this, Upcoming.class);
                startActivity(i);
                finish();
            }
        });


        title1  = (TextView)findViewById(R.id.title);
        desc1  = (TextView)findViewById(R.id.desc);
        location1  = (TextView)findViewById(R.id.writer);
        eventdt1  = (TextView)findViewById(R.id.date);
        eventtime1 = (TextView)findViewById(R.id.eventtime);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Getting Events list ready...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();


        Thread t = new Thread(new Runnable() {
            public void run() {
                String result = null;
                InputStream is = null;
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost(serverUrl); // make sure the url is correct.
                    //add your data

                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                    nameValuePairs.add(new BasicNameValuePair("eid", pqr));  // $Edittext_value = $_POST['Edittext_value'];
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);
                    Log.d("andro", "2");
                    httpentity = response.getEntity();
                    is = httpentity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while((line= reader.readLine())!=null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    Log.d("bc", result);

                    EventDescription.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progress.dismiss();

                        }
                    });

                    if (!(result.startsWith("F"))) {
                        Log.i("andro", result);
                        try {
                            jsonobj = new JSONObject(result);
                            list1 = jsonobj.getJSONArray("result");
                            for (int i = 0; i < list1.length(); i++) {
                                JSONObject c = list1.getJSONObject(i);
                                title = c.getString("title");
                                desc = c.getString("detail");
                                location = c.getString("location");
                                eventdt = c.getString("eventdt");
                                eventtime = c.getString("eventtime");

                                Log.d("andro","info="+title+eventtime);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("andro","3");
                                        title1.setText(Html.fromHtml("<b>Title :</b> "+title));

                                        if(desc.length()!=0)
                                            desc1.setText(Html.fromHtml("<b>Description :</b> "+desc));
                                        else
                                            desc1.setText(Html.fromHtml("<b>Description : </b>Not Available"));

                                        if(location.length()!=0)
                                            location1.setText(Html.fromHtml("<b>Location : </b>"+location));
                                        else
                                            location1.setText(Html.fromHtml("<b>Location : </b>Not Available"));

                                        if(eventdt.length()!=0)
                                            eventdt1.setText(Html.fromHtml("<b>Address : </b>"+eventdt));
                                        else
                                            eventdt1.setText(Html.fromHtml("<b>Address : </b>Not Available"));


                                        if(eventtime.length()!=0)
                                            eventtime1.setText(Html.fromHtml("<b>Time : </b>"+eventtime));
                                        else
                                            eventtime1.setText(Html.fromHtml("<b>Time : </b>Not Available"));



                                    }
                                });

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(EventDescription.this, "Cant fetch info", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("andro", e.toString());
                }
            }

        });
        t.start();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent i = new Intent(EventDescription.this, Events.class);
        startActivity(i);
        finish();


    }

}
