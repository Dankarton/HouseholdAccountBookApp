package com.example.householdaccountbook.module.calendarentity;

import java.util.List;

public interface HasListVisible {
    public boolean isListVisible();
    public boolean isListVisibleValid();
    public void setListVisible(boolean isListVisible);
    public void setListVisibleValid(boolean valid);
    public List<CalendarDisplayItem> getChildItems();
}
