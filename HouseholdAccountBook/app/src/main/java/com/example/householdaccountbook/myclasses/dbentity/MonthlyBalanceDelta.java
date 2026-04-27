package com.example.householdaccountbook.myclasses.dbentity;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;

import java.util.Calendar;

public class MonthlyBalanceDelta extends DatabaseEntity implements HasWallet {
    long walletId;
    int yearMonthKey;
    int deltaAmount;

    public MonthlyBalanceDelta(Long id, long walletId, int yearMonthKey, int deltaAmount) {
        super(id, false);
        this.walletId = walletId;
        this.yearMonthKey = yearMonthKey;
        this.deltaAmount = deltaAmount;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_WALLET_ID, this.walletId);
        values.put(MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_YEAR_MONTH_KEY, this.yearMonthKey);
        values.put(MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_DELTA_AMOUNT, this.deltaAmount);
        return values;
    }
    public long getWalletId() { return  this.walletId; }
    
    public int getYearMonthKey() { return this.yearMonthKey; }

    public int getDeltaAmount() { return this.deltaAmount; }

    public void setDeltaAmount(int amount) { this.deltaAmount = amount; }

    public static int makeYearMonthKey(Calendar date) {
        return date.get(Calendar.YEAR) * 100 + date.get(Calendar.MONTH);
    }
}
