package com.efelnic.driveapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by PCS on 2016-03-30.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "race.db";
    public static final String TABLE_NAME = "race_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DISTANCE";
    public static final String COL_3 = "ACCELERATION";
    public static final String COL_4 = "DURATION";
    public static final String COL_5 = "SPEED";
    public static final String COL_6 = "DATE";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db .execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DISTANCE TEXT, ACCELERATION TEXT, DURATION TEXT, SPEED TEXT, DATE TEXT)"); //, DATE TEXT)");

    }

    public boolean insertData(String distance, String acceleration, String duration, String speed){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        contentValues.put(COL_2, distance);
        contentValues.put(COL_3, acceleration);
        contentValues.put(COL_4, duration);
        contentValues.put(COL_5, speed);
        contentValues.put(COL_6, timeStamp);

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

    public void deleteEverything(){
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME);

    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.delete(TABLE_NAME, "ID = ?", new String[] { id });

    }


}
