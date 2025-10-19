package com.example.householdaccountbook.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.data.SettingMenuFragmentKind;
import com.example.householdaccountbook.fragments.ExpensesCategoryEditFragment;
import com.example.householdaccountbook.fragments.IncomeCategoryEditFragment;
import com.example.householdaccountbook.fragments.PaymentMethodEditFragment;
import com.example.householdaccountbook.util.SettingMenuIntentKeys;

/**
 *
 */
public abstract class SettingMotherActivity extends AppCompatActivity {
    protected abstract Fragment init();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        setBackButtonEvent(findViewById(R.id.back_button));
        Fragment fragment = init();
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
}