package myclasses;

import android.content.ContentValues;

import com.example.householdaccountbook.MyOpenHelper;
import com.example.householdaccountbook.MyStdlib;

import java.io.Serializable;
import java.util.Calendar;

public class Expenses extends BOP implements Serializable {
    private final int paymentMethodId;
    private final Calendar paymentDate;
    private final boolean isSameDay;
    public Expenses(int id, Calendar date, int amount, String memo, String category, int paymentMethodId, Calendar paymentDate) {
        super(id, date, amount, memo, category);
        this.paymentMethodId = paymentMethodId;
        this.paymentDate = paymentDate;
        this.isSameDay = MyStdlib.isSameDay(this.getDate(), this.paymentDate);
    }
    public static ContentValues makeContentValues(int _year, int _month, int _day, int _amount, String _memo, String _category, int _paymentMethodId, int pYear, int pMonth, int pDay) {
        ContentValues values = new ContentValues();
        values.put(MyOpenHelper.COLUMN_YEAR, _year);
        values.put(MyOpenHelper.COLUMN_MONTH, _month);
        values.put(MyOpenHelper.COLUMN_DAY, _day);
        values.put(MyOpenHelper.COLUMN_AMOUNT, _amount);
        values.put(MyOpenHelper.COLUMN_MEMO, _memo);
        values.put(MyOpenHelper.COLUMN_CATEGORY, _category);
        values.put(MyOpenHelper.COLUMN_PAYMENT_METHOD_ID, _paymentMethodId);
        values.put(MyOpenHelper.COLUMN_PAYMENT_YEAR, pYear);
        values.put(MyOpenHelper.COLUMN_PAYMENT_MONTH, pMonth);
        values.put(MyOpenHelper.COLUMN_PAYMENT_DAY, pDay);
        return values;
    }

    public boolean isSameDay() {
        return this.isSameDay;
    }
    public int getPaymentMethodId() {
        return this.paymentMethodId;
    }
    public Calendar getPaymentDate() {
        return this.paymentDate;
    }
    @Override
    public String getDatabaseName() { return MyOpenHelper.EXPENSES_TABLE_NAME; }
    @Override
    public ContentValues getContentValues() {
        return Expenses.makeContentValues(
                this.getYear(),
                this.getMonth(),
                this.getDay(),
                this.getAmount(),
                this.getMemo(),
                this.getCategory(),
                this.getPaymentMethodId(),
                this.paymentDate.get(Calendar.YEAR),
                this.paymentDate.get(Calendar.MONTH) + 1,
                this.paymentDate.get(Calendar.DATE)
        );
    }
}
