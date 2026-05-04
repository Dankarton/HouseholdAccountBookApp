package com.example.householdaccountbook.myclasses.dbentity;

import com.example.householdaccountbook.myclasses.calendarentity.BopBaseUiModel;

import java.util.List;

public class DailyWalletUiModel {
    private final int year, month, date;
    private final String walletName;
    private final int deltaAmount;
    private final int totalAmount;

    private final List<BopBaseUiModel> displayItems;

    public DailyWalletUiModel(int year, int month, int date, String walletName, int deltaAmount, int totalAmount, List<BopBaseUiModel> displayItems) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.walletName = walletName;
        this.deltaAmount = deltaAmount;
        this.totalAmount = totalAmount;
        this.displayItems = displayItems;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }
    public String getWalletName() {
        return this.walletName;
    }

    public int getDeltaAmount() {
        return deltaAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public List<BopBaseUiModel> getDisplayItems() {
        return displayItems;
    }
}
