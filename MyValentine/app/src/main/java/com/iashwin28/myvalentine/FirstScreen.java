package com.iashwin28.myvalentine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.dynamitechetan.flowinggradient.FlowingGradientClass;

import java.util.HashMap;

import static com.iashwin28.myvalentine.R.id.slider;


public class FirstScreen extends AppCompatActivity
        implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener
{

    SliderLayout sliderLayout ;

    HashMap<String, String> HashMapForURL ;

    HashMap<String, Integer> HashMapForLocalRes ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.RL2);
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onRelativeLayout(rl)
                .setTransitionDuration(4000)
                .start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        sliderLayout = (SliderLayout)findViewById(slider);

        //Call this method if you want to add images from URL .
     //   AddImagesUrlOnline();

        //Call this method to add images from local drawable folder .
        AddImageUrlFormLocalRes();

        //Call this method to stop automatic sliding.
        //sliderLayout.stopAutoCycle();

        for(String name : HashMapForLocalRes.keySet()){

            TextSliderView textSliderView = new TextSliderView(FirstScreen.this);

            textSliderView
                    .description(name)
                    .image(HashMapForLocalRes.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());

            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(5000);

        sliderLayout.addOnPageChangeListener(FirstScreen.this);
    }

    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

//    public void AddImagesUrlOnline(){
//
//        HashMapForURL = new HashMap<String, String>();
//
//        HashMapForURL.put("CupCake", "http://androidblog.esy.es/images/cupcake-1.png");
//        HashMapForURL.put("Donut", "http://androidblog.esy.es/images/donut-2.png");
//        HashMapForURL.put("Eclair", "http://androidblog.esy.es/images/eclair-3.png");
//        HashMapForURL.put("Froyo", "http://androidblog.esy.es/images/froyo-4.png");
//        HashMapForURL.put("GingerBread", "http://androidblog.esy.es/images/gingerbread-5.png");
//    }

    public void AddImageUrlFormLocalRes(){

        HashMapForLocalRes = new HashMap<String, Integer>();

        HashMapForLocalRes.put("My Favorite", R.drawable.kirri17);
        HashMapForLocalRes.put("Your Favorite", R.drawable.kirri18);
        HashMapForLocalRes.put("Our favorite", R.drawable.kirri9);
        HashMapForLocalRes.put("Our First one... cropped :p", R.drawable.kirri2);
        HashMapForLocalRes.put("Freshers :) Mr. ROSE & Ms Freshers", R.drawable.kirri4);
        HashMapForLocalRes.put("Beach Love", R.drawable.kirri10);
        HashMapForLocalRes.put("Devil Couple", R.drawable.kirri6);
        HashMapForLocalRes.put("Your surprise BDAY", R.drawable.kirri);
        HashMapForLocalRes.put("First Iphone selfie", R.drawable.kirri21);
        HashMapForLocalRes.put("Shaily ki shadi me hawa :p", R.drawable.kirri22);
        HashMapForLocalRes.put("Devang se choti tu :p", R.drawable.kirri23);
        HashMapForLocalRes.put("Mera Stresswala Bday", R.drawable.kirri25);
        HashMapForLocalRes.put("First time Bombay Bistro", R.drawable.kirri24);
        HashMapForLocalRes.put("Meri CUTE Girlfriend", R.drawable.kirri20);
        HashMapForLocalRes.put("Meri BORED Girlfriend", R.drawable.kirri13);
        HashMapForLocalRes.put("CANDID :p", R.drawable.kirri29);
        HashMapForLocalRes.put("BATMAN", R.drawable.kirri11);
        HashMapForLocalRes.put("Meri KATTHAKALI wali Girlfriend", R.drawable.kirri14);
        HashMapForLocalRes.put("Our First awesome photo", R.drawable.kirri8);
        HashMapForLocalRes.put("Mere Hair :(", R.drawable.kirri26);
        HashMapForLocalRes.put("My Contact BHOOT", R.drawable.kirri27);
        HashMapForLocalRes.put("Filtered", R.drawable.kirri28);

    }

    public void message (View v)
    {
        Toast.makeText(this, "Happy Valentines Day my baby :)", Toast.LENGTH_LONG).show();

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("Happy Valentines Day Ashwin!!!", "default content");
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
        finish();
    }


}

