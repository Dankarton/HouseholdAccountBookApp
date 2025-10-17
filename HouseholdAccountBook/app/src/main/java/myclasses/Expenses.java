package myclasses;

import android.content.ContentValues;

import com.example.householdaccountbook.MyOpenHelper;
import com.example.householdaccountbook.MyStdlib;

import java.util.Calendar;

public class Expenses extends BOP {
    private final int paymentMethodId;
    private final Calendar paymentDate;
    private final boolean isSameDay;
    public Expenses(Integer id, Calendar date, int amount, String memo, String category, int paymentMethodId, Calendar paymentDate) {
        super(id, date, amount, memo, category);
        this.paymentMethodId = paymentMethodId;
        this.paymentDate = paymentDate;
        this.isSameDay = MyStdlib.isSameDay(this.getDate(), this.paymentDate);
    }
    public static ContentValues convertContentValues(int _year, int _month, int _day, int _amount, String _memo, String _category, int _paymentMethodId, int pYear, int pMonth, int pDay) {
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
    @Override
    public ContentValues getInsertContentValues() {
        ContentValues values = new ContentValues();
        values.put(MyOpenHelper.COLUMN_YEAR, this.getYear());
        values.put(MyOpenHelper.COLUMN_MONTH, this.getMonth());
        values.put(MyOpenHelper.COLUMN_DAY, this.getDay());
        values.put(MyOpenHelper.COLUMN_AMOUNT, this.getAmount());
        values.put(MyOpenHelper.COLUMN_MEMO, this.getMemo());
        values.put(MyOpenHelper.COLUMN_CATEGORY, this.getCategory());
        values.put(MyOpenHelper.COLUMN_PAYMENT_METHOD_ID, this.paymentMethodId);
        values.put(MyOpenHelper.COLUMN_PAYMENT_YEAR, paymentDate.get(Calendar.YEAR));
        values.put(MyOpenHelper.COLUMN_PAYMENT_MONTH, paymentDate.get(Calendar.MONTH));
        values.put(MyOpenHelper.COLUMN_PAYMENT_DAY, paymentDate.get(Calendar.DATE));
        return values;
    }
    @Override
    public ContentValues getUpdateContentValues() {
        if (this.getId() == null) return null;
        ContentValues values = new ContentValues();
        values.put(MyOpenHelper.ID, this.getId());
        values.put(MyOpenHelper.COLUMN_YEAR, this.getYear());
        values.put(MyOpenHelper.COLUMN_MONTH, this.getMonth());
        values.put(MyOpenHelper.COLUMN_DAY, this.getDay());
        values.put(MyOpenHelper.COLUMN_AMOUNT, this.getAmount());
        values.put(MyOpenHelper.COLUMN_MEMO, this.getMemo());
        values.put(MyOpenHelper.COLUMN_CATEGORY, this.getCategory());
        values.put(MyOpenHelper.COLUMN_PAYMENT_METHOD_ID, this.paymentMethodId);
        values.put(MyOpenHelper.COLUMN_PAYMENT_YEAR, paymentDate.get(Calendar.YEAR));
        values.put(MyOpenHelper.COLUMN_PAYMENT_MONTH, paymentDate.get(Calendar.MONTH));
        values.put(MyOpenHelper.COLUMN_PAYMENT_DAY, paymentDate.get(Calendar.DATE));
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
}
