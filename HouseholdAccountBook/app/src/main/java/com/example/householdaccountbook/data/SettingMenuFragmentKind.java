package com.example.householdaccountbook.data;

public enum SettingMenuFragmentKind {
    INCOME_CATEGORY_EDIT("income_category_edit"),
    EXPENSES_CATEGORY_EDIT("expenses_category_edit"),
    PAYMENT_METHOD_EDIT("payment_method_edit");

    private final String fragmentName;

    SettingMenuFragmentKind(String name) {
        this.fragmentName = name;
    }
    public String getName() {
        return this.fragmentName;
    }
}
