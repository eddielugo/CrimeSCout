package com.crime.cout.GPS;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class GpsManager
{
    private static final int gpsUpdateTime = 1000; // 15 seconds
    private static final int gpsUpdateDistance = 0;
    private static LocationManager locationManager = null;
    private static LocationListener locationListener = null;
    private static GpsUpdate gpsUpdate = null;
    Context mContext;
    // constructor
    public GpsManager(Context context) {
        mContext=context;// assign context

        // GPS location listener
        GpsManager.locationListener = new LocationListener() {
            @Override
            // when location change detected
            public void onLocationChanged(final Location location) {
                if (GpsManager.gpsUpdate != null) {
                    GpsManager.gpsUpdate.onGPSUpdate(location); // call the interface
                }
            }
            @Override
            public void onProviderDisabled(final String provider) {
            }
            @Override
            public void onProviderEnabled(final String provider) {
            }
            @Override
            public void onStatusChanged(final String provider, final int status, final Bundle extras) {
            }
        };
    }
    // set call back
    public void setGPSCallback(final GpsUpdate gpsUpdate) {
        GpsManager.gpsUpdate = gpsUpdate;
    }

    // startListening() function will start gps
    public void startGPS(final Context context) {
        if (GpsManager.locationManager == null) {
            GpsManager.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        final Criteria GPSCriteria = new Criteria();
        GPSCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        GPSCriteria.setSpeedRequired(true);
        GPSCriteria.setAltitudeRequired(false);
        GPSCriteria.setBearingRequired(false);
        GPSCriteria.setCostAllowed(true);
        GPSCriteria.setPowerRequirement(Criteria.POWER_HIGH);
        final String bestProvider = GpsManager.locationManager.getBestProvider(GPSCriteria, true);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        if (bestProvider != null && bestProvider.length() > 0) {
            GpsManager.locationManager.requestLocationUpdates(bestProvider, GpsManager.gpsUpdateTime,
                    GpsManager.gpsUpdateDistance, GpsManager.locationListener);
        }
        else {
            final List<String> providers = GpsManager.locationManager.getProviders(true);
            for (final String provider : providers)
            {
                // request gps location update
                GpsManager.locationManager.requestLocationUpdates(provider, GpsManager.gpsUpdateTime,
                GpsManager.gpsUpdateDistance, GpsManager.locationListener);
            }
        }
    }
}