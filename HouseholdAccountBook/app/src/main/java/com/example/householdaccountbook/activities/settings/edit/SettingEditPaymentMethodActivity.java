package com.example.householdaccountbook.activities.settings.edit;

import com.example.householdaccountbook.fragments.edit.PaymentMethodEditFragment;

import myclasses.PaymentMethod;

public class SettingEditPaymentMethodActivity extends SettingEditBaseActivity<PaymentMethod, PaymentMethodEditFragment> {
    @Override
    protected PaymentMethodEditFragment createFragment(PaymentMethod data) {
        return new PaymentMethodEditFragment(data);
    }
    @Override
    protected String getExtraKey() {
        return "PaymentMethod";
    }
    @Override
    protected Class<PaymentMethod> getEntityClass() {
        return PaymentMethod.class;
    }
}
