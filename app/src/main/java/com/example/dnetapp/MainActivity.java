package com.example.dnetapp;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    LocationHelper.LocationResult locationResult;
    LocationHelper locationHelper;

    String latitude = "";
    String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        try {
            this.locationResult = new LocationHelper.LocationResult(){
                @Override
                public void gotLocation(Location location){

                    //Got the location!
                    if(location != null){

                        latitude = "" + location.getLatitude();
                        longitude = "" + location.getLongitude();

                        // here you can save the latitude and longitude values
                        // maybe in your text file or database

                    } else {

                    }

                }

            };

            // initialize our useful class,
            this.locationHelper = new LocationHelper();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        this.locationHelper.getLocation(getApplicationContext(), this.locationResult);
        new Thread(new Task()).start();
    }

    class Task implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(1000);
                        String ocdParams = "q=" + latitude +
                                        "+" + longitude +
                                        "&key=" + getString(R.string.OCD_API_KEY);

                        JSONObject ocdJson = new JSONObject(getJSONFromURLConnection("https://api.opencagedata.com/geocode/v1/json?" + ocdParams));

                        final String fullAddress = new JSONObject(ocdJson.getJSONArray("results").get(0).toString())
                                .getString("formatted");
                        String owmParams = "lat=" + latitude + "&lon=" + longitude +
                                           "&appid=" + getString(R.string.OWM_API_KEY);
                        JSONObject owmJson = new JSONObject(getJSONFromURLConnection("http://api.openweathermap.org/data/2.5/weather?" + owmParams));
                        final String weatherIconId = new JSONObject(owmJson.getJSONArray("weather")
                                                                    .get(0).toString())
                                                                    .getString("icon");

                        final String weatherStatus =  new JSONObject(owmJson.getJSONArray("weather")
                                .get(0).toString())
                                .getString("main");
                        final String weatherDescription =  new JSONObject(owmJson.getJSONArray("weather")
                                .get(0).toString())
                                .getString("description");

                        runOnUiThread (new Thread(new Runnable() {
                            public void run() {
                                EditText multiLineText = (EditText)findViewById(R.id.editText2);
                                Picasso.get().load("http://openweathermap.org/img/wn/" + weatherIconId + "@2x.png")
                                        .resize(200,200)
                                        .into((ImageView)findViewById(R.id.imageView));
                                String textDisplay = "Status: " + weatherStatus + "\n" +
                                                     "Description: " + weatherDescription + "\n" +
                                                     "Location: " + fullAddress;
                                multiLineText.setText(textDisplay);
                            }
                        }));
                        Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    //e.printStackTrace();
                }
            }
        }
    }

    private String getJSONFromURLConnection(String urlString) {
        Log.v("URL",urlString);
        BufferedReader reader = null;
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");

            if (urlConnection.getResponseCode() == 200) {
                InputStream stream = urlConnection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return buffer.toString();
            } else {
                Log.e("Error", "Error response code: " +
                        urlConnection.getResponseCode());
                return "err";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }
}
