package com.example.householdaccountbook.myclasses.dbentity;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;
import com.example.householdaccountbook.db.MyDbManager;

import java.util.Calendar;

public class Expenses extends BOP implements HasCategory, HasWallet {
    private final long purchaseId;
    private final long paymentMethodId;
    private final long walletId;
    private final long categoryId;

    public Expenses(Long id, Calendar date, int amount, String memo, long categoryId, long paymentMethodId, long purchaseId, long walletId) {
        super(id, date, amount, memo);
        this.categoryId = categoryId;
        this.paymentMethodId = paymentMethodId;
        this.purchaseId = purchaseId;
        this.walletId = walletId;
    }
    public long getCategoryId() { return this.categoryId; }
    public Class<PurchaseCategory> getCategoryClass() { return PurchaseCategory.class; }

    public long getWalletId() { return this.walletId; }

    public long getPaymentMethodId() {
        return this.paymentMethodId;
    }

    public long getPurchaseId() {
        return this.purchaseId;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MyDbContract.ExpensesEntry.COLUMN_YEAR, this.getYear());
        values.put(MyDbContract.ExpensesEntry.COLUMN_MONTH, this.getMonth());
        values.put(MyDbContract.ExpensesEntry.COLUMN_DAY, this.getDay());
        values.put(MyDbContract.ExpensesEntry.COLUMN_AMOUNT, this.getAmount());
        values.put(MyDbContract.ExpensesEntry.COLUMN_MEMO, this.getMemo());
        values.put(MyDbContract.ExpensesEntry.COLUMN_CATEGORY_ID, this.getCategoryId());
        values.put(MyDbContract.ExpensesEntry.COLUMN_PAYMENT_METHOD_ID, this.paymentMethodId);
        values.put(MyDbContract.ExpensesEntry.COLUMN_PURCHASE_ID, this.purchaseId);
        values.put(MyDbContract.ExpensesEntry.COLUMN_WALLET_ID, this.walletId);
        return values;
    }

    @Override
    public void onAfterInsert(long newId) {
        super.onAfterInsert(newId);
        MyDbManager.updateMonthlyBalanceDelta(this.getDate(), this.walletId, -1 * Math.abs(this.getAmount()));
    }

    @Override
    public void onAfterUpdate(DatabaseEntity before) {
        MyDbManager.updateMonthlyBalanceDelta(((Expenses) before).getDate(), this.walletId, Math.abs(((Expenses) before).getAmount()));
        MyDbManager.updateMonthlyBalanceDelta(this.getDate(), this.walletId, -1 * Math.abs(this.getAmount()));
    }
    @Override
    public void onAfterDelete() {
        MyDbManager.updateMonthlyBalanceDelta(this.getDate(), this.walletId, Math.abs(this.getAmount()));
    }
}
