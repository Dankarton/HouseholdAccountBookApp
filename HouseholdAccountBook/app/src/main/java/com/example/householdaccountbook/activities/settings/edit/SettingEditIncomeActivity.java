package com.example.householdaccountbook.activities.settings.edit;

import com.example.householdaccountbook.fragments.edit.IncomeEditFragment;

import com.example.householdaccountbook.myclasses.dbentity.Income;

public class SettingEditIncomeActivity extends SettingEditBaseActivity<Income, IncomeEditFragment> {
    @Override
    protected IncomeEditFragment createFragment(Income data) {
        return new IncomeEditFragment(data);
    }

    @Override
    protected String getExtraKey() {
        return "Income";
    }

    @Override
    protected Class<Income> getEntityClass() {
        return Income.class;
    }
    @Override
    protected String setTitleText() {
        return "収入データ編集";
    }
}
