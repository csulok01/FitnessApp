package com.example.fitnessapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import static android.text.TextUtils.isEmpty;

public class FoodListAdapter  extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {
    private final List<String> mFoodList;
    private LayoutInflater mInflater;
    private Context context;
    @NonNull
    @Override
    public FoodListAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemview = mInflater.inflate(R.layout.foodlist_item,parent,false);
        return new FoodViewHolder(mItemview,this);
    }

    public FoodListAdapter(Context context, List<String> foodList){
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mFoodList = foodList;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.FoodViewHolder holder, int position) {
        String mCurrent = mFoodList.get(position);
        holder.foodItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView foodItemView;
        final FoodListAdapter mAdapter;

        public FoodViewHolder(View itemView, FoodListAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            foodItemView = itemView.findViewById(R.id.food);
            this.mAdapter = adapter;
        }

        @Override
        public void onClick(View v) {
            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_item));
            int mPosition = getLayoutPosition();
            String food = mFoodList.get(mPosition);

            Intent selectedFoodDetailsIntent = new Intent(context, SelectedFoodDetails.class);
            selectedFoodDetailsIntent.putExtra("food",food);
            selectedFoodDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(selectedFoodDetailsIntent);
        }
    }
}
