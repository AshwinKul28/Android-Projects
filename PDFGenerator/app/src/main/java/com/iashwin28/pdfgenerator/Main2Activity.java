package com.iashwin28.pdfgenerator;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class Main2Activity extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        String FILE = Environment.getExternalStorageDirectory().toString()
                +"/Health PDF/"+"Testdoc.pdf";

        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromFile(new File(FILE)).defaultPage(1).enableSwipe(true).load();


    }
}
