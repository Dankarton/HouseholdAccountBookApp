package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.R;

public class DailyRecordCustomView extends ConstraintLayout {
    private TextView dateTextView;
    private RecyclerView listRecyclerView;
    public DailyRecordCustomView(@NonNull Context context) {
        super(context);
    }
    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_daily_record, this);
        dateTextView = layout.findViewById(R.id.date_title);
        listRecyclerView = layout.findViewById(R.id.day_data_recyclerview);
    }
    public void setDate(int year, int month, int day) {
        String formattedDate = year + "/" + month + "/" + day;
        dateTextView.setText(formattedDate);
    }
    public void setItem(ConstraintLayout item) {
        //TODO
    }
}
