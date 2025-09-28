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
        Log.d("TransactionDataListFragment", "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        monthTextView = view.findViewById(R.id.month_text_view);
        dailyRecordRecyclerView = view.findViewById(R.id.transaction_list_recycler_view);
        this.transactionDateAdapter = new TransactionDateAdapter();
        this.transactionDateAdapter.setData(new ArrayList<>());
        this.dailyRecordRecyclerView.setAdapter(this.transactionDateAdapter);
        this.dailyRecordRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        setMonthUpButtonEvent(view);
        setMonthDownButtonEvent(view);
        currentDate = Calendar.getInstance();
        updateMonthTextView();
        updateDailyData();
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
        transactionDateAdapter.setData(loadCurrentMonthDailyData(this.currentDate));
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
