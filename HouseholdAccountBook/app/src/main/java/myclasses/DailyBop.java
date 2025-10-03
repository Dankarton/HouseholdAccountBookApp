package myclasses;

import android.os.Debug;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

///
/// 一日の収支
///
public class DailyBop {
    // TODO 支出は支払日と購入日の二種類に分けて
    private final int year;
    private final int month;
    private final int date;

    private int _incomeAmount;
    private int _purchaseAmount;    // 購入金額
    private int _paymentAmount;     // 支払金額
    private final List<Income> _incomeList;
    private final List<Expenses> _purchaseList;
    private final List<Expenses> _paymentList;

    public DailyBop(int year, int month, int date, List<Income> incomeList, List<Expenses> purchaseList, List<Expenses> paymentList) {
        this.year = year;
        this.month = month;
        this.date = date;
        this._incomeList = incomeList;
        this._purchaseList = purchaseList;
        this._paymentList = paymentList;

        this._purchaseAmount = 0;
        this._paymentAmount = 0;
        for (int i = 0; i < this._incomeList.size(); i++) {
            this._incomeAmount += Math.abs(this._incomeList.get(i).getAmount());
        }
        for (int i = 0; i < this._purchaseList.size(); i++) {
            this._purchaseAmount -= Math.abs(this._purchaseList.get(i).getAmount());
        }
        for (int i = 0; i < this._paymentList.size(); i++) {
            this._paymentAmount -= Math.abs(this._paymentList.get(i).getAmount());
        }
    }
    public int getYear() { return this.year; }
    public int getMonth() { return this.month; }
    public int getDate() { return this.date; }

    public int getIncomeAmount() { return this._incomeAmount; }
    public int getPurchaseAmount() {
        return this._purchaseAmount;
    }

    public int getPaymentAmount() { return this._paymentAmount; }

    public List<Income> getIncomeList() {
        return this._incomeList;
    }

    public List<Expenses> getPurchaseList() {
        return this._purchaseList;
    }

    public List<Expenses> getPaymentList() { return this._paymentList; }

    public List<BalanceOfPayments> getBopList() {
        List<BalanceOfPayments> bopList = new ArrayList<>();
        bopList.addAll(this._incomeList);
        bopList.addAll(this._paymentList);
        bopList.addAll(this._purchaseList);
        return bopList;
    }
}
