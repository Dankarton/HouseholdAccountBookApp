package com.example.householdaccountbook.myclasses;

public abstract class BopBaseUiModel {
    private final ViewType viewType;
    private final long id;
    private final int amount;
    private final String memo;

    public BopBaseUiModel(ViewType viewType, long id, int amount, String memo) {
        this.id = id;
        this.viewType = viewType;
        this.amount = Math.abs(amount);
        this.memo = memo;
    }

    public long getId() { return this.id; }
    public ViewType getViewType() { return this.viewType; }
    public int getAmount() { return this.amount; }
    public int getSignedAmount() {
        return switch (this.viewType) {
            case INCOME -> amount;
            case PURCHASE -> -1 * amount;
            case EXPENSES -> -1 * amount;
            default -> 0;
        };
    }
    public String getMemo() { return this.memo; }

    public enum ViewType {
        INCOME(0, UiLayoutType.BASIC_BOP),
        PURCHASE(1, UiLayoutType.BASIC_BOP),
        EXPENSES(2, UiLayoutType.BASIC_BOP),
        MONEY_MOVEMENT(3, UiLayoutType.MONEY_MOVEMENT);

        private final int code;
        private final UiLayoutType uiLayoutType;

        ViewType(int code, UiLayoutType uiType) {
            this.code = code;
            this.uiLayoutType = uiType;
        }
        public int getCode() {
            return this.code;
        }
        public UiLayoutType getUiType() {
            return this.uiLayoutType;
        }
    }
    public enum UiLayoutType {
        BASIC_BOP,
        MONEY_MOVEMENT;
    }
}
