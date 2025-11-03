package com.example.householdaccountbook.activities.settings.edit;

import com.example.householdaccountbook.fragments.edit.IncomeCategoryEditFragment;

import myclasses.IncomeCategory;

public class SettingEditIncomeCategoryActivity extends SettingEditBaseActivity<IncomeCategory, IncomeCategoryEditFragment> {

    @Override
    protected IncomeCategoryEditFragment createFragment(IncomeCategory data) {
        return new IncomeCategoryEditFragment(data);
    }

    @Override
    protected String getExtraKey() {
        return "IncomeCategory";
    }

    @Override
    protected Class<IncomeCategory> getEntityClass() {
        return IncomeCategory.class;
    }
}
