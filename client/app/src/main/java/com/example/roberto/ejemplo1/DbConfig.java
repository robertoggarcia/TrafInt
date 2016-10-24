package com.example.roberto.ejemplo1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Roberto on 28/08/2016.
 */
public class DbConfig extends SQLiteOpenHelper {

    String queryCreate = "CREATE TABLE config (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, ip TEXT, port INTEGER, rate INTEGER)";
    String queryData = "INSERT INTO config(name, ip, port, rate) VALUES('roberto', '192.168.1.1', '3001', 5)";
    String newQueryCreate = "CREATE TABLE config (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, ip TEXT, port INTEGER, rate INTEGER)";
    public DbConfig(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(queryCreate);
        db.execSQL(queryData);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS config");
        db.execSQL(newQueryCreate);
    }
}
