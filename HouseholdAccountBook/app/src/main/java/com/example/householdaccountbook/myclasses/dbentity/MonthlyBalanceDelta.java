package com.example.householdaccountbook.myclasses.dbentity;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;

import java.util.Calendar;

public class MonthlyBalanceDelta extends DatabaseEntity {

    int yearMonthKey;
    int deltaAmount;

    public MonthlyBalanceDelta(Long id, int yearMonthKey, int deltaAmount) {
        super(id);
        this.yearMonthKey = yearMonthKey;
        this.deltaAmount = deltaAmount;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_YEAR_MONTH_KEY, this.yearMonthKey);
        values.put(MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_DELTA_AMOUNT, this.deltaAmount);
        return values;
    }

    public int getYearMonthKey() { return this.yearMonthKey; }

    public int getDeltaAmount() { return this.deltaAmount; }

    public void setDeltaAmount(int amount) { this.deltaAmount = amount; }

    public static int makeYearMonthKey(Calendar date) {
        return date.get(Calendar.YEAR) * 100 + date.get(Calendar.MONTH);
    }
}
