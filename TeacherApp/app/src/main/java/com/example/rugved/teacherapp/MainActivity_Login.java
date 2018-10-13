package com.example.rugved.teacherapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import static android.R.attr.buttonStyleInset;
import static android.R.attr.name;

public class MainActivity_Login extends AppCompatActivity {
//All declarations-------------------
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    HttpEntity httpentity;
    List<NameValuePair> nameValuePairs;
    String myJSON;
    JSONObject jsonobj;
    JSONArray student = null;
    private final String serverUrl = "http://cseapp.16mb.com/tlogin.php";
    int flag = 0;
    EditText editText_username;
    EditText editText_password;
    String username;
    String password;
    String received_username;
    String received_name;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        editText_username = (EditText)findViewById(R.id.editText_username);
        editText_password = (EditText)findViewById(R.id.editText_password);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loginClicked(View view) {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        if (isNetworkAvailable()) {
            username = editText_username.getText().toString().trim().toUpperCase();
            password = editText_password.getText().toString();
            if (username.isEmpty()) {
                editText_username.setError("Username must not be empty!");
            } else {
                editText_username.setError(null);
                flag++;
            }

            if (password.isEmpty() || password.length() < 3 || password.length() > 18) {
                editText_password.setError("Password must be alphanumeric between 3 to 18 charachters!");
            } else {
                editText_password.setError(null);
                flag++;
            }

            if (flag == 2) {
                dialog = ProgressDialog.show(MainActivity_Login.this, "Validation", "Validating user", true);
                flag = 0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        attemptLogin();
                    }
                }).start();
            }
        } else if (!isNetworkAvailable()){
            Log.d("android", "not connected to internet");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity_Login.this);
            alertDialogBuilder.setTitle("Connectivity Error");
            alertDialogBuilder.setMessage("Check your Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton("Goto Phone Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogInterface, int id){
                    final Context ctx = getApplicationContext();
                    Intent i = new Intent(Settings.ACTION_SETTINGS);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(i);
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialogBuilder.show();
        }
    }

    public void attemptLogin(){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null; InputStream is = null;
        try{

            httpclient=new DefaultHttpClient();
            httppost= new HttpPost(serverUrl); // make sure the url is correct.
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tid",username));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("pass",password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);
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
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            final String response = httpclient.execute(httppost, responseHandler);
//            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    // tv.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });

            if(!(result.startsWith("F"))){
                Log.i("andro",result);
                try {
                    jsonobj = new JSONObject(result);
                    student = jsonobj.getJSONArray("result");
                    for (int i = 0; i < student.length(); i++) {
                        JSONObject c = student.getJSONObject(i);
                        received_username = c.getString("tid");
                        received_name = c.getString("name");
                        Log.d("andro",received_username+received_name);

                    }
                }catch(JSONException j)
                {
                    j.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
//                        SaveSharedPreference.setUserName(MainActivity_Login.this,received_username,received_name);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString(Roll,name);
                        Toast.makeText(MainActivity_Login.this,"Login Success", Toast.LENGTH_SHORT).show();
                        Intent menuIntent = new Intent(MainActivity_Login.this, MainActivity.class);
                        menuIntent.putExtra("rollnum",name);
                        startActivity(menuIntent);
                        finish();
                    }
                });

                // startActivity(new Intent(AndroidPHPConnectionDemo.this, UserPage.class));
            }else{
                Log.d("andro",result);
                showAlert();
            }

        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
            }
        });
        t.start();
    }

    public void showAlert(){
        MainActivity_Login.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_Login.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
