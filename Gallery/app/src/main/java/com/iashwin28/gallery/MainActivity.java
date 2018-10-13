package com.iashwin28.gallery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import android.provider.MediaStore;
import android.content.pm.PackageInfo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.util.ArrayList;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.view.Menu;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    GridView gv;
    ArrayList<File> files;
    String path[], name[], Photopath;
    int CALL_FROM_SUBCLASS = 0;
    LruCache<File, Bitmap> myCache = new LruCache<>((int) Runtime.getRuntime().maxMemory());//TODO NEW USE IT
    static final int Image_capture = 1;
    FloatingActionButton cam;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cam = (FloatingActionButton
                ) findViewById(R.id.Camera);
        gv = (GridView) findViewById(R.id.mygrid1);
        img = (ImageView) findViewById(R.id.Camview);
        files = imageReader(Environment.getExternalStorageDirectory());
        final CacheThread cacheThread = new CacheThread();
        if (!hascamera()) {
            cam.setEnabled(false);
            Toast.makeText(MainActivity.this, "Camera nahi hai", Toast.LENGTH_SHORT).show();
        }
        if (true) {
            Log.i("abc", "thread population started");
            cacheThread.start();
        }
        gv.setAdapter(new GridAdapter());
        path = new String[files.size()];
        name = new String[files.size()];
        myCache.evictAll();
        Log.e("Cache", "Evicted!");
        onTrimMemory(TRIM_MEMORY_RUNNING_LOW);
        Log.e("Memory", "Memory cleared");
        for (int i = 0; i < files.size(); i++) {
            path[i] = files.get(i).getAbsolutePath();
            name[i] = files.get(i).getName();
//            myCache.put(images.get(i), ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(images.get(i).toString()), 100, 100));
        }
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ProgressDialog dialog = new ProgressDialog(MainActivity.this); // this = YourActivity
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMessage("Loading. Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMax(100);
                dialog.show();
                StartIntent pictureIntent = new StartIntent(position, dialog);
                pictureIntent.start();
            }
        });

        //galleryAddPic();
    }

    private boolean hascamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        Photopath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void launchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(MainActivity.this, "Error creating file", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, Image_capture);
            }

        }

        Log.i("abc", "click zala");
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(Photopath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_capture && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            img.setImageBitmap(photo);
        }

    }

    class StartIntent extends Thread {
        int position;
        ProgressDialog dialog;

        StartIntent(int position, ProgressDialog dialog) {
            this.position = position;
            this.dialog = dialog;
        }

        public void run() {
            Intent showImage = new Intent(MainActivity.this, ViewImage.class);
            dialog.setProgress(25);
            showImage.putExtra("filepath", path);
            dialog.setProgress(50);
            showImage.putExtra("filename", name);
            dialog.setProgress(75);
            showImage.putExtra("position", position);
            dialog.setProgress(100);
            startActivity(showImage);
            dialog.dismiss();
        }
    }

    class CacheThread extends Thread {
        public void run() {
            int CACHE_POPULATOR = 0;
            Log.e("File", String.valueOf(CACHE_POPULATOR));
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "DebaGallery");
            boolean success = false;
            File cache_master = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DebaGallery" + File.separator + "handler.txt");
            if (!folder.exists()) {
                success = folder.mkdir();
                Log.e("File", "Cache Master has been generated");
                Log.e("File", String.valueOf(success));
                if (success) {
                    boolean result = false;
                    if (!cache_master.exists()) {
                        try {
                            result = cache_master.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("File", String.valueOf(result));
                    if (result) {
                        try {
                            FileWriter master = new FileWriter(cache_master);
                            master.write("1");
                            master.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                Log.e("File", "Else block executing");
                try {
                    FileReader master = new FileReader(cache_master);
                    BufferedReader br = new BufferedReader(master);
                    if ((CACHE_POPULATOR = Integer.parseInt(br.readLine())) == 1) {
                        Log.e("Cache", "Cache has already been built");
                    } else {
                        FileWriter slave = new FileWriter(cache_master);
                        master.close();
                        slave.write("1");
                        slave.close();
                    }
                    master.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (CACHE_POPULATOR != 1) {
                Log.e("Images Size", String.valueOf(files.size()));
                Log.e("Cache", String.valueOf(myCache.size()));
                for (int i = 0; i < files.size(); i++) {
                    myCache.put(files.get(i), ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(files.get(i).toString()), 100, 100));
                    if (((int) Runtime.getRuntime().maxMemory() - myCache.size()) < 10000) {
                        Log.e("Cache", "Cache Evicted");
                        myCache.evictAll();
                    }
                    Log.e("Cache", String.valueOf(myCache.size()));
                    CACHE_POPULATOR = 1;
                    yield();
                }
            }
        }
    }

    class GridAdapter extends BaseAdapter {
        private Activity activity;

        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public Object getItem(int position) {
            return files.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }//if we use database

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.images, parent, false);
            ImageView newimg = (ImageView) convertView.findViewById(R.id.imageView);
//            newimg.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getItem(position).toString()), 100, 100));
            newimg.setImageBitmap(myCache.get((File) getItem(position)));
            return convertView;
        }//this method will set each and every image to the grid view
    }

    ArrayList<File> a = new ArrayList<>();

    ArrayList<File> imageReader(File root) {

        addtolist(root);
        return a;
    }

    void addtolist(File root) {
        File files[] = root.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                addtolist(files[i]);
            } else {
                if (files[i].getName().endsWith(".jpg")) {
                    a.add(files[i]);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        File subclass_checker = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DebaGallery" + File.separator + "onDestroychecker.txt");
        try {
            Log.e("File", "Subclass call checking sequence initiated!");
            FileReader checker = new FileReader(subclass_checker);
            BufferedReader br = new BufferedReader(checker);
            if (Integer.parseInt(br.readLine()) == 1) {
                Log.e("File", "CALL FROM SUBCLASS");
                CALL_FROM_SUBCLASS = 1;
                checker.close();
                FileWriter checker_write = new FileWriter(subclass_checker);
                checker_write.write("0");
                checker_write.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (CALL_FROM_SUBCLASS != 1) {
            File cache_master = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AshGallery" + File.separator + "handler.txt");
            try {
                Log.e("File", String.valueOf(cache_master.exists()));
                FileWriter master = new FileWriter(cache_master);
                Log.e("File", "Reinitialized to 0");
                master.write("0");
                master.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CALL_FROM_SUBCLASS = 0;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
