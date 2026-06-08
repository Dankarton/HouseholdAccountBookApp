package com.example.householdaccountbook.module.calendarentity;

import com.example.householdaccountbook.customviews.calendar.GroupableItem;

import java.util.List;

public class WalletUiModel implements CalendarDisplayItem, HasListVisible, HasGroupable {
    private final String walletName;
    private final long id;
    private final int deltaAmount;
    private final int currentAmount;
    private final List<CalendarDisplayItem> displayItems;
    private GroupableItem.PositionType groupPos;
    private boolean isListVisible;
    private boolean isListVisibleValid;
    private boolean didUpdated;


    public WalletUiModel(long id, String walletName, int deltaAmount, int currentAmount, List<CalendarDisplayItem> displayItems, GroupableItem.PositionType groupPos, boolean isListVisible) {
        this.id = id;
        this.walletName = walletName;
        this.deltaAmount = deltaAmount;
        this.currentAmount = currentAmount;
        this.displayItems = displayItems;
        this.groupPos = groupPos;
        this.isListVisible = isListVisible;
        this.isListVisibleValid = false;
        this.didUpdated = true;
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

    @Override
    public GroupableItem.PositionType getPositionType() {
        return this.groupPos;
    }

    @Override
    public void setPositionType(GroupableItem.PositionType type) {
        this.groupPos = type;
        this.didUpdated = true;
    }

    @Override
    public boolean equalData(Object o) {
        if (this == o) return true;
        if (WalletUiModel.class != o.getClass()) return false;
        var obj = (WalletUiModel) o;
        return this.getId() == obj.getId() &&
                this.getDeltaAmount() == obj.getDeltaAmount() &&
                this.getCurrentAmount() == obj.getCurrentAmount() &&
                this.isListVisible() == obj.isListVisible() &&
                this.getPositionType() == obj.getPositionType();
    }
    @Override
    public boolean didUpdated() { return this.didUpdated; }
    @Override
    public void used() { this.didUpdated = false; }
    @Override
    public boolean isListVisibleValid() { return this.isListVisibleValid; }
    @Override
    public void setListVisibleValid(boolean valid) { this.isListVisibleValid = valid; }
}
