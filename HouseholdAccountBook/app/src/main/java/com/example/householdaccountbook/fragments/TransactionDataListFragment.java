package com.example.householdaccountbook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.MyDbManager;
import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;

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
        dailyRecordRecyclerView = view.findViewById(R.id.transaction_list_recycler_view);
        currentDate = Calendar.getInstance();

    }
    private void setMonthUpButtonEvent(View view) {
        ImageButton monthUpButton = view.findViewById(R.id.month_up_button);
        monthUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                  currentDate.add(Calendar.MONTH, 1);
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
    private List<DailyBop> loadCurrentMonthDailyData(Calendar date) {
        ArrayList<Income> incomeList = MyDbManager.getIncomeDataByDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), null);
        ArrayList<Expenses> expensesList = MyDbManager.getExpensesByPurchaseOrPaymentDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), null);
        ArrayList<Expenses> purchaseList = new ArrayList<>();
        ArrayList<Expenses> paymentList = new ArrayList<>();
        // TODO 購入日と支払日で支出のグループを分ける処理．たぶんExpensesクラスに現在の日付と購入日(もしくは支払日)が一致するかどうか判定するメソッドがあった方がいい．
        for (int i = 0; i < expensesList.size(); i++) {
            if (expensesList.get(i).isSameDay()) {

            }
        }
    }
}
