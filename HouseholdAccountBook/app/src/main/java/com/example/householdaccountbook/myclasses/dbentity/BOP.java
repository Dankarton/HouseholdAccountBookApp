package com.example.householdaccountbook.myclasses.dbentity;

import java.util.Calendar;

/**
 BalanceOfPaymentsクラス
 支出と収支の親クラス
 */
public abstract class BOP extends DatabaseEntity implements HasDate {
    private final Calendar date;
    private final int amount;
    private final String memo;

    BOP(Long id, Calendar date, int amount, String memo) {
        super(id, false);
        this.date = date;
        this.amount = amount;
        this.memo = memo;
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
    /**
     * 日付をYYYYMMDDの形のint型に変換する関数。HashMapのKeyとかで使う用。
     * @param YY Year
     * @param MM Month
     * @param DD Date
     * @return int
     */
    public static int getDateInteger(int YY, int MM, int DD) {
        return YY * 10000 + MM * 100 + DD;
    }
}
