package com.example.householdaccountbook.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.DailyRecordAdapter;
import com.example.householdaccountbook.R;

import java.util.ArrayList;
import java.util.List;

import myclasses.BalanceOfPayments;
import myclasses.DailyBop;

public class DailyRecordCustomView extends ConstraintLayout {
    private TextView dateTextView;
    private RecyclerView dailyRecyclerView;
    private DailyRecordAdapter adapter;
    private DailyBop dailyBop;

    public DailyRecordCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }
    public void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_daily_record, this);
        dateTextView = layout.findViewById(R.id.date_title);
        dailyRecyclerView = layout.findViewById(R.id.day_data_recyclerview);
        this.adapter = new DailyRecordAdapter();
        adapter.setData(new ArrayList<BalanceOfPayments>());
        dailyRecyclerView.setAdapter(adapter);
        dailyRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(DailyBop dailyBop) {
        Log.d("DailyRecordCustomView", "setData");
        this.dailyBop = dailyBop;
        setDate();
        adapter.setData(dailyBop.getBopList());
        adapter.notifyDataSetChanged();
    }
    private void setDate() {
        String formattedDate = String.valueOf(this.dailyBop.getDate()) + "日";
        this.dateTextView.setText(formattedDate);
    }
}
