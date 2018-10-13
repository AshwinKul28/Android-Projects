package com.iashwin28.pdfgenerator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    Location location;
    List<Address> addresses;

    String city,state,country,address,postalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 5);


        locationManager = (LocationManager) MainActivity.this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                Toast.makeText(this, "State : "+state+" City : "+city+" Country : "+country, Toast.LENGTH_SHORT).show();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }




        String FILE = Environment.getExternalStorageDirectory().toString()
                +"/Health PDF/"+"Testdoc.pdf";
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

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


//    public void location (View v)
//    {
//        locationManager = (LocationManager) MainActivity.this.getSystemService(LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        Geocoder gcd = new Geocoder(getBaseContext(),
//                Locale.getDefault());
//
//
//        try {
//            addresses = gcd.getFromLocation(location.getLatitude(),
//                    location.getLongitude(), 1);
//            if (addresses.size() > 0) {
//                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                city = addresses.get(0).getLocality();
//                String subLocality = addresses.get(0).getSubLocality();
//                state = addresses.get(0).getAdminArea();
//                country = addresses.get(0).getCountryName();
//                postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();
//
//                Log.e("test",state+" "+country);
//
//                Toast.makeText(this, "State : "+state+" City : "+city+" Country : "+country, Toast.LENGTH_SHORT).show();
//            }
//
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//    }

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

// Now Start another New Paragraph
        Paragraph prPersinalInfo = new Paragraph();
        prPersinalInfo.setFont(smallBold);
        prPersinalInfo.add("Name : Kirt Keskar\n\n");
        prPersinalInfo.add("AGE : 22 Years\n\n");
        prPersinalInfo.add("Address : "+address+"\n\n");
        prPersinalInfo.add("City: "+city+"    State: "+state+"\n\n");
        prPersinalInfo.add("Country: "+country+"    Zip Code: "+postalCode+"\n\n");
        prPersinalInfo.add("Mobile: 7798080437 \n\n Email: ashwin.kulkarni128@gmail.com \n\n");

        prPersinalInfo.setAlignment(Element.ALIGN_CENTER);

        document.add(prPersinalInfo);
        document.add(myTable);
        document.add(myTable);

        Paragraph prProfile = new Paragraph();
        prProfile.setFont(smallBold);
        prProfile.add("\n \n Symptoms \n ");
        prProfile.setFont(normal);
        prProfile.add("\nHeadache, Heart disease");

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


    public void pdfview(View v)
    {
        Intent i = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(i);
    }




}
