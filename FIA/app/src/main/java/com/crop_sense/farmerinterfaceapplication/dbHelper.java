package com.crop_sense.farmerinterfaceapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nima on 11/20/2015.
 */
public class dbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "test2.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + dbContract.FeedEntry.TABLE_NAME + " (" +
                    dbContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    dbContract.FeedEntry.COLUMN_NAME_MAC + TEXT_TYPE + COMMA_SEP +
                    dbContract.FeedEntry.COLUMN_NAME_SERIAL + TEXT_TYPE + COMMA_SEP +
                    dbContract.FeedEntry.COLUMN_NAME_AID + TEXT_TYPE + COMMA_SEP +
                    dbContract.FeedEntry.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    dbContract.FeedEntry.COLUMN_NAME_GPS + TEXT_TYPE + COMMA_SEP +
                    dbContract.FeedEntry.COLUMN_NAME_PESTS + TEXT_TYPE +" )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + dbContract.FeedEntry.TABLE_NAME;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        database.execSQL(SQL_DELETE_ENTRIES);
        onCreate(database);
    }
}