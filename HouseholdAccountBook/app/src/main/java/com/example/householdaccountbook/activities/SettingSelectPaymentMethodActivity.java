package com.example.householdaccountbook.activities;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.fragments.PaymentMethodListingFragment;

import myclasses.PaymentMethod;

public class SettingSelectPaymentMethodActivity extends SettingMotherActivity implements PaymentMethodListingFragment.OnInputEventListener {
    PaymentMethodListingFragment attachedFragment;
    @Override
    protected Fragment init() {
        PaymentMethodListingFragment fragment = new PaymentMethodListingFragment();
        fragment.setListener(this);
        this.attachedFragment = fragment;
        return fragment;
    }

    /**
     * PaymentMethodListingFragmentからの通知
     * @param data PaymentMethod
     */
    @Override
    public void onInputDetected(PaymentMethod data) {
        Intent intent = new Intent(this, SettingEditPaymentMethodActivity.class);
        intent.putExtra("PaymentMethod", data);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.attachedFragment.reload();
    }
}
