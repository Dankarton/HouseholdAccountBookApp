package com.example.householdaccountbook.module.calendarentity;

import com.example.householdaccountbook.customviews.item.GroupableItem;

public abstract class BopBaseUiModel implements CalendarDisplayItem, HasGroupable {
    private final DataType viewType;
    private final long id;
    private final int amount;
    private final String memo;
    private GroupableItem.PositionType groupType = GroupableItem.PositionType.SINGLE;

    public BopBaseUiModel(DataType viewType, long id, int amount, String memo) {
        this.id = id;
        this.viewType = viewType;
        this.amount = amount;
        this.memo = memo;
    }

    public long getId() { return this.id; }
    public DataType getDataType() { return this.viewType; }
    public int getAmount() { return Math.abs(this.amount); }
    public int getSignedAmount() {
        switch (this.viewType) {
            case INCOME:
                return amount;
            case PURCHASE:
                return -1 * amount;
            case EXPENSES:
                return -1 * amount;
            default:
                return amount;
        }
    }
    public String getMemo() { return this.memo; }

    @Override
    public GroupableItem.PositionType getPositionType() { return this.groupType; }
    @Override
    public void setPositionType(GroupableItem.PositionType type) {
        this.groupType = type;
    }

    public enum DataType {
        INCOME(0),
        PURCHASE(1),
        EXPENSES(2),
        MONEY_MOVEMENT(3);

        private final int code;

        DataType(int code) {
            this.code = code;
        }
        public int getCode() {
            return this.code;
        }
    }
}
