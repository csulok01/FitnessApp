package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivitiesListActivity extends AppCompatActivity {
    private String sportActivitiesDoneString;
    private String foodActivitiesDoneString;
    private List<String> activtiesDone;
    private List<String> sportsDone;
    private RecyclerView mRecyclerView;
    private ActivitiesListAdapter mAdapter;
    private String kor;
    private String nem;
    private String suly;
    private String magassag;
    private String cel;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);

        Toolbar myToolbar = findViewById(R.id.ActivitiesListActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        kor = sharedPref.getString("kor", "0");
        nem = sharedPref.getString("nem", "");
        suly = sharedPref.getString("súly", "0");
        magassag = sharedPref.getString("magasság", "0");
        cel = sharedPref.getString("cél", "");
        date = sharedPref.getString("dátum","0");

        HistoryTableDML hTD = new HistoryTableDML(this);
        Cursor todayActivity = hTD.getHistoryBydate(date);
        todayActivity.moveToFirst();

        foodActivitiesDoneString = todayActivity.getString(7).replace("NUTRITION SEPARATOR", "\n");
        sportActivitiesDoneString = todayActivity.getString(8).replace("SPORTDETAILS SEPARATOR","\n");
        do{
            activtiesDone = Arrays.asList(foodActivitiesDoneString.split("FOOD SEPARATOR"));
            sportsDone = Arrays.asList(sportActivitiesDoneString.split("SPORT SEPARATOR"));
        }while(todayActivity.moveToNext());
        List<String> sportAndFoodActivities = new ArrayList<String>(activtiesDone);
        sportAndFoodActivities.addAll(sportsDone);
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new ActivitiesListAdapter(this, sportAndFoodActivities,kor,nem,suly,magassag,cel,date);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}