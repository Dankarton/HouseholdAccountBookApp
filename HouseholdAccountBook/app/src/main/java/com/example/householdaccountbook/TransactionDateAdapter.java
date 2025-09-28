package com.example.householdaccountbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.customviews.DailyRecordCustomView;

import java.util.List;

import myclasses.DailyBop;

public class TransactionDateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DailyBop> dailyBopList;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_view_daily_record, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TransactionViewHolder) holder).bind(dailyBopList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.dailyBopList.size();
    }
    public void setData(List<DailyBop> dailyList) {
        this.dailyBopList = dailyList;
    }

    //
    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final DailyRecordCustomView dailyView;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dailyView = new DailyRecordCustomView(itemView.getContext());
        }
        public void bind(DailyBop dailyBop) {
            this.dailyView.setData(dailyBop);
        }
    }
}
