package com.iashwin28.speech;

//Third Activity


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private EditText txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private static Socket s;
    private static Socket s1;
    private static ServerSocket ss;
    private static InputStreamReader isr;
    private static BufferedReader br;
    private static PrintWriter pw;
    private EditText ed;
    String id_ip;
    String currentDateandTime;

    SweetAlertDialog pDialog;

    String ID;
    String temp_text;
    String message =  "";
    private static String ip = "172.20.10.3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ID = getIntent().getExtras().getString("ID");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
       currentDateandTime = sdf.format(new Date());

        Toast.makeText(this, "time"+currentDateandTime, Toast.LENGTH_SHORT).show();

        txtSpeechInput = (EditText) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
//        ed = (EditText)findViewById(R.id.idgenerate);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
        // hide the action bar


        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

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
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
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





                    txtSpeechInput.setText(message);

                }
                break;
            }

        }
    }


    public void sendtext(View v)
    {

        message = txtSpeechInput.getText().toString();
        int flag =0;

        for(int i=0; i<(message.length()-1);i++)
        {
            if(!((message.indexOf(i)=='B' && message.indexOf(i+1)=='P') || (message.indexOf(i)=='b' && message.indexOf(i+1)=='p')))
            {
                flag++;
            }

            if(!((message.indexOf(i)=='H' && message.indexOf(i+1)=='R') || (message.indexOf(i)=='h' && message.indexOf(i+1)=='r')))
            {
                flag++;
            }

        }




        myTask mt = new myTask();
        mt.execute();



    }



    class myTask extends AsyncTask<Void,Void,Void>
    {




        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Take a deep breath...");
                        pDialog.setContentText("Wait! Your data is under diagnosis");
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }
                }));



                s = new Socket(ip, 9000);
                pw = new PrintWriter(s.getOutputStream());
//                pw.write(id_ip);
                pw.write(ID+"    ");
                pw.write(currentDateandTime+"    ");
                pw.write(message);
                Log.e("test", message);
                pw.flush();
                pw.close();
                s.close();
                Log.e("test", "sent");

               // Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();


                ss = new ServerSocket(9000);
                Log.e("test", "Server running");
                s1 = ss.accept();

                Log.e("test","accepted");




                isr = new InputStreamReader(s1.getInputStream());
                Log.e("test","step1");
                br = new BufferedReader(isr);
                Log.e("test","step2");                 ////////message from server to client

                temp_text = br.readLine();

                Log.e("test","temp_text"+temp_text);

                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(temp_text.equals("OK")) {
                            pDialog.dismissWithAnimation();

                            Intent i = new Intent(getApplicationContext(), ActivityQuery.class);
                            i.putExtra("ID",ID);
                            MainActivity.this.startActivity(i);
                            finish();
                        }

                    }
                }));



            }
            catch(Exception e)
            {
                Log.e("test", e.getMessage());
            }

            return null;
        }
    }





//    public void generate(View v)
//    {
//        myTask1 mt = new myTask1();
//        mt.execute();
//    }


    class myTask1 extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                s = new Socket(ip, 5600);
                pw = new PrintWriter(s.getOutputStream());
//                pw.write(id_ip);
                pw.write("exit"+"    ");
                Log.e("test", message);
                pw.flush();
                pw.close();
                s.close();
                Log.e("test", "sent");

                // Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();


                ss = new ServerSocket(9000);
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
                        if(temp_text.equals("OK"));
                        txtSpeechInput.setText("Yeah!! Data is processed, you can ask a query now.");

                    }
                }));



            }
            catch(Exception e)
            {
                Log.e("test", e.getMessage());
            }

            return null;
        }
    }


//    public void ask(View v)
//    {
//
//        myTask2 mt = new myTask2();
//        mt.execute();
//    }

    class myTask2 extends AsyncTask<Void,Void,Void>
    {
  ///////////////////////////////btn for query sending
        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                Log.e("test","query socket is opened");
                s = new Socket(ip, 5600);
                pw = new PrintWriter(s.getOutputStream());
//              pw.write(id_ip);
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

                        txtSpeechInput.setText(temp_text);
                        Toast.makeText(MainActivity.this, "Yeah!! you can ask another query", Toast.LENGTH_SHORT).show();
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
