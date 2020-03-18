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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class SelectedFoodDetails extends AppCompatActivity {

    private String foodName;
    private String foodCal;
    private String foodProtein;
    private String foodFat;
    private String foodCarb;
    private int foodAmount;
    private String formattedFood;
    private String food;

    private TextView nameTextView;
    private TextView calTextView;
    private TextView protTextView;
    private TextView fatTextView;
    private TextView carbTextView;
    private Button ChangeAmountButton;
    private Button addFoodAmountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_food_details);

        Toolbar myToolbar = findViewById(R.id.SelectedListActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();

        Intent thisActivity = getIntent();
        food = thisActivity.getStringExtra("food");
        formatFoodString(10);
        fillDetails();
    }

    private void getViews() {
        nameTextView = findViewById(R.id.nameTextView);
        calTextView = findViewById(R.id.calTextView);
        protTextView = findViewById(R.id.protTextView);
        fatTextView = findViewById(R.id.fatTextView);
        carbTextView = findViewById(R.id.carbTextView);
        ChangeAmountButton = findViewById(R.id.ChangeAmountButton);
        addFoodAmountButton = findViewById(R.id.addFoodAmountButton);
    }

    private void fillDetails() {
        nameTextView.setText(foodName);
        calTextView.setText(foodCal);
        protTextView.setText(foodProtein);
        fatTextView.setText(foodFat);
        carbTextView.setText(foodCarb);
    }

    private void formatFoodString(int amount) {
        String[] foodSplit = food.split("\\n");
        foodName = foodSplit[0].split(",")[0];
        foodCal = "Calories: " + String.valueOf((Double.parseDouble(foodSplit[1].split("\\s+")[1].replace(",",".")) * (double)amount )/10);
        foodProtein = "protein: " + String.valueOf((Double.parseDouble(foodSplit[2].split("\\s+")[1].replace(",",".")) * (double)amount )/10);
        foodFat = "fat: " + String.valueOf((Double.parseDouble(foodSplit[3].split("\\s+")[1].replace(",",".")) * (double)amount )/10);
        foodCarb ="Carbohydrate: " + String.valueOf((Double.parseDouble(foodSplit[4].split("\\s+")[1].replace(",",".")) * (double)amount )/10);
        fillDetails();
        formattedFood = foodSplit[0].replace("10 dkgs",String.valueOf(amount*10) + " dkgs")+ " NUTRITION SEPARATOR "+
                foodCal+" NUTRITION SEPARATOR "+
                foodProtein+" NUTRITION SEPARATOR "+
                foodFat+" NUTRITION SEPARATOR "
                +foodCarb;
    }


    public void changeFoodAmount(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mennyiség (DKG)");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChangeAmountButton.setText(Integer.parseInt(input.getText().toString()) + " dkgs");
                foodAmount = Integer.parseInt(input.getText().toString());
                formatFoodString(foodAmount);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        }

    public void updateFoodInDatabase(View view) {
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
        String inDB = updatedDate.getString(7);
        String foodsChained = inDB + formattedFood + " FOOD SEPARATOR ";
        hTD.updateHistoryFood(date,cel,Integer.parseInt(kor),Integer.parseInt(magassag), Integer.parseInt(suly),nem,foodsChained);
        Toast.makeText(this,"hozzáadva a mai tevékenységedhez!",Toast.LENGTH_LONG).show();
        finish();
    }
}
