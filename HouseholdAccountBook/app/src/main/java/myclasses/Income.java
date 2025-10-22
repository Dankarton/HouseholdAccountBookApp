package myclasses;

import android.content.ContentValues;

import java.io.Serializable;
import java.util.Calendar;

public class Income extends BOP implements Serializable {

    public Income(int id, Calendar date, int amount, String memo, String category) {
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
}
