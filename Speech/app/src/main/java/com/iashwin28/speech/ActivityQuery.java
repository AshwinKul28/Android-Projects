package com.iashwin28.speech;
//Fourth Activity


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

public class ActivityQuery extends AppCompatActivity {

    private static Socket s;
    private static Socket s1;
    private static ServerSocket ss;
    private static InputStreamReader isr;
    private static BufferedReader br;
    private static PrintWriter pw;

    EditText question,answer;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String ID;
    String temp_text;
    String message =  "";
    private static String ip = "172.20.10.3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        ID = getIntent().getExtras().getString("ID");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            question = (EditText)findViewById(R.id.question);
            answer = (EditText)findViewById(R.id.answer);
            btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

            btnSpeak.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    promptSpeechInput();
                }
            });

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ActivityQuery.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


                    message = result.get(0);


                    message = message.replace(" comma", ",");
                    message = message.replace("full stop", ".");





                    question.setText(message);

                }
                break;
            }

        }
    }


    public void sendtext(View v)
    {

        message = question.getText().toString();
        myTask2 mt = new myTask2();
        mt.execute();
    }

    class myTask2 extends AsyncTask<Void,Void,Void>
    {
        ///////////////////////////////btn for query sending
        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                Log.e("test","query socket is opened");
                s = new Socket(ip, 5600);
                pw = new PrintWriter(s.getOutputStream());
//                pw.write(id_ip);
                pw.write(message);
                Log.e("test", message);
                pw.flush();
                pw.close();
                s.close();
                Log.e("test", "sent");

                // Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();


                ss = new ServerSocket(8800);
                Log.e("test", "Server running");
                s1 = ss.accept();

                Log.e("test","accepted");


                isr = new InputStreamReader(s1.getInputStream());
                Log.e("test","step1");
                br = new BufferedReader(isr);
                Log.e("test","step2");                 ////////message from server to client

                temp_text = br.readLine();



                Log.e("test",temp_text);

                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {

                        answer.setText(temp_text);
                        Toast.makeText(ActivityQuery.this, "Yeah!! you can ask another query", Toast.LENGTH_SHORT).show();
                    }
                }));

                ss.close();
                br.close();
                s1.close();


            }
            catch(Exception e)
            {
                Log.e("test", e.getMessage());
            }

            return null;
        }
    }


    public void reportgenerate(View v)
    {

        message = question.getText().toString();
        myTask3 mt = new myTask3();
        mt.execute();
    }


    class myTask3 extends AsyncTask<Void,Void,Void>
    {
        ///////////////////////////////btn for query sending
        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                Log.e("test","query socket is opened");
                s = new Socket(ip, 5600);
                pw = new PrintWriter(s.getOutputStream());
//                pw.write(id_ip);
                pw.write("exit");
                Log.e("test", "exit");
                pw.flush();
                pw.close();
                s.close();
                Log.e("test", "sent");

                // Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();


                ss = new ServerSocket(8800);
                Log.e("test", "Server running");
                s1 = ss.accept();

                Log.e("test","accepted");


                isr = new InputStreamReader(s1.getInputStream());
                Log.e("test","step1");
                br = new BufferedReader(isr);
                Log.e("test","step2");                 ////////message from server to client

                temp_text = br.readLine();



                Log.e("test",temp_text);

                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if(temp_text.equals("exit"))
                        {
                            Intent i = new Intent(ActivityQuery.this, ReportActivity.class);
                            i.putExtra("ID",ID);
                            startActivity(i);
                            finish();
                            Toast.makeText(ActivityQuery.this, "Generating Report", Toast.LENGTH_SHORT).show();
                        }


                    }
                }));

                ss.close();
                br.close();
                s1.close();


            }
            catch(Exception e)
            {
                Log.e("test", e.getMessage());
            }

            return null;
        }
    }
}
