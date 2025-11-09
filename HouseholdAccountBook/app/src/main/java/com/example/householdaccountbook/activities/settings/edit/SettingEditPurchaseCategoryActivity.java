package com.example.householdaccountbook.activities.settings.edit;

import com.example.householdaccountbook.fragments.edit.PurchaseCategoryEditFragment;

import myclasses.PurchaseCategory;

public class SettingEditPurchaseCategoryActivity extends SettingEditBaseActivity<PurchaseCategory, PurchaseCategoryEditFragment> {
    @Override
    protected PurchaseCategoryEditFragment createFragment(PurchaseCategory data) {
        return new PurchaseCategoryEditFragment(data);
    }

    @Override
    protected String getExtraKey() {
        return "PurchaseCategory";
    }

    @Override
    protected Class<PurchaseCategory> getEntityClass() {
        return PurchaseCategory.class;
    }
}
