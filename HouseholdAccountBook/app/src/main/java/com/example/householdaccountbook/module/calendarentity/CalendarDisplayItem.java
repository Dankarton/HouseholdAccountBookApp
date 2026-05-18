package com.example.householdaccountbook.module.calendarentity;

/**
 * カレンダーFragmentでリスト表示するアイテム
 */
public interface CalendarDisplayItem {
    public enum ViewType {
        CALENDAR_UI(0, UiLayoutType.CALENDAR),
        DAILY_UI(1, UiLayoutType.DAILY),
        WALLET_UI(2, UiLayoutType.WALLET),
        TRANSACTION_UI(3, UiLayoutType.TRANSACTION),
        MONEY_MOVEMENT_UI(4, UiLayoutType.MONEY_MOVEMENT);

        private final int code;
        private final UiLayoutType layoutType;

        ViewType(int code, UiLayoutType type) {
            this.code = code;
            this.layoutType = type;
        }
        public int getCode() { return this.code; }
        public UiLayoutType getUiLayoutType() { return this.layoutType; }
    }
    public enum UiLayoutType {
        CALENDAR(0),
        DAILY(1),
        WALLET(2),
        TRANSACTION(3),
        MONEY_MOVEMENT(4);

        private final int code;

        UiLayoutType(int code) {
            this.code = code;
        }
        public int getCode() { return this.code; }
    }
    public ViewType getViewType();
    public String getUniqueKey();

    public boolean equalData(Object obj);
}
