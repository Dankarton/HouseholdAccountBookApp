package com.example.householdaccountbook.myclasses;

import com.example.householdaccountbook.myclasses.dbentity.BOP;
import com.example.householdaccountbook.myclasses.dbentity.Expenses;
import com.example.householdaccountbook.myclasses.dbentity.Income;
import com.example.householdaccountbook.myclasses.dbentity.Purchase;

import java.util.ArrayList;
import java.util.List;

///
/// 一日の収支
///
public class DailyBop {
    private final int year;
    private final int month;
    private final int date;

    private int _incomeAmount;
    private int _purchaseAmount;    // 購入金額
    private int _paymentAmount;     // 支払金額
    private int _nextMonthPaymentAmount; // 翌月以降の支払金額
    private final List<Income> _incomeList;
    private final List<Purchase> _purchaseList;
    private final List<Expenses> _paymentList;
    private final List<BOP> _adapterDataList;

    public DailyBop(int year, int month, int date, List<Income> incomeList, List<Purchase> purchaseList, List<Expenses> paymentList) {
        this.year = year;
        this.month = month;
        this.date = date;
        this._incomeList = incomeList;
        this._purchaseList = purchaseList;
        this._paymentList = paymentList;

        this._purchaseAmount = 0;
        this._paymentAmount = 0;
        this._nextMonthPaymentAmount = 0;
        this._adapterDataList = new ArrayList<>();
        for (Income income : this._incomeList) {
            this._adapterDataList.add(income);
            this._incomeAmount += Math.abs(income.getAmount());
        }
        for (Purchase purchase : this._purchaseList) {
            // 購入日と支払日が同じ場合，ダプって表示されるのを防ぐためリストには追加しない
            // TODO 購入日、支払日は別画面で表示するようにする
            this._purchaseAmount += Math.abs(purchase.getAmount());
        }
        for (Expenses expenses : this._paymentList) {
            this._adapterDataList.add(expenses);
            this._paymentAmount += Math.abs(expenses.getAmount());
        }
    }
    public int getYear() { return this.year; }
    public int getMonth() { return this.month; }
    public int getDate() { return this.date; }

    public int getIncomeAmount() { return this._incomeAmount; }
    public int getPurchaseAmount() {
        return this._purchaseAmount;
    }
    public int get_nextMonthPaymentAmount() { return this._nextMonthPaymentAmount; }
    public int getPaymentAmount() { return this._paymentAmount; }
    public int getTotalAmount() { return this._incomeAmount + this._paymentAmount; }
    public List<Income> getIncomeList() { return this._incomeList; }
    public List<Purchase> getPurchaseList() { return this._purchaseList; }
    public List<Expenses> getPaymentList() { return this._paymentList; }
    public List<BOP> getAdapterDataList() { return this._adapterDataList; }
}
