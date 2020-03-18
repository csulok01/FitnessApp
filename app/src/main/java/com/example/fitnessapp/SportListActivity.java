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

public class SportListActivity extends AppCompatActivity {
    Cursor selectSport;
    SportDBAdapter mDbHelper;
    List<String> sportWithGivenString = new ArrayList<>();
    TextView searchedSportName;
    private RecyclerView mRecyclerView;
    private SportListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_list);

        Toolbar myToolbar = findViewById(R.id.SportListActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDbHelper = new SportDBAdapter(this);
        mDbHelper.createDatabase();

        mDbHelper.open();
        selectSport = mDbHelper.getSportWhichContainsPassedString("");
        mDbHelper.close();

        sportCursorToList(selectSport);

        mRecyclerView = findViewById(R.id.recyclerviewSport);
        mAdapter = new SportListAdapter(getApplicationContext(), sportWithGivenString);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        searchedSportName = (EditText) SportListActivity.this.findViewById(R.id.searchSportPlainText);
        searchedSportName.addTextChangedListener(new TextWatcher() {
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
                selectSport = mDbHelper.getSportWhichContainsPassedString(searchedSportName.getText().toString());
                mDbHelper.close();

                sportCursorToList(selectSport);

                mAdapter = new SportListAdapter(getApplicationContext(), sportWithGivenString);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddSport = new Intent(SportListActivity.this,AddSportActivity.class);
                startActivity(intentAddSport);
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
    public void sportCursorToList(Cursor selectedSport){
        sportWithGivenString = new ArrayList<>();
        if (selectedSport.moveToFirst()) {
            while (!selectSport.isAfterLast()) {

                String name = selectSport.getString(selectSport.getColumnIndex("name"))+" 10 min : \n"+
                        "cal: " + selectSport.getString(selectSport.getColumnIndex("CaloriesInTenMin"));

                sportWithGivenString.add(name);
                selectSport.moveToNext();
            }
        }
    }
}
