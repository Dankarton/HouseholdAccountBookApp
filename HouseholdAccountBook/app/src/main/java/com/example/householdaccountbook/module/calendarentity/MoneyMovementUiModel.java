package com.example.householdaccountbook.module.calendarentity;

import com.example.householdaccountbook.customviews.calendar.GroupableItem;

public class MoneyMovementUiModel extends BopBaseUiModel {
    private String toWalletName;
    private String fromWalletName;
    public MoneyMovementUiModel(DataType viewType, long id, int amount, String memo, String toWalletName, String fromWalletName, GroupableItem.PositionType groupPos) {
        super(viewType, id, amount, memo, groupPos);
        this.toWalletName = toWalletName;
        this.fromWalletName = fromWalletName;
    }

    @Override
    public ViewType getViewType() { return ViewType.MONEY_MOVEMENT_UI; }
    @Override
    public String getUniqueKey() { return String.valueOf(getViewType().getCode()) + String.valueOf(getId()); }
    @Override
    public boolean equalData(Object o) {
        if (this == o) return true;
        if (MoneyMovementUiModel.class != o.getClass()) return false;
        MoneyMovementUiModel obj = (MoneyMovementUiModel) o;
        return this.getId() == obj.getId() &&
                this.getPositionType() == obj.getPositionType();

    }
    public String getToWalletName() { return this.toWalletName; }
    public String getFromWalletName() { return this.fromWalletName; }
}
