package com.example.householdaccountbook.myclasses;

public class MoneyMovementUiModel extends TransactionUiModel {
    private String toWalletName;
    private String fromWalletName;
    public MoneyMovementUiModel(ViewType viewType, int amount, String memo, String toWalletName, String fromWalletName) {
        super(viewType, amount, memo);
        this.toWalletName = toWalletName;
        this.fromWalletName = fromWalletName;
    }
    public String getToWalletName() { return this.toWalletName; }
    public String getFromWalletName() { return this.fromWalletName; }
}
