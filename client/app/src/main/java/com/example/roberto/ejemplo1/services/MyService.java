package com.example.roberto.ejemplo1.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.media.VolumeProviderCompat;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Roberto on 28/08/2016.
 */
public class MyService extends Service implements LocationListener {
    private Context ctx;
    double longitude;
    double latitude;
    Location location;
    boolean gpsActive;
    TextView txtView;
    LocationManager locationManager;

    public MyService() {
        super();
        this.ctx = this.getApplicationContext();
    }

    public MyService(Context c) {
        super();
        this.ctx = c;
        getLocation();
    }

    public void setView(View v) {
        txtView = (TextView) v;
        txtView.setText("Location: Lat: " + latitude + " Long: " + longitude);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) this.ctx.getSystemService(LOCATION_SERVICE);
            gpsActive = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        } catch (Exception e) {
        }

        if (gpsActive) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 60 * 1000, 10, this);
            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            System.out.println(latitude);
            System.out.println(longitude);

        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
