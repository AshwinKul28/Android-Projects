package com.iashwin28.speech;
// First Page


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Login_activity extends AppCompatActivity {

    private EditText name;
    private EditText pwd;
    private EditText year;
    private static Socket s;
    private static ServerSocket ss;
    private static InputStreamReader isr;
    private static BufferedReader br;
    private static PrintWriter pw;
    String username,pd,yearof;

    String message =  "";
    private static String ip = "172.20.10.3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        name = (EditText)findViewById(R.id.name_text);
        pwd = (EditText)findViewById(R.id.password);
        year = (EditText)findViewById(R.id.yeartext);





    }

    public void sendsignup(View v)
    {
        username = name.getText().toString();
        pd = pwd.getText().toString();
        yearof = year.getText().toString();

        myTask mt = new myTask();
        mt.execute();

        myTask1 mt1 = new myTask1();
        mt1.execute();

        Intent i = new Intent(getApplicationContext(), Login_2.class);
        Login_activity.this.startActivity(i);


    }


    class myTask extends AsyncTask<Void,Void,Void>
    {

        BufferedReader in = null;
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                s = new Socket(ip, 5000);
                pw = new PrintWriter(s.getOutputStream());
//                pw.write(id_ip);
                pw.write(username+" ");
                pw.write(pd+" ");
                pw.write(yearof);

                pw.flush();
                pw.close();
                s.close();
                Log.e("test", "sent");



                ss = new ServerSocket(5000);
                Log.e("test", "Server running");
                s = ss.accept();

                Log.e("test","accepted");

                isr = new InputStreamReader(s.getInputStream());
                br = new BufferedReader(isr);
                message = br.readLine();
//
                Log.e("test", "from server = "+message);

            }
            catch(Exception e)
            {
                Log.e("testp", e.getMessage());
            }

            return null;
        }
    }


    class myTask1 extends AsyncTask<Void,Void,Void>
    {

        BufferedReader in = null;
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                s = new Socket(ip, 5000);
                pw = new PrintWriter(s.getOutputStream());
                pw.write("exit ");
                pw.write("exit ");
                pw.write("exit");


                pw.flush();
                pw.close();
                s.close();
                Log.e("test", "sent");
                Log.e("test", "from server = "+message);

            }
            catch(Exception e)
            {
                Log.e("testp", e.getMessage());
            }

            return null;
        }
    }


    public void directsignin(View v)
    {
        myTask1 mt1 = new myTask1();
        mt1.execute();

        Intent i = new Intent(Login_activity.this, Login_2.class);
        Login_activity.this.startActivity(i);
        finish();
    }
}
