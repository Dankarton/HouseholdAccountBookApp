package myclasses;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;

import java.util.Calendar;

public class Purchase extends BOP {
    private final int categoryId;
    private final int paymentMethodId;
    private final boolean isSameDay;
    public Purchase(Integer id, Calendar date, int amount, String memo, int categoryId, int paymentMethodId, boolean isSameDay) {
        super(id, date, amount, memo);
        this.categoryId = categoryId;
        this.paymentMethodId = paymentMethodId;
        this.isSameDay = isSameDay;
    }
    public int getCategoryId() { return this.categoryId; }
    public int getPaymentMethodId() { return this.paymentMethodId; }
    public boolean isSameDay() { return this.isSameDay; }
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MyDbContract.PurchaseEntry.COLUMN_YEAR, this.getYear());
        values.put(MyDbContract.PurchaseEntry.COLUMN_MONTH, this.getMonth());
        values.put(MyDbContract.PurchaseEntry.COLUMN_DAY, this.getDay());
        values.put(MyDbContract.PurchaseEntry.COLUMN_AMOUNT, this.getAmount());
        values.put(MyDbContract.PurchaseEntry.COLUMN_MEMO, this.getMemo());
        values.put(MyDbContract.PurchaseEntry.COLUMN_CATEGORY_ID, this.categoryId);
        values.put(MyDbContract.PurchaseEntry.COLUMN_PAYMENT_METHOD_ID, this.paymentMethodId);
        values.put(MyDbContract.PurchaseEntry.COLUMN_IS_SAME_DAY, this.isSameDay);
        return values;
    }
}
