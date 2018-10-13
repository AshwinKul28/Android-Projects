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

public class New extends AppCompatActivity {

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
    private final String serverUrl = "http://nipmaurangabad.com/whatsclick.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pqr = getIntent().getExtras().getString("roll");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backbutton pressed
                Intent i = new Intent(New.this, Whats_new.class);
                startActivity(i);
                finish();
            }
        });

        title1  = (TextView)findViewById(R.id.title);
        desc1  = (TextView)findViewById(R.id.desc);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Getting Information ready...");
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
                    nameValuePairs.add(new BasicNameValuePair("wid", pqr));  // $Edittext_value = $_POST['Edittext_value'];
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

                    New.this.runOnUiThread(new Runnable() {
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





                                    }
                                });

                            }

//                            Activity_info.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Log.d("abc","3");
//                                    name.setText("Name : "+name1);
//                                    std.setText("Standard : "+std1);
//                                    if(school1.length()!=0)
//                                        school.setText("School : "+school1);
//                                    else
//                                        school.setText("School : Not Available");
//
//                                    if(dob1.length()!=0)
//                                        dob.setText("Date of Birth : "+dob1);
//                                    else
//                                        dob.setText("Date of Birth : Not Available");
//
//                                    if(area1.length()!=0)
//                                        area.setText("Address : "+area1);
//                                    else
//                                        area.setText("Address : Not Available");
//
//                                    c1.setText("Contact No. 1 : "+c1_1);
//
//                                    if(c2_1.length()!=0)
//                                        c2.setText("Contact No. 2 : "+c2_1);
//                                    else
//                                        c2.setText("Contact No. 2 : Not Available");
//
//                                    if(date.length()!=0)
//                                        date.setText("Date : "+school1);
//                                    else
//                                        date.setText("Date : Not Available");
//
//                                }
//                            });



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(New.this, "Cant fetch info", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(New.this, Whats_new.class);
        startActivity(i);
        finish();

    }


}
