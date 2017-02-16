package com.uc3m.trippy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TripDbHelper extends SQLiteOpenHelper {

    public TripDbHelper(Context context) {
        super(context, TripContract.DB_NAME, null, TripContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TripContract.TripEntry.TABLE + " ( " +
                TripContract.TripEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TripContract.TripEntry.COL_TRIP_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TripContract.TripEntry.TABLE);
        onCreate(db);
    }
}
