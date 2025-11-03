package myclasses;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;

import java.util.Calendar;

public class Income extends BOP {

    public Income(Integer id, Calendar date, int amount, String memo, int categoryId) {
        super(id, date, amount, memo, categoryId);
    }

    public static ContentValues makeContentValues(int _year, int _month, int _day, int _amount, String _memo, int _categoryId) {
        ContentValues values = new ContentValues();
        values.put(MyDbContract.IncomeEntry.COLUMN_YEAR, _year);
        values.put(MyDbContract.IncomeEntry.COLUMN_MONTH, _month);
        values.put(MyDbContract.IncomeEntry.COLUMN_DAY, _day);
        values.put(MyDbContract.IncomeEntry.COLUMN_AMOUNT, _amount);
        values.put(MyDbContract.IncomeEntry.COLUMN_MEMO, _memo);
        values.put(MyDbContract.IncomeEntry.COLUMN_CATEGORY_ID, _categoryId);
        return values;
    }
    @Override
    public ContentValues getContentValues() {
        return Income.makeContentValues(
                this.getYear(),
                this.getMonth(),
                this.getDay(),
                this.getAmount(),
                this.getMemo(),
                this.getCategoryId()
        );
    }
}
