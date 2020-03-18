package com.example.fitnessapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class SelectedSportDetails extends AppCompatActivity {

    private String sportName;
    private String sportCal;
    private int sportAmount;
    private String formattedSport;
    private String sport;

    private TextView nameTextView;
    private TextView calTextView;
    private Button ChangeAmountButton;
    private Button addSportAmountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_sport_details);

        Toolbar myToolbar = findViewById(R.id.SelectedListActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();

        Intent thisActivity = getIntent();
        sport = thisActivity.getStringExtra("sport");
        formatFoodString(1);
        fillDetails();
    }

    private void getViews() {
        nameTextView = findViewById(R.id.nameTextView);
        calTextView = findViewById(R.id.calTextView);
        ChangeAmountButton = findViewById(R.id.ChangeAmountButton);
        addSportAmountButton = findViewById(R.id.addSportAmountButton);
    }

    private void fillDetails() {
        nameTextView.setText(sportName);
        calTextView.setText(sportCal);
    }

    private void formatFoodString(int amount) {
        String[] sportSplit = sport.split("\\n");
        sportName = sportSplit[0].replace("10 min",String.valueOf(amount*10) + " min");
        sportCal = "Calories: " + String.valueOf(Double.parseDouble(sportSplit[1].split("\\s+")[1].replace(",",".")) * amount );
        fillDetails();
        formattedSport = sportSplit[0].replace("10 min",String.valueOf(amount*10) + " min")+ " SPORTDETAILS SEPARATOR "+
                sportCal;
    }


    public void changeSportAmount(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item));
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(40);
        numberPicker.setMinValue(0);

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int temp = value * 10;
                return "" + temp + " perc";
            }
        };
        numberPicker.setFormatter(formatter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mennyiség");
        builder.setMessage("Válassz mennyiséget!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChangeAmountButton.setText(String.valueOf(numberPicker.getValue()*10) + " perc");
                sportAmount = numberPicker.getValue();
                formatFoodString(sportAmount);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setView(numberPicker).create().show();
    }

    public void updateSportInDatabase(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item));

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String kor = sharedPref.getString("kor","0");
        String nem = sharedPref.getString("nem","");
        String suly = sharedPref.getString("súly","0");
        String magassag = sharedPref.getString("magasság","0");
        String cel = sharedPref.getString("cél","");
        String date = sharedPref.getString("dátum","0");

        Cursor updatedDate;
        HistoryTableDML hTD = new HistoryTableDML(this);
        updatedDate = hTD.getHistoryBydate(date);
        String inDB = updatedDate.getString(8);
        String SportsChained = inDB + formattedSport + " SPORT SEPARATOR ";
        hTD.updateHistorySport(date,cel,Integer.parseInt(kor),Integer.parseInt(magassag), Integer.parseInt(suly),nem,SportsChained);
        Toast.makeText(this,"hozzáadva a mai tevékenységedhez!",Toast.LENGTH_LONG).show();
        finish();
    }
}