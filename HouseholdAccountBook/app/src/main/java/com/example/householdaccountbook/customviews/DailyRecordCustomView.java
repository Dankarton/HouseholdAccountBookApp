package com.example.householdaccountbook.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import myclasses.Expenses;
import myclasses.Income;

public class DailyRecordCustomView extends ConstraintLayout {
    private TextView dateTextView;
    private TextView amountTextView;
    private LinearLayout dailyRecordLinearLayout;
    private ImageView listStateImageView;
    private DailyRecordAdapter adapter;
    private DailyBop dailyBop;

    public DailyRecordCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }
    public void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_daily_record, this);
        this.dateTextView = layout.findViewById(R.id.date_title);
        this.amountTextView = layout.findViewById(R.id.amount_text_view);
        this.listStateImageView = layout.findViewById(R.id.list_sate_view);
        this.dailyRecordLinearLayout = layout.findViewById(R.id.daily_record_linear_layout);

        setListVisibilityButtonEvent(layout);
        // リストを非表示
        this.dailyRecordLinearLayout.setVisibility(View.GONE);
        this.listStateImageView.setImageResource(R.drawable.arrow_drop_down_24px);
    }

    public void setData(DailyBop dailyBop) {
        this.dailyBop = dailyBop;
        setDate();
        setAmount();
        setDataToLinearLayout(this.dailyBop);
    }
    private void setDataToLinearLayout(DailyBop bopData) {
        this.dailyRecordLinearLayout.removeAllViews();
        List<BalanceOfPayments> dataList = bopData.getBopList();
        for (int i = 0; i < dataList.size(); i++) {
            BalanceOfPayments bop = dataList.get(i);
            if (bop instanceof Income) {
                IncomeSettingsCustomView incView = new IncomeSettingsCustomView(this.getContext());
                incView.setData((Income) bop);
                this.dailyRecordLinearLayout.addView(incView);
            }
            else if (bop instanceof Expenses) {
                ExpensesSettingsCustomView expView = new ExpensesSettingsCustomView(this.getContext());
                expView.setData((Expenses) bop);
                this.dailyRecordLinearLayout.addView(expView);
            }
        }
    }
    private void setDate() {
        String formattedDate = String.valueOf(this.dailyBop.getDate()) + "日";
        this.dateTextView.setText(formattedDate);
    }
    private void setAmount() {
        int paymentAmount = this.dailyBop.getPaymentAmount();
        String formattedAmount = String.valueOf(paymentAmount);
        this.amountTextView.setText(formattedAmount);
        if (paymentAmount > 0) {
            this.amountTextView.setTextColor(getContext().getColor(R.color.income_text_color));
        }
        else if(paymentAmount < 0) {
            this.amountTextView.setTextColor(getContext().getColor(R.color.expenses_text_color));
        }
        else {
            this.amountTextView.setTextColor(getContext().getColor(R.color.white));
        }
    }
    private void setListVisibilityButtonEvent(View view) {
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (dailyRecordLinearLayout.getVisibility() == View.VISIBLE) {
                    dailyRecordLinearLayout.setVisibility(View.GONE);
                    listStateImageView.setImageResource(R.drawable.arrow_drop_down_24px);
                }
                else {
                    dailyRecordLinearLayout.setVisibility(View.VISIBLE);
                    listStateImageView.setImageResource(R.drawable.arrow_drop_up_24px);
                }
            }
        });
    }
}
