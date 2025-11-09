package com.example.householdaccountbook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.customviews.DailyRecordCustomView;

import java.util.List;

import myclasses.BOP;
import myclasses.DailyBop;

public class TransactionDateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private enum ViewType {
        DAILY_BRIEF,
        INCOME,
        PURCHASE,
        EXPENSES
    }
    public interface OnListItemActionListener {
        void OnActionButtonClicked(BOP data);
    }
    private OnListItemActionListener listener;
    private final Context context;
    private final List<Object> itemDataList;
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public TransactionDateAdapter(Context context, List<Object> dataList) {
        this.context = context;
        this.itemDataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ViewType.DAILY_BRIEF.ordinal()) {

        }
        else if (viewType == ViewType.INCOME.ordinal()) {

        }
        else if (viewType == ViewType.PURCHASE.ordinal()) {

        }
        else {

        }
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

}
