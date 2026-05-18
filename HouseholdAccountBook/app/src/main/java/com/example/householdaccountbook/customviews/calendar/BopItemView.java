package com.example.householdaccountbook.customviews.calendar;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;

import java.util.Locale;

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
    protected int getAmountColor(int amount) {
        if (amount > 0) {
            return getContext().getColor(R.color.income_text_color);
        }
        else if (amount < 0) {
            return getContext().getColor(R.color.expenses_text_color);
        }
        else {
            return getContext().getColor(R.color.idle_text_color);
        }
    }
    public abstract void setListener(OnActionListener listener);
}
