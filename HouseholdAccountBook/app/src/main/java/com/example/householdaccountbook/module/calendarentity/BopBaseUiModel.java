package com.example.householdaccountbook.module.calendarentity;

import com.example.householdaccountbook.customviews.calendar.GroupableItem;

public abstract class BopBaseUiModel implements CalendarDisplayItem, HasGroupable {
    private final DataType viewType;
    private final long id;
    private final int amount;
    private final String memo;
    private GroupableItem.PositionType groupType = GroupableItem.PositionType.SINGLE;
    private boolean didUpdated;

    public BopBaseUiModel(DataType viewType, long id, int amount, String memo, GroupableItem.PositionType groupType) {
        this.id = id;
        this.viewType = viewType;
        this.amount = amount;
        this.memo = memo;
        this.groupType = groupType;
        didUpdated = true;
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
        this.didUpdated = true;
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
    @Override
    public boolean didUpdated() { return this.didUpdated; }
    @Override
    public void used() { this.didUpdated = false; }
}
