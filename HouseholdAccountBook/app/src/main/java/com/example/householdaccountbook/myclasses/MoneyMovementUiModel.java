package com.example.householdaccountbook.myclasses;

public class MoneyMovementUiModel extends BopBaseUiModel {
    private String toWalletName;
    private String fromWalletName;
    public MoneyMovementUiModel(ViewType viewType, long id, int amount, String memo, String toWalletName, String fromWalletName) {
        super(viewType, id, amount, memo);
        this.toWalletName = toWalletName;
        this.fromWalletName = fromWalletName;
    }
    public String getToWalletName() { return this.toWalletName; }
    public String getFromWalletName() { return this.fromWalletName; }
}
