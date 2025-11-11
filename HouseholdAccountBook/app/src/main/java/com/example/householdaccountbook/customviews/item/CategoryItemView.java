package com.example.householdaccountbook.customviews.item;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;

import myclasses.BopCategory;
import myclasses.SelectableItem;

public class CategoryItemView <T extends BopCategory> extends ConstraintLayout implements SelectableItem<T> {
    View colorDot;
    TextView categoryText;
    T bopCategory;
    boolean isSelected;
    public CategoryItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CategoryItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoryItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_view_category_item, this, true);
        this.colorDot = findViewById(R.id.category_color_dot);
        this.categoryText = findViewById(R.id.category_text);
    }
    public void setData(T category) {
        this.bopCategory = category;
        Drawable background = colorDot.getBackground();
        if (background instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) background.mutate();
            drawable.setColor(category.getColorCode());  // shape の塗り色を変更
        }
//        this.colorDot.setBackgroundTintList(ColorStateList.valueOf(category.getColorCode()));
        this.categoryText.setText(category.getName());
    }
    @Override
    public void setSelectedState(boolean selected) {
        this.isSelected = selected;
        this.setSelected(selected);
    }
    @Override
    public boolean isSelected() {
        return this.isSelected;
    }
    @Override
    public long getEigenvalue() { return this.bopCategory.getId(); }
    @Override
    public T getData() {
        return this.bopCategory;
    }
}
