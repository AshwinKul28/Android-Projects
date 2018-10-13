package com.iashwin28.speech;
// Second Activity


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

public class Login_2 extends AppCompatActivity {

    private EditText name;
    private EditText pwd;
    private static Socket s;
    private static Socket s1;
    private static ServerSocket ss;
    private static ServerSocket ss1;
    private static InputStreamReader isr;
    private static BufferedReader br;
    private static PrintWriter pw;

    String message = "";
    String namesend =  "";
    String pwdsend = "";
    private static String ip = "172.20.10.3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        name = (EditText)findViewById(R.id.textnameedit);
        pwd = (EditText)findViewById(R.id.textpassword);

    }

    public void login(View v)
    {
        namesend = name.getText().toString();
        pwdsend = pwd.getText().toString();

        myTask1 mt = new myTask1();
        mt.execute();
    }


    class myTask1 extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                s = new Socket(ip, 6000);
                pw = new PrintWriter(s.getOutputStream());

                pw.write(namesend+" ");
                pw.write(pwdsend);

                pw.flush();
                pw.close();
                s.close();
                Log.e("test", "sent");



                ss = new ServerSocket(6000);
                Log.e("test", "Server running");
                s1 = ss.accept();

                Log.e("test","accepted");



                isr = new InputStreamReader(s1.getInputStream());
                Log.e("test","step1");
                br = new BufferedReader(isr);
                Log.e("test","step2");////////message from server to client

                message = br.readLine();

                Log.e("test",message);

                if(!message.equals(null))
                {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("ID",message);
                    Login_2.this.startActivity(i);
                    finish();
                }
                else
                {
//                    runOnUiThread(new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(Login_2.this, "Incorrect username/password", Toast.LENGTH_SHORT).show();
//                        }
//                    }));
                    
                }

                s1.close();
                br.close();
                ss.close();

            }
            catch(Exception e)
            {
                Log.e("test", e.getMessage());
            }

            return null;
        }
    }


}
