package com.example.householdaccountbook.myclasses.dbentity;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;
import com.example.householdaccountbook.db.MyDbManager;

import java.util.ArrayList;
import java.util.Calendar;

public class Purchase extends BOP {
    private final long paymentMethodId;

    public Purchase(Long id, Calendar date, int amount, String memo, long categoryId, long paymentMethodId) {
        super(id, date, amount, memo, categoryId);
        this.paymentMethodId = paymentMethodId;
    }

    public long getPaymentMethodId() {
        return this.paymentMethodId;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MyDbContract.PurchaseEntry.COLUMN_YEAR, this.getYear());
        values.put(MyDbContract.PurchaseEntry.COLUMN_MONTH, this.getMonth());
        values.put(MyDbContract.PurchaseEntry.COLUMN_DAY, this.getDay());
        values.put(MyDbContract.PurchaseEntry.COLUMN_AMOUNT, this.getAmount());
        values.put(MyDbContract.PurchaseEntry.COLUMN_MEMO, this.getMemo());
        values.put(MyDbContract.PurchaseEntry.COLUMN_CATEGORY_ID, this.getCategoryId());
        values.put(MyDbContract.PurchaseEntry.COLUMN_PAYMENT_METHOD_ID, this.paymentMethodId);
        return values;
    }
    @Override
    public void onAfterInsert(long newId) {
        super.onAfterInsert(newId);
        ArrayList<Expenses> newExpList = MyDbManager.getDataById(PaymentMethod.class, this.paymentMethodId).makeExpenses(this);
        for (Expenses exp: newExpList) {
            MyDbManager.setDataSafely(exp);
        }
    }
    @Override
    public void onAfterUpdate(DatabaseEntity before) {
        // 自身に連なっていたExpensesを全て削除
        ArrayList<Expenses> oldExpList = MyDbManager.getChildExpensesList(this);
        for (Expenses exp : oldExpList) {
            MyDbManager.deleteDataSafely(exp);
        }
        // 更新されたデータを基に作成されたExpensesを全て挿入
        ArrayList<Expenses> newExpList = MyDbManager.getDataById(PaymentMethod.class, this.paymentMethodId).makeExpenses(this);
        for(Expenses exp: newExpList) {
            MyDbManager.setDataSafely(exp);
        }
    }

    @Override
    public void onAfterDelete() {
        // 自身に連なっていたExpensesを全て削除
        ArrayList<Expenses> oldExpList = MyDbManager.getChildExpensesList(this);
        for (Expenses exp : oldExpList) {
            MyDbManager.deleteDataSafely(exp);
        }
    }
}
