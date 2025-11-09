package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.adapter.DailyRecordAdapter;
import com.example.householdaccountbook.R;

import java.util.Locale;

public class DailyRecordCustomView extends ConstraintLayout {
    private TextView dateTextView;
    private TextView purchaseAmountTextView;
    private TextView paymentAmountTextView;
    private RecyclerView dailyRecordRecyclerView;
    private ImageView listStateImageView;

    public DailyRecordCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }
    public DailyRecordCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DailyRecordCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_daily_record, this);
        this.dateTextView = layout.findViewById(R.id.date_title);
        this.purchaseAmountTextView = layout.findViewById(R.id.purchase_amount_text_view);
        this.paymentAmountTextView = layout.findViewById(R.id.amount_text_view);
        this.listStateImageView = layout.findViewById(R.id.list_sate_view);
        this.dailyRecordRecyclerView = layout.findViewById(R.id.daily_record_recycler_view);
        layout.setOnClickListener(view -> {
            if (dailyRecordRecyclerView.getVisibility() == View.VISIBLE) {
                dailyRecordRecyclerView.setVisibility(View.GONE);
                listStateImageView.setImageResource(R.drawable.arrow_drop_down_24px);
            } else {
                dailyRecordRecyclerView.setVisibility(View.VISIBLE);
                listStateImageView.setImageResource(R.drawable.arrow_drop_up_24px);
            }
        });
        // リストを非表示
        this.dailyRecordRecyclerView.setVisibility(View.GONE);
        this.listStateImageView.setImageResource(R.drawable.arrow_drop_down_24px);
    }

    public void bind(int day, int purchaseAmount, int paymentAmount, DailyRecordAdapter adapter) {
        Log.d("DailyRecordCustomView", "day=" + day + ", purchase=" + purchaseAmount + ", payment=" + paymentAmount + ", adapterCount=" + adapter.getItemCount());

        setDate(day);
        setPurchaseAmount(purchaseAmount);
        setPaymentAmount(paymentAmount);
        if (dailyRecordRecyclerView.getLayoutManager() == null) {
            dailyRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        this.dailyRecordRecyclerView.setAdapter(adapter);
    }

    private void setDate(int day) {
        String formattedDate = String.valueOf(day) + "日";
        this.dateTextView.setText(formattedDate);
    }

    private void setPurchaseAmount(int amount) {
        this.paymentAmountTextView.setText(String.format(Locale.JAPANESE, "￥%,d", amount));
        if (amount > 0) {
            this.paymentAmountTextView.setTextColor(getContext().getColor(R.color.income_text_color));
        } else if (amount < 0) {
            this.paymentAmountTextView.setTextColor(getContext().getColor(R.color.expenses_text_color));
        } else {
            this.paymentAmountTextView.setTextColor(getContext().getColor(R.color.white));
        }
    }
    private void setPaymentAmount(int amount) {
        this.purchaseAmountTextView.setText(String.format(Locale.JAPANESE, "￥%,d", amount));
        if (amount < 0) {
            this.purchaseAmountTextView.setTextColor(getContext().getColor(R.color.expenses_text_color));
        } else {
            this.purchaseAmountTextView.setTextColor(getContext().getColor(R.color.white));
        }
    }

    public void setRecycledViewPool(RecyclerView.RecycledViewPool viewPool) {
        this.dailyRecordRecyclerView.setRecycledViewPool(viewPool);
    }
}
