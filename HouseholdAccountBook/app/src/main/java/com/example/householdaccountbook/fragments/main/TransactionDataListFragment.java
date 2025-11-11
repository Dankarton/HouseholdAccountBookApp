package com.example.householdaccountbook.fragments.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.adapter.TransactionDateAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import myclasses.DailyBop;
import myclasses.Expenses;
import myclasses.Purchase;

public class TransactionDataListFragment extends Fragment {
    private Context context;
    RecyclerView dailyRecordRecyclerView;
    TextView monthTextView;
    TextView incomeAmountTextView;
    TextView purchaseAmountTextView;
    TextView totalAmountTextView;
    TextView paymentAmountTextView;
    Calendar currentDate;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_data_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = view.getContext();
        this.monthTextView = view.findViewById(R.id.month_text_view);
        this.dailyRecordRecyclerView = view.findViewById(R.id.transaction_list_recycler_view);
        this.incomeAmountTextView = view.findViewById(R.id.income_amount_text);
        this.purchaseAmountTextView = view.findViewById(R.id.purchase_amount_text);
        this.totalAmountTextView = view.findViewById(R.id.total_amount_text);
        this.paymentAmountTextView = view.findViewById(R.id.payment_amount_text);

        view.findViewById(R.id.month_up_button).setOnClickListener(view1 -> {
            currentDate.add(Calendar.MONTH, 1);
            List<DailyBop> newDataList = loadCurrentMonthDailyData(currentDate);
            updateAmountTextViews(newDataList);
            updateDailyListData(newDataList);
            updateMonthTextView();

        });
        view.findViewById(R.id.month_down_button).setOnClickListener(view2 -> {
            currentDate.add(Calendar.MONTH, -1);
            List<DailyBop> newDataList = loadCurrentMonthDailyData(currentDate);
            updateAmountTextViews(newDataList);
            updateDailyListData(newDataList);
            updateMonthTextView();
        });
        this.currentDate = Calendar.getInstance();
        List<DailyBop> dataList = loadCurrentMonthDailyData(this.currentDate);
        updateMonthTextView();
        updateAmountTextViews(dataList);
        updateDailyListData(dataList);

    }

    private void updateMonthTextView(){
        monthTextView.setText(
                MyStdlib.convertCalendarToString(
                        currentDate.get(Calendar.YEAR),
                        currentDate.get(Calendar.MONTH),
                        null,
                        null
                )
        );
    }
    @SuppressLint("NotifyDataSetChanged")
    private void updateAmountTextViews(List<DailyBop> dailyList) {

        int incomeAmount = 0;
        int purchaseAmount = 0;
        int totalAmount = 0;
        int paymentAmount = 0;
        for (DailyBop daily : dailyList) {
            incomeAmount += daily.getIncomeAmount();
            purchaseAmount += Math.abs(daily.getPurchaseAmount());
            paymentAmount += Math.abs(daily.getPaymentAmount());
        }
        totalAmount = incomeAmount - purchaseAmount;
        this.incomeAmountTextView.setText(incomeAmount + "円");
        this.purchaseAmountTextView.setText(purchaseAmount + "円");
        this.totalAmountTextView.setText(totalAmount + "円");
        this.paymentAmountTextView.setText(paymentAmount + "円");
    }
    private void updateDailyListData(List<DailyBop> dailyList) {
//        Log.d("TransactionDataListFragment.updateAmountTextView", "dailyList size: " + dailyList.size());
        if (dailyRecordRecyclerView.getLayoutManager() == null) {
            this.dailyRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        }
        this.dailyRecordRecyclerView.setAdapter(new TransactionDateAdapter(this.context, dailyList));
    }

    private List<DailyBop> loadCurrentMonthDailyData(Calendar date) {
        List<Expenses> expList = MyDbManager.getAll(Expenses.class);
        for (Expenses exp : expList) {
            Log.d("TransactionTest", "Expenses: " + exp.getYear() + "/" + exp.getMonth() + "/" + exp.getDay());
        }
        List<DailyBop> dailyBopList = new ArrayList<>();
        for (int i = 1; i <= date.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            DailyBop dailyBop = MyDbManager.getDailyData(date.get(Calendar.YEAR), date.get(Calendar.MONTH), i);
            if (dailyBop != null) {
                dailyBopList.add(dailyBop);
            }
        }
        return dailyBopList;
    }
}
