package com.example.roberto.ejemplo1;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Switch btnOn;
    private TextView txtView;
    private BroadcastReceiver broadcastReceiver;
    Context ctx;
    private Button btnConfig, btnMysql;
    private static final String KEY_INDEX = "index";
    int isStartService;
    private Intent i;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt("startService", isStartService);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isStartService = savedInstanceState.getInt("startService");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(ctx,GPS_Service.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.roberto.trafint.R.layout.activity_main);
        ctx = this;
        i = new Intent(ctx, GPS_Service.class);

        btnOn = (Switch)findViewById(com.example.roberto.trafint.R.id.btnOn);
        txtView = (TextView)findViewById(com.example.roberto.trafint.R.id.txtView);
        btnConfig = (Button)findViewById(com.example.roberto.trafint.R.id.btnConfig);
        btnMysql = (Button)findViewById(com.example.roberto.trafint.R.id.btnMySql);

        if(isStartService == 1){
            btnOn.setChecked(true);
        } else {
            btnOn.setChecked(false);
        }

        btnConfig.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intConfig = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity((intConfig));
            }
        });

        btnMysql.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intMySql = new Intent(MainActivity.this, MySqlActivity.class);
                startActivity((intMySql));
            }
        });

        if (btnOn.isChecked()) {
            btnOn.setText("On");
        } else {
            btnOn.setText("Off");
        }

        if(!runtime_permissions())
            enable_buttons();
    }

    private void enable_buttons() {
        btnOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    if(isStartService == 0) {
                        btnOn.setText("On");
                        System.out.println("On");
                        isStartService = 1;

                        startService(i);
                        txtView.setText("Servicio funcionando...");
                    } else {
                        Toast.makeText(getApplicationContext(),"El servicio ya esta corriendo", Toast.LENGTH_SHORT);
                    }
                } else {
                    btnOn.setText("Off");
                    System.out.println("Off");
                    isStartService = 0;
                    txtView.setText("Iniciar el servicio");
                    stopService(i);
                }
            }
        });
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else {
                runtime_permissions();
            }
        }
    }


    }
