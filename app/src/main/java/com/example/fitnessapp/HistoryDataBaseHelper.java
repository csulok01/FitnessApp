package com.example.fitnessapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "History";
    protected static final String HISTORY_TABLE_NAME = "history";

    public static final String CREATE_FIRST_TABLE = "create table if not exists "
            + HISTORY_TABLE_NAME
            + " ( id INTEGER primary key autoincrement,date TEXT NOT NULL, goal TEXT NOT NULL, age INTEGER NOT NULL, height INTEGER NOT NULL,gender TEXT NOT NULL, " +
              "weight int NOT NULL, foodEaten TEXT, sportsDone TEXT, waterDrunken INTEGER, currentWeight INTEGER)";


    public HistoryDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FIRST_TABLE);
        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //THIS WILL BE EXECUTED WHEN YOU UPDATED VERSION OF DATABASE_VERSION
        //YOUR DROP AND CREATE QUERIES
    }

}
