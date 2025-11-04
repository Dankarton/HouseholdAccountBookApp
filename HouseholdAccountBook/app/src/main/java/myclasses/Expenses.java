package myclasses;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;
import com.example.householdaccountbook.MyStdlib;

import java.util.Calendar;

public class Expenses extends BOP {
    private final int purchaseId;

    public Expenses(Integer id, Calendar date, int amount, String memo, int purchaseId) {
        super(id, date, amount, memo);
        this.purchaseId = purchaseId;
    }

    public int getPurchaseId() {
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
        values.put(MyDbContract.ExpensesEntry.COLUMN_PURCHASE_ID, this.purchaseId);
        return values;
    }
}
