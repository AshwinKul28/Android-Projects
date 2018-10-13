package com.syscort.nipm;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    ListView ls;
    ImageView dp;
    ImageView front;
    String Photopath;
    String imagepath;
    Bitmap photo;
    TextView name;
    List<String> web = new ArrayList<String>();
    Integer[] imageId = {
            R.drawable.home,
            R.drawable.events,
            R.drawable.articles,
            R.drawable.gallery,
            R.drawable.idea,
            R.drawable.about,
            R.drawable.doc


    };
    Integer[] back = {
            R.color.listbg,
            R.color.listbg,
            R.color.listbg,
            R.color.listbg,
            R.color.listbg,
            R.color.listbg,
            R.color.listbg


    };

    private PayUmoneySdkInitilizer.PaymentParam paymentParam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (SaveSharedPreferences.getUserName(MainActivity.this).length() == 0) {
            Intent i = new Intent(MainActivity.this, Signup.class);
            startActivity(i);
            finish();
        } else {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            OneSignal.startInit(this).setNotificationOpenedHandler(new ExampleNotificationOpenedHandler()).init();

            web.add("HOME");
            web.add("EVENTS");
            web.add("ARTICLES");
            web.add("GALLERY");
            web.add("WHAT'S NEW");
            web.add("NIPM AURANGABAD");
            web.add("KNOWLEDGE BANK");


            dp = (ImageView) findViewById(R.id.dp);
            front = (ImageView) findViewById(R.id.front);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    front.setImageResource(R.drawable.splash2);
                }
            }, 3000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    front.setImageResource(R.drawable.splash3);
                }
            }, 6000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    front.setImageResource(R.drawable.splash1);
                }
            }, 9000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    front.setImageResource(R.drawable.splash0);
                }
            }, 15000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    front.setImageResource(R.drawable.front1);
                }
            }, 22000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    front.setImageResource(R.drawable.splash2);
                }
            }, 30000);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    front.setImageResource(R.drawable.splash3);
                }
            }, 35000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    front.setImageResource(R.drawable.front1);
                }
            }, 40000);



            try {
                File myfile = new File(Environment.getExternalStorageDirectory(), "displaypic.jpg");
                BitmapDrawable d = new BitmapDrawable(getResources(), myfile.getAbsolutePath());
                Bitmap a = d.getBitmap();
                Bitmap b = getRoundedShape(a);
                dp.setImageBitmap(b);
            } catch (NullPointerException ex) {

                Log.d("andro", ex.toString());
            }
            CustomList adapter = new
                    CustomList(MainActivity.this, web, imageId, back);

            ls = (ListView) findViewById(R.id.listView);
            ls.setAdapter(adapter);


            ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

            ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //  Toast.makeText(LoginSuccesss.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

                    if (position == 0) {
                        Intent i = new Intent(MainActivity.this, Home.class);
                        startActivity(i);
                        finish();
                    }
                    if (position == 1) {
                        Intent i = new Intent(MainActivity.this, Events.class);
                        startActivity(i);
                        finish();


                    }
//               if(position==3)
//               {
//                   Toast.makeText(LoginSuccesss.this,"New notification generated!",Toast.LENGTH_SHORT ).show();
//                   notification.setSmallIcon(R.mipmap.user);
//                   notification.setTicker("New Record is Added");
//                   notification.setWhen(SystemClock.currentThreadTimeMillis());
//                   notification.setContentTitle("My Project");
//                   notification.setContentText("One new Record is Added");
//                   //Toast.makeText(getApplicationContext(),"New record added",Toast.LENGTH_SHORT).show();
//
//                   Intent intent = new Intent(LoginSuccesss.this, LoginSuccesss.class);
//                   PendingIntent pendingIntent = PendingIntent.getActivity(LoginSuccesss.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                   notification.setContentIntent(pendingIntent);
//
//                   NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                   nm.notify(uniqueID[0], notification.build());
//                   uniqueID[0]++;
//               }

                    if (position == 2) {
                        Intent i = new Intent(MainActivity.this, Articles.class);
                        startActivity(i);
                        finish();
                    }

                    if (position == 3) {
                        Intent i = new Intent(MainActivity.this, Gallery.class);
                        startActivity(i);
                        finish();

                    }

                    if (position == 4) {

                        Intent i = new Intent(MainActivity.this, Whats_new.class);
                        startActivity(i);
                        finish();

                    }

                    if (position == 5) {
                        Intent i = new Intent(MainActivity.this, NIOMAWB.class);
                        startActivity(i);
                        finish();
                    }

                    if (position == 6) {
                        Intent i = new Intent(MainActivity.this, Document.class);
                        startActivity(i);
                        finish();
                    }


                }
            });
        }

    }


        private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

            @Override

            public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
                try {
                    if (additionalData != null) {
                        if (additionalData.has("actionSelected"))

                            Log.d("OneSignalExample", "OneSignal notification button with id " + additionalData.getString("actionSelected") + " pressed");


                        Log.d("OneSignalExample", "Full additionalData:\n" + additionalData.toString());
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }

    private File imageFile() throws IOException {

        File directory = Environment.getExternalStorageDirectory();
        Photopath = directory.getAbsolutePath();
        return new File(directory,"displaypic.jpg");

    }
    public void bio(View view) throws IOException
    {
        final CharSequence[] options = { "Snap a new one", "Choose another from Gallery","Logout","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Snap a new one")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;



                        try {
                            photoFile = imageFile();
                            imagepath = photoFile.getAbsolutePath();
                            galleryAddPic();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Toast.makeText(MainActivity.this, "Error creating file", Toast.LENGTH_SHORT).show();
                        }
                        if (photoFile != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(intent, 1);

                        }

                    }
                }
                else if (options[item].equals("Choose another from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if(options[item].equals("Logout")){
                    SaveSharedPreferences.clearUserName(MainActivity.this);
                    Intent i =new Intent(MainActivity.this, Signup.class);
                    startActivity(i);
                    finish();
                }

                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        AlertDialog alert = builder.create();
        alert.show();



        Log.i("abc", "click zala");
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagepath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
//            Bundle extras = data.getExtras();
//            Bitmap photo = (Bitmap) extras.get("data");
                photo = BitmapFactory.decodeFile(imagepath);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
//            photo.compress(Bitmap.CompressFormat.JPEG,200,out);
//            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                Bitmap temp = getRoundedShape(photo);
                dp.setImageBitmap(temp);
                Toast.makeText(MainActivity.this,"Photo Changed Successfully",Toast.LENGTH_SHORT).show();

            }
            else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //  Bitmap round = getRoundedShape(thumbnail);
                //imageencode = getStringImage(round);
                // Log.i("path of image from gallery......******************.........", picturePath+"");
                Bitmap temp = getRoundedShape(thumbnail);
                dp.setImageBitmap(temp);
                Toast.makeText(MainActivity.this,"Photo Changed Successfully",Toast.LENGTH_SHORT).show();
                File picture = new File(picturePath);
                String picturename = picture.getName();
                File dir = Environment.getExternalStorageDirectory();
                File f = new File(dir,"displaypic.jpg");
                try {
                    copyFile(picture,f);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // ekek transfer max 5 mb cha pic
        byte[] buf = new byte[5000];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 300;
        int targetHeight = 300;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
    }

