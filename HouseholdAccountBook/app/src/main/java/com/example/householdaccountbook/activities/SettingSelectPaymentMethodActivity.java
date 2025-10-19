package com.example.householdaccountbook.activities;

import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.fragments.PaymentMethodEditFragment;

import myclasses.OnInputActionListener;
import myclasses.PaymentMethod;

public class SettingSelectPaymentMethodActivity extends SettingMotherActivity implements OnInputActionListener<PaymentMethod> {
    @Override
    protected Fragment init() {
        PaymentMethodEditFragment fragment = new PaymentMethodEditFragment();
        fragment.setListener(this);
        return fragment;
    }
    @Override
    public void onCompleted(PaymentMethod data) {
        // TODO
    }
}
