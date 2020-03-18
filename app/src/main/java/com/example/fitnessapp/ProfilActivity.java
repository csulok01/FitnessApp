package com.example.fitnessapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ProfilActivity extends AppCompatActivity {
    TextView celText;
    TextView korText;
    TextView nemText;
    TextView magassagText;
    TextView sulyText;

    private String kor;
    private String nem;
    private String suly;
    private String magassag;
    private String cel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Toolbar myToolbar = findViewById(R.id.ProfilActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        celText = findViewById(R.id.celText);
        korText = findViewById(R.id.korText);
        nemText = findViewById(R.id.nemText);
        magassagText = findViewById(R.id.magassagText);
        sulyText = findViewById(R.id.sulyText);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        kor = sharedPref.getString("kor","");
        nem = sharedPref.getString("nem","");
        suly = sharedPref.getString("súly","");
        magassag = sharedPref.getString("magasság","");
        cel = sharedPref.getString("cél","");

        celText.setText(cel);
        korText.setText(kor);
        sulyText.setText(suly);
        magassagText.setText(magassag);
        nemText.setText(nem);
    }

    public void startProfilUpdate(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item));
        Intent updateProfilIntent = new Intent(this,UpdateProfilActivity.class);
        startActivityForResult(updateProfilIntent,1);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        kor = sharedPref.getString("kor","");
        nem = sharedPref.getString("nem","");
        suly = sharedPref.getString("súly","");
        magassag = sharedPref.getString("magasság","");
        cel = sharedPref.getString("cél","");

        celText.setText(cel);
        korText.setText(kor + " ÉV");
        sulyText.setText(suly + " KG");
        magassagText.setText(magassag + " CM");
        nemText.setText(nem);
    }
}
