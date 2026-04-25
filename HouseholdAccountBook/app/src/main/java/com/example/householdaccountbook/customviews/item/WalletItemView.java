package com.example.householdaccountbook.customviews.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.myclasses.dbentity.Wallet;

public class WalletItemView extends ConstraintLayout implements SelectableItem<Wallet> {
    boolean isSelected;
    private Wallet data;
    private TextView nameText;
    public WalletItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public WalletItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WalletItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_view_payment_method_item, this, true);
        this.nameText = findViewById(R.id.name_text);
    }
    public void setData(Wallet data) {
        this.data = data;
        changeText();
    }
    @Override
    public void setSelectedState(boolean selected) {
        this.isSelected = selected;
        this.setSelected(selected);
    }
    @Override
    public boolean isSelected() { return this.isSelected; }

    @Override
    public long getEigenvalue() {
        return this.data.getId();
    }

    @Override
    public Wallet getData() {
        return this.data;
    }
    private void changeText() { this.nameText.setText(this.data.getName()); }
}
