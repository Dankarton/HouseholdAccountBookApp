package com.example.householdaccountbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.customviews.DailyRecordCustomView;

import java.util.List;

import myclasses.Income;

public class TransactionDateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> dailyList;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    //
    static class DailyViewHolder extends RecyclerView.ViewHolder {
        private final DailyRecordCustomView dailyView;
        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dailyView = (DailyRecordCustomView) itemView;
        }

        public void bind(Income item) {

        }
    }
}
