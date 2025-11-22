package com.example.householdaccountbook.activities.settings;

import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.fragments.edit.BalanceEditFragment;

public class SettingBalanceEditActivity extends SettingMotherActivity {

    @Override
    protected Fragment init() {
        BalanceEditFragment fragment = new BalanceEditFragment();
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int initialBalance = preferences.getInt("initial_balance", 0);
        fragment.setItems(initialBalance);
        fragment.setListener(new BalanceEditFragment.OnInputActionListener() {
            @Override
            public void onSaveButtonClicked(int amount) {
                onSaveToPreference(amount);
                finish();
            }
        });
        return fragment;
    }

    @Override
    protected String setTitleText() {
        return "残高設定";
    }

    private void onSaveToPreference(int balanceAmount) {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        preferences.edit().putInt("initial_balance", balanceAmount).apply();
    }
}
