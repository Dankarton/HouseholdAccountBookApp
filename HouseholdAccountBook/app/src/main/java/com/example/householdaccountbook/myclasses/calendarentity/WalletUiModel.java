package com.example.householdaccountbook.myclasses.calendarentity;

import java.util.List;

public class WalletUiModel implements CalendarDisplayItem, HasListVisible {
    private final String walletName;
    private final long id;
    private final int deltaAmount;
    private final int currentAmount;
    private final List<CalendarDisplayItem> displayItems;
    private boolean isListVisible;


    public WalletUiModel(long id, String walletName, int deltaAmount, int currentAmount, List<CalendarDisplayItem> displayItems, boolean isListVisible) {
        this.id = id;
        this.walletName = walletName;
        this.deltaAmount = deltaAmount;
        this.currentAmount = currentAmount;
        this.displayItems = displayItems;
        this.isListVisible = isListVisible;
    }
    public long getId() { return this.id; }
    public String getWalletName() { return this.walletName; }
    public int getDeltaAmount() { return this.deltaAmount; }
    public int getCurrentAmount() { return this.currentAmount; }
    public List<CalendarDisplayItem> getChildItems() { return this.displayItems; }
    public boolean isListVisible() { return this.isListVisible; }
    public void setListVisible(boolean isListVisible) { this.isListVisible = isListVisible; }

    @Override
    public ViewType getViewType() {
        return ViewType.WALLET_UI;
    }

    @Override
    public String getUniqueKey() {
        return String.valueOf(getViewType().getCode()) + String.valueOf(this.id);
    }
}
