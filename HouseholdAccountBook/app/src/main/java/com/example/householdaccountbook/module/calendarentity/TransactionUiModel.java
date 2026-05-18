package com.example.householdaccountbook.module.calendarentity;

import com.example.householdaccountbook.customviews.item.GroupableItem;

public class TransactionUiModel extends BopBaseUiModel {
    private String additionalMemo;
    private int categoryColor;
    private String categoryName;

    public TransactionUiModel(DataType viewType, long id, int amount, String memo, String additionMemo, int categoryColor, String categoryName) {
        super(viewType, id, amount, memo);
        this.additionalMemo = additionMemo;
        this.categoryColor = categoryColor;
        this.categoryName = categoryName;
    }
    public int getCategoryColor() { return this.categoryColor; }
    public String getCategoryName() { return this.categoryName; }
    public String getAdditionalMemo() { return this.additionalMemo; }

    @Override
    public ViewType getViewType() {
        return ViewType.TRANSACTION_UI;
    }

    @Override
    public String getUniqueKey() {
        return String.valueOf(getViewType().getCode()) + String.valueOf(getId());
    }
}
