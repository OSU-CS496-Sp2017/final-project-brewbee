package com.example.android.brewbee.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cameron on 6/10/2017.
 */

public class BrewSearchDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favoriteBrew.db";
    private static final int DATABASE_VERSION = 1;

    public BrewSearchDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void  onCreate(SQLiteDatabase db){
        final String SQL_CREATE_FAVORITE_BEER_TABLE =
                "CREATE TABLE " + BrewSearchContract.FavoriteBrew.TABLE_NAME + " (" +
                 BrewSearchContract.FavoriteBrew._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 BrewSearchContract.FavoriteBrew.COLUMN_BEER_NAME + " TEXT NOT NULL, " +
                 BrewSearchContract.FavoriteBrew.COLUMN_DESCRIPTION + " TEXT, " +
                 BrewSearchContract.FavoriteBrew.COLUMN_BREWERY + " TEXT, " +
                 BrewSearchContract.FavoriteBrew.COLUMN_API_ID + " TEXT NOT NULL, " +
                 BrewSearchContract.FavoriteBrew.COLUMN_ABV_MIN + " REAL " +
                 BrewSearchContract.FavoriteBrew.COLUMN_ABV_MAX + " REAL " +
                 ");";
        db.execSQL(SQL_CREATE_FAVORITE_BEER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + BrewSearchContract.FavoriteBrew.TABLE_NAME);
        onCreate(db);
    }
}
