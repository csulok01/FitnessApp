package com.example.fitnessapp;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SportDBAdapter{


    protected static final String TAG = "SportDBAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private SportDataBaseHelper mDbHelper;

    public SportDBAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new SportDataBaseHelper(mContext);
    }

    public SportDBAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public SportDBAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public Cursor getSportWhichContainsPassedString(String keyword){
        String sql;
        Cursor mCur;
        try{
            if(keyword != ""){
                sql = "SELECT * FROM Sport WHERE name LIKE '%"+keyword+"%'";
                mCur = mDb.rawQuery(sql,null);
            }else{
                sql ="SELECT * FROM Sport";
                mCur = mDb.rawQuery(sql, null);
            }
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public boolean insertInSport(String name,int calories){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("CaloriesInTenMin", calories);
        long result = mDb.insert("Sport",null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    };



}