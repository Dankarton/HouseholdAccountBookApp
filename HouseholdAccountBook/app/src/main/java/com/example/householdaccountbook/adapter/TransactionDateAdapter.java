package com.example.householdaccountbook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.customviews.DailyRecordCustomView;

import java.util.ArrayList;
import java.util.List;

import myclasses.BOP;
import myclasses.DailyBop;

public class TransactionDateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnListItemActionListener {
        void OnActionButtonClicked(BOP data);
    }

    private OnListItemActionListener listener;
    private final Context context;
    private final List<DailyItemBinder> binderList;

    public TransactionDateAdapter(Context context, List<DailyBop> dataList) {
        this.context = context;
        this.binderList = new ArrayList<>();
        for (DailyBop daily : dataList) {
            this.binderList.add(new DailyItemBinder(daily));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DailyRecordCustomView view = new DailyRecordCustomView(parent.getContext());
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (this.binderList.get(position).getAdapter() == null) {
            this.binderList.get(position).setAdapter(makeAdapter(this.binderList.get(position).dailyBop.getAdapterDataList()));
        }
        ((TransactionViewHolder) holder).bind(this.binderList.get(position));
    }
    private DailyRecordAdapter makeAdapter(List<BOP> dataList) {
        DailyRecordAdapter adapter = new DailyRecordAdapter(context, dataList);
        adapter.setListener(new DailyRecordAdapter.OnListItemActionListener() {
            @Override
            public void onMoreActionButtonClicked(BOP data) {
                if (listener != null) listener.OnActionButtonClicked(data);
            }
        });
        return adapter;
    }
    public void setListener(OnListItemActionListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return this.binderList.size();
    }

    //
    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final DailyRecordCustomView dailyView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dailyView = (DailyRecordCustomView) itemView;
        }

        public void bind(DailyItemBinder binder) {
            this.dailyView.setListener(binder);
            this.dailyView.bind(binder.getDate(), binder.getIncomeAmount(), binder.getPurchaseAmount(), binder.getPaymentAmount(), binder.getAdapter(), binder.isPullDownVisible());
        }
    }

    static class DailyItemBinder implements DailyRecordCustomView.onActionListener {
        private final DailyBop dailyBop;
        private DailyRecordAdapter adapter = null;
        private boolean isPullDownVisible = false;

        public DailyItemBinder(DailyBop dailyData) {
            this.dailyBop = dailyData;
        }
        public int getDate() { return this.dailyBop.getDate(); }
        public int getIncomeAmount() { return this.dailyBop.getIncomeAmount(); }
        public int getPurchaseAmount() { return this.dailyBop.getPurchaseAmount(); }
        public int getPaymentAmount() { return this.dailyBop.getPaymentAmount(); }
        public int getTotalAmount() { return this.dailyBop.getTotalAmount(); }
        public DailyRecordAdapter getAdapter() { return this.adapter; }
        public boolean isPullDownVisible() { return this.isPullDownVisible; }

        public void setAdapter(DailyRecordAdapter adapter) { this.adapter = adapter; }
        @Override
        public void onPullDownClicked(boolean visible) {
            this.isPullDownVisible = visible;
        }
    }
}
