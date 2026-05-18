package com.example.householdaccountbook.customviews.calendar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;

import java.util.Calendar;
import java.util.HashMap;

public class CalendarCustomView extends ConstraintLayout {
    GridLayout daysGridLayout;
    // カレンダー内Dayオブジェへ高速にアクセスるための保持配列
    CalendarItemView[] dayViews = new CalendarItemView[42];

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
        int defaultTextColor = context.getColor(R.color.gray);
        int defaultCircleColor = context.getColor(R.color.gray);
        int defaultSolidColor = context.getColor(R.color.base_background);

        // 42はカレンダー内の日付オブジェクトの最大数。一月当たりの週数は最大6で、前月・来月の日で穴埋めすると42個の日付オブジェが必要。
        for (int i = 0; i < 42; i++) {
            CalendarItemView dayView = (CalendarItemView) this.daysGridLayout.getChildAt(i);
//            CalendarItemView dayView = new CalendarItemView(context);
//            dayView.setAppearance(defaultItemSize, defaultCircleScale, defaultTextColor, defaultCircleColor, defaultSolidColor);
//            this.daysGridLayout.addView(dayView, getItemParams(defaultItemSize));
//            this.daysGridLayout.addView(dayView, getItemParams());
            dayView.setAppearance(defaultTextColor, defaultCircleColor, defaultSolidColor);
            this.dayViews[i] = dayView;
        }

    }

    public void bind(Calendar targetDate, HashMap<Integer, Integer> amountDataMap, int maxAmount) {
//        Log.d("CalendarCustomView", "bind");
        setDataToItems(getContext(), targetDate, amountDataMap, maxAmount);
//        this.daysGridLayout.post(() -> {
//            Log.d("CalendarCustomView", "post: GridLayout update.");
//            setItemParams();
//            this.daysGridLayout.requestLayout();
//        });
//        this.daysGridLayout.post(() -> {
//            setDataToItems(getContext(), targetDate, amountDataMap, maxAmount);
//        });
    }
    public void setItemParams() {
        int motherWidth = this.daysGridLayout.getWidth() - this.daysGridLayout.getPaddingStart() - this.daysGridLayout.getPaddingEnd();
        int itemSize = motherWidth / 7;
        Log.d("CalendarCustomView", "item size: " + itemSize);
        for (int i = 0; i < 42; i++) {
            CalendarItemView dayView = this.dayViews[i];
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) dayView.getLayoutParams();
            params.width = itemSize;
            params.height = itemSize;
            dayView.setLayoutParams(params);
            dayView.setAppearance(this.dayViews[i].getCircleScale());

//            this.dayViews[i].setLayoutParams(getItemParams(itemSize));
        }
    }
    private void setDataToItems(Context context, Calendar targetDate, HashMap<Integer, Integer> amountData, int maxAmount) {
        Calendar fistDay = Calendar.getInstance();
        fistDay.set(Calendar.YEAR, targetDate.get(Calendar.YEAR));
        fistDay.set(Calendar.MONTH, targetDate.get(Calendar.MONTH));
        fistDay.set(Calendar.DAY_OF_MONTH, 1);

        Calendar currItemDate = (Calendar) fistDay.clone();
        // 日曜始まりのカレンダー、月初めの空白分後退(例えば、水曜日始まりだったら、前月の日、月、火で余白を埋める)
        // (29) (30) (31) [ 1] [ 2] [ 3] [ 4]
        // [ 5] [ 6] [ 7] [ 8] [ 9] [10] [11]
        // [12] [13] [14] [15] [16] [17] [18]
        // [19] [20] [21] [22] [23] [24] [25]
        // [26] [27] [28] [29] [30] ( 1) ( 2)
        currItemDate.add(Calendar.DAY_OF_MONTH, -1 * (currItemDate.get(Calendar.DAY_OF_WEEK) - 1));
        int dayViewIndex = 0;
        do {
            for (int i = 0; i < 7; i++) {
                int textColor = context.getColor(R.color.white);
                int circleColor = context.getColor(R.color.base_background);
                int solidColor = context.getColor(R.color.white);
                float scale = CalendarItemView.minCircleScale;
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
                    scale = CalendarItemView.calcCircleScale(Math.abs(amount), 0, maxAmount);
                }
                this.dayViews[dayViewIndex].setVisibility(View.VISIBLE);
                this.dayViews[dayViewIndex].bind(String.valueOf(currItemDate.get(Calendar.DAY_OF_MONTH)));
                this.dayViews[dayViewIndex].setAppearance(scale, textColor, circleColor, solidColor);

                currItemDate.add(Calendar.DAY_OF_MONTH, 1);
                dayViewIndex++;
            }
        } while (currItemDate.get(Calendar.MONTH) == targetDate.get(Calendar.MONTH));
        // 余り部分を非表示
        for (int i = dayViewIndex; i < 42; i++) {
            this.dayViews[i].setVisibility(View.GONE);
        }
    }
    private GridLayout.LayoutParams getItemParams() {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1);
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.setGravity(Gravity.CENTER);
        return params;
    }
}
