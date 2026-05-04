package com.example.householdaccountbook.myclasses.calendarentity;

public class MoneyMovementUiModel extends BopBaseUiModel {
    private String toWalletName;
    private String fromWalletName;
    public MoneyMovementUiModel(DataType viewType, long id, int amount, String memo, String toWalletName, String fromWalletName) {
        super(viewType, id, amount, memo);
        this.toWalletName = toWalletName;
        this.fromWalletName = fromWalletName;
    }

    @Override
    public ViewType getViewType() { return ViewType.MONEY_MOVEMENT_UI; }
    @Override
    public String getUniqueKey() { return String.valueOf(getViewType().getCode()) + String.valueOf(getId()); }

    public String getToWalletName() { return this.toWalletName; }
    public String getFromWalletName() { return this.fromWalletName; }
}
