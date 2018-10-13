package com.iashwin28.sql;


import  android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.support.v4.view.GestureDetectorCompat;


public class MainActivity extends AppCompatActivity {

    EditText ashinput;
    TextView ashtext;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ashinput= (EditText)findViewById(R.id.ashinput);
        ashtext = (TextView)findViewById(R.id.ashtext);

        dbHandler = new MyDBHandler(this, null, null, 1);
        try {
            printDatabase();
        }catch (Exception e){
            Log.i("exxxx", e.toString());

        }



    }

    public void printDatabase() {
        String dbString = dbHandler.databaseToString();
        ashtext.setText(dbString);
        ashinput.setText("");
    }


    public void addbuttonClick(View view){

        String product = ashinput.getText().toString();
        Products p = new Products(product);
        dbHandler.addProduct(p);
        printDatabase();
    }

    //Delete a product to the database
    public void delbuttonClick(View view){

        String inputText = ashinput.getText().toString();
        dbHandler.deleteProduct(inputText);
        printDatabase();
    }
}