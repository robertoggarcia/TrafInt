package com.example.roberto.ejemplo1;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlActivity extends AppCompatActivity {
    private TextView txtName, txtIp, txtPort, txtRate, txtId;
    private Button btnSave;

    private static final String url = "jdbc:mysql://148.220.63.30:3306/android";
    private static final String user = "android";
    private static final String pass = "";
    private Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.roberto.trafint.R.layout.activity_my_sql);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        txtName = (TextView)findViewById(com.example.roberto.trafint.R.id.txtName);
        txtIp = (TextView)findViewById(com.example.roberto.trafint.R.id.txtIp);
        txtPort = (TextView)findViewById(com.example.roberto.trafint.R.id.txtPort);
        txtRate = (TextView)findViewById(com.example.roberto.trafint.R.id.txtRate);
        txtId = (TextView)findViewById(com.example.roberto.trafint.R.id.txtId);
        btnSave = (Button)findViewById(com.example.roberto.trafint.R.id.btnSave);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Databaseection success");
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

            String result = "Database connection success\n";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from config");
            ResultSetMetaData rsmd = rs.getMetaData();

            while(rs.next()) {
                System.out.println(String.valueOf(rs.getInt(1)));
                txtId.setText(String.valueOf(rs.getInt(1)));
                txtName.setText(rs.getString(2));
                txtIp.setText(rs.getString(3));
                txtPort.setText(String.valueOf(rs.getInt(4)));
                txtRate.setText(String.valueOf(rs.getInt(5)));
                System.out.println(String.valueOf(rs.getInt(5)));
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Statement st = null;
                try {
                    st = con.createStatement();
                    st.executeUpdate("UPDATE config SET name='" + txtName.getText() + "', ip='" + txtIp.getText() + "', port=" + txtPort.getText() + ", rate=" + txtRate.getText() + " WHERE id=" + txtId.getText());
                    con.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }

                finish();
            }
        });
    }

    public void testDB() {



    }
}
