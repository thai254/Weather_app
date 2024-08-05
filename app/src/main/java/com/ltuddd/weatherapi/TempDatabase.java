package com.ltuddd.weatherapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class TempDatabase extends SQLiteOpenHelper {
    Context context;
    final static public String dbname = "TempDB";
    final static public int version = 1;
    final static public String table_name = "Student";
    final static public String col_1 = "Id";
    final static public String col_2 = "Province_Name";
    final static public String col_3 = "District_Name";
    final static public String query = "Create table " + table_name + " " +
            "( " + col_1 + " integer PRIMARY KEY autoincrement, " + col_2 + " text, " + col_3 + " text)";

    public TempDatabase(Context context) {
        super(context, dbname, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + table_name);
        onCreate(db);
    }

    public long insertNewLocation(String prov, String dist){
        Log.v("data2",prov + "-" + dist );
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2, prov);
        contentValues.put(col_3, dist);
        SQLiteDatabase db = this.getWritableDatabase();
        long check = db.insert(table_name, null, contentValues);
        return check;
    }

    public Cursor DisplayLocation() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from " + table_name + " order by "
                + col_1 + " desc limit 1", null);
        return cursor;
    }
}
