package com.example.householdaccountbook.myclasses.calendarentity;

import java.util.Calendar;
import java.util.HashMap;

public class CalendarUiModel implements CalendarDisplayItem {
    private Calendar targetDate;
    private HashMap<Integer, Integer> dateData;
    private int maxAmount;

    public CalendarUiModel(Calendar targetDate, HashMap<Integer, Integer> dateData, int maxAmount) {
        this.targetDate = targetDate;
        this.dateData = dateData;
        this.maxAmount = maxAmount;
    }

    public Calendar getTargetDate() {
        return targetDate;
    }

    public HashMap<Integer, Integer> getDateData() {
        return dateData;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.CALENDAR_UI;
    }

    @Override
    public String getUniqueKey() {
        return targetDate.toString() + String.valueOf(getViewType().getCode());
    }
}
