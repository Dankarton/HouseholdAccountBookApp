package com.example.householdaccountbook.customviews.item;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.CalendarCustomView;

public class CalendarItemView extends ConstraintLayout {

    private View colorDot;
    private TextView dayTextView;

    private static float minCircleScale = 0.65f;
    private static float maxCircleScale = 1.0f;
    public CalendarItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CalendarItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_calendar_item, this);
        this.colorDot = layout.findViewById(R.id.back_color_dot);
        this.dayTextView = layout.findViewById(R.id.day_num_text);
    }
    public void bind(String dayText) {
        this.dayTextView.setText(dayText);
    }
    public void setAppearance(int itemSize, float circleScale, int textColor, int circleColor, int strokeColor) {
        // 下限値上限値を超えないように丸め込める処理
        circleScale = Math.max(minCircleScale, circleScale);
        circleScale = Math.min(maxCircleScale, circleScale);
        // サークルのサイズ変更
        int circleSize = (int) (itemSize * circleScale);
        ConstraintLayout.LayoutParams circleParams = (ConstraintLayout.LayoutParams) colorDot.getLayoutParams();
        circleParams.width = circleSize;
        circleParams.height = circleSize;
        this.colorDot.setLayoutParams(circleParams);
        // 色付け
        Drawable background = this.colorDot.getBackground();
        if (background instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) background.mutate();
            int strokeWidthPx = (int) (2 * getContext().getResources().getDisplayMetrics().density);
            drawable.setColor(circleColor);
            drawable.setStroke(strokeWidthPx, strokeColor);
        }
        this.dayTextView.setTextColor(textColor);
    }
    public static float calcCircleScale(int num, int minNum, int maxNum) {
        double numSqr = Math.sqrt(num);
        double minSqr = Math.sqrt(minNum);
        double maxSqr = Math.sqrt(maxNum);
        float ratio = (float) ((numSqr - minSqr) / maxSqr);
        return minCircleScale + (ratio * (maxCircleScale - minCircleScale));
    }
}
