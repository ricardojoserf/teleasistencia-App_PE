package com.example.app;

/**
 * @author Ricardo Ruiz
 *
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ValoresSQLiteOpenHelper extends SQLiteOpenHelper {

    public ValoresSQLiteOpenHelper(Context context, String nombre, CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table valores(pulso integer, tension integer, azucar integer,"
        		+ " humedad integer, gas integer )");
       
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
   
        db.execSQL("drop table if exists valores");
        db.execSQL("create table valores(pulso integer, tension integer, azucar integer,"
        		+ " humedad integer, gas integer )");
         
    }    

}