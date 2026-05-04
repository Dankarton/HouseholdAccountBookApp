package com.example.householdaccountbook.myclasses.calendarentity;

public abstract class BopBaseUiModel implements CalendarDisplayItem {
    private final DataType viewType;
    private final long id;
    private final int amount;
    private final String memo;

    public BopBaseUiModel(DataType viewType, long id, int amount, String memo) {
        this.id = id;
        this.viewType = viewType;
        this.amount = Math.abs(amount);
        this.memo = memo;
    }

    public long getId() { return this.id; }
    public DataType getDataType() { return this.viewType; }
    public int getAmount() { return this.amount; }
    public int getSignedAmount() {
        switch (this.viewType) {
            case INCOME:
                return amount;
            case PURCHASE:
                return -1 * amount;
            case EXPENSES:
                return -1 * amount;
            default:
                return 0;
        }
    }
    public String getMemo() { return this.memo; }

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
