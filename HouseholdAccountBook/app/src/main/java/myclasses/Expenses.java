package myclasses;

import android.content.ContentValues;

import com.example.householdaccountbook.MyOpenHelper;

import java.util.Calendar;

public class Expenses extends BOP {
    private final int paymentMethodId;
    private final Calendar paymentDate;
    public Expenses(int id, Calendar date, int amount, String memo, String category, int paymentMethodId, Calendar paymentDate) {
        super(id, date, amount, memo, category);
        this.paymentMethodId = paymentMethodId;
        this.paymentDate = paymentDate;
    }
    public static ContentValues convertContentValues(int _year, int _month, int _day, int _amount, String _memo, String _category, int _paymentMethodId, int pYear, int pMonth, int pDay) {
        ContentValues values = new ContentValues();
        values.put(MyOpenHelper.COLUMN_YEAR, _year);
        values.put("month", _month);
        values.put("day", _day);
        values.put("amount", _amount);
        values.put("memo", _memo);
        values.put("category", _category);
        values.put(MyOpenHelper.COLUMN_PAYMENT_METHOD_ID, _paymentMethodId);
        values.put(MyOpenHelper.COLUMN_PAYMENT_YEAR, pYear);
        values.put(MyOpenHelper.COLUMN_PAYMENT_MONTH, pMonth);
        values.put(MyOpenHelper.COLUMN_PAYMENT_DAY, pDay);
        return values;
    }

    public int getPaymentMethodId() {
        return this.paymentMethodId;
    }
    public Calendar getPaymentDate() {
        return this.paymentDate;
    }
}
