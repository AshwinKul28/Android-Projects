package com.iashwin28.maps;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    ArrayList<LatLng> markerPoints;
    LocationManager locationManager;
    Location location;
    LatLng myPos;
    ProgressDialog progressDialog;
    Context context;
    String bestProvider;
    Marker currentLocation;
    LatLng origin, dest;
    CameraUpdate Cu;
    float bearing;
    Location prev, fin;
    CameraPosition cameraPosition, cp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        PlaceAutocompleteFragment placeFrag = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        placeFrag.setFilter(typeFilter);


        placeFrag.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(MapsActivity.this, "Place Selected " + place.getAddress(), Toast.LENGTH_SHORT).show();
                // markerPoints.add(place.getLatLng());
                // map.addMarker(new MarkerOptions().title("Destination").position(place.getLatLng()));

            }

            @Override
            public void onError(Status status) {

            }
        });

        progressDialog = new ProgressDialog(this);
        context = this;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    LatLng mumbai, myLoc;

    //    @Override
//    public void onLocationChanged(Location location) {
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        LatLng latLng = new LatLng(latitude, longitude);
//        currentLocation.remove();
//        currentLocation=map.addMarker(new MarkerOptions().position(latLng));
//        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Initializing array List
        markerPoints = new ArrayList<LatLng>();


        map = googleMap;

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


        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                //currentLocation.remove();
                currentLocation = map.addMarker(new MarkerOptions().position(latLng));
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                if (location.hasBearing()) {
                    cp = new CameraPosition.Builder()
                            .target(latLng)
                            .bearing(location.getBearing())
                            .tilt(90)
                            .zoom(map.getCameraPosition().zoom + 0.5f)
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
                }


            }
        });

        map.setBuildingsEnabled(true);
        map.setMyLocationEnabled(true);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {

            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("GPS isn't enabled. Do you want to enable? ");
            dialog.setPositiveButton("Yes, Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getBaseContext(), "GPS not enabled, Exiting!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            dialog.show();
        }
        map.setBuildingsEnabled(true);

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        Criteria criteria = new Criteria();
        bestProvider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            myPos = new LatLng(location.getLatitude(), location.getLongitude());
            currentLocation = map.addMarker(new MarkerOptions().position(myPos).title("Current Position"));
            map.moveCamera(CameraUpdateFactory.newLatLng(myPos));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                if (markerPoints.size() >= 1) {
                    markerPoints.clear();
                    map.clear();
                    location = locationManager.getLastKnownLocation(bestProvider);
                    if (location != null) {
                        Toast.makeText(getApplicationContext(), "Obtained current location", Toast.LENGTH_SHORT).show();
                        myPos = new LatLng(location.getLatitude(), location.getLongitude());
                        currentLocation = map.addMarker(new MarkerOptions().position(myPos).title("Current Position"));
                        map.animateCamera(CameraUpdateFactory.zoomTo(15));
                    } else {
                        Toast.makeText(getApplicationContext(), "Still obtaining current location!", Toast.LENGTH_SHORT).show();

                    }
                }

                markerPoints.add(point);

                MarkerOptions options = new MarkerOptions();

                options.position(point);
                options.draggable(true);

                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }

                map.addMarker(options);

                if (markerPoints.size() == 1) {
                    if (myPos != null) {
                        origin = myPos;
                        dest = markerPoints.get(0);

//                        String url = getDirectionsUrl(origin, dest);
//
//                        DownloadTask downloadTask = new DownloadTask();
//
//                        downloadTask.execute(url);
                    } else {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        location = locationManager.getLastKnownLocation(bestProvider);
                        if (location != null) {
                            markerPoints.clear();
                            map.clear();
                            Toast.makeText(getApplicationContext(), "Obtained current location", Toast.LENGTH_SHORT).show();
                            myPos = new LatLng(location.getLatitude(), location.getLongitude());
                            currentLocation = map.addMarker(new MarkerOptions().position(myPos).title("Current Position"));
                        } else {
                            Toast.makeText(getApplicationContext(), "Still obtaining current location!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public void Locate(View view) {
        String url = getDirectionsUrl(origin, dest);
        prev = convertLatLngToLocation(origin);
        fin = convertLatLngToLocation(dest);
        bearing = prev.bearingTo(fin);
        cameraPosition =
                new CameraPosition.Builder()
                        .target(myPos)
                        .bearing(bearing)
                        .tilt(90)
                        .zoom(map.getCameraPosition().zoom + 0.5f)
                        .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        DownloadTask downloadTask = new DownloadTask();

        downloadTask.execute(url);
    }


    private Location convertLatLngToLocation(LatLng latLng) {
        Location location = new Location("someLoc");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            int count;
            long total = 0;
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();
            int lengthOfFile = urlConnection.getContentLength();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
                count = line.length();
                total += count;
                // Publish the progress which triggers onProgressUpdate method
                progressDialog.setProgress((int) ((total * 100) / lengthOfFile));
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception downloading", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Routing... Please Wait!");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Routing Complete", Toast.LENGTH_SHORT).show();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJASONParser parser = new PathJASONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                if (path != null) {
                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        Location current = convertLatLngToLocation(position);
                        float bearing1 = prev.bearingTo(current);
                        CameraPosition cameraPosition1 =
                                new CameraPosition.Builder()
                                        .target(myPos)
                                        .bearing(bearing1)
                                        .tilt(90)
                                        .zoom(map.getCameraPosition().zoom + 0.5f)
                                        .build();
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1));


                        points.add(position);

                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(12);
                    lineOptions.color(Color.GREEN);
                }
            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }
}

