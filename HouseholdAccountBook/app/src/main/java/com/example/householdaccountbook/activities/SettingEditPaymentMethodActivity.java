package com.example.householdaccountbook.activities;

import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.MyDbContract;
import com.example.householdaccountbook.MyDbManager;
import com.example.householdaccountbook.MyOpenHelper;
import com.example.householdaccountbook.fragments.PaymentMethodEditFragment;

import myclasses.PaymentMethod;

public class SettingEditPaymentMethodActivity extends SettingMotherActivity implements PaymentMethodEditFragment.OnInputActionListener {
    @Override
    protected Fragment init() {
        PaymentMethod data = getIntent().getSerializableExtra("PaymentMethod", PaymentMethod.class);
        PaymentMethodEditFragment fragment = new PaymentMethodEditFragment(data);
        fragment.setListener(this);
        return fragment;
    }
    @Override
    public void onSaveButtonClicked(PaymentMethod data) {
        // IDが無い場合(新規追加の場合)
        if (data.getId() == null) {
            MyDbManager.setRecordToDataBase(
                    MyDbContract.PaymentMethodEntry.TABLE_NAME,
                    data.getContentValuesWithoutId());
        }
        // 編集の場合
        else {
            MyDbManager.upsertDatabase(
                    MyDbContract.PaymentMethodEntry.TABLE_NAME,
                    data.getContentValues());
        }
        finish();
    }
    @Override
    public void onDeleteButtonClicked(PaymentMethod data) {
        if (data.getId() != null) {
            MyDbManager.deleteRecordByID(
                    MyDbContract.PaymentMethodEntry.TABLE_NAME,
                    String.valueOf(data.getId()));
        }
        finish();
    }
}
