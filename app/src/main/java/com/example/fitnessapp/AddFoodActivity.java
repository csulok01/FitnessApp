package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddFoodActivity extends AppCompatActivity {
    TextView foodName;
    TextView foodCalories;
    TextView foodProtein;
    TextView foodFat;
    TextView foodCarbohydrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        Toolbar myToolbar = findViewById(R.id.AddFoodActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addFoodToDatabase(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item));

        foodName = (EditText) this.findViewById(R.id.foodNamePlainText);
        foodCalories = (EditText) this.findViewById(R.id.foodCaloriesPlainText);
        foodProtein = (EditText) this.findViewById(R.id.foodProteinPlainText);
        foodFat = (EditText) this.findViewById(R.id.foodFatPlainText);
        foodCarbohydrate = (EditText) this.findViewById(R.id.foodCarbohydratePlainText);
        FoodDBAdapter mDbHelper = new FoodDBAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();
        if(!foodName.getText().toString().matches("") &&
                !foodCalories.getText().toString().matches("") &&
                !foodProtein.getText().toString().matches("") &&
                !foodFat.getText().toString().matches("") &&
                !foodCarbohydrate.getText().toString().matches(""))
        {
            mDbHelper.insertInFood(foodName.getText().toString(),Integer.parseInt(foodCalories.getText().toString()),
                    Integer.parseInt(foodProtein.getText().toString()),Integer.parseInt(foodFat.getText().toString()),Integer.parseInt(foodCarbohydrate.getText().toString()));
            mDbHelper.close();
            finish();
        }else {
            Toast.makeText(this,"Nem adtál meg minden adatot!",Toast.LENGTH_LONG);
            mDbHelper.close();
        }

    }
}
