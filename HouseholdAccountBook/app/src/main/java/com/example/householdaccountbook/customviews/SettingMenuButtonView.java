package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.activities.MainActivity;
import com.example.householdaccountbook.activities.SettingActivity;
import com.example.householdaccountbook.data.SettingMenuFragmentKind;
import com.example.householdaccountbook.util.SettingMenuIntentKeys;

public class SettingMenuButtonView extends ConstraintLayout {
    private TextView titleTextView;
    private SettingMenuFragmentKind destination;

    public SettingMenuButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // レイアウトをinflate
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view_setting_menu_button, this, true);

        titleTextView = findViewById(R.id.setting_title_text);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SettingMenuButtonView);
            String title = ta.getString(R.styleable.SettingMenuButtonView_titleText);
            if (title != null) {
                titleTextView.setText(title);
            }
            ta.recycle();
        }
        // TODO setOnClickListenrを入れたら意味不明なエラーが出るようになった
        setClickButtonEvent(view, context);
    }

    private void setClickButtonEvent(View view, Context context) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destination != null && context instanceof MainActivity) {
                    try {
                        Intent intent = new Intent(context, SettingActivity.class);
                        intent.putExtra(SettingMenuIntentKeys.FRAGMENT_TYPE_KEY, destination);
                        context.startActivity(intent);
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // プログラムからも変更できるようにメソッドを用意
    public void setTitleText(String text) {
        titleTextView.setText(text);
    }

    public void setDestination(SettingMenuFragmentKind fragmentType) {
        this.destination = fragmentType;
    }
    // クリック処理をここに書く

}
