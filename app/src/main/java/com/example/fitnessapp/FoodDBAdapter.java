package com.example.fitnessapp;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FoodDBAdapter{


    protected static final String TAG = "FoodDBAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private FoodDataBaseHelper mDbHelper;

    public FoodDBAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new FoodDataBaseHelper(mContext);
    }

    public FoodDBAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public FoodDBAdapter open() throws SQLException {
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


    public Cursor getFoodWhichContainsPassedString(String keyword){
        String sql;
        Cursor mCur;
        try{
            if(keyword != ""){
            sql = "SELECT * FROM Food WHERE name LIKE '%"+keyword+"%'";
            mCur = mDb.rawQuery(sql,null);
            }else{
                sql ="SELECT * FROM Food";
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

    public boolean insertInFood(String name,int calories, int protein, int fat, int carbohydrate){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("CaloriesInTenDkgs", calories);
        contentValues.put("ProteinInTenDkgs", protein);
        contentValues.put("FatInTenDkgs", fat);
        contentValues.put("CarbohydrateInTenDkgs", carbohydrate);
        long result = mDb.insert("Food",null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    };



}