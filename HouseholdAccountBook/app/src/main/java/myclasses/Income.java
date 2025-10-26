package myclasses;

import android.content.ContentValues;

import com.example.householdaccountbook.MyOpenHelper;

import java.io.Serializable;
import java.util.Calendar;

public class Income extends BOP implements Serializable {

    public Income(int id, Calendar date, int amount, String memo, String category) {
        super(id, date, amount, memo, category);
    }

    public static ContentValues makeContentValues(int _year, int _month, int _day, int _amount, String _memo, String _category) {
        ContentValues values = new ContentValues();
        values.put(MyOpenHelper.COLUMN_YEAR, _year);
        values.put(MyOpenHelper.COLUMN_MONTH, _month);
        values.put(MyOpenHelper.COLUMN_DAY, _day);
        values.put(MyOpenHelper.COLUMN_AMOUNT, _amount);
        values.put(MyOpenHelper.COLUMN_MEMO, _memo);
        values.put(MyOpenHelper.COLUMN_CATEGORY, _category);
        return values;
    }
    @Override
    public String getDatabaseName() {
        return MyOpenHelper.INCOME_TABLE_NAME;
    }
    @Override
    public ContentValues getContentValues() {
        return Income.makeContentValues(
                this.getYear(),
                this.getMonth(),
                this.getDay(),
                this.getAmount(),
                this.getMemo(),
                this.getCategory()
        );
    }
}
