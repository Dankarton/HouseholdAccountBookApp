package com.example.householdaccountbook.myclasses.calendarentity;

import java.util.List;

public class WalletUiModel implements CalendarDisplayItem {
    private final String walletName;
    private final long id;
    private final int deltaAmount;
    private final int currentAmount;
    private final List<BopBaseUiModel> displayItems;

    public WalletUiModel(long id, String walletName, int deltaAmount, int currentAmount, List<BopBaseUiModel> displayItems) {
        this.id = id;
        this.walletName = walletName;
        this.deltaAmount = deltaAmount;
        this.currentAmount = currentAmount;
        this.displayItems = displayItems;
    }
    public long getId() { return this.id; }
    public String getWalletName() { return this.walletName; }
    public int getDeltaAmount() { return this.deltaAmount; }
    public int getCurrentAmount() { return this.currentAmount; }
    public List<BopBaseUiModel> getDisplayItems() { return this.displayItems; }

    @Override
    public ViewType getViewType() {
        return ViewType.WALLET_UI;
    }

    @Override
    public String getUniqueKey() {
        return String.valueOf(getViewType().getCode()) + String.valueOf(this.id);
    }
}
