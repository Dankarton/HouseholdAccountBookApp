package com.example.householdaccountbook.customviews.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;

import java.util.Locale;

public class DailyUiItem extends ConstraintLayout implements ListableItem {
    private TextView dateTextView;
    private TextView amountTextView;
    private ImageView listStateImageView;
    private boolean isListVisible = false;

    private OnActionListener listener = null;

    public DailyUiItem(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DailyUiItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DailyUiItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初期化
     * @param context
     */
    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_daily_ui_item, this);
        this.dateTextView = layout.findViewById(R.id.date_text);
        this.amountTextView = layout.findViewById(R.id.amount_text);
        this.listStateImageView = layout.findViewById(R.id.list_sate_view);
        this.listStateImageView.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeDropDownVisible(!isListVisible);
                    }
                }
        );
        changeDropDownVisible(false);
    }

    /**
     * データ挿入
     * @param date
     * @param amount
     * @param isDropDownVisible
     */
    public void bind(int date, int amount, boolean isDropDownVisible) {
        setDate(date);
        setAmount(amount);
        changeDropDownVisible(isDropDownVisible);
    }

    /**
     * 日付変更
     * @param date
     */
    private void setDate(int date) {
        this.dateTextView.setText(String.format(Locale.JAPANESE, "%d日", date));
    }

    /**
     * 金額変更
     * @param amount
     */
    private void setAmount(int amount) {
        this.amountTextView.setText(String.format(Locale.JAPANESE, "￥%,d", amount));
        if (amount > 0) {
            this.amountTextView.setTextColor(getContext().getColor(R.color.income_text_color));
        }
        else if (amount < 0) {
            this.amountTextView.setTextColor(getContext().getColor(R.color.expenses_text_color));
        }
        else {
            this.amountTextView.setTextColor(getContext().getColor(R.color.idle_text_color));
        }
    }

    /**
     * リスト表示非表示切り替え
     * @param visible
     */
    private void changeDropDownVisible(boolean visible) {
        if (visible) {
            this.listStateImageView.setImageResource(R.drawable.arrow_drop_up_24px);
        }
        else {
            listStateImageView.setImageResource(R.drawable.arrow_drop_down_24px);
        }
        this.isListVisible = visible;
        if (this.listener != null) this.listener.onListableButtonClicked(visible);
    }

    /**
     * リスナー登録
     * @param listener
     */
    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }
}
