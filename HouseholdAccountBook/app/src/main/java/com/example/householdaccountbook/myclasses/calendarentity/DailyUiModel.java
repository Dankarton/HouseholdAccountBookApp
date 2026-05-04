package com.example.householdaccountbook.myclasses.calendarentity;

import java.util.List;

public class DailyUiModel implements CalendarDisplayItem {
    private final int year, month, date;
    private final int deltaAmount;

    private final List<CalendarDisplayItem> displayItems;

    public DailyUiModel(int year, int month, int date, int deltaAmount, List<CalendarDisplayItem> displayItems) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.deltaAmount = deltaAmount;
        this.displayItems = displayItems;
    }

    public int getYear() { return year; }
    public int getMonth() { return this.month; }
    public int getDate() { return this.date; }
    public int getDeltaAmount() { return this.deltaAmount; }
    public List<CalendarDisplayItem> getChildItems() { return this.displayItems; }

    @Override
    public ViewType getViewType() {
        return ViewType.DAILY_UI;
    }

    @Override
    public String getUniqueKey() {
        return String.valueOf(getViewType().getCode()) + String.valueOf(this.date);
    }
}
