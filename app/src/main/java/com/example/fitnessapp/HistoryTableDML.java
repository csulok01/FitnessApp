package com.example.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.prolificinteractive.materialcalendarview.CalendarDay;


public class HistoryTableDML extends HistoryDataBaseHelper {

    public HistoryTableDML(Context context) {
        super(context);
    }


    public Cursor getAllDateHistory() {
        String refQuery = "Select * From " + HISTORY_TABLE_NAME + " ORDER BY date asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(refQuery, null);
        cursor.moveToFirst();
        db.close();
        return cursor;
    }

    public void updateHistoryFood(String date, String goal, int age, int height, int weight, String gender, String foodEater){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("age",age);
        contentValues.put("height",height);
        contentValues.put("weight",weight);
        contentValues.put("gender",gender);
        contentValues.put("foodEaten",foodEater);
        contentValues.put("goal",goal);
        db.update(HISTORY_TABLE_NAME,contentValues,
                "date = '"+date + "'",null);
        db.close();
    }

    public void deleteHistory(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HISTORY_TABLE_NAME,"date = '"+date+"'",null);
    }


    public void updateHistorySport(String date,String goal, int age, int height, int weight, String gender, String sportsDone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("age",age);
        contentValues.put("height",height);
        contentValues.put("weight",weight);
        contentValues.put("gender",gender);
        contentValues.put("sportsDone",sportsDone);
        contentValues.put("goal",goal);
        db.update(HISTORY_TABLE_NAME,contentValues,
                "date = '"+date + "'",null);
        db.close();
    }

    public void updateHistoryCurrentWeight(String date, String goal, int age, int height, int weight, String gender, String currentWeight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("age",age);
        contentValues.put("height",height);
        contentValues.put("weight",weight);
        contentValues.put("gender",gender);
        contentValues.put("currentWeight",currentWeight);
        contentValues.put("goal",goal);
        db.update(HISTORY_TABLE_NAME,contentValues,
                "date = '"+date + "'",null);
        db.close();
    }

    public void updateHistoryCurrentWater(String date, String goal, int age, int height, int weight, String gender, String water) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("age",age);
        contentValues.put("height",height);
        contentValues.put("weight",weight);
        contentValues.put("gender",gender);
        contentValues.put("waterDrunken",water);
        contentValues.put("goal",goal);
        db.update(HISTORY_TABLE_NAME,contentValues,
                "date = '"+date + "'",null);
        db.close();
    }

    public Cursor getHistoryBydate(String date){
        String refQuery = "Select * From " + HISTORY_TABLE_NAME + " WHERE date = '" +  date + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(refQuery, null);
        if(!cursor.moveToFirst()){
        }
        db.close();
        return cursor;
    }


    public int createHistory(String currentDate,String goal, int age, int height, int weight, String gender) {
        String refQuery = "Select * From " + HISTORY_TABLE_NAME + " WHERE date = '" +  currentDate + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(refQuery, null);
        if(!cursor.moveToFirst()) {
            SQLiteDatabase dbToWrite = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("date", currentDate);
            values.put("goal", goal);
            values.put("age", age);
            values.put("height", height);
            values.put("gender", gender);
            values.put("weight", weight);
            values.put("foodEaten", "");
            values.put("sportsDone", "");
            values.put("waterDrunken", 0);
            values.put("currentWeight", 0);
            dbToWrite.insert(HISTORY_TABLE_NAME, null, values);
            return 1;
        }else{
            return 0;
        }
    }
}
