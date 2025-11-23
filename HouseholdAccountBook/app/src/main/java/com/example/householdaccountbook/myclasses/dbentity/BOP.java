package com.example.householdaccountbook.myclasses.dbentity;

import java.util.Calendar;

/**
 BalanceOfPaymentsクラス
 支出と収支の親クラス
 */
public abstract class BOP extends DatabaseEntity {
    private final Calendar date;
    private final int amount;
    private final String memo;
    private final long categoryId;

    BOP(Long id, Calendar date, int amount, String memo, long categoryId) {
        super(id);
        this.date = date;
        this.amount = amount;
        this.memo = memo;
        this.categoryId = categoryId;
    }

    public Calendar getDate() {
        return this.date;
    }
    public int getYear() {
        return this.date.get(Calendar.YEAR);
    }
    public int getMonth() {
        return this.date.get(Calendar.MONTH);
    }
    public int getDay() {
        return this.date.get(Calendar.DATE);
    }
    public int getAmount() {
        return amount;
    }
    public String getMemo() {
        return memo;
    }
    public long getCategoryId() { return categoryId; }
}
