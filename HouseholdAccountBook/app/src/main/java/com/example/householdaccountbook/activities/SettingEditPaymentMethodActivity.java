package com.example.householdaccountbook.activities;

import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.MyDbManager;
import com.example.householdaccountbook.MyOpenHelper;
import com.example.householdaccountbook.fragments.PaymentMethodEditFragment;

import myclasses.OnInputActionListener;
import myclasses.PaymentMethod;

public class SettingEditPaymentMethodActivity extends SettingMotherActivity implements OnInputActionListener<PaymentMethod> {
    @Override
    protected Fragment init() {
        PaymentMethod data = (PaymentMethod) getIntent().getSerializableExtra("PaymentMethod");
        PaymentMethodEditFragment fragment = new PaymentMethodEditFragment(data);
        fragment.setListener(this);
        return fragment;
    }
    @Override
    public void onCompleted(PaymentMethod data) {
        // IDが無い場合(新規追加の場合)
        if (data.getId() == null) {
            MyDbManager.setRecordToDataBase(MyOpenHelper.PAYMENT_METHOD_TABLE_NAME, data.getContentValuesWithoutId());
        }
        // 編集の場合
        else {
            MyDbManager.upsertDatabase(MyOpenHelper.PAYMENT_METHOD_TABLE_NAME, data.getContentValues());
        }
    }
}
