package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;

import myclasses.PaymentMethod;

public class PaymentMethodItemView extends ConstraintLayout {
    private PaymentMethod data;
    private TextView nameText;
    public PaymentMethodItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PaymentMethodItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaymentMethodItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_view_payment_method_item, this, true);
        this.nameText = findViewById(R.id.name_text);
    }
    public void setData(PaymentMethod data) {
        this.data = data;
        changeText();
    }
    public PaymentMethod getData() {
        return this.data;
    }
    private void changeText() {
        this.nameText.setText(this.data.getName());
    }
}
