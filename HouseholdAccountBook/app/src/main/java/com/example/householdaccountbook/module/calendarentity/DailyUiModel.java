package com.example.householdaccountbook.module.calendarentity;

import com.example.householdaccountbook.customviews.calendar.GroupableItem;

import java.util.List;

public class DailyUiModel implements CalendarDisplayItem, HasListVisible, HasGroupable {
    private final int year, month, date;
    private final Integer deltaAmount;
    private GroupableItem.PositionType groupPos = GroupableItem.PositionType.SINGLE;
    private boolean isListVisible;
    private boolean isListVisibleValid;
    private boolean didUpdated;

    private final List<CalendarDisplayItem> displayItems;

    public DailyUiModel(int year, int month, int date, Integer deltaAmount, List<CalendarDisplayItem> displayItems, GroupableItem.PositionType groupPos, boolean isListVisible) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.deltaAmount = deltaAmount;
        this.displayItems = displayItems;
        this.groupPos = groupPos;
        this.isListVisible = isListVisible;
        this.isListVisibleValid = false;
        this.didUpdated = true;
    }

    public int getYear() { return year; }
    public int getMonth() { return this.month; }
    public int getDate() { return this.date; }
    public Integer getDeltaAmount() { return this.deltaAmount; }
    public List<CalendarDisplayItem> getChildItems() { return this.displayItems; }

    public boolean isListVisible() { return this.isListVisible; }
    public void setListVisible(boolean isListVisible) { this.isListVisible = isListVisible; }

    @Override
    public ViewType getViewType() {
        return ViewType.DAILY_UI;
    }

    @Override
    public String getUniqueKey() {
        return String.valueOf(getViewType().getCode()) + String.valueOf(this.date);
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
        if (DailyUiModel.class != o.getClass()) return false;
        DailyUiModel obj = (DailyUiModel) o;
        return this.getYear() == obj.getYear() &&
                this.getMonth() == obj.getMonth() &&
                this.getDate() == obj.getDate() &&
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
