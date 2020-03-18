package com.example.fitnessapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateProfilActivity extends AppCompatActivity {
    Button goalText;
    EditText ageText;
    Button genderText;
    EditText heightText;
    EditText weightText;
    String gender;
    String goal;
    String age;
    String height;
    String weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profil);

        Toolbar myToolbar = findViewById(R.id.ProfilActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillProfilButtons();

        ageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                age = ageText.getText().toString();
            }
        });
        heightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                height = heightText.getText().toString();
            }
        });
        weightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                weight = weightText.getText().toString();
            }
        });
    }
    private void fillProfilButtons() {
        goalText = findViewById(R.id.celszerkeszt);
        ageText = findViewById(R.id.korszerkeszt);
        genderText = findViewById(R.id.nemszerkeszt);
        heightText = findViewById(R.id.magassagszerkeszt);
        weightText = findViewById(R.id.sulyszerkeszt);


        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        goal = sharedPref.getString("cél","");
        age =sharedPref.getString("kor", "");
        gender = sharedPref.getString("nem", "");
        weight = sharedPref.getString("súly", "");
        height = sharedPref.getString("magasság", "");

        if(!goal.equals(""))
            goalText.setText(goal);
        if(!age.equals(""))
            ageText.setText(age);
        if(!genderText.equals(gender))
            genderText.setText(gender);
        if(!heightText.equals(height))
            heightText.setText(height);
        if(!weightText.equals(weight))
            weightText.setText(weight);
    }
    public void saveProfilData(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item));
        if (goalText.getText().toString().matches("") ||
                ageText.getText().toString().matches("") ||
                genderText.getText().toString().matches("") ||
                heightText.getText().toString().matches("") ||
                weightText.getText().toString().matches("")) {
            Toast.makeText(this,"A profil adatok mentéséhez minden mező kitöltésére szükség van!",Toast.LENGTH_LONG).show();
        }else {
                SharedPreferences sharedPref = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("cél", goal);
                editor.putString("kor", age);
                editor.putString("nem", gender);
                editor.putString("magasság", height);
                editor.putString("súly", weight);
                editor.apply();
                finish();
            }

    }
    public void setGoalAlertDialog(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item));
        final AlertDialog.Builder goalDialog = new AlertDialog.Builder(this);
        goalDialog.setTitle("Cél megadása");
        goalDialog.setMessage("Kérem a cél kiválasztását!");
        goalDialog.setPositiveButton("tömegnövelés", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goal = "tömegnövelés";
                goalText.setText(goal);
            }
        });
        goalDialog.setNegativeButton("fogyás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goal = "fogyás";
                goalText.setText(goal);
            }
        });
        goalDialog.setNeutralButton("szinten tartás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goal = "szinten tartás";
                goalText.setText(goal);
            }
        });

        goalDialog.create().show();
    }
    public void setGenderAlertDialog(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item));
        final AlertDialog.Builder goalDialog = new AlertDialog.Builder(this);
        goalDialog.setTitle("Nem megadása");
        goalDialog.setMessage("Kérem a nem kiválasztását!");
        goalDialog.setPositiveButton("férfi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gender = "férfi";
                genderText.setText(gender);
            }
        });
        goalDialog.setNegativeButton("nő", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gender = "nő";
                genderText.setText(gender);
            }
        });

        goalDialog.create().show();
    }

}
