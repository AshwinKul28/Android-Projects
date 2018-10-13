package com.iashwin28.speech;
//


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ReportActivity extends AppCompatActivity {

    EditText email,phone;
    String ID;
    String data;
    private static Socket s;
    private static Socket s1;
    private static ServerSocket ss;
    private static InputStreamReader isr;
    private static BufferedReader br;
    private static PrintWriter pw;
    private static String ip = "172.20.10.3";
    LocationManager locationManager;
    Location location;
    List<Address> addresses;
    SweetAlertDialog pDialog;
    String rname, rage, rphone, rdis, remail;

    String city,state,country,address,postalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ID = getIntent().getExtras().getString("ID");

        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    public void create_report(View v)
    {
        myTask2 mt = new myTask2();
        mt.execute();
    }

    class myTask2 extends AsyncTask<Void,Void,Void>
    {
        ///////////////////////////////btn for report sending
        @Override
        protected Void doInBackground(Void... arg0) {

            try {


                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog = new SweetAlertDialog(ReportActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Hold On...");
                        pDialog.setContentText("Your Report is getting ready");
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }
                }));

                Log.e("test","report socket is opened");
                s = new Socket(ip, 9800);
                pw = new PrintWriter(s.getOutputStream());
//                pw.write(id_ip);
                pw.write(ID);
                Log.e("test", ID);
                pw.flush();
                pw.close();
                s.close();
                Log.e("test", "sent");

                // Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();


                ss = new ServerSocket(9800);
                Log.e("test", "Server running");
                s1 = ss.accept();

                Log.e("test","accepted");


                isr = new InputStreamReader(s1.getInputStream());
                Log.e("test","step1");
                br = new BufferedReader(isr);
                Log.e("test","step2");                 ////////message from server to client

                data = br.readLine();


                String[] parts = data.split("    ");
                rname = parts[0]; // 004
                rage = parts[1];
                rdis = parts[2];


                Log.e("test",data);

                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {


                        // location


                        locationManager = (LocationManager) ReportActivity.this.getSystemService(LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        Geocoder gcd = new Geocoder(getBaseContext(),
                                Locale.getDefault());


                        try {
                            addresses = gcd.getFromLocation(location.getLatitude(),
                                    location.getLongitude(), 1);
                            if (addresses.size() > 0) {
                                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                city = addresses.get(0).getLocality();
                                String subLocality = addresses.get(0).getSubLocality();
                                state = addresses.get(0).getAdminArea();
                                country = addresses.get(0).getCountryName();
                                postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                Log.e("test",state+" "+country);
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        String FILE = Environment.getExternalStorageDirectory().toString()
                                +"/Health PDF/"+rname+"_doc.pdf";
// Add Permission into Manifest.xml
//
                        Document document = new Document(PageSize.A4);

// Create Directory in External Storage
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/Health PDF");
                        myDir.mkdirs();

// Create Pdf Writer for Writting into New Created Document
                        try {
                            PdfWriter.getInstance(document, new FileOutputStream(FILE));
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

// Open Document for Writting into document
                        document.open();

// User Define Method
                        addMetaData(document);
                        try {
                            addTitlePage(document);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
// Close Document after writting all content
                        document.close();

                        pDialog.dismissWithAnimation();
                        Toast.makeText(ReportActivity.this, "PDF Created", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(ReportActivity.this, View_Report.class);
                        i.putExtra("Name",rname);
                        startActivity(i);
                        finish();
                    }
                }));

                ss.close();
                br.close();
                s1.close();


            }
            catch(Exception e)
            {
                Log.e("test", e.getMessage());
            }

            return null;
        }
    }



    public void addMetaData(Document document)

    {
        document.addTitle("RESUME");
        document.addSubject("Person Info");
        document.addKeywords("Personal, Education, Skills");
        document.addAuthor("TAG");
        document.addCreator("TAG");
    }

    public void addTitlePage(Document document) throws DocumentException
    {
// Font Style for Document
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 32, Font.BOLD
                | Font.UNDERLINE, BaseColor.GRAY);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL);

// Start New Paragraph
        Paragraph prHead = new Paragraph();
// Set Font in this Paragraph
        prHead.setFont(titleFont);
// Add item into Paragraph
        prHead.add("Health Report\n");

// Create Table into Document with 1 Row
        PdfPTable myTable = new PdfPTable(1);
// 100.0f mean width of table is same as Document size
        myTable.setWidthPercentage(100.0f);

// Create New Cell into Table
        PdfPCell myCell = new PdfPCell(new Paragraph(""));
        myCell.setBorder(Rectangle.BOTTOM);

// Add Cell into Table
        myTable.addCell(myCell);

        prHead.setFont(catFont);
        prHead.add("\nInformation About the Patient\n\n");
        prHead.setAlignment(Element.ALIGN_CENTER);

// Add all above details into Document
        document.add(prHead);
        document.add(myTable);
        document.add(myTable);

        remail = email.getText().toString().trim();
        rphone = phone.getText().toString().trim();

// Now Start another New Paragraph
        Paragraph prPersinalInfo = new Paragraph();
        prPersinalInfo.setFont(smallBold);
        prPersinalInfo.add("Name : "+rname+"\n\n");
        prPersinalInfo.add("AGE : "+rage+" Years\n\n");
        prPersinalInfo.add("Address : "+address+"\n\n");
        prPersinalInfo.add("City: "+city+"    State: "+state+"\n\n");
        prPersinalInfo.add("Country: "+country+"    Zip Code: "+postalCode+"\n\n");
        prPersinalInfo.add("Mobile: "+rphone+" \n\n Email: "+remail+" \n\n");

        prPersinalInfo.setAlignment(Element.ALIGN_CENTER);

        document.add(prPersinalInfo);
        document.add(myTable);
        document.add(myTable);

        Paragraph prProfile = new Paragraph();
        prProfile.setFont(smallBold);
        prProfile.add("\n \n Symptoms \n ");
        prProfile.setFont(normal);
        prProfile.add("\n"+rdis);

        prProfile.setFont(smallBold);
        prProfile.setAlignment(Element.ALIGN_CENTER);
        document.add(prProfile);


        Paragraph bottom = new Paragraph();
        bottom.setFont(normal);
        bottom.add("\n\n\n\n\n\n\n\n\n\nCopyright-Medicure Applications 2018.");
        bottom.setAlignment(Element.ALIGN_BOTTOM);
        bottom.setAlignment(Element.ALIGN_CENTER);
        document.add(bottom);



// Create new Page in PDF
        document.newPage();
    }



}
