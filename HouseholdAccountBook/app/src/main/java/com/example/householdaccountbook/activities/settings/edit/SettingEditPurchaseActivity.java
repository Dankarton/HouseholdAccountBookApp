package com.example.householdaccountbook.activities.settings.edit;

import com.example.householdaccountbook.fragments.edit.PurchaseEditFragment;

import com.example.householdaccountbook.myclasses.dbentity.Purchase;

public class SettingEditPurchaseActivity extends SettingEditBaseActivity<Purchase, PurchaseEditFragment> {
    @Override
    protected PurchaseEditFragment createFragment(Purchase data) {
        return new PurchaseEditFragment(data);
    }

    @Override
    protected String getExtraKey() {
        return "Purchase";
    }

    @Override
    protected Class<Purchase> getEntityClass() {
        return Purchase.class;
    }
    @Override
    protected String setTitleText() {
        return "支出データ編集";
    }
}
