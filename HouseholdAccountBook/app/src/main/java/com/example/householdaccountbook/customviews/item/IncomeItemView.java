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

public class IncomeItemView extends ConstraintLayout {
    public interface OnActionListener {
        void onMoreActionButtonClicked();
    }
    private View categoryColorDot;
    private TextView categoryTextView;
    private TextView memoTextView;
    private TextView amountTextView;

    private OnActionListener listener = null;

    public IncomeItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public IncomeItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IncomeItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.item_income, this);
        this.categoryColorDot = layout.findViewById(R.id.category_color_dot);
        this.categoryTextView = layout.findViewById(R.id.category_text);
        this.memoTextView = layout.findViewById(R.id.memo_text_view);
        this.amountTextView = layout.findViewById(R.id.amount_text_view);
        // 詳細ボタンが押されたことを上位層に伝えるリスナー登録
        layout.findViewById(R.id.more_action_button).setOnClickListener(view -> {
            if (this.listener != null) this.listener.onMoreActionButtonClicked();
        });
    }
    public void bind(int colorInt, String categoryText, String memoText, int amount) {
        // カラー変更
        Drawable background = this.categoryColorDot.getBackground();
        if (background instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) background.mutate();
            drawable.setColor(colorInt);
        }
        // テキスト変更
        this.categoryTextView.setText(categoryText);
        this.memoTextView.setText(memoText);
        this.amountTextView.setText(String.format(Locale.JAPANESE, "￥%,d", amount));
    }
    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }
}
