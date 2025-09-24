package myclasses;

import java.util.List;

///
/// 一日の収支
///
public class DailyBop {
    // TODO 支出は支払日と購入日の二種類に分けて
    private int _amount;
    private final List<Income> _incomeList;
    private final List<Expenses> _expensesList;

    public DailyBop(List<Income> incomeList, List<Expenses> expensesList) {
        this._incomeList = incomeList;
        this._expensesList = expensesList;

        this._amount = 0;
        for (int i = 0; i < this._incomeList.size(); i++) {
            this._amount += Math.abs(this._incomeList.get(i).getAmount());
        }
        for (int i = 0; i < this._expensesList.size(); i++) {
            this._amount -= Math.abs(this._expensesList.get(i).getAmount());
        }
    }

    public int getAmount() {
        return this._amount;
    }

    public List<Income> getIncomeList() {
        return this._incomeList;
    }

    public List<Expenses> getExpensesList() {
        return this._expensesList;
    }
}
