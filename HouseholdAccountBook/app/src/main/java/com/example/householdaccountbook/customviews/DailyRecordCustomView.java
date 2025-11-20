package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
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
    public interface onActionListener {
        void onPullDownClicked(boolean visible);
    }
    private TextView dateTextView;
    private TextView purchaseAmountTextView;
    private TextView paymentAmountTextView;
    private RecyclerView dailyRecordRecyclerView;
    private ImageView listStateImageView;
    private onActionListener listener = null;

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

    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_daily_record, this);
        this.dateTextView = layout.findViewById(R.id.date_title);
        this.purchaseAmountTextView = layout.findViewById(R.id.income_amount_text);
        this.paymentAmountTextView = layout.findViewById(R.id.expenses_amount_text);
        this.listStateImageView = layout.findViewById(R.id.list_sate_view);
        this.dailyRecordRecyclerView = layout.findViewById(R.id.daily_record_recycler_view);
        layout.setOnClickListener(view -> {
            if (dailyRecordRecyclerView.getVisibility() == View.VISIBLE) {
                // リストが表示されてる状態でクリックされたらリストを非表示にする
                changeDropDownVisible(false);
            } else {
                changeDropDownVisible(true);
            }
        });
        // リストを非表示
        changeDropDownVisible(false);
    }

    public void bind(int day, int incomeAmount, int purchaseAmount, int paymentAmount, DailyRecordAdapter adapter, boolean isDropDownVisible) {
        setDate(day);
        setIncomeAmount(incomeAmount);
        setPaymentAmount(paymentAmount);
        if (dailyRecordRecyclerView.getLayoutManager() == null) {
            dailyRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        this.dailyRecordRecyclerView.setAdapter(adapter);
        changeDropDownVisible(isDropDownVisible);
    }
    private void changeDropDownVisible(boolean visible) {
        if (visible) {
            this.dailyRecordRecyclerView.setVisibility(View.VISIBLE);
            this.listStateImageView.setImageResource(R.drawable.arrow_drop_up_24px);
        }
        else {
            dailyRecordRecyclerView.setVisibility(View.GONE);
            listStateImageView.setImageResource(R.drawable.arrow_drop_down_24px);
        }
        if (this.listener != null) this.listener.onPullDownClicked(visible);
    }
    public void setListener(onActionListener listener) {
        this.listener = listener;
    }
    public boolean existListener() {
        return this.listener != null;
    }

    private void setDate(int day) {
        String formattedDate = String.valueOf(day) + "日";
        this.dateTextView.setText(formattedDate);
    }

    private void setPaymentAmount(int amount) {
        this.paymentAmountTextView.setText(String.format(Locale.JAPANESE, "￥%,d", amount));
        if (amount > 0) {
            this.paymentAmountTextView.setTextColor(getContext().getColor(R.color.expenses_text_color));
        } else {
            this.paymentAmountTextView.setTextColor(getContext().getColor(R.color.idle_text_color));
        }
    }

    private void setIncomeAmount(int amount) {
        String formalText = "";
        this.purchaseAmountTextView.setText(String.format(Locale.JAPANESE, "￥%,d", amount));
        if (amount > 0) {
            this.purchaseAmountTextView.setTextColor(getContext().getColor(R.color.income_text_color));
            formalText = String.format(Locale.JAPANESE, "￥+%,d", amount);
        } else {
            this.purchaseAmountTextView.setTextColor(getContext().getColor(R.color.idle_text_color));
            formalText = String.format(Locale.JAPANESE, "￥%,d", amount);
        }
        this.purchaseAmountTextView.setText(formalText);
    }

    public void setRecycledViewPool(RecyclerView.RecycledViewPool viewPool) {
        this.dailyRecordRecyclerView.setRecycledViewPool(viewPool);
    }
}
