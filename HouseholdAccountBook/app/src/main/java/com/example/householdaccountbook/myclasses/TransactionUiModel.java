package com.example.householdaccountbook.myclasses;

public class TransactionUiModel extends BopBaseUiModel {
    private int categoryColor;
    private String categoryName;

    public TransactionUiModel(ViewType viewType, long id, int amount, String memo, int categoryColor, String categoryName) {
        super(viewType, id, amount, memo);
        this.categoryColor = categoryColor;
        this.categoryName = categoryName;
    }
    public int getCategoryColor() { return this.categoryColor; }
    public String getCategoryName() { return this.categoryName; }
}
