package com.iashwin28.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.view.ScaleGestureDetector;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.util.Log;
import android.graphics.PointF;
import java.lang.Math;
import android.database.Cursor;






public class ViewImage extends Activity implements GestureDetector.OnGestureListener {
    TextView text;
    ImageView imageview;
    ScaleGestureDetector sd;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    final static int NONE = 0;
    final static int DRAG = 1;
    final static int ZOOM = 2;
    static final float MIN_ZOOM = 0.45f, MAX_ZOOM = 1.96f;
    int mode = 0;
    GestureDetectorCompat gd;
    Bitmap bmp;
    String[] filepath;
    String[] filename;
    int position;
    private float init_x = 0, fin_x = 0;
    ProgressBar spinner;
    int flag = 0;
    float d = 0f;
    int width = 100;
    int height = 100;
    int event1;

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        return true;
    }

    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewimage);

        Intent i = getIntent();


        position = i.getExtras().getInt("position");


        filepath = i.getStringArrayExtra("filepath");


        filename = i.getStringArrayExtra("filename");


        text = (TextView) findViewById(R.id.imagename);


        text.setText(filename[position]);


        imageview = (ImageView) findViewById(R.id.fullimage);

        //this.gd=new GestureDetectorCompat();
        bmp = BitmapFactory.decodeFile(filepath[position]);

        imageview.setImageBitmap(bmp);
        //sd = new ScaleGestureDetector(this,new ScaleListener());
        super.onCreate(savedInstanceState);

        imageview.setOnTouchListener(new View.OnTouchListener() {
            float scale;


            @Override
            public boolean onTouch(View v, MotionEvent event) {


//            }
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:   // first finger down only
                        // matrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        init_x = event.getX();
                        mode = NONE;
                        event1 = MotionEventCompat.getActionIndex(event);


                        flag = 1;
                        // float finalx = event.
                        break;

                    case MotionEvent.ACTION_UP:
                        if (flag == 0 && mode != ZOOM && event1 == 0) {
                            fin_x = event.getX();
                            if (fin_x > init_x) {
                                Log.e("Action", "Swipe left");
                                if (!(position == 0)) {
                                    position -= 1;
                                    Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
                                    imageview.setImageBitmap(bmp);
                                    text.setText(filename[position]);
                                }
                            } else {
                                if (fin_x < init_x) {
                                    Log.e("Action", "Swipe right");
                                    position += 1;
                                    Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
                                    imageview.setImageBitmap(bmp);
                                    text.setText(filename[position]);
                                }
                                init_x = 0;
                            }
                        }

                        imageview.setMinimumWidth(width);
                        imageview.setMinimumHeight(height);
                        break;

                    // first finger lifted

                    case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                        mode = NONE;
                        break;


                    case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down
                        event1 = MotionEventCompat.getActionIndex(event);
                        oldDist = spacing(event);
                        Log.i("abc", "zoom zala");
                        Log.i("abc", "oldDist=" + oldDist);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            matrix.postScale(scale, scale, mid.x, mid.y);
                            mode = ZOOM;
//                            Log.i("abc","zoom zala");

                            imageview.setMinimumWidth(width);
                            imageview.setMinimumHeight(height);
                            imageview.setScaleType(ImageView.ScaleType.MATRIX);
                        }


                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.i("abc", "Zoom zala");
                        if (imageview.getLeft() >= -100 && mode == ZOOM) {
                            matrix.set(savedMatrix);
//
//                            matrix.postTranslate(event.getX()-start.x, event.getY()-start.y); //for transformation

                        }

                        if (mode == ZOOM) {
                            // pinch zooming
                            float newDist = spacing(event);
                            Log.i("abc", "newdistance= " + newDist);
                            if (newDist > 100f) {
                                matrix.set(savedMatrix);
                                scale = newDist / oldDist;
                                Log.i("abc", "scale" + scale);
                                int mh = imageview.getMeasuredHeight();
                                int mw = imageview.getMeasuredWidth();
                                Log.i("abc", "height" + mh);
                                Log.i("abc", "height" + mw);
                                if (scale < 0.5f) {
                                    Log.i("abc", "minzoom");
                                    matrix.postScale(0.5f, 0.5f, mid.x, mid.y);// setting the scaling of the
                                } else if (scale >= 2.0f) {
                                    Log.i("abc", "Maxzoom");
                                    matrix.postScale(2.0f, 2.0f, mid.x, mid.y);
                                } else {
                                    matrix.postScale(scale, scale, mid.x, mid.y);
                                }
                                imageview.setMinimumWidth(width);
                                imageview.setMinimumHeight(height);
                                imageview.setScaleType(ImageView.ScaleType.MATRIX);
//                                if(imageview.getWidth() < 100 || imageview.getHeight() < 100)
//                                {
//                                    imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                                    Log.i("abc", "minzoom" + imageview.getWidth()   + imageview.getHeight());
//
//                                }

                                matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);

                                midPoint(mid, event);
                                Log.i("abc", "zoom zala");
//


                            }
                        }

//
                        break;


                }
                flag = 0;
                imageview.setMinimumWidth(width);
                imageview.setMinimumHeight(height);
                imageview.setImageMatrix(matrix);
                return true;
            }

        });


    }
    //Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);


    public void onefectclicked(View view) {
        Bitmap image = invertImage(bmp);
        imageview.setImageBitmap(image);

    }

    public Bitmap invertImage(Bitmap original) {
        Bitmap finalimage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        int A, R, G, B;
        int pixel;
        int height = original.getHeight();
        int width = original.getWidth();

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                pixel = original.getPixel(y, x);
                A = Color.alpha(pixel);
                R = 300 - Color.red(pixel);
                G = 300 - Color.green(pixel);
                B = 300 - Color.blue(pixel);
                finalimage.setPixel(y, x, Color.argb(A, R, G, B));
            }
        }

        return finalimage;
    }

    public void onoriginalClick(View view) {
        imageview.setImageBitmap(bmp);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

}

