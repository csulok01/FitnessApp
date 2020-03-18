package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

public class SportListAdapter  extends RecyclerView.Adapter<SportListAdapter.SportViewHolder> {
    private final List<String> mSportList;
    private LayoutInflater mInflater;
    private Context context;
    @NonNull
    @Override
    public SportListAdapter.SportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemview = mInflater.inflate(R.layout.sportlist_item,parent,false);
        return new com.example.fitnessapp.SportListAdapter.SportViewHolder(mItemview,this);
    }

    public SportListAdapter(Context context, List<String> sportList){
        mInflater = LayoutInflater.from(context);
        this.mSportList = sportList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull SportListAdapter.SportViewHolder holder, int position) {
        String mCurrent = mSportList.get(position);
        holder.sportItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mSportList.size();
    }

    class SportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView sportItemView;
        final com.example.fitnessapp.SportListAdapter mAdapter;

        public SportViewHolder(View itemView, com.example.fitnessapp.SportListAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            sportItemView = itemView.findViewById(R.id.sport);
            this.mAdapter = adapter;
        }

        @Override
        public void onClick(View v) {
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_item));
            int mPosition = getLayoutPosition();
            String sport = mSportList.get(mPosition);

            Intent selectedSportDetailsIntent = new Intent(context, SelectedSportDetails.class);
            selectedSportDetailsIntent.putExtra("sport",sport);
            selectedSportDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(selectedSportDetailsIntent);
        }
    }
}
