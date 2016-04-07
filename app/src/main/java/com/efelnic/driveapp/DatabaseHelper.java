package com.efelnic.driveapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "race.db";
    public static final String TABLE_NAME = "race_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DISTANCE";
    public static final String COL_3 = "ACCELERATION";
    public static final String COL_4 = "DURATION";
    public static final String COL_5 = "SPEED";
    public static final String COL_6 = "DATE";
    public static final String COL_7 = "XACCEL";
    public static final String COL_8 = "YACCEL";
    public static final String COL_9 = "ZACCEL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DISTANCE TEXT, ACCELERATION TEXT, DURATION TEXT, SPEED TEXT, DATE TEXT, XACCEL TEXT, YACCEL TEXT, ZACCEL TEXT)");
    }

    public boolean insertData(String distance, String acceleration, String duration, String speed, String xaccel, String yaccel, String zaccel){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        // Have the Date in a nice format
        String Date = timeStamp.substring(4,6) + "/" + timeStamp.substring(6,8) + "/" + timeStamp.substring(0,4) + " - " + timeStamp.substring(9,11) + ":" + timeStamp.substring(11,13) + ":" + timeStamp.substring(13, 15) + "s";

        contentValues.put(COL_2, distance);
        contentValues.put(COL_3, acceleration);
        contentValues.put(COL_4, duration);
        contentValues.put(COL_5, speed);
        contentValues.put(COL_6, Date);
        contentValues.put(COL_7, xaccel);
        contentValues.put(COL_8, yaccel);
        contentValues.put(COL_9, zaccel);

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
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DISTANCE TEXT, ACCELERATION FLOAT, DURATION TEXT, SPEED TEXT, DATE TEXT, XACCEL TEXT, YACCEL TEXT, ZACCEL TEXT)"); //, DATE TEXT)"); //Creates emtpy table in order to restart ids
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
