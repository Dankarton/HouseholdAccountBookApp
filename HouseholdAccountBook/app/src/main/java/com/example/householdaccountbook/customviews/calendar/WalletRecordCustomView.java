package com.example.householdaccountbook.customviews.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;

import java.util.Locale;

public class WalletRecordCustomView extends ConstraintLayout implements ListableItem, GroupableItem {
    private TextView walletNameTextView;
    private TextView bopAmountTextView;
    private TextView totalAmountTextView;
    private ImageView listStateView;
    private boolean isListVisible;
    private boolean isListButtonValid = true;

    private PositionType groupPosition = PositionType.SINGLE;

    private OnActionListener listener = null;

    public WalletRecordCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public WalletRecordCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WalletRecordCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_wallet_record, this);
        this.walletNameTextView = layout.findViewById(R.id.wallet_text);
        this.bopAmountTextView = layout.findViewById(R.id.bop_amount_text);
        this.totalAmountTextView = layout.findViewById(R.id.total_amount_text);
        this.listStateView = layout.findViewById(R.id.list_sate_view);
        this.listStateView.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isListButtonValid) return;
                        if (listener != null) listener.onListableButtonClicked(!isListVisible);
                        changeDropDownVisible(!isListVisible);


                    }
                }
        );
        changeDropDownVisible(false);
    }

    public void bind(String walletName, int bopAmount, int totalAmount, PositionType type, boolean isListVisible) {
        this.walletNameTextView.setText(walletName);
        setBopAmount(bopAmount);
        this.totalAmountTextView.setText(String.format(Locale.JAPANESE, "￥%,d", totalAmount));
        this.groupPosition = type;
        this.setBackgroundResource(type.getResource());
        changeDropDownVisible(isListVisible);
    }

    private void setBopAmount(int amount) {
        this.bopAmountTextView.setText(String.format(Locale.JAPANESE, "￥%,d", amount));
        if (amount > 0) {
            this.bopAmountTextView.setTextColor(getContext().getColor(R.color.income_text_color));
        }
        else if (amount < 0) {
            this.bopAmountTextView.setTextColor(getContext().getColor(R.color.expenses_text_color));
        }
        else {
            this.bopAmountTextView.setTextColor(getContext().getColor(R.color.idle_text_color));
        }
    }

    private void changeDropDownVisible(boolean visible) {
        if (visible) {
            this.listStateView.setImageResource(R.drawable.keyboard_arrow_up_24px);
        }
        else {
            this.listStateView.setImageResource(R.drawable.keyboard_arrow_down_24px);
        }
        this.isListVisible = visible;

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
    @Override
    public void setListButtonValid(boolean visible) {
        this.isListButtonValid = visible;
        if (this.listStateView == null) return;

        // カラー変更
        Drawable background = this.listStateView.getBackground();
        if (background instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) background.mutate();
            if (this.isListButtonValid) {
                drawable.setColor(getResources().getColor(R.color.white));
            }
            else {
                drawable.setColor(getResources().getColor(R.color.idle_item_color));
            }
        }
    }
}
