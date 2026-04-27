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

public class TransactionItemView extends BopItemView {
    private View colorDot;
    private TextView categoryText;
    private TextView memoText;
    private TextView additionalMemoText;
    private TextView amountText;

    public TransactionItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TransactionItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_transaction_item, this);
        this.colorDot = layout.findViewById(R.id.category_color_dot);
        this.categoryText = layout.findViewById(R.id.category_text);
        this.memoText = layout.findViewById(R.id.memo_text_view);
        this.additionalMemoText = layout.findViewById(R.id.additional_text_view);
        this.amountText = layout.findViewById(R.id.amount_text_view);
        layout.findViewById(R.id.more_action_button).setOnClickListener(
                v -> {
                    if (listener != null) listener.onMoreActionButtonClicked();
                }
        );
    }
    public void bind(int colorInt, String categoryStr, String memo, String additionMemo, int amount) {
        // カラー変更
        Drawable background = this.colorDot.getBackground();
        if (background instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) background.mutate();
            drawable.setColor(colorInt);
        }
        this.categoryText.setText(categoryStr);
        this.memoText.setText(memo);
        this.additionalMemoText.setText(additionMemo);
        this.amountText.setText(String.format(Locale.JAPANESE, "￥%,d", amount));
    }
    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }
}
