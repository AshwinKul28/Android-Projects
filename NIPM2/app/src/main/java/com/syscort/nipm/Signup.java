package com.syscort.nipm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Signup extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String fname,lname,phone,mail,address,organisation;
    String fname1,lname1,phone1,mail1,address1,organisation1;
    ImageView pic;
    String Photopath;
    Bitmap bmp;
    Bitmap photo,decoded;
    Uri uri;
    String imagepath;
    JSONObject jsonobj;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    JSONArray list;
    HttpEntity httpentity;
    EditText first_name, last_name, ph, email,city, org;
    private final String serverUrl = "http://nipmaurangabad.com/formreg.php";
    List<NameValuePair> nameValuePairs;
    TextView photo1;
    final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        pic = (ImageView)findViewById(R.id.userimg);
        photo1 = (TextView)findViewById(R.id.photo);

        if(!isStoragePermissionGranted())
        {
            Toast.makeText(this, "Please check your permission for app from settings", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("abc","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    private File imageFile() throws IOException {
        // Create an image file name


        //  ContextWrapper cw = new ContextWrapper(getActivity());
        File directory = Environment.getExternalStorageDirectory();

        //   FileOperations.checkDirectory(directory, false);
        Photopath = directory.getAbsolutePath();
        return new File(directory,"displaypic.jpg");

    }

    public void userimage(View view) throws IOException {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;



                        try {
                            photoFile = imageFile();
                            imagepath = photoFile.getAbsolutePath();
                            galleryAddPic();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Toast.makeText(Signup.this, "Error creating file", Toast.LENGTH_SHORT).show();
                        }
                        if (photoFile != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(intent, 1);

                        }

                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

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

    public void signup(View v)
    {

        if (!isNetworkAvailable()) {
            Log.d("andro", "not connected");

            AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
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

            else{

                first_name = (EditText) findViewById(R.id.input_fname);
                last_name = (EditText) findViewById(R.id.input_lname);
                ph = (EditText) findViewById(R.id.input_phone);
                email = (EditText) findViewById(R.id.input_email);
                city = (EditText) findViewById(R.id.input_address);
                org = (EditText) findViewById(R.id.input_org);

                fname = first_name.getText().toString();
                lname = last_name.getText().toString();
                phone = ph.getText().toString();
                mail = email.getText().toString();
                address = city.getText().toString();
                organisation = org.getText().toString();
                if (fname.length() == 0 || lname.length() == 0 || phone.length() == 0 || mail.length() == 0 || address.length() == 0 || organisation.length() == 0) {
                    Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();

                } else {


                    final ProgressDialog progress = new ProgressDialog(this);
                    progress.setTitle("Wait!");
                    progress.setMessage("Fetching your credentials...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String result = null;
                            InputStream isr = null;


                            try {
                                httpclient = new DefaultHttpClient();
                                httppost = new HttpPost(serverUrl); // make sure the url is correct.
                                Log.d("andro", "url passed");
                                nameValuePairs = new ArrayList<NameValuePair>(6);
                                // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                                nameValuePairs.add(new BasicNameValuePair("phn", phone));
                                nameValuePairs.add(new BasicNameValuePair("fname", fname));  // $Edittext_value = $_POST['Edittext_value'];
                                nameValuePairs.add(new BasicNameValuePair("lname", lname));
                                nameValuePairs.add(new BasicNameValuePair("org", organisation));
                                nameValuePairs.add(new BasicNameValuePair("email", mail));
                                nameValuePairs.add(new BasicNameValuePair("city", address));

//                    nameValuePairs.add(new BasicNameValuePair("image", imageencode));

                                Log.d("andro", "2" + fname + lname + phone + mail + address + organisation);
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                Log.d("andro", "3");
                                response = httpclient.execute(httppost);
                                Log.d("andro", "4");
                                httpentity = response.getEntity();
                                isr = httpentity.getContent();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "UTF-8"), 8);
                                StringBuilder sb = new StringBuilder();
                                Log.d("andro", "5");
                                String line = null;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line + "\n");
                                }
                                result = sb.toString();
                                Log.d("bc", result);
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        // tv.setText("Response from PHP : " + response);
//                                        Toast.makeText(ProfileActivity.this, "Bio Updated Successfully", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
                                Signup.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        progress.dismiss();

                                    }
                                });


                                if ((result.startsWith("s"))) {
                                    Log.i("andro", result);

                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            SaveSharedPreferences.setUserName(Signup.this, fname);
                                            Intent i = new Intent(Signup.this, MainActivity.class);
                                            startActivity(i);
                                            finish();

//                        Intent menuIntent = new Intent(SignupActivity.this, LoginSuccesss.class);
//                            menuIntent.putExtra("rollnum",name);
                                        }
                                    });


                                    // startActivity(new Intent(AndroidPHPConnectionDemo.this, UserPage.class));
                                } else {
                                    try {
                                        jsonobj = new JSONObject(result);
                                        list = jsonobj.getJSONArray("result");
                                        for (int i = 0; i < list.length(); i++) {
                                            JSONObject c = list.getJSONObject(i);
                                            phone1 = c.getString("phn");
                                            fname1 = c.getString("fname");
                                            lname1 = c.getString("lname");
                                            organisation1 = c.getString("org");
                                            mail1 = c.getString("email");
                                            address1 = c.getString("city");


                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    first_name.setText(fname1);
                                                    last_name.setText(lname1);
                                                    ph.setText(phone1);
                                                    email.setText(mail1);
                                                    city.setText(address1);
                                                    org.setText(organisation1);

                                                    Toast.makeText(Signup.this, "Yes! " + fname1 + " " + lname1 + " you're already exist!", Toast.LENGTH_LONG).show();

                                                    SaveSharedPreferences.setUserName(Signup.this, fname1);
                                                    Intent i = new Intent(Signup.this, MainActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            });
                                        }


                                    } catch (JSONException j) {
                                        j.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {

                                Log.d("andro", "Exception :" + e.toString());
                            }

                        }
                    });
                    t.start();


                }
            }
        }

    public Context getActivity() {
        Context activity = getApplicationContext();
        return activity;
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
                Bitmap round = getRoundedShape(photo);

                pic.setImageBitmap(round);
                photo1.setText("Photo Added Successfully");


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
                Bitmap round = getRoundedShape(thumbnail);
                pic.setImageBitmap(round);
                photo1.setText("Photo Added Successfully");
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

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("abc","Permission is granted");
                return true;
            } else {

                Log.v("abc","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("abc","Permission is granted");
            return true;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
