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

import java.util.Locale;

public class CategoryTotalItemView extends ConstraintLayout {
    private View colorDot;
    private TextView categoryTextView;
    private TextView totalAmountTextView;
    private TextView ratioTextView;
    public CategoryTotalItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CategoryTotalItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoryTotalItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.item_category_total, this);
        this.colorDot = layout.findViewById(R.id.category_color_dot);
        this.categoryTextView = layout.findViewById(R.id.category_text);
        this.totalAmountTextView = layout.findViewById(R.id.amount_text);
        this.ratioTextView = layout.findViewById(R.id.ratio_text);
    }
    public void bind(int color, String categoryName, int totalAmount, float ratio) {
        Drawable background = this.colorDot.getBackground();
        if (background instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) background.mutate();
            drawable.setColor(color);
        }
        this.categoryTextView.setText(categoryName);
        this.totalAmountTextView.setText(String.format(Locale.JAPANESE, "￥%,d", totalAmount));
        this.ratioTextView.setText(String.format(Locale.JAPANESE, "%5.1f%%", ratio * 100f));
    }
}
