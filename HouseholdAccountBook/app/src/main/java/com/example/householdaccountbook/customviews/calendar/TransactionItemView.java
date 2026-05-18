package com.example.householdaccountbook.customviews.calendar;

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
import com.example.householdaccountbook.customviews.item.GroupableItem;

import java.util.Locale;

public class TransactionItemView extends BopItemView implements GroupableItem {
    private View colorDot;
    private TextView categoryText;
    private TextView memoText;
    private TextView additionalMemoText;
    private TextView amountText;

    private PositionType groupPosition = PositionType.SINGLE;

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
    public void bind(int colorInt, String categoryStr, String memo, String additionMemo, int amount, PositionType type) {
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
        this.amountText.setTextColor(getAmountColor(amount));

        this.groupPosition = type;
        this.setBackgroundResource(type.getResource());
    }
    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void setGroupPosition(PositionType type) {
        this.groupPosition = type;
    }

    @Override
    public PositionType getGroupPosition() {
        return this.groupPosition;
    }
}
