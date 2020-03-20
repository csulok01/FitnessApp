package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView imageView = findViewById(R.id.imageView);
        Intent thisIntent = getIntent();
        imageView.setImageBitmap(BitmapFactory.decodeFile(getApplicationContext().getFilesDir().getPath()+
                "/"+ thisIntent.getStringExtra("date")+".jpg"));
        Toast.makeText(this,thisIntent.getStringExtra("date"),Toast.LENGTH_LONG).show();
    }

    public void finishActivity(View view) {
        finish();
    }
}
