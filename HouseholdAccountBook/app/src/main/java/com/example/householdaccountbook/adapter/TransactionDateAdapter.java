package com.example.householdaccountbook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.customviews.DailyRecordCustomView;

import java.util.List;

import myclasses.BOP;
import myclasses.DailyBop;

public class TransactionDateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnListItemActionListener {
        void OnActionButtonClicked(BOP data);
    }
    private OnListItemActionListener listener;
    private final Context context;
    private final List<DailyBop> dailyBopList;
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public TransactionDateAdapter(Context context, List<DailyBop> dataList) {
        this.context = context;
        this.dailyBopList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DailyRecordCustomView view = new DailyRecordCustomView(parent.getContext());
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("TransactionDateAdapter.onBindViewHolder", "position: " + position);
        DailyBop daily = this.dailyBopList.get(position);
        ((TransactionViewHolder) holder).bind(daily, this.listener, this.viewPool);
    }
    public void setListener(OnListItemActionListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return this.dailyBopList.size();
    }
    //
    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final DailyRecordCustomView dailyView;
        private DailyRecordAdapter subAdapter;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dailyView = (DailyRecordCustomView) itemView;
        }

        public void bind(DailyBop dailyData, OnListItemActionListener listener, RecyclerView.RecycledViewPool viewPool) {
            if (this.subAdapter == null) {
                subAdapter = new DailyRecordAdapter(itemView.getContext(), dailyData.getAdapterDataList());
                subAdapter.setListener(new DailyRecordAdapter.OnListItemActionListener() {
                    @Override
                    public void onMoreActionButtonClicked(BOP data) {
                        listener.OnActionButtonClicked(data);
                    }
                });
                this.dailyView.setRecycledViewPool(viewPool);
            }
            this.dailyView.bind(dailyData.getYear(), dailyData.getMonth(), dailyData.getDate(), subAdapter);
        }
    }
}
