package myclasses;

import android.content.ContentValues;

import java.util.Calendar;

public class Purchase extends BOP {
    private final int paymentMethodId;
    private final boolean isSameDay;
    public Purchase(Integer id, Calendar date, int amount, String memo, int categoryId, int paymentMethodId, boolean isSameDay) {
        super(id, date, amount, memo, categoryId);
        this.paymentMethodId = paymentMethodId;
        this.isSameDay = isSameDay;
    }
    @Override
    public ContentValues getContentValues() {
        return new ContentValues(

        );
    }
}
