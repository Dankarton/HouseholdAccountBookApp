package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.item.CalendarItemView;
import com.example.householdaccountbook.myclasses.dbentity.BOP;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CalendarCustomView extends ConstraintLayout {
    GridLayout daysGridLayout;

    public CalendarCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CalendarCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_calendar, this);
        this.daysGridLayout = layout.findViewById(R.id.item_list_grid_layout);
    }
    public void bind(Calendar targetDate, HashMap<Integer, Integer> amountDataMap, int maxAmount) {
        Log.d("CalendarCustomView", "bind");
        this.daysGridLayout.post(() -> {
            setCalendarItems(getContext(), targetDate, amountDataMap, maxAmount);
        });
    }
    private void setCalendarItems(Context context, Calendar targetDate, HashMap<Integer, Integer> amountData, int maxAmount) {
        this.daysGridLayout.removeAllViews();
        int motherWidth = this.daysGridLayout.getWidth() - this.daysGridLayout.getPaddingStart() - this.daysGridLayout.getPaddingEnd();
        int itemSize = motherWidth / 7;

        Calendar fistDay = Calendar.getInstance();
        fistDay.set(Calendar.YEAR, targetDate.get(Calendar.YEAR));
        fistDay.set(Calendar.MONTH, targetDate.get(Calendar.MONTH));
        fistDay.set(Calendar.DAY_OF_MONTH, 1);

        Calendar currItemDate = (Calendar) fistDay.clone();
        // 日曜始まりのカレンダー、月初めの空白分後退
        currItemDate.add(Calendar.DAY_OF_MONTH, -1 * (currItemDate.get(Calendar.DAY_OF_WEEK) - 1));
        do {
            for (int i = 0; i < 7; i++) {
                var dayView = new CalendarItemView(context);
                dayView.bind(String.valueOf(currItemDate.get(Calendar.DAY_OF_MONTH)));
                this.daysGridLayout.addView(dayView, getItemParams(itemSize));

                int textColor = context.getColor(R.color.white);
                int circleColor = context.getColor(R.color.base_background);
                int solidColor = context.getColor(R.color.white);
                float scale = 0.75f;
                if (currItemDate.get(Calendar.MONTH) != targetDate.get(Calendar.MONTH)) {
                    textColor = context.getColor(R.color.gray);
                    solidColor = context.getColor(R.color.gray);
                    scale = CalendarItemView.calcCircleScale(0, 0, 1);
                }
                else {
                    Integer amount = amountData.get(currItemDate.get(Calendar.DAY_OF_MONTH));
                    if (amount == null) {
                        amount = 0;
                    }
                    else if (amount < 0) {
                        textColor = context.getColor(R.color.base_background);
                        circleColor = context.getColor(R.color.expenses_text_color);
                        solidColor = context.getColor(R.color.expenses_text_color);
                    }
                    else if (amount > 0) {
                        textColor = context.getColor(R.color.base_background);
                        circleColor = context.getColor(R.color.income_text_color);
                        solidColor = context.getColor(R.color.income_text_color);
                    }
                    scale = CalendarItemView.calcCircleScale(amount, 0, maxAmount);
                }
                dayView.setAppearance(itemSize, scale, textColor, circleColor, solidColor);

                currItemDate.add(Calendar.DAY_OF_MONTH, 1);
            }
        } while (currItemDate.get(Calendar.MONTH) == targetDate.get(Calendar.MONTH));
    }
    private GridLayout.LayoutParams getItemParams(int cellSize) {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = cellSize;
        params.height = cellSize;
        params.setGravity(Gravity.CENTER);
        return params;
    }
}
