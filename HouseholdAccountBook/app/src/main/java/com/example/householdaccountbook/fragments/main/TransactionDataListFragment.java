package com.example.householdaccountbook.fragments.main;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.activities.settings.edit.SettingEditIncomeActivity;
import com.example.householdaccountbook.activities.settings.edit.SettingEditPurchaseActivity;
import com.example.householdaccountbook.customviews.CalendarCustomView;
import com.example.householdaccountbook.customviews.item.MonthlySummaryCustomView;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.adapter.TransactionDateAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.householdaccountbook.myclasses.dbentity.BOP;
import com.example.householdaccountbook.myclasses.DailyBop;
import com.example.householdaccountbook.myclasses.dbentity.Expenses;
import com.example.householdaccountbook.myclasses.dbentity.Income;
import com.example.householdaccountbook.myclasses.dbentity.MonthlyBalanceDelta;
import com.example.householdaccountbook.myclasses.dbentity.Purchase;

public class TransactionDataListFragment extends Fragment {
    private Context context;
    RecyclerView dailyRecordRecyclerView;
    TextView monthTextView;
    MonthlySummaryCustomView summaryView;
    CalendarCustomView calendarView;
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
        this.calendarView = view.findViewById(R.id.calendar_view);
        this.summaryView = view.findViewById(R.id.monthly_summary_view);

        view.findViewById(R.id.month_up_button).setOnClickListener(view1 -> {
            currentDate.add(Calendar.MONTH, 1);
            updateFragment();

        });
        view.findViewById(R.id.month_down_button).setOnClickListener(view2 -> {
            currentDate.add(Calendar.MONTH, -1);
            updateFragment();
        });
        this.currentDate = Calendar.getInstance();
        updateFragment();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (this.currentDate == null) {
            this.currentDate = Calendar.getInstance();
        }
        updateFragment();
    }

    /**
     * 更新
     */
    private void updateFragment() {
        List<DailyBop> dataList = loadCurrentMonthDailyData(this.currentDate);
        MonthlyBalanceDelta balanceDelta = MyDbManager.getLatestMonthlyDeltaUpTo(this.currentDate);
        this.calendarView.bind(this.currentDate, new ArrayList<>());
        int deltaAmount = 0;
        int initialBalanceAmount = 0;
        if (balanceDelta != null) {
            deltaAmount = balanceDelta.getDeltaAmount();
        }
        SharedPreferences preferences = this.context.getSharedPreferences("app_prefs", MODE_PRIVATE);
        if (preferences.contains("initial_balance")) {
            initialBalanceAmount = preferences.getInt("initial_balance", 0);
        }
        updateMonthTextView();
        updateAmountTextViews(dataList, deltaAmount + initialBalanceAmount);
        updateDailyListData(dataList);
    }

    /**
     * 日付を表示するTextViewを更新
     */
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

    /**
     * ひと月の全体結果を更新
     * @param dailyList
     */
    private void updateAmountTextViews(List<DailyBop> dailyList, int balanceAmount) {

        int incomeAmount = 0;
        int purchaseAmount = 0;
        int paymentAmount = 0;
        int nextPaymentAmount = 0;
        for (DailyBop daily : dailyList) {
            incomeAmount += daily.getIncomeAmount();
            purchaseAmount += Math.abs(daily.getPurchaseAmount());
            paymentAmount += Math.abs(daily.getPaymentAmount());
            nextPaymentAmount += daily.get_nextMonthPaymentAmount();
        }
        this.summaryView.set(incomeAmount, purchaseAmount, paymentAmount, nextPaymentAmount, balanceAmount);
    }

    /**
     * 一日ごとのデータリストを更新
     * @param dailyList
     */
    private void updateDailyListData(List<DailyBop> dailyList) {
//        Log.d("TransactionDataListFragment.updateAmountTextView", "dailyList size: " + dailyList.size());
        if (dailyRecordRecyclerView.getLayoutManager() == null) {
            this.dailyRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        }
        TransactionDateAdapter adapter = new TransactionDateAdapter(this.context, dailyList);
        // RecyclerView内のDailyRecordCustomViewが持つ子オブジェクトにリスナーを登録する作業
        adapter.setListener(new TransactionDateAdapter.OnListItemActionListener() {
            @Override
            public void OnActionButtonClicked(BOP data) {
                onChildItemsActionButtonClicked(data);
            }
        });
        this.dailyRecordRecyclerView.setAdapter(adapter);
    }

    /**
     * RecyclerView内のDailyRecordCustomViewが持つ子オブジェクトのActionButtonが押された時に実行する関数
     * @param data
     */
    private void onChildItemsActionButtonClicked(BOP data) {
        if (data instanceof Income) {
            Intent incomeEditActIntent = new Intent(this.context, SettingEditIncomeActivity.class);
            incomeEditActIntent.putExtra("Income", data);
            this.context.startActivity(incomeEditActIntent);
        }
        else if (data instanceof Purchase) {
            Intent purchaseEditActIntent = new Intent(this.context, SettingEditPurchaseActivity.class);
            purchaseEditActIntent.putExtra("Purchase", data);
            this.context.startActivity(purchaseEditActIntent);
        }
        else if (data instanceof Expenses) {
            Purchase motherPurchase = MyDbManager.getDataById(Purchase.class, ((Expenses) data).getPurchaseId());
            if (motherPurchase != null) {
                Intent purchaseEditActIntent = new Intent(this.context, SettingEditPurchaseActivity.class);
                purchaseEditActIntent.putExtra("Purchase", motherPurchase);
                this.context.startActivity(purchaseEditActIntent);
            }
        }
    }

    /**
     * 指定日時でMyDbManagerからデータを取ってくる関数
     * @param date
     * @return
     */
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
