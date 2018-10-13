package com.syscort.nipm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Document extends AppCompatActivity {

    TextView enroll,casestudy,gen,company,epfo,vivek,isf,history,member;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pb = (ProgressBar)findViewById(R.id.progressBar3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backbutton pressed
                Intent i = new Intent(Document.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

//        enroll = (TextView)findViewById(R.id.enroll);
//        casestudy = (TextView) findViewById(R.id.cases);
//        gen = (TextView)findViewById(R.id.gen);
//        company =(TextView)findViewById(R.id.company);
//        epfo = (TextView)findViewById(R.id.epfo);
//        vivek = (TextView)findViewById(R.id.vivek);
//        isf = (TextView)findViewById(R.id.isf);
//        history = (TextView)findViewById(R.id.history);
//        member = (TextView)findViewById(R.id.member);
//
//
//
//
//        enroll.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://nipmaurangabad.com/data/pdf/Enrollment Campaign 2017.pdf"));
//                startActivity(intent);
//            }
//        });
//
//        casestudy.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://nipmaurangabad.com/data/pdf/Case Study.pdf"));
//                startActivity(intent);
//            }
//        });
//
//        gen.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://nipmaurangabad.com/data/pdf/Article- Generation Y.pdf"));
//                startActivity(intent);
//            }
//        });
//
//        company.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://nipmaurangabad.com/data/pdf/Company Profile White Globe.pdf"));
//                startActivity(intent);
//            }
//        });
//
//        epfo.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://nipmaurangabad.com/data/pdf/EPFO_INITIATIVES.pdf"));
//                startActivity(intent);
//            }
//        });
//
//        vivek.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://nipmaurangabad.com/data/ppt/Information - NIPM Abad Chapter- Vivekanand Student Chapter -.ppt"));
//                startActivity(intent);
//            }
//        });
//
//        isf.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://nipmaurangabad.com/data/ppt/isf services profile.docx"));
//                startActivity(intent);
//            }
//        });
//
//        history.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://nipmaurangabad.com/data/ppt/NIPM Abad chapter- History till date.pptx"));
//                startActivity(intent);
//            }
//        });
//
//        member.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://nipmaurangabad.com/data/ppt/NIPM-June16-_form membership--.doc"));
//                startActivity(intent);
//            }
//        });
//

        if (!isNetworkAvailable()) {
            Log.d("andro", "not connected");

            AlertDialog.Builder builder = new AlertDialog.Builder(Document.this);
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

            myWebView.loadUrl("http://nipmaurangabad.com/androidDoc.php");
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

            myWebView.setDownloadListener(new DownloadListener() {
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Document.this, MainActivity.class);
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
