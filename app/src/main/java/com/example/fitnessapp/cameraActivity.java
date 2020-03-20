package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.io.FileOutputStream;

public class cameraActivity extends AppCompatActivity {
    private CameraKitView cameraKitView;
    private Button takePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraKitView = findViewById(R.id.camera);

        takePhoto = findViewById(R.id.takePhotoButtonId);
        takePhoto.setOnClickListener(photoOnClickListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private View.OnClickListener photoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                @Override
                public void onImage(CameraKitView cameraKitView, final byte[] photo) {
                    String todayMonth=String.valueOf(CalendarDay.today().getMonth());
                    String todayDay = String.valueOf(CalendarDay.today().getDay());
                    if(CalendarDay.today().getMonth() < 10){
                        todayMonth = "0"+CalendarDay.today().getMonth();
                    }
                    if(CalendarDay.today().getDay() < 10){
                        todayDay = "0"+CalendarDay.today().getDay();
                    }
                    File savedPhoto = new File(getApplicationContext().getFilesDir().getPath(), CalendarDay.today().getYear()+"-"+todayMonth+"-"+todayDay+".jpg");
                    try {
                        FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                        outputStream.write(photo);
                        outputStream.close();
                        Toast.makeText(cameraActivity.this,"kép elkészítve!",Toast.LENGTH_LONG).show();
                        finish();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                        Log.e("CKDemo", "Exception in photo callback");
                    }
                }
            });
        }
    };
}
