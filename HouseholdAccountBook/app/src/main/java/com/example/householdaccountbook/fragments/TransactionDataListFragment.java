package com.example.householdaccountbook.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.DailyRecordAdapter;
import com.example.householdaccountbook.MyDbManager;
import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.TransactionDateAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import myclasses.DailyBop;
import myclasses.Expenses;
import myclasses.Income;

public class TransactionDataListFragment extends Fragment {
    RecyclerView dailyRecordRecyclerView;
    TextView monthTextView;
    TextView incomeAmountTextView;
    TextView purchaseAmountTextView;
    TextView totalAmountTextView;
    TextView paymentAmountTextView;
    Calendar currentDate;
    private TransactionDateAdapter transactionDateAdapter;
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
        Log.d("TransactionDataListFragment", "onViewCreated start");
        super.onViewCreated(view, savedInstanceState);
        this.monthTextView = view.findViewById(R.id.month_text_view);
        this.dailyRecordRecyclerView = view.findViewById(R.id.transaction_list_recycler_view);
        this.incomeAmountTextView = view.findViewById(R.id.income_amount_text);
        this.purchaseAmountTextView = view.findViewById(R.id.purchase_amount_text);
        this.totalAmountTextView = view.findViewById(R.id.total_amount_text);
        this.paymentAmountTextView = view.findViewById(R.id.payment_amount_text);
        this.transactionDateAdapter = new TransactionDateAdapter();
        this.transactionDateAdapter.setData(new ArrayList<>());
        this.transactionDateAdapter.notifyDataSetChanged();
        this.dailyRecordRecyclerView.setAdapter(this.transactionDateAdapter);
        Log.d("TransactionDataListFragment", "Adapter set to inner RecyclerView: " + transactionDateAdapter);
        this.dailyRecordRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        Log.d("TransactionDataListFragment", "Setting adapter for RecyclerView id=" + dailyRecordRecyclerView.getId());
        setMonthUpButtonEvent(view);
        setMonthDownButtonEvent(view);
        currentDate = Calendar.getInstance();
        updateMonthTextView();
        updateDailyData();
        Log.d("TransactionDataListFragment", "onViewCreated end");
    }
    private void setMonthUpButtonEvent(View view) {
        ImageButton monthUpButton = view.findViewById(R.id.month_up_button);
        monthUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                  currentDate.add(Calendar.MONTH, 1);
                  updateDailyData();
                  updateMonthTextView();

            }
        });
    }
    private void setMonthDownButtonEvent(View view) {
        ImageButton monthDownButton = view.findViewById(R.id.month_down_button);
        monthDownButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                currentDate.add(Calendar.MONTH, -1);
                updateDailyData();
                updateMonthTextView();
            }
        });
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
    private void updateDailyData() {
        List<DailyBop> bopList = loadCurrentMonthDailyData(this.currentDate);
        int incomeAmount = 0;
        int purchaseAmount = 0;
        int totalAmount = 0;
        int paymentAmount = 0;
        for (int i = 0; i < bopList.size(); i++) {
            incomeAmount += bopList.get(i).getIncomeAmount();
            purchaseAmount += Math.abs(bopList.get(i).getPurchaseAmount());
            paymentAmount += Math.abs(bopList.get(i).getPaymentAmount());
        }
        totalAmount = incomeAmount - purchaseAmount;
        this.incomeAmountTextView.setText(incomeAmount + "円");
        this.purchaseAmountTextView.setText(purchaseAmount + "円");
        this.totalAmountTextView.setText(totalAmount + "円");
        this.paymentAmountTextView.setText(paymentAmount + "円");
        transactionDateAdapter.setData(bopList);
        transactionDateAdapter.notifyDataSetChanged();
    }

    private List<DailyBop> loadCurrentMonthDailyData(Calendar date) {
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
