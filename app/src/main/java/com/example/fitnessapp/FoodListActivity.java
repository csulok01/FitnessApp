package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity {
    Cursor selectFood;
    FoodDBAdapter mDbHelper;
    List<String> foodWithGivenString = new ArrayList<>();
    TextView searchedFoodName;
    private RecyclerView mRecyclerView;
    private FoodListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        Toolbar myToolbar = findViewById(R.id.FoodListActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDbHelper = new FoodDBAdapter(this);
        mDbHelper.createDatabase();

        mDbHelper.open();
        selectFood = mDbHelper.getFoodWhichContainsPassedString("");
        mDbHelper.close();

        foodCursorToList(selectFood);

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new FoodListAdapter(getApplicationContext(), foodWithGivenString);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        searchedFoodName = (EditText) FoodListActivity.this.findViewById(R.id.searchFoodPlainText);
        searchedFoodName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDbHelper.open();
                selectFood = mDbHelper.getFoodWhichContainsPassedString(searchedFoodName.getText().toString());
                mDbHelper.close();

                foodCursorToList(selectFood);

                mAdapter = new FoodListAdapter(getApplicationContext(), foodWithGivenString);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddFood = new Intent(FoodListActivity.this,AddFoodActivity.class);
                startActivity(intentAddFood);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void foodCursorToList(Cursor selectedFood){
        foodWithGivenString = new ArrayList<>();
        if (selectedFood.moveToFirst()) {
            while (!selectFood.isAfterLast()) {

                String name = selectFood.getString(selectFood.getColumnIndex("name"))+" 10 dkgs : \n"+
                        "cal: " + selectFood.getString(selectFood.getColumnIndex("CaloriesInTenDkgs")) + " \n" +
                        "prot: " + selectFood.getString(selectFood.getColumnIndex("ProteinInTenDkgs")) + " \n" +
                        "fat: " + selectFood.getString(selectFood.getColumnIndex("FatInTenDkgs")) + " \n" +
                        "carb: " + selectFood.getString(selectFood.getColumnIndex("CarbohydrateInTenDkgs"));

                foodWithGivenString.add(name);
                selectFood.moveToNext();
            }
        }
    }
}
