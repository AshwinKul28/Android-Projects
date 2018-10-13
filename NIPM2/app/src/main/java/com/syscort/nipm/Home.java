package com.syscort.nipm;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backbutton pressed
                Intent i = new Intent(Home.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        TextView body = (TextView)findViewById(R.id.bodycse);
        body.setText(Html.fromHtml("<b><i>About NIPM:</i></b><br /><small>NIPM, the short form of the National Institute of Personnel Management, is the only all-India body of professional managers engaged in the profession of personnel management, industrial relations, labour welfare, training and HRD in the country. It came into existence in March 1980 as a result of merger of two professional institutions, namely the Indian Institute of Personnel Management(IIPM) " +
                "established in 1948 in Kolkata and the National Institute of Labour Management(NILM) established in1950 in Bombay," +
                "now Mumbai.<br />With its National Office at Kolkata, NIPM has a total membership of about 8,000 spread over 50 Chapters all over the country<br />" +
                "NIPM is a non-profit making body devoted to the development of skill and expertise of the professionals engaged in the management of human resources through regular lecture, meetings, seminars, " +
                "training courses, conferences and publication in its chapters all over the country.</small>"));

        Typeface mytypeface2;
        mytypeface2 = Typeface.createFromAsset(Home.this.getAssets(), "fonts/trebuc.ttf");
        body.setTypeface(mytypeface2);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Home.this, MainActivity.class);
        startActivity(i);
        finish();

    }

}
