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
import myclasses.SelectableItem;

public class PaymentMethodItemView extends ConstraintLayout implements SelectableItem {
    private TextView paymentMethodText;
    private boolean isSelected;
    private PaymentMethod paymentMethod;

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
        this.paymentMethodText = findViewById(R.id.payment_method_text);
    }
    public void setData(PaymentMethod data) {
        this.paymentMethod = data;
        this.paymentMethodText.setText(this.paymentMethod.getName());
    }

    @Override
    public void setSelectedState(boolean selected) {

    }
    @Override
    public boolean isSelected(){
        return this.isSelected;
    }
    @Override
    public Object getData(){
        return this.paymentMethod;
    }
}
