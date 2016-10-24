package com.example.roberto.ejemplo1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigActivity extends AppCompatActivity {
    private TextView txtName, txtIp, txtPort, txtRate, txtId;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.roberto.trafint.R.layout.activity_config);

        txtName = (TextView)findViewById(com.example.roberto.trafint.R.id.txtName);
        txtIp = (TextView)findViewById(com.example.roberto.trafint.R.id.txtIp);
        txtPort = (TextView)findViewById(com.example.roberto.trafint.R.id.txtPort);
        txtRate = (TextView)findViewById(com.example.roberto.trafint.R.id.txtRate);
        txtId = (TextView)findViewById(com.example.roberto.trafint.R.id.txtId);
        btnSave = (Button)findViewById(com.example.roberto.trafint.R.id.btnSave);

        txtId.setEnabled(false);
        try {
            DbConfig dbConfig =
                    new DbConfig(this, "DbConfig", null, 2);

            final SQLiteDatabase db = dbConfig.getWritableDatabase();
            Cursor c = db.rawQuery(" SELECT * FROM config", null);


            if (c.moveToFirst()) {
                txtId.setText(String.valueOf(c.getInt(0)));
                txtName.setText(c.getString(1));
                txtIp.setText(c.getString(2));
                txtPort.setText(String.valueOf(c.getInt(3)));
                txtRate.setText(String.valueOf(c.getInt(4)));


            } else {
                System.out.println("Records not found!");
                System.out.println(c.getCount());

            }

            btnSave.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    try{
                        db.execSQL("UPDATE config SET name='" + txtName.getText() + "', ip='" + txtIp.getText() + "', port=" + txtPort.getText() + ", rate=" + txtRate.getText() + " WHERE id=" + txtId.getText());
                        Toast.makeText(getApplicationContext(), "Guardando...", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e){
                        System.out.println("Error DB: " + e);
                        Toast.makeText(getApplicationContext(),"Error, intentalo de nuevo!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        } catch (Exception e){
            System.out.println("Error, connect to db: " + e);
        }
    }
}
