package com.example.householdaccountbook.customviews.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;

import java.util.Locale;

public class MoneyMovementItemView extends BopItemView {
    View colorDot;
    TextView toWalletText;
    TextView fromWalletText;
    TextView memoText;
    TextView amountText;
    public MoneyMovementItemView(@NonNull Context context) {
        super(context);
    }

    public MoneyMovementItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoneyMovementItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_money_movement_item, this);
        this.colorDot = layout.findViewById(R.id.category_color_dot);
        this.toWalletText = layout.findViewById(R.id.to_wallet_text);
        this.fromWalletText = layout.findViewById(R.id.from_wallet_text);
        this.memoText = layout.findViewById(R.id.memo_text_view);
        this.amountText = layout.findViewById(R.id.amount_text);
        layout.findViewById(R.id.more_action_button).setOnClickListener(
                v -> {
                    if (listener != null) listener.onMoreActionButtonClicked();
                }
        );
    }
    public void bind(String toWalletName, String fromWalletName, String memo, int amount) {
        this.toWalletText.setText(toWalletName);
        this.fromWalletText.setText(fromWalletName);
        this.memoText.setText(memo);
        this.amountText.setText(String.format(Locale.JAPANESE, "￥%,d", amount));
    }

    @Override
    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }
}
