package com.example.householdaccountbook.myclasses;

import java.util.List;

public class DailyUiModel {
    private final int year, month, date;

    private final int totalIncome;
    private final int totalExpense;

    private final List<BopBaseUiModel> displayItems;

    public DailyUiModel(int year, int month, int date, int totalIncome, int totalExpense, List<BopBaseUiModel> displayItems) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.displayItems = displayItems;
    }

    public int getYear() { return year; }
    public int getMonth() { return this.month; }
    public int getDate() { return this.date; }
    public int getTotalIncome() { return this.totalIncome; }
    public int getTotalExpense() { return this.totalExpense; }
    public List<BopBaseUiModel> getChildItems() { return this.displayItems; }
}
