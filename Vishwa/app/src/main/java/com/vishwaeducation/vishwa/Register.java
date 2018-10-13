package com.vishwaeducation.vishwa;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Register extends AppCompatActivity {
    EditText name,school,c1,c2,email,wno,dob,date;
    AutoCompleteTextView std,area,egn;
    ProgressDialog p;
    String resp;
    String server = "http://www.vishwaeducation.com/Enquiry",auth;
    FloatingActionButton fab;
    SimpleDateFormat dateFormatter;
    Calendar c;
    int plen = 0;
    boolean isFormatting;
    boolean deletingHyphen;
    int hyphenStart;
    boolean deletingBackward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Enquiry Form");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        p = new ProgressDialog(this);
        p.setMessage("Connecting to server..");
        p.setIndeterminate(true);
        p.show();
        init();
        load();
    }
    public void init(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
        name = (EditText)findViewById(R.id.name);
        school = (EditText)findViewById(R.id.school);
        c1 = (EditText)findViewById(R.id.c1);
        c2 = (EditText)findViewById(R.id.c2);
        email = (EditText)findViewById(R.id.email);
        wno = (EditText)findViewById(R.id.wno);
        std = (AutoCompleteTextView)findViewById(R.id.std);
        area = (AutoCompleteTextView)findViewById(R.id.area);
        egn = (AutoCompleteTextView)findViewById(R.id.egn);
        date = (EditText) findViewById(R.id.date);
        dob = (EditText) findViewById(R.id.dob);
        c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        date.setText(df.format(c.getTime()));
        auth = "name="+getIntent().getStringExtra("name")+"&pass="+getIntent().getStringExtra("pass");
        dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (isFormatting)
                    return;
                // Make sure user is deleting one char, without a selection
                final int selStart = Selection.getSelectionStart(s);
                final int selEnd = Selection.getSelectionEnd(s);
                if (s.length() > 1 // Can delete another character
                        && count == 1 // Deleting only one character
                        && after == 0 // Deleting
                        && s.charAt(start) == '-' // a hyphen
                        && selStart == selEnd) { // no selection
                    deletingHyphen = true;
                    hyphenStart = start;
                    // Check if the user is deleting forward or backward
                    if (selStart == start + 1) {
                        deletingBackward = true;
                    } else {
                        deletingBackward = false;
                    }
                } else {
                    deletingHyphen = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (isFormatting)
                    return;
                isFormatting = true;
                // If deleting hyphen, also delete character before or after it
                if (deletingHyphen && hyphenStart > 0) {
                    if (deletingBackward) {
                        if (hyphenStart - 1 < text.length()) {
                            text.delete(hyphenStart - 1, hyphenStart);
                        }
                    } else if (hyphenStart < text.length()) {
                        text.delete(hyphenStart, hyphenStart + 1);
                    }
                }
                if (text.length() == 2 || text.length() == 5) {
                    text.append('-');
                }
                isFormatting = false;
            }
        });
    }

    public void load(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String d = "";
                String text = "";
                BufferedReader reader = null;
                try
                {
                    URL url = new URL(server + "/backend/settings");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                    }
                    text = sb.toString();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    return;
                }
                finally
                {
                    try
                    {

                        reader.close();
                    }

                    catch(Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                }
                try {
                    final JSONObject jObj = new JSONObject(text);
                    final String s1 = jObj.getString("std"),s2 = jObj.getString("area"),s3 = jObj.getString("egn");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            std.setAdapter(new ArrayAdapter<String>(Register.this, android.R.layout.simple_dropdown_item_1line, s1.split(",")));
                            area.setAdapter(new ArrayAdapter<String>(Register.this, android.R.layout.simple_dropdown_item_1line, s2.split(",")));
                            egn.setAdapter(new ArrayAdapter<String>(Register.this, android.R.layout.simple_dropdown_item_1line, s3.split(",")));
                            p.dismiss();
                        }
                    });
                }
                catch(Exception e){
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }
    public void send(){
        String s;
        if(name.getText().toString().equals("") || std.getText().toString().equals("") || c1.getText().toString().equals("")){
            Snackbar.make(fab,"Fill all required fields",Snackbar.LENGTH_LONG).show();
            return;
        }
        try {
            s = "i0=" + URLEncoder.encode(name.getText().toString(), "utf-8") + "&i1=" + URLEncoder.encode(std.getText().toString(), "utf-8") + "&i2=" + URLEncoder.encode(school.getText().toString(), "utf-8") + "&i4=" + URLEncoder.encode(area.getText().toString(), "utf-8") + "&i3=" + dob.getText().toString() + "&i5=" + URLEncoder.encode(c1.getText().toString(), "utf-8") + "&i6=" + URLEncoder.encode(c2.getText().toString(), "utf-8") + "&i7=" + date.getText().toString() + "&i8=" + URLEncoder.encode(email.getText().toString(), "utf-8") + "&i9=" + URLEncoder.encode(wno.getText().toString(), "utf-8") + "&i10=" + URLEncoder.encode(egn.getText().toString(), "utf-8");
        }   catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return;
        }
        Snackbar.make(fab,"Sending data.. Please wait..",Snackbar.LENGTH_LONG).show();
        sendData(s);
    }

    private void sendData(final String data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String text = "";
                BufferedReader reader = null;
                try
                {
                    URL url = new URL(server + "/backend/action.php?task=1&"+data+"&"+auth);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }

                    text = sb.toString();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    resp = "Server unreachable.";
                    return;
                }
                finally
                {
                    try
                    {

                        reader.close();
                    }

                    catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
                resp =  text;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(resp.equals("OK\n")){
                            Snackbar.make(fab,"Enquiry uploaded.",Snackbar.LENGTH_LONG).show();
                            reset();
                        }else{
                            Snackbar.make(fab,"Error : "+resp,Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            reset();
            return true;
        }else if(id == R.id.action_logout) {
            SharedPreferences sp = getSharedPreferences("Preference", Context.MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.putString("email","");
            e.putString("pass", "");
            e.apply();
            Intent i = new Intent(Register.this,LoginActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void reset(){
        name.setText("");
        std.setText("");
        school.setText("");
        c1.setText("");
        c2.setText("");
        area.setText("");
        email.setText("");
        wno.setText("");
        egn.setText("");
        dob.setText("");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date.setText(df.format(c.getTime()));
    }
}
