package myclasses;

import java.util.Calendar;

/**
 BalanceOfPaymentsクラス
 支出と収支の親クラス
 */
public abstract class BOP implements DatabaseEntity {
    private final int id;   // データベース用ID
    private final Calendar date;
    private final int amount;
    private final String memo;
    private final String category;

    BOP(int id, Calendar date, int amount, String memo, String category) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.memo = memo;
        this.category = category;
    }
    @Override
    public Integer getId() {
        return id;
    }

    public Calendar getDate() {
        return this.date;
    }
    public int getYear() {
        return this.date.get(Calendar.YEAR);
    }
    public int getMonth() {
        return this.date.get(Calendar.MONTH) + 1;
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
    public String getCategory() {
        return category;
    }
}
