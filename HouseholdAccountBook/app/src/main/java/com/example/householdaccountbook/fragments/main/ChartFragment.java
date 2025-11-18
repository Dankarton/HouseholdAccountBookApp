package com.example.householdaccountbook.fragments.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.HouseHoldApp;
import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.PieChartView;
import com.example.householdaccountbook.db.MyDbManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myclasses.Purchase;
import myclasses.PurchaseCategory;

public class ChartFragment extends Fragment {
    private PieChartView pieChartView;
    private Calendar currentDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_chart, container, false);
        this.pieChartView = layout.findViewById(R.id.pie_chart);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HouseHoldApp app = (HouseHoldApp) view.getContext().getApplicationContext();
        this.currentDate = Calendar.getInstance();
        // カテゴリーIDをキーにした辞書型
        Map<Long, CategoryTotal> categoryMap = new HashMap<>();
        for (Purchase purchase : loadCurrentMonthPurchaseData(this.currentDate)) {
            // キーが存在しない場合
            if (!categoryMap.containsKey(purchase.getCategoryId())) {
                PurchaseCategory newCategory = app.getPurchaseCategoryRepository().getDataById(purchase.getCategoryId());
                categoryMap.put(newCategory.getId(), new CategoryTotal(newCategory));
            }
            CategoryTotal targetCategoryTotal = categoryMap.get(purchase.getCategoryId());
            if (targetCategoryTotal != null) {
                targetCategoryTotal.set(purchase);
            }
        }
        // 合計金額で降順に並べる
        CategoryTotal[] categoryTotalArray = MyStdlib.mergeSort(
                new ArrayList<>(categoryMap.values()).toArray(new CategoryTotal[0]),
                (a, b) -> Integer.compare(b.getTotalAmount(), a.getTotalAmount())
        );
        float[] amountDataList = new float[categoryTotalArray.length];
        int[] colors = new int[categoryTotalArray.length];
        String[] labels = new String[categoryTotalArray.length];
        for (int i = 0; i < categoryTotalArray.length; i++) {
            amountDataList[i] = categoryTotalArray[i].getTotalAmount();
            colors[i] = categoryTotalArray[i].getCategory().getColorCode();
            labels[i] = categoryTotalArray[i].getCategory().getName();
        }
        Log.d("ChartFragment", colors.length + ", " + amountDataList.length);
        this.pieChartView.setData(amountDataList);
        this.pieChartView.setColors(colors);
        this.pieChartView.setLabelData(labels);
    }

    private List<Purchase> loadCurrentMonthPurchaseData(Calendar date) {
        List<Purchase> monthlyPurchaseList = new ArrayList<>();
        for (int i = 1; i <= date.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            List<Purchase> dailyPurchase = MyDbManager.getBopDataByDate(Purchase.class, date.get(Calendar.YEAR), date.get(Calendar.MONTH), i);
            if (!dailyPurchase.isEmpty()) {
                monthlyPurchaseList.addAll(dailyPurchase);
            }
        }
        return monthlyPurchaseList;
    }

    private static class CategoryTotal {
        private final PurchaseCategory category;
        private int totalAmount;
        private List<Purchase> dataList;

        public CategoryTotal(PurchaseCategory category) {
            this.category = category;
            this.totalAmount = 0;
            this.dataList = new ArrayList<>();
        }


        public void set(Purchase data) {
            this.dataList.add(data);
            this.totalAmount += data.getAmount();
        }
        public PurchaseCategory getCategory() { return this.category; }
        public int getTotalAmount() { return this.totalAmount; }
    }
}
