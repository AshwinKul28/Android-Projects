package com.iashwin28.speech;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class View_Report extends AppCompatActivity {

    PDFView pdfView;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__report);

         name = getIntent().getExtras().getString("Name");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        String FILE = Environment.getExternalStorageDirectory().toString()
                +"/Health PDF/"+name+"_doc.pdf";

        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromFile(new File(FILE)).defaultPage(1).enableSwipe(true).load();

    }
}
