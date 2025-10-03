package com.example.householdaccountbook.customviews;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.householdaccountbook.R;

public class SettingMenuButtonView extends ConstraintLayout {
    private TextView titleTextView;
    private String destination;

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
            destination = ta.getString(R.styleable.SettingMenuButtonView_destination);
            ta.recycle();
        }
        // TODO setOnClickListenrを入れたら意味不明なエラーが出るようになった
        setClickButtonEvent(view);
    }

    private void setClickButtonEvent(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destination == null) return;

                // ContextがFragmentの親ActivityかFragmentActivityかを確認
                FragmentActivity activity = null;
                if (getContext() instanceof FragmentActivity) {
                    activity = (FragmentActivity) getContext();
                } else if (getContext() instanceof ContextWrapper) {
                    Context baseContext = ((ContextWrapper) getContext()).getBaseContext();
                    if (baseContext instanceof FragmentActivity) {
                        activity = (FragmentActivity) baseContext;
                    }
                }

                if (activity != null) {
                    try {
                        Fragment fragment = (Fragment) Class.forName(destination).newInstance();
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout, fragment)
                                .addToBackStack(null)
                                .commit();
                    } catch (Exception e) {
//                    e.printStackTrace();
                    }
                }
            }
        });
    }

    // プログラムからも変更できるようにメソッドを用意
    public void setTitleText(String text) {
        titleTextView.setText(text);
    }

    public String getDestination() {
        return destination;
    }
    // クリック処理をここに書く

}
