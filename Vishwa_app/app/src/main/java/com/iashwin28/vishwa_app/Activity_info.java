package com.iashwin28.vishwa_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
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

public class Activity_info extends AppCompatActivity {

    TextView name, std,school,dob,area,c1,c2,date,email,whatsapp,timestamp,egn;
    String name1, std1,school1,dob1,area1,c1_1,c2_1,date1,email1,whatsapp1,timestamp1,egn1;
    HttpPost httppost;
    String pqr;
    HttpResponse response;
    HttpClient httpclient;
    JSONArray list;
    JSONArray student = null;
    List<NameValuePair> nameValuePairs;
    HttpEntity httpentity;

    JSONObject jsonobj;
    private final String serverUrl = "http://vishwaeducation.com/SearchEnquiry.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
         pqr = getIntent().getExtras().getString("roll");
//        Log.d("abc", " Clicked on Item " + pqr);
//        TextView t = (TextView)findViewById(R.id.textView3);
//        t.setText(pqr);

        name = (TextView)findViewById(R.id.name);
        std = (TextView)findViewById(R.id.std);
        school = (TextView)findViewById(R.id.school);
        dob = (TextView)findViewById(R.id.dob);
        area = (TextView)findViewById(R.id.area);
        c1 = (TextView)findViewById(R.id.contact1);
        c2 = (TextView)findViewById(R.id.contact2);
        date = (TextView)findViewById(R.id.date);
        email = (TextView)findViewById(R.id.email);
        whatsapp = (TextView)findViewById(R.id.wa);
        timestamp = (TextView)findViewById(R.id.time);
        egn = (TextView)findViewById(R.id.egn);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Getting Students information ready...");
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
                    nameValuePairs.add(new BasicNameValuePair("id", pqr));  // $Edittext_value = $_POST['Edittext_value'];
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

                    Activity_info.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progress.dismiss();

                        }
                    });

                    if (!(result.startsWith("F"))) {
                        Log.i("andro", result);
                        try {
                            jsonobj = new JSONObject(result);
                            list = jsonobj.getJSONArray("result");
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject c = list.getJSONObject(i);
                                name1=c.getString("name");
                                std1=c.getString("std");
                                school1=c.getString("school");
                                dob1=c.getString("dob");
                                area1=c.getString("area");
                                c1_1=c.getString("contact1");
                                c2_1=c.getString("contact2");
                                date1=c.getString("date");
                                email1=c.getString("email");
                                whatsapp1=c.getString("whatsapp");
                                timestamp1=c.getString("timestamp");
                                egn1=c.getString("egn");

                                Log.d("andro","info="+name1+school1);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("andro","3");
                                        name.setText(Html.fromHtml("<b>Name :</b> "+name1));
                                        std.setText(Html.fromHtml("<b>Standard :</b> "+std1));
                                        if(school1.length()!=0)
                                            school.setText(Html.fromHtml("<b>School : </b>"+school1));
                                        else
                                            school.setText(Html.fromHtml("<b>School : </b>Not Available"));

                                        if(dob1.length()!=0)
                                            dob.setText(Html.fromHtml("<b>Date of Birth : </b>"+dob1));
                                        else
                                            dob.setText(Html.fromHtml("<b>Date of Birth : </b>Not Available"));

                                        if(area1.length()!=0)
                                            area.setText(Html.fromHtml("<b>Address : </b>"+area1));
                                        else
                                            area.setText(Html.fromHtml("<b>Address : </b>Not Available"));

                                        c1.setText(Html.fromHtml("<b>Contact No. 1 : </b>"+c1_1));

                                        if(c2_1.length()!=0)
                                            c2.setText(Html.fromHtml("<b>Contact No. 2 : </b>"+c2_1));
                                        else
                                            c2.setText(Html.fromHtml("<b>Contact No. 2 : </b>Not Available"));

                                        if(date1.length()!=0)
                                            date.setText(Html.fromHtml("<b>Date of Registration : </b>"+date1));
                                        else
                                            date.setText(Html.fromHtml("<b>Date of Registration : </b>Not Available"));

                                        if(email1.length()!=0)
                                            email.setText(Html.fromHtml("<b>Email Id : </b>"+email1));
                                        else
                                            email.setText(Html.fromHtml("<b>Email Id : </b>Not Available"));

                                        if(whatsapp1.length()!=0)
                                            whatsapp.setText(Html.fromHtml("<b>Whatsapp No : </b>"+whatsapp1));
                                        else
                                            whatsapp.setText(Html.fromHtml("<b>Whatsapp No : </b>Not Available"));

                                        if(timestamp1.length()!=0)
                                            timestamp.setText(Html.fromHtml("<b>Time : </b>"+timestamp1));
                                        else
                                            timestamp.setText(Html.fromHtml("<b>Time : </b>Not Available"));

                                        if(egn1.length()!=0)
                                            egn.setText(Html.fromHtml("<b>EGN : </b>"+egn1));
                                        else
                                            egn.setText(Html.fromHtml("<b>EGN : </b>Not Available"));

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
                        Toast.makeText(Activity_info.this, "Cant fetch info", Toast.LENGTH_SHORT).show();
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
            Intent i = new Intent(Activity_info.this, MainActivity.class);
            startActivity(i);
            finish();


    }
}
