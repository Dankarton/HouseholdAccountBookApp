package myclasses;

import java.util.Calendar;

/**
 BalanceOfPaymentsクラス
 支出と収支の親クラス
 */
public class BOP implements BalanceOfPayments {
    private final int id;
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

    public int getId() {
        return id;
    }

    public Calendar getDate() {
        return this.date;
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
