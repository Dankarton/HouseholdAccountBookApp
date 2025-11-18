package com.example.householdaccountbook.customviews.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;

import java.util.Locale;

public class MonthlySummaryCustomView extends ConstraintLayout {
    private Context context;
    private TextView incomeAmountText;
    private TextView purchaseAmountText;
    private TextView expensesAmountText;
    private TextView incomeAndPurchaseTotalText;
    private TextView incomeAndExpensesTotalText;
    private TextView nextMonthExpensesAmountText;


    public MonthlySummaryCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MonthlySummaryCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MonthlySummaryCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_amount_brief, this);
        this.incomeAmountText = layout.findViewById(R.id.income_amount_text);
        this.purchaseAmountText = layout.findViewById(R.id.purchase_amount_text);
        this.expensesAmountText = layout.findViewById(R.id.expenses_amount_text);
        this.incomeAndPurchaseTotalText = layout.findViewById(R.id.income_and_purchase_amount_text);
        this.incomeAndExpensesTotalText = layout.findViewById(R.id.income_and_expenses_amount_text);
        this.nextMonthExpensesAmountText = layout.findViewById(R.id.next_mounth_expenses_amount_text);
    }

    public void set(int incomeAmount, int purchaseAmount, int expensesAmount) {
        // 収入金額
        this.incomeAmountText.setText(String.format(Locale.JAPANESE, "￥%,d", incomeAmount));
        setColorByAmount(this.incomeAmountText, incomeAmount);
        // 支出金額
        this.purchaseAmountText.setText(String.format(Locale.JAPANESE, "￥%,d", purchaseAmount));
        if (purchaseAmount > 0) {
            this.purchaseAmountText.setTextColor(this.context.getColor(R.color.purchase_text_color));
        }
        else {
            this.purchaseAmountText.setTextColor(this.context.getColor(R.color.normal_text_color));
        }
        // 支払金額
        this.expensesAmountText.setText(String.format(Locale.JAPANESE, "￥%,d", expensesAmount));
        setColorByAmount(this.expensesAmountText, expensesAmount * -1);
        // 収入-支出
        int bopTotal = incomeAmount - purchaseAmount;
        this.incomeAndPurchaseTotalText.setText(String.format(Locale.JAPANESE, "￥%,d", bopTotal));
        setColorByAmount(this.incomeAndPurchaseTotalText, bopTotal);
        // 収入-支払い
        int paymentTotal = incomeAmount - expensesAmount;
        this.incomeAndExpensesTotalText.setText(String.format(Locale.JAPANESE, "￥%,d", paymentTotal));
        setColorByAmount(this.incomeAndExpensesTotalText, paymentTotal);
        // 翌月以降の支払い
        int nextPaymentAmount = expensesAmount - purchaseAmount;
        this.nextMonthExpensesAmountText.setText(String.format(Locale.JAPANESE, "￥%,d", nextPaymentAmount));
        setColorByAmount(this.nextMonthExpensesAmountText, nextPaymentAmount);

    }
    public void setColorByAmount(TextView view, int amount) {
        if (amount > 0) {
            view.setTextColor(this.context.getColor(R.color.income_text_color));
        }
        else if (amount < 0) {
            view.setTextColor(this.context.getColor(R.color.expenses_text_color));
        }
        else {
            view.setTextColor(this.context.getColor(R.color.normal_text_color));
        }
    }
}
