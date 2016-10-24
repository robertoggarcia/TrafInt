package com.example.roberto.ejemplo1;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by roberto
 */
public class GPS_Service extends Service  {

    private LocationManager locationManager;
    double lon, lat;
    MyTask myTask;
    private Context ctx;
    TextView txtView;
    boolean gpsActive;
    String ip, name;
    int id, port, rate;
    Socket socket;
    boolean socketActive;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


        try
        {
            DbConfig dbConfig =
                    new DbConfig(this, "DbConfig", null, 2);

            final SQLiteDatabase db = dbConfig.getWritableDatabase();
            Cursor c = db.rawQuery(" SELECT * FROM config", null);


            if (c.moveToFirst()) {
                id = c.getInt(0);
                name = (c.getString(1));
                ip = c.getString(2);
                port = c.getInt(3);
                rate = c.getInt(4);

                socket = IO.socket("http://" + ip + ":" + String.valueOf(port));
                socket.connect();  // initiate connection to socket server
            } else {
                System.out.println("Records not found!");
                System.out.println(c.getCount());
            }

            Toast.makeText(this, "Servicio creado", Toast.LENGTH_SHORT).show();
            myTask = new MyTask();
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //noinspection MissingPermission
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5*1000, 10, location);

            getLocation();

        }
        catch (Exception e){}


    }

    public void getLocation(){
        //noinspection MissingPermission
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        if (location!=null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
        }
    }

    private LocationListener location = new LocationListener(){
        public void onLocationChanged(Location location) {
            getLocation();
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();

        myTask.cancel(true);
        socket.close();
        super.onDestroy();
    }

    private class MyTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute(){
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("Servicio iniciado");
            for (int i = 1; i>0; i++){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress("" + lat + "," + lon);
                socket.emit("data",  id+":" + name + ":"+ lat + ":" + lon);
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(String... Values){
            super.onProgressUpdate();
            Toast.makeText(getApplicationContext(), Values[0], Toast.LENGTH_SHORT).show();
        }


        @Override
        protected void onCancelled(String s){
            super.onCancelled();
        }

    }
}
