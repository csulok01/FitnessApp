package com.example.fitnessapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private int cal;
    private int calMinus;
    private int protein;
    private int fat;
    private int carbohydrate;
    private int water;
    private boolean areProfilDetailsEmpty;
    private String kor;
    private String nem;
    private String suly;
    private String magassag;
    private String cel;
    private String currentDate;
    private Cursor pickedDate;
    private ProgressBar calorieProgressBar;
    private ProgressBar carboHydrateProgressBar;
    private ProgressBar fatProgressbar;
    private ProgressBar proteinProgressBar;
    private ProgressBar waterProgressBar;
    private Toolbar myToolbar;
    private TextView waterText;
    private TextView calorieText;
    private TextView fatText;
    private TextView proteinText;
    private TextView carbText;
    private TextView weightText;
    private LinearLayout progressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = findViewById(R.id.MainActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(myToolbar);

        createOrLoadHistoryData(getTodayDate());


    }

    private void createOrLoadHistoryData(String date) {
        setProfilData();
        HistoryTableDML hTD2 = new HistoryTableDML(this);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("dátum", date);
        editor.apply();
        currentDate = date;
        myToolbar.setTitle(currentDate);
        myToolbar.setNavigationIcon(null);
        HistoryTableDML hTD = new HistoryTableDML(this);
        pickedDate = hTD.getHistoryBydate(currentDate);

        if (!(pickedDate.moveToFirst()) || pickedDate.getCount() == 0) {
            hTD.createHistory(currentDate, cel, Integer.parseInt(kor), Integer.parseInt(magassag), Integer.parseInt(suly), nem);
            pickedDate = hTD.getHistoryBydate(currentDate);
        }
        setProgressBar();
    }

    private void setProgressBar() {
        getProgressViews();
        getNutritions();
        getCaloriesBurnt();
        getWater();
        HarrisBenedictFormula harrisBenedictFormula = new HarrisBenedictFormula(Integer.parseInt(kor), Integer.parseInt(magassag), Integer.parseInt(suly), nem, cel);
        int bmr = harrisBenedictFormula.getBmr();
        int caloriePercentage = (int) ((((double) cal - (double) calMinus) / (double) bmr) * 100);
        calorieProgressBar.setProgress(caloriePercentage);
        calorieText.setText("kalória: \n" + caloriePercentage + "%" + " (" + (int) ((double) cal - (double) calMinus) + ")");
        int fatPercentage = (int) ((((double) fat * 9) / ((double) bmr * 0.30)) * 100);
        fatProgressbar.setProgress(fatPercentage);
        fatText.setText("zsír: \n" + fatPercentage + "%" + " (" + fat + ")");
        int proteinPercentage = (int) (((double) protein / (Double) (Double.parseDouble(suly) * 1.5)) * 100);
        proteinProgressBar.setProgress(proteinPercentage);
        proteinText.setText("protein: \n" + proteinPercentage + "%" + " (" + protein + ")");
        int carbohydratePercentage = (int) (((double) carbohydrate * 4) / ((double) bmr * 0.50) * 100);
        carboHydrateProgressBar.setProgress(carbohydratePercentage);
        carbText.setText("szénhidrátok: \n" + carbohydratePercentage + "%" + " (" + carbohydrate + " liter" + ")");
        int waterPercentage = (int) (((double) water / 25) * 100);
        waterProgressBar.setProgress(waterPercentage);
        waterText.setText("víz: " + waterPercentage + "%" + " (" + water + " dl" + ")");
        weightText.setText("jelenlegi súlyod: " + pickedDate.getString(10) + " kg");

        if (caloriePercentage > 100 || fatPercentage > 100 || proteinPercentage > 100 || carbohydratePercentage > 100) {
            progressView.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_layout_red));
        } else {
            progressView.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_layout));
        }
        cal = 0;
        protein = 0;
        carbohydrate = 0;
        fat = 0;
        calMinus = 0;
    }

    private void getWater() {
        water = Integer.parseInt(pickedDate.getString(9));
    }

    private void getProgressViews() {
        waterProgressBar = findViewById(R.id.waterProgressBar);
        progressView = findViewById(R.id.progressView);
        calorieProgressBar = findViewById(R.id.calorieProgressBar);
        proteinProgressBar = findViewById(R.id.proteinProgressBar);
        fatProgressbar = findViewById(R.id.fatProgressBar);
        carboHydrateProgressBar = findViewById(R.id.carboHydrateProgressBar);
        calorieText = findViewById(R.id.calorieText);
        fatText = findViewById(R.id.fat);
        proteinText = findViewById(R.id.protein);
        carbText = findViewById(R.id.carboHydrate);
        waterText = findViewById(R.id.waterText);
        weightText = findViewById(R.id.weightText);
    }

    private void getCaloriesBurnt() {
        String currentSport = pickedDate.getString(8);
        String[] sportsSplit = currentSport.split("SPORT SEPARATOR ");
        for (String sportSplit : sportsSplit) {
            if (sportSplit.contains("Calories")) {
                String[] sport = sportSplit.split("SPORTDETAILS SEPARATOR");
                int calIndex = sport[1].split(":")[1].trim().indexOf(".");
                calMinus += Integer.parseInt(sport[1].split(":")[1].substring(0, calIndex + 1).trim());
            }
        }
    }

    private void getNutritions() {
        String currentFood = pickedDate.getString(7);
        String[] foodsSplit = currentFood.split("FOOD SEPARATOR ");
        for (String foodSplit : foodsSplit) {
            if (foodSplit.contains("Calories")) {
                String[] food = foodSplit.split("NUTRITION SEPARATOR");
                int calIndex = food[1].split(":")[1].trim().indexOf(".");
                int proteinIndex = food[2].split(":")[1].trim().indexOf(".");
                int fatIndex = food[3].split(":")[1].trim().indexOf(".");
                int carbIndex = food[4].split(":")[1].trim().indexOf(".");

                cal += Integer.parseInt(food[1].split(":")[1].substring(0, calIndex + 1).trim());
                protein += Integer.parseInt(food[2].split(":")[1].substring(0, proteinIndex + 1).trim());
                fat += Integer.parseInt(food[3].split(":")[1].substring(0, fatIndex + 1).trim());
                carbohydrate += Integer.parseInt(food[4].split(":")[1].substring(0, carbIndex + 1).trim());
            }
        }
    }

    private void setProfilData() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        currentDate = sharedPref.getString("dátum", "0");
        kor = sharedPref.getString("kor", "0");
        nem = sharedPref.getString("nem", "");
        suly = sharedPref.getString("súly", "0");
        magassag = sharedPref.getString("magasság", "0");
        cel = sharedPref.getString("cél", "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar:
                setProfilData();
                areProfilDetailsEmpty = chechIfProfilDetailsEmpty();
                if (areProfilDetailsEmpty) {
                    Toast.makeText(this, "ehhez a funkcióhoz szükségesek a profil adatok!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(this, CalendarActivity.class);
                    startActivityForResult(intent, 1);

                }
                return true;
            case R.id.action_profil:
                Intent intentprofil = new Intent(this, ProfilActivity.class);
                startActivityForResult(intentprofil, 2);
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String date = data.getStringExtra("dátum");
            createOrLoadHistoryData(date);
        }
        if (requestCode == 2) {
            createOrLoadHistoryData(currentDate);
        }
    }

    public void onMainMenuButtonClicked(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item));
        boolean canEnter = getAuthority();
        if (canEnter) {
            switch (view.getId()) {
                case R.id.addMealButton:
                    Intent intentFoodList = new Intent(this, FoodListActivity.class);
                    startActivityForResult(intentFoodList, 2);
                    break;

                case R.id.addPhotoButton:
                    deleteToday();
                    break;

                case R.id.addPlanButton:
                    setCurrentWeight();
                    break;

                case R.id.addSportButton:
                    Intent intentSportList = new Intent(this, SportListActivity.class);
                    startActivityForResult(intentSportList, 2);
                    break;

                case R.id.addWaterButton:
                    setWater();
                    break;

                case R.id.progressView:
                    Intent intentActivitiesList = new Intent(this, ActivitiesListActivity.class);
                    startActivityForResult(intentActivitiesList, 2);
                    break;
                case R.id.showStatisticsButton:
                    Intent graphIntent = new Intent(this, GraphActivity.class);
                    startActivityForResult(graphIntent, 2);
                    break;
            }
        }

    }

    private void deleteToday() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nap törlése");
        builder.setMessage("Biztosan törölni szeretnéd a mai napot?");
        builder.setPositiveButton("IGEN", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                HistoryTableDML hTD = new HistoryTableDML(MainActivity.this);
                hTD.deleteHistory(currentDate);
                createOrLoadHistoryData(getTodayDate());
            }
        });
        builder.setNegativeButton("NEM", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    private void setWater() {
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(0);

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return "" + value + " dl";
            }
        };
        numberPicker.setFormatter(formatter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mennyiség");
        builder.setMessage("Válassz mennyiséget!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                setProfilData();
                String date = currentDate;
                Cursor updatedDate;
                HistoryTableDML hTD = new HistoryTableDML(MainActivity.this);
                updatedDate = hTD.getHistoryBydate(date);
                water = Integer.parseInt(updatedDate.getString(9)) + numberPicker.getValue();
                hTD.updateHistoryCurrentWater(date, cel, Integer.parseInt(kor), Integer.parseInt(magassag), Integer.parseInt(suly), nem, String.valueOf(water));
                Toast.makeText(MainActivity.this, "víz mennyiség mentve!", Toast.LENGTH_LONG).show();
                createOrLoadHistoryData(currentDate);
                setProgressBar();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setView(numberPicker).create().show();
    }

    private void setCurrentWeight() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mai súlyod: (Kg)");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setProfilData();
                String date = currentDate;
                HistoryTableDML hTD = new HistoryTableDML(MainActivity.this);
                pickedDate = hTD.getHistoryBydate(date);
                hTD.updateHistoryCurrentWeight(date, cel, Integer.parseInt(kor), Integer.parseInt(magassag), Integer.parseInt(suly), nem, input.getText().toString());
                Toast.makeText(MainActivity.this, "súly mentve!", Toast.LENGTH_LONG).show();
                weightText.setText("jelenlegi súlyod: " + input.getText().toString() + " kg");
                if (currentDate.equals(getTodayDate())) {
                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("súly", input.getText().toString());
                    editor.apply();
                }
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

    private boolean getAuthority() {
        setProfilData();
        areProfilDetailsEmpty = chechIfProfilDetailsEmpty();
        if (areProfilDetailsEmpty) {
            Toast.makeText(this, "ehhez a funkcióhoz szükségesek a profil adatok!", Toast.LENGTH_LONG).show();
        } else
            return true;

        return false;
    }

    private boolean chechIfProfilDetailsEmpty() {
        if (kor.matches("") || nem.matches("") || suly.matches("") || magassag.matches("") || cel.matches("")) {
            return true;
        }
        return false;
    }

    private String getTodayDate() {
        String dateMonth = String.valueOf(CalendarDay.today().getMonth());
        String dateDay = String.valueOf(CalendarDay.today().getDay());
        if (CalendarDay.today().getMonth() < 10) {
            dateMonth = "0" + CalendarDay.today().getMonth();
        }
        if (CalendarDay.today().getDay() < 10) {
            dateDay = "0" + CalendarDay.today().getDay();
        }
        return CalendarDay.today().getYear() + "-" + dateMonth + "-" + dateDay;
    }

    public void onTakePhotoButtonClicked(View view){
        if(getTodayDate().equals(currentDate)) {
            Intent cameraIntent = new Intent(this, cameraActivity.class);
            startActivityForResult(cameraIntent, 2);
        }else{
            Toast.makeText(this,"csak a mai napra készíthetsz fotót!",Toast.LENGTH_LONG).show();
        }
    }

}
