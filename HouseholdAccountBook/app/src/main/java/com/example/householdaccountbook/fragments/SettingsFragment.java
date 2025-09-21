package com.example.householdaccountbook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.householdaccountbook.customviews.ExpensesSettingsCustomView;
import com.example.householdaccountbook.customviews.IncomeSettingsCustomView;
import com.example.householdaccountbook.MyDbManager;
import com.example.householdaccountbook.R;

import java.util.ArrayList;

import myclasses.Expenses;
import myclasses.Income;

public class SettingsFragment extends Fragment {
    LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        linearLayout = view.findViewById(R.id.expenses_list_linearlayout);
        ArrayList<Expenses> expensesList = MyDbManager.getAllExpensesData();
        ArrayList<Income> incomeList = MyDbManager.getAllIncomeData();
        int expPointer = 0;
        int incPointer = 0;
        for (int i = 0; i < expensesList.size() + incomeList.size(); i++){
            if (incPointer >= incomeList.size()){
                ExpensesSettingsCustomView expView = new ExpensesSettingsCustomView(view.getContext());
                expView.setData(expensesList.get(expPointer));
                linearLayout.addView(expView);
                expPointer++;
                continue;
            }
            if (expPointer >= expensesList.size()) {
                IncomeSettingsCustomView incView = new IncomeSettingsCustomView(view.getContext());
                incView.setData(incomeList.get(incPointer));
                linearLayout.addView(incView);
                incPointer++;
                continue;
            }
            Income incTmp = incomeList.get(incPointer);
            int incDateNum = incTmp.getYear() * 10000 + incTmp.getMonth() * 100 + incTmp.getDay();
            Expenses expTmp = expensesList.get(expPointer);
            int expDateNum = expTmp.getYear() * 10000 + expTmp.getMonth() * 100 + expTmp.getDay();
            if (expDateNum < incDateNum){
                ExpensesSettingsCustomView expView = new ExpensesSettingsCustomView(view.getContext());
                expView.setData(expensesList.get(expPointer));
                linearLayout.addView(expView);
                expPointer++;
            }
            else {
                IncomeSettingsCustomView incView = new IncomeSettingsCustomView(view.getContext());
                incView.setData(incomeList.get(incPointer));
                linearLayout.addView(incView);
                incPointer++;
            }

        }
    }
}