package com.example.householdaccountbook.customviews.item;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class BopItemView extends ConstraintLayout {
    protected OnActionListener listener = null;
    public interface OnActionListener {
        void onMoreActionButtonClicked();
    }

    public BopItemView(@NonNull Context context) {
        super(context);
    }

    public BopItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BopItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void setListener(OnActionListener listener);
}
