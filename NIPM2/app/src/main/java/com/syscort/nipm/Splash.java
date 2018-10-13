package com.syscort.nipm;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    private static int Splash_screen_time = 2000;
    ImageView logo;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window window = Splash.this.getWindow();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(Splash.this.getResources().getColor(R.color.colorPrimaryDark));
        }

        logo = (ImageView)findViewById(R.id.logo);
        title = (TextView)findViewById(R.id.title);
        //title = (TextView)findViewById(R.id.title);
        Animation anim = AnimationUtils.loadAnimation(Splash.this, R.anim.fadein_splash);
        Typeface mytypeface;
        mytypeface = Typeface.createFromAsset(Splash.this.getAssets(), "fonts/trebuc.ttf");
        title.setTypeface(mytypeface);
        logo.setAnimation(anim);
        title.setAnimation(anim);
//        title.setAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, Splash_screen_time);

    }
    }

