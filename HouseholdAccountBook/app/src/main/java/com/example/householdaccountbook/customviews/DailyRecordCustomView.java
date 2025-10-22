package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.adapter.DailyRecordAdapter;
import com.example.householdaccountbook.R;

import java.util.List;

import myclasses.DailyBop;
import myclasses.Expenses;
import myclasses.Income;

public class DailyRecordCustomView extends ConstraintLayout {
    private TextView dateTextView;
    private TextView purchaseAmountTextView;
    private TextView paymentAmountTextView;
    private LinearLayout dailyRecordLinearLayout;
    private ImageView listStateImageView;
    private DailyRecordAdapter adapter;
    private DailyBop dailyBop;

    public DailyRecordCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
//        CustomViewDailyRecordBinding binding =
//                CustomViewDailyRecordBinding.inflate(LayoutInflater.from(context), this, true);
//        this.dateTextView = binding.dateTitle;
//        this.purchaseAmountTextView = binding.purchaseAmountTextView;
//        this.paymentAmountTextView = binding.paymentAmountTextView;
//        this.listStateImageView = binding.listSateView;
//        this.dailyRecordLinearLayout = binding.dailyRecordLinearLayout;

        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_daily_record, this);
        this.dateTextView = layout.findViewById(R.id.date_title);
        this.purchaseAmountTextView = layout.findViewById(R.id.purchase_amount_text_view);
        this.paymentAmountTextView = layout.findViewById(R.id.payment_amount_text_view);
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
        List<Income> incomeList = bopData.getIncomeList();
        for (int i = 0; i < incomeList.size(); i++) {
            IncomeSettingsCustomView incView = new IncomeSettingsCustomView(this.getContext());
            incView.setData(incomeList.get(i));
            this.dailyRecordLinearLayout.addView(incView);
        }
        List<Expenses> expensesList = bopData.getPurchaseList();
        for (int i = 0; i < expensesList.size(); i++) {
            Expenses exp = expensesList.get(i);
            if (!exp.isSameDay()) {
                ExpensesSettingsCustomView expView = new ExpensesSettingsCustomView(this.getContext());
                expView.setData(exp);
                this.dailyRecordLinearLayout.addView(expView);
            }
        }
        expensesList = bopData.getPaymentList();
        for (int i = 0; i < expensesList.size(); i++) {
            ExpensesSettingsCustomView expView = new ExpensesSettingsCustomView(this.getContext());
            expView.setData(expensesList.get(i));
            this.dailyRecordLinearLayout.addView(expView);
        }
    }

    private void setDate() {
        String formattedDate = String.valueOf(this.dailyBop.getDate()) + "日";
        this.dateTextView.setText(formattedDate);
    }

    private void setAmount() {
        int purchaseAmount = Math.abs(this.dailyBop.getPurchaseAmount());
        String formattedPurAmo = String.valueOf(purchaseAmount);
        this.purchaseAmountTextView.setText(formattedPurAmo);
        if (purchaseAmount < 0) {
            this.purchaseAmountTextView.setTextColor(getContext().getColor(R.color.expenses_text_color));
        } else {
            this.purchaseAmountTextView.setTextColor(getContext().getColor(R.color.white));
        }
        int paymentAmount = Math.abs(this.dailyBop.getPaymentAmount());
        int incomeAmount = this.dailyBop.getIncomeAmount();
        int totalAmount = incomeAmount - paymentAmount;
        String formattedAmount = String.valueOf(totalAmount);
        this.paymentAmountTextView.setText(formattedAmount);
        if (totalAmount > 0) {
            this.paymentAmountTextView.setTextColor(getContext().getColor(R.color.income_text_color));
        } else if (totalAmount < 0) {
            this.paymentAmountTextView.setTextColor(getContext().getColor(R.color.expenses_text_color));
        } else {
            this.paymentAmountTextView.setTextColor(getContext().getColor(R.color.white));
        }
    }

    private void setListVisibilityButtonEvent(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dailyRecordLinearLayout.getVisibility() == View.VISIBLE) {
                    dailyRecordLinearLayout.setVisibility(View.GONE);
                    listStateImageView.setImageResource(R.drawable.arrow_drop_down_24px);
                } else {
                    dailyRecordLinearLayout.setVisibility(View.VISIBLE);
                    listStateImageView.setImageResource(R.drawable.arrow_drop_up_24px);
                }
            }
        });
    }
}
