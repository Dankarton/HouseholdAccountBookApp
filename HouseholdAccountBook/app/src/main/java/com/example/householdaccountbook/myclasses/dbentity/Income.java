package com.example.householdaccountbook.myclasses.dbentity;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;
import com.example.householdaccountbook.db.MyDbManager;

import java.util.Calendar;

public class Income extends BOP implements HasCategory, HasWallet {
    private final long categoryId;
    private final long walletId;
    public Income() {
        super(null, Calendar.getInstance(), 0, "");
        this.categoryId = -1;
        this.walletId = -1;
    }
    public Income(Long id, Calendar date, int amount, String memo, long categoryId, long walletId) {
        super(id, date, amount, memo);
        this.categoryId = categoryId;
        this.walletId = walletId;
    }

    public long getCategoryId() { return this.categoryId; }
    public Class<IncomeCategory> getCategoryClass() { return IncomeCategory.class; }
    public long getWalletId() {
        return this.walletId;
    }

    public static ContentValues makeContentValues(int _year, int _month, int _day, int _amount, String _memo, long _categoryId, long _walletId) {
        ContentValues values = new ContentValues();
        values.put(MyDbContract.IncomeEntry.COLUMN_YEAR, _year);
        values.put(MyDbContract.IncomeEntry.COLUMN_MONTH, _month);
        values.put(MyDbContract.IncomeEntry.COLUMN_DAY, _day);
        values.put(MyDbContract.IncomeEntry.COLUMN_AMOUNT, _amount);
        values.put(MyDbContract.IncomeEntry.COLUMN_MEMO, _memo);
        values.put(MyDbContract.IncomeEntry.COLUMN_CATEGORY_ID, _categoryId);
        values.put(MyDbContract.IncomeEntry.COLUMN_WALLET_ID, _walletId);
        return values;
    }
    @Override
    public ContentValues getContentValues() {
        return Income.makeContentValues(
                this.getYear(),
                this.getMonth(),
                this.getDay(),
                this.getAmount(),
                this.getMemo(),
                this.getCategoryId(),
                this.walletId
        );
    }
    @Override
    public void onAfterInsert(long newId) {
        super.onAfterInsert(newId);
//        Log.d("Income", "on after insert: " + this.getAmount());
        MyDbManager.updateMonthlyBalanceDelta(this.getDate(), this.walletId, Math.abs(this.getAmount()));
    }
    @Override
    public void onAfterUpdate(DatabaseEntity before) {
//        Log.d("Income", "on after update --- before date: " + ((Income) before).getDate().get(Calendar.YEAR) + "-" + ((Income) before).getDate().get(Calendar.MONTH) + "-" + ((Income) before).getDate().get(Calendar.DAY_OF_MONTH) + ", " + ((Income) before).getAmount());
        MyDbManager.updateMonthlyBalanceDelta(((Income) before).getDate(), this.walletId, -1 * Math.abs(((Income) before).getAmount()));
//        Log.d("Income", "on after update ---   this date: " + this.getDate().get(Calendar.YEAR) + "-" + this.getDate().get(Calendar.MONTH) + "-" + this.getDate().get(Calendar.DAY_OF_MONTH) + ", " + this.getAmount());
        MyDbManager.updateMonthlyBalanceDelta(this.getDate(), this.walletId, Math.abs(this.getAmount()));
    }
    @Override
    public void onAfterDelete() {
//        Log.d("Income", "on after delete");
        MyDbManager.updateMonthlyBalanceDelta(this.getDate(), this.walletId, -1 * Math.abs(this.getAmount()));
    }
}
