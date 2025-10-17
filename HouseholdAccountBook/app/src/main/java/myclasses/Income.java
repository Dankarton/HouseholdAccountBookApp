package myclasses;

import android.content.ContentValues;

import com.example.householdaccountbook.MyOpenHelper;

import java.util.Calendar;

public class Income extends BOP {

    public Income(Integer id, Calendar date, int amount, String memo, String category) {
        super(id, date, amount, memo, category);
    }

    public static ContentValues convertContentValues(int _year, int _month, int _day, int _amount, String _memo, String _category) {
        ContentValues values = new ContentValues();
        values.put("year", _year);
        values.put("month", _month);
        values.put("day", _day);
        values.put("amount", _amount);
        values.put("memo", _memo);
        values.put("category", _category);
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
        return values;
    }
    @Override
    public ContentValues getUpdateContentValues() {
        if (null == this.getId()) return null;
        ContentValues values = new ContentValues();
        values.put(MyOpenHelper.ID, this.getId());
        values.put(MyOpenHelper.COLUMN_YEAR, this.getYear());
        values.put(MyOpenHelper.COLUMN_MONTH, this.getMonth());
        values.put(MyOpenHelper.COLUMN_DAY, this.getDay());
        values.put(MyOpenHelper.COLUMN_AMOUNT, this.getAmount());
        values.put(MyOpenHelper.COLUMN_MEMO, this.getMemo());
        values.put(MyOpenHelper.COLUMN_CATEGORY, this.getCategory());
        return values;
    }
}
