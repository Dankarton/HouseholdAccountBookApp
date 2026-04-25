package com.example.householdaccountbook.myclasses;

public abstract class TransactionUiModel {
    public final ViewType viewType;
    public final int amount;
    public final String memo;

    public TransactionUiModel(ViewType viewType, int amount, String memo) {
        this.viewType = viewType;
        this.amount = Math.abs(amount);
        this.memo = memo;
    }

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
