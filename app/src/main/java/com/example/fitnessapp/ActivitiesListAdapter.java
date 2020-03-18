package com.example.fitnessapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ActivitiesListAdapter extends RecyclerView.Adapter<ActivitiesListAdapter.ActivitiesViewHolder>{
    private List<String> mActivitieList;
    private LayoutInflater mInflater;
    private Context context;
    private String kor;
    private String nem;
    private String suly;
    private String magassag;
    private String cel;
    private String datum;
    @NonNull
    @Override
    public ActivitiesListAdapter.ActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.activitielist_item,
                parent, false);
        return new ActivitiesViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesListAdapter.ActivitiesViewHolder holder, int position) {
        String mCurrent = mActivitieList.get(position);
        holder.activtieItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mActivitieList.size();
    }

    public ActivitiesListAdapter(Context context, List<String> activitieList, String kor, String nem, String suly, String magassag, String cel, String datum) {
        mInflater = LayoutInflater.from(context);
        this.mActivitieList = activitieList;
        this.context = context;
        this.kor = kor;
        this.nem = nem;
        this.suly = suly;
        this.magassag = magassag;
        this.cel = cel;
        this.datum = datum;
    }
    class ActivitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView activtieItemView;
        final ActivitiesListAdapter mAdapter;

        public ActivitiesViewHolder(View itemView, ActivitiesListAdapter adapter) {
            super(itemView);
            activtieItemView = itemView.findViewById(R.id.activitie);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_item));

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Törlés");
            builder.setMessage("Törölni kívánod a tevékenységet?");
            builder.setPositiveButton("IGEN", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int mPosition = getLayoutPosition();
                    String element = mActivitieList.get(mPosition);
                    HistoryTableDML hTD = new HistoryTableDML(context);
                    Cursor todayActivity = hTD.getHistoryBydate(datum);
                    String todayFoodActivity = todayActivity.getString(7);
                    String todaySportActivity = todayActivity.getString(8);
                    if(element.contains("protein")){
                        element = element.replace("\n","NUTRITION SEPARATOR") + "FOOD SEPARATOR ";
                        element = Pattern.quote(element);
                        todayFoodActivity = todayFoodActivity.replaceFirst(element," ");
                        hTD.updateHistoryFood(datum,cel,Integer.parseInt(kor),Integer.parseInt(magassag),Integer.parseInt(suly),nem,todayFoodActivity);
                    }else{
                        element = element.replace("\n","SPORTDETAILS SEPARATOR") + "SPORT SEPARATOR ";
                        todaySportActivity = todaySportActivity.replaceFirst(element," ");
                        hTD.updateHistorySport(datum,cel,Integer.parseInt(kor),Integer.parseInt(magassag),Integer.parseInt(suly),nem,todaySportActivity);
                    }

                    todayActivity = hTD.getHistoryBydate(datum);
                    todayActivity.moveToFirst();
                    String foodActivitiesDoneString = todayActivity.getString(7).replace("NUTRITION SEPARATOR", "\n");
                    String sportActivitiesDoneString = todayActivity.getString(8).replace("SPORTDETAILS SEPARATOR","\n");
                    List<String>activtiesDone;
                    List<String>sportsDone;
                    do{
                        activtiesDone = Arrays.asList(foodActivitiesDoneString.split("FOOD SEPARATOR"));
                        sportsDone = Arrays.asList(sportActivitiesDoneString.split("SPORT SEPARATOR"));
                    }while(todayActivity.moveToNext());
                    mActivitieList = new ArrayList<String>(activtiesDone);
                    mActivitieList.addAll(sportsDone);

                    mAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("NEM", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();





        }
    }
}
