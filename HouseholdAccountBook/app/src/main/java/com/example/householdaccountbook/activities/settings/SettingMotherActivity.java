package com.example.householdaccountbook.activities.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.householdaccountbook.R;

/**
 *
 */
public abstract class SettingMotherActivity extends AppCompatActivity {
    private TextView titleText;
    protected abstract Fragment init();
    protected abstract String setTitleText();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        this.titleText = findViewById(R.id.title_text);
        setBackButtonEvent(findViewById(R.id.back_button));
        Fragment fragment = init();
        this.titleText.setText(setTitleText());
        if (fragment != null) {
            replaceFragment(fragment);
        }
    }
    private void setBackButtonEvent(ImageButton button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.setting_container, fragment);
        transaction.commit();
    }

    protected void reloadFragment(Fragment fragment) {
        if (fragment != null) {
            replaceFragment(fragment);
        }
    }
}