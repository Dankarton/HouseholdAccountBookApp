package com.example.householdaccountbook.myclasses;

public class BopUiModel extends TransactionUiModel {
    private int categoryColor;
    private String categoryName;

    public BopUiModel(ViewType viewType, int amount, String memo, int categoryColor, String categoryName) {
        super(viewType, amount, memo);
        this.categoryColor = categoryColor;
        this.categoryName = categoryName;
    }
    public int getCategoryColor() { return this.categoryColor; }
    public String getCategoryName() { return this.categoryName; }
}
