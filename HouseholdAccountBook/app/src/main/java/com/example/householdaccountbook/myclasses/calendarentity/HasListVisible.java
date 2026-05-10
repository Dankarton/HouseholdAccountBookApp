package com.example.householdaccountbook.myclasses.calendarentity;

import java.util.List;

public interface HasListVisible {
    public boolean isListVisible();
    public void setListVisible(boolean isListVisible);
    public List<CalendarDisplayItem> getChildItems();
}
