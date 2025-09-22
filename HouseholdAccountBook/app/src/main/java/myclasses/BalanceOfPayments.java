package myclasses;

import java.util.Calendar;

public interface BalanceOfPayments {
    public int getId();

    public Calendar getDate();

    public int getYear();

    public int getMonth();

    public int getDay();

    public int getAmount();

    public String getMemo();

    public String getCategory();
}
