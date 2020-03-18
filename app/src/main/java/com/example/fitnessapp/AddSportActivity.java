package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddSportActivity extends AppCompatActivity {
    TextView sportName;
    TextView sportCalories;
    TextView sportProtein;
    TextView sportFat;
    TextView sportCarbohydrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sport);

        Toolbar myToolbar = findViewById(R.id.AddSportActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addSportToDatabase(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item));
        sportName = (EditText) this.findViewById(R.id.sportNamePlainText);
        sportCalories = (EditText) this.findViewById(R.id.sportCaloriesPlainText);
        SportDBAdapter mDbHelper = new SportDBAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();
        if(!sportName.getText().toString().matches("") &&
                !sportCalories.getText().toString().matches(""))
        {
            mDbHelper.insertInSport(sportName.getText().toString(),Integer.parseInt(sportCalories.getText().toString()));
            mDbHelper.close();
            finish();
        }else {
            Toast.makeText(this,"Nem adtál meg minden adatot!",Toast.LENGTH_LONG);
            mDbHelper.close();
        }
    }
}