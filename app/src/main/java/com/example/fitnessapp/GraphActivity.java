package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class GraphActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        myToolbar = findViewById(R.id.GraphViewActivityToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setGraphByWeight();
    }

    private void setGraphByWeight() {
        HistoryTableDML hTD = new HistoryTableDML(this);
        Cursor cursor = hTD.getAllDateHistory();
        cursor.moveToFirst();
        Date lastDate = parseDate("1971-01-01");
        Date firstDate = parseDate("3000-01-01");
        int maxWeight=0;
        final GraphView graph = findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        int size = 0;
        do{
            if(parseDate(cursor.getString(1)).before(firstDate)){
                firstDate = parseDate(cursor.getString(1));
            }
            if(parseDate(cursor.getString(1)).after(lastDate)){
                lastDate = parseDate(cursor.getString(1));
            }
            if(cursor.getInt(10) > maxWeight){
                maxWeight = cursor.getInt(10);
            }
            int weightOnThatDay=cursor.getInt(10);

            if(weightOnThatDay!=0){
                size++;
            series.appendData(new DataPoint(parseDate(cursor.getString(1)), weightOnThatDay), true, Integer.MAX_VALUE);
            }
        }while(cursor.moveToNext());

        setGraphView(graph,lastDate.getTime(), firstDate.getTime(), maxWeight,series,size);

    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setGraphView(GraphView graph, float maxDayCount, float minDayCount, int maxWeight,LineGraphSeries<DataPoint> series, int size) {
        Calendar calendarMin = Calendar.getInstance();
        calendarMin.setTimeInMillis((long)minDayCount);
        calendarMin.add(Calendar.DATE,1);
        Date plusOneDateMin = calendarMin.getTime();
        System.out.println(plusOneDateMin);

        Calendar calendarMax = Calendar.getInstance();
        calendarMax.setTimeInMillis((long)maxDayCount);
        calendarMax.add(Calendar.DATE,1);
        Date plusOneDateMax = calendarMax.getTime();
        System.out.println(plusOneDateMax);


        graph.removeAllSeries();
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(size); // only 4 because of the space
        graph.getGridLabelRenderer().setNumVerticalLabels(5);
        GridLabelRenderer renderer = graph.getGridLabelRenderer();
        renderer.setHorizontalLabelsAngle(90);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(maxWeight);
        graph.getViewport().setMinX(/*minDayCount*/plusOneDateMin.getTime());
        graph.getViewport().setMaxX(/*maxDayCount*/plusOneDateMax.getTime());
        graph.getGridLabelRenderer().setHumanRounding(false);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(15f);
        graph.addSeries(series);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Date pickedDate = new java.sql.Date((long) dataPoint.getX());
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String formatted = format1.format(pickedDate.getTime());
                Intent loadPhotoIntent = new Intent(GraphActivity.this,ImageActivity.class);
                loadPhotoIntent.putExtra("date",formatted);
                startActivity(loadPhotoIntent);
            }
        });
        graph.setVisibility(View.VISIBLE);
    }
}
