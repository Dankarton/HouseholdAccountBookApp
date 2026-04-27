package com.example.householdaccountbook.myclasses.dbentity;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;

import java.util.Calendar;

public class MoneyMovement extends BOP {

    private final long fromWalletId;
    private final long toWalletId;

    public MoneyMovement() {
        super(null, Calendar.getInstance(), 0, "");
        this.fromWalletId = -1;
        this.toWalletId = -1;
    }
    public MoneyMovement(Long id, Calendar date, int amount, String memo, long fromWalletId, long toWalletId) {
        super(id, date, amount, memo);
        this.fromWalletId = fromWalletId;
        this.toWalletId = toWalletId;
    }

    public long getFromWalletId() { return this.fromWalletId; }
    public long getToWalletId() { return this.toWalletId; }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MyDbContract.MoneyMovementsEntry.COLUMN_YEAR, this.getYear());
        values.put(MyDbContract.MoneyMovementsEntry.COLUMN_MONTH, this.getMonth());
        values.put(MyDbContract.MoneyMovementsEntry.COLUMN_DAY, this.getDay());
        values.put(MyDbContract.MoneyMovementsEntry.COLUMN_AMOUNT, this.getAmount());
        values.put(MyDbContract.MoneyMovementsEntry.COLUMN_MEMO, this.getMemo());
        return values;
    }
}
