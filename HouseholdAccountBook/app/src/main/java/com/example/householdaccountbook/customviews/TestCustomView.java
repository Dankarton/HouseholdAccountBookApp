package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;

public class TestCustomView extends ConstraintLayout {

    public TestCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TestCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        ConstraintLayout.inflate(context, R.layout.a_test_view, this);
    }
}
