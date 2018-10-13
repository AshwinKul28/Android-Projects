package com.example.rugved.teacherapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ImageView background,profile;
    ListView ls;
    TextView name;
    TextView rollnumber;
    TextView yr;
    int id = 0;
    String Name;
    String roll;
    String year;
    String image;
    Bitmap a;
    TextView rol;
    ImageView tryimg;
    View view;


    //    String[] web = {
//            "TIME-TABLE",
//            "Notifications",
//            "TimeLine",
//            "MCQ Test",
//            "File Sharing",
//            "Your Profile",
//            "Logout"
//    } ;
    List<String> web = new ArrayList<String>();
//    Integer[] imageId = {
//            R.drawable.calendar,
//            R.drawable.notifications,
//            R.drawable.tl,
//            R.drawable.discuss,
//            R.drawable.share,
//            R.drawable.profile,
//            R.drawable.logout
//
//    };
    Integer[] back = {
            R.color.listbg1,
            R.color.listbg2,
            R.color.listbg3,
            R.color.listbg4,
            R.color.listbg5,
            R.color.listbg6,
            R.color.listbg7
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ls = (ListView)findViewById(R.id.listView);


//        Name = SaveSharedPreference.getUserName(MainActivity.this);
//        roll = SaveSharedPreference.getUserRoll(MainActivity.this);
////        year = SaveSharedPreference.getUserYear(MainActivity.this);
////        image = SaveSharedPreference.getUserImage(MainActivity.this);
//
//
//        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
//        {
//            Intent i = new Intent(MainActivity.this,MainActivity_Login.class);
//            startActivity(i);
//            finish();
//        }
//        else
//        {
//
//        }

        web.add("HELLO "+ Name);
        web.add("NOTIFICATIONS");
        web.add("TIMELINE");
//        web.add("TIMETABLE");
//        web.add("MCQ TEST");
//        web.add("FILE SHARING");
        web.add("LOGOUT");



    CustomList adapter = new
            CustomList(MainActivity.this, web,back);



    ls.setAdapter(adapter);

    ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view,
        int position, long id) {
            //  Toast.makeText(LoginSuccesss.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            if(position==0)
            {
                Intent i = new Intent(MainActivity.this, EditDetails.class);
                //tid, name, pass, mobile, email
                startActivity(i);
            }

            if(position==1)
            {
                Intent i = new Intent(MainActivity.this,Notification.class);
                i.putExtra("name",Name);
                i.putExtra("image",image);
                i.putExtra("year",year);
                startActivity(i);
            }
            if (position==2)
            {
                Intent i = new Intent(MainActivity.this,Timeline.class);
                i.putExtra("name",Name);
                i.putExtra("image",image);
                i.putExtra("year",year);
                startActivity(i);
            }
            if(position==3)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Logout!");
                builder.setMessage("Are you sure want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SaveSharedPreference.clearUserName(MainActivity.this);
                                Intent i = new Intent(MainActivity.this,MainActivity_Login.class);
                                startActivity(i);
                                finish();
                            }
                        });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        return;
                    }
                });
                builder.show();


            }

        }
    });

    }
}
