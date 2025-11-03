package com.example.householdaccountbook.activities.settings.edit;

import com.example.householdaccountbook.fragments.edit.ExpensesCategoryEditFragment;

import myclasses.ExpensesCategory;

public class SettingEditExpensesCategoryActivity extends SettingEditBaseActivity<ExpensesCategory, ExpensesCategoryEditFragment> {
    @Override
    protected ExpensesCategoryEditFragment createFragment(ExpensesCategory data) {
        return new ExpensesCategoryEditFragment(data);
    }

    @Override
    protected String getExtraKey() {
        return "ExpensesCategory";
    }

    @Override
    protected Class<ExpensesCategory> getEntityClass() {
        return ExpensesCategory.class;
    }
}
