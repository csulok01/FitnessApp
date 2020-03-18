package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class CalendarActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar myToolbar = findViewById(R.id.CalendarActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MaterialCalendarView materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String dateMonth = String.valueOf(date.getMonth());
                String dateDay = String.valueOf(date.getDay());
                if(date.getMonth()<10){
                    dateMonth = "0"+date.getMonth();
                }
                if(date.getDay()<10){
                    dateDay = "0"+date.getDay();
                }

                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("dátum", date.getYear() + "-" + dateMonth + "-" + dateDay);
                    editor.apply();
                    //getBackToMain();
                    Intent intent = getIntent();
                    intent.putExtra("dátum", date.getYear() + "-" + dateMonth + "-" + dateDay);
                    setResult(RESULT_OK, intent);
                    finish();
            }
        });

    }

    private boolean getSelectedDateData(String date) {
        HistoryTableDML historyTableDML = new HistoryTableDML(this);
        Cursor cursor = historyTableDML.getHistoryBydate(date);
        if(!cursor.moveToFirst()){
            Toast.makeText(this,"nincs adat ezen a napon!",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void getBackToMain() {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}
