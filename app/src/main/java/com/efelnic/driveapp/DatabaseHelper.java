package com.efelnic.driveapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "race.db";
    public static final String TABLE_NAME = "race_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DATE";
    public static final String COL_3 = "DURATION";
    public static final String COL_4 = "DISTANCE";
    public static final String COL_5 = "SPEED";
    public static final String COL_6 = "ACCELERATION";
    public static final String COL_7 = "XACCEL";
    public static final String COL_8 = "YACCEL";
    public static final String COL_9 = "ZACCEL";
    public static final String COL_10 = "LAPTIME";
    public static final String COL_11 = "AVERAGESPEED";
    public static final String COL_12 = "AVERAGELAPTIME";
    public static final String COL_13 = "MAXLINACCELVALUE";
    public static final String COL_14 = "MAXXACC";
    public static final String COL_15 = "MAXYACC";
    public static final String COL_16 = "MAXZACC";
    public static final String COL_17 = "MAXSPEED";
    public static final String COL_18 = "AVGXACC";
    public static final String COL_19 = "AVGYACC";
    public static final String COL_20 = "AVGZACC";
    public static final String COL_21 = "AVGLINACC";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE TEXT, DURATION TEXT, DISTANCE TEXT, SPEED TEXT, ACCELERATION TEXT, XACCEL TEXT, YACCEL TEXT, ZACCEL TEXT, LAPTIME TEXT, AVERAGESPEED TEXT, AVERAGELAPTIME TEXT, MAXLINACCELVALUE TEXT, MAXXACC TEXT, MAXYACC TEXT, MAXZACC TEXT, MAXSPEED TEXT, AVGXACC TEXT, AVGYACC TEXT, AVGZACC TEXT, AVGLINACC TEXT)");
    }

    public boolean insertData(String duration, String distance, String speed, String acceleration, String xaccel, String yaccel, String zaccel, String laptime, String avgspd, String avglaptime, String maxlinaccel, String maxxaccel, String maxyaccel, String maxzaccel, String maxspeed, String avgxacc, String avgyacc, String avgzacc, String avglinacc){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();




        String date = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(Calendar.getInstance().getTime());//With MILLISECONDS, remove the '.SS' to get rid of them

        contentValues.put(COL_2, date);
        contentValues.put(COL_3, duration);
        contentValues.put(COL_4, distance);
        contentValues.put(COL_5, speed);
        contentValues.put(COL_6, acceleration);
        contentValues.put(COL_7, xaccel);
        contentValues.put(COL_8, yaccel);
        contentValues.put(COL_9, zaccel);
        contentValues.put(COL_10, laptime);
        contentValues.put(COL_11, avgspd);
        contentValues.put(COL_12, avglaptime);
        contentValues.put(COL_13, maxlinaccel);
        contentValues.put(COL_14, maxxaccel);
        contentValues.put(COL_15, maxyaccel);
        contentValues.put(COL_16, maxzaccel);
        contentValues.put(COL_17, maxspeed);
        contentValues.put(COL_18, avgxacc);
        contentValues.put(COL_19, avgyacc);
        contentValues.put(COL_20, avgzacc);
        contentValues.put(COL_21, avglinacc);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if ( result == -1 ){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);

        return result;
    }

    public Cursor getIdData(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.query("select * from " + TABLE_NAME, new String[]{"ID", "DATE"}, null, null, null, null, null);

        return result;
    }

    public String getRowcol(int id, int col){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor stuff = db.rawQuery("select * from " + TABLE_NAME + " WHERE ID=" + Integer.toString(id), null);

        if(stuff != null)
            stuff.moveToFirst();

        String data = stuff.getString(col);
        return data;
    }

    public Cursor getRow(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor stuff = db.rawQuery("select * from " + TABLE_NAME + " WHERE ID=" + id, null);

//        if(stuff != null)
//            stuff.moveToFirst();

        return stuff;
    }


    public void deleteEverything(){
        SQLiteDatabase db = this.getReadableDatabase();

        //db.execSQL("DELETE FROM " + TABLE_NAME);
        //temporary way, until we need to delete individual items
        db.execSQL("DROP TABLE " + TABLE_NAME);//Deletes entire table
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE TEXT, DURATION TEXT, DISTANCE TEXT, SPEED TEXT, ACCELERATION TEXT, XACCEL TEXT, YACCEL TEXT, ZACCEL TEXT, AVERAGESPEED TEXT, AVERAGELAPTIME TEXT, MAXLINACCELVALUE TEXT, MAXXACC TEXT, MAXYACC TEXT, MAXZACC TEXT, MAXSPEED TEXT, AVGXACC TEXT, AVGYACC TEXT, AVGZACC TEXT, AVGLINACC TEXT)"); //Creates emtpy table in order to restart ids
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.delete(TABLE_NAME, "ID = ?", new String[] { id });
    }

    //---deletes a particular title---
    public void deleteByID(String ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.delete(TABLE_NAME, "ID = ?", new String[] {ID});
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }


}
