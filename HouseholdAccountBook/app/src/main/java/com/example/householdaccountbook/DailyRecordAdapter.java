package com.example.householdaccountbook;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.customviews.ExpensesSettingsCustomView;
import com.example.householdaccountbook.customviews.IncomeSettingsCustomView;

import java.util.List;

import myclasses.BalanceOfPayments;
import myclasses.DailyBop;
import myclasses.Expenses;
import myclasses.Income;

public class DailyRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private enum ViewType {
        INCOME,
        EXPENSE
    }

    private List<BalanceOfPayments> itemList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_view_bop_settings, parent, false);
        if (viewType == ViewType.INCOME.ordinal()) {
            Log.d("DailyRecordAdapter", String.valueOf(ViewType.INCOME.ordinal()));
            return new IncomeViewHolder(view);
        }
        else {
            Log.d("DailyRecordAdapter", String.valueOf(ViewType.EXPENSE.ordinal()));
            return new ExpensesViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof IncomeViewHolder) {
            Log.d("DailyRecordAdapter", "income");
            ((IncomeViewHolder) holder).bind((Income) itemList.get(position));
        }
        else if (holder instanceof ExpensesViewHolder) {
            Log.d("DailyRecordAdapter", "expenses");
            ((ExpensesViewHolder) holder).bind((Expenses) itemList.get(position));
        }
    }
    public void setData(List<BalanceOfPayments> itemList) {
        Log.d("DailyRecordAdapter", "setData");
        this.itemList = itemList;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    @Override
    public int getItemViewType(int position) {
        BalanceOfPayments obj = this.itemList.get(position);
        if (obj instanceof Income) {
            return ViewType.INCOME.ordinal();
        }
        else if (obj instanceof Expenses) {
            return ViewType.EXPENSE.ordinal();
        }
        return -1;
    }
    static class IncomeViewHolder extends RecyclerView.ViewHolder {
        IncomeSettingsCustomView incomeView;
        public IncomeViewHolder(@NonNull View view) {
            super(view);
            incomeView = new IncomeSettingsCustomView(view.getContext());
        }
        public void bind(Income income) {
            incomeView.setData(income);
        }
    }
    static class ExpensesViewHolder extends RecyclerView.ViewHolder {
        ExpensesSettingsCustomView expensesView;

        public ExpensesViewHolder(@NonNull View view) {
            super(view);
            expensesView = new ExpensesSettingsCustomView(view.getContext());
        }
        public void bind(Expenses expenses) {
            expensesView.setData(expenses);
        }
    }
}
