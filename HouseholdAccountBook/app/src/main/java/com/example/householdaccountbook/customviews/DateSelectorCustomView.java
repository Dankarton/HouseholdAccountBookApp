package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;

import java.util.Calendar;

public class DateSelectorCustomView extends ConstraintLayout {
    public enum DisplayMode { MONTHLY, DAILY }

    TextView dateText;
    private Calendar currentDate = Calendar.getInstance();
    private DisplayMode mode = DisplayMode.DAILY;
    private OnActionListener listener = null;

    public interface OnActionListener {
        void onUpButtonClicked();
        void onBackButtonClicked();
        void onDateTextClicked();
        void onDateChanged();
    }

    public DateSelectorCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DateSelectorCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DateSelectorCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View layout = ConstraintLayout.inflate(context, R.layout.custom_view_date_selector, this);
        this.dateText = layout.findViewById(R.id.date_text);
        layout.findViewById(R.id.up_button).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeDate(1);
                        setInDateText();
                        if (listener != null) {
                            listener.onUpButtonClicked();
                            listener.onDateChanged();
                        }
                    }
                }
        );
        layout.findViewById(R.id.back_button).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeDate(-1);
                        setInDateText();
                        if (listener != null) {
                            listener.onBackButtonClicked();
                            listener.onDateChanged();
                        }
                    }
                }
        );
        setInDateText();
    }
    private void setInDateText() {
        switch (this.mode) {
            case MONTHLY:
                this.dateText.setText(MyStdlib.convertCalendarToString(
                        this.currentDate.get(Calendar.YEAR),
                        this.currentDate.get(Calendar.MONTH),
                        null, null
                ));
                break;
            case DAILY:
                this.dateText.setText(MyStdlib.convertCalendarToString(
                        this.currentDate.get(Calendar.YEAR),
                        this.currentDate.get(Calendar.MONTH),
                        this.currentDate.get(Calendar.DAY_OF_MONTH),
                        null
                ));
                break;
        }
    }
    private void changeDate(int changeNum) {
        switch (this.mode) {
            case MONTHLY:
                this.currentDate.add(Calendar.MONTH, changeNum);
                break;
            case DAILY:
                this.currentDate.add(Calendar.DAY_OF_MONTH, changeNum);
                break;
        }
    }
    public void setDate(Calendar date) {
        this.currentDate = date;
        // データ更新
        setInDateText();
    }
    public Calendar getCurrentDate() {
        return this.currentDate;
    }
    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }
    public void setDisplayMode(DisplayMode mode) {
        this.mode = mode;
    }
}
