package com.example.dnetapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

class MyLocationListener implements LocationListener {

    private static String currLongitude = "";
    private static String currLatitude = "";

    public String getLongitude(){
        return currLongitude;
    }

    public String getLatitude(){
        return currLatitude;
    }

    @Override
    public void onLocationChanged(Location loc) {
        currLongitude = "" + loc.getLongitude();
        currLatitude = "" + loc.getLatitude();

    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
