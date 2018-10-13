package com.syscort.nipm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Vector;

public class Gallery extends AppCompatActivity {

    private TextView text;
    int i=0;
    int imgid[]={R.drawable.splash0, R.drawable.splash1, R.drawable.splash2,R.drawable.splash3};
    ImageView slide_0;
    ImageView slide_1;
    ImageView lastSlide;
    Vector<Integer> imageIds;
    int count = 0;
    ProgressBar pb ;
    private Handler transparencyHandler = new Handler();
    private Handler timerHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        pb = (ProgressBar)findViewById(R.id.progressBar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backbutton pressed
                Intent i = new Intent(Gallery.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        if (!isNetworkAvailable()) {
            Log.d("andro", "not connected");

            AlertDialog.Builder builder = new AlertDialog.Builder(Gallery.this);
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

            WebView myWebView = (WebView) findViewById(R.id.web_view);

            myWebView.loadUrl("http://nipmaurangabad.com/gallery/androidGallery.php");
            // myWebView.setInitialScale(200);
            myWebView.getSettings().setUseWideViewPort(true);
            myWebView.getSettings().getLoadWithOverviewMode();
            myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            myWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    pb.setProgress(progress);
                    if (progress == 100) {
                        pb.setVisibility(View.GONE);

                    } else {
                        pb.setVisibility(View.VISIBLE);

                    }
                }
            });
            myWebView.getSettings().setSupportMultipleWindows(true);
            myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            myWebView.getSettings().setLoadsImagesAutomatically(true);
            myWebView.getSettings().setLightTouchEnabled(true);
            myWebView.getSettings().setDomStorageEnabled(true);
            myWebView.getSettings().setLoadWithOverviewMode(true);
            myWebView.setScrollbarFadingEnabled(false);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                    viewx.loadUrl(urlx);
                    return false;
                }
            });


        }


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Gallery.this, MainActivity.class);
        startActivity(i);
        finish();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
