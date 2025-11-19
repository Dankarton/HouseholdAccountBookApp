package com.example.householdaccountbook.fragments.chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.HouseHoldApp;
import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.adapter.CategoryTotalListAdapter;
import com.example.householdaccountbook.customviews.PieChartView;
import com.example.householdaccountbook.db.MyDbManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import myclasses.BOP;
import myclasses.BopCategory;
import myclasses.IncomeCategory;
import myclasses.PurchaseCategory;

public class BaseChartFragment<T1 extends BOP, T2 extends BopCategory> extends Fragment {
    private final Class<T1> bopClazz;
    private final Class<T2> categoryClazz;
    private HouseHoldApp app;
    private PieChartView pieChartView;
    private TextView monthTextView;
    private RecyclerView categoryTotalListRecyclerView;
    private Calendar currentDate;
    public BaseChartFragment(Class<T1> bopClazz, Class<T2> categoryClazz) {
        this.bopClazz = bopClazz;
        this.categoryClazz = categoryClazz;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_base_chart, container, false);
        this.app = (HouseHoldApp) layout.getContext().getApplicationContext();
        this.monthTextView = layout.findViewById(R.id.month_text_view);
        this.pieChartView = layout.findViewById(R.id.pie_chart);
        this.categoryTotalListRecyclerView = layout.findViewById(R.id.category_total_list_recycler_view);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.currentDate = Calendar.getInstance();

        view.findViewById(R.id.month_up_button).setOnClickListener(buttonView -> {
            this.currentDate.add(Calendar.MONTH, 1);
            update();
        });
        view.findViewById(R.id.month_down_button).setOnClickListener(buttonView -> {
            this.currentDate.add(Calendar.MONTH, -1);
            update();
        });

        update();
    }

    /**
     * 日付を更新
     */
    private void updateMonthTextView(Calendar date) {
        this.monthTextView.setText(
                MyStdlib.convertCalendarToString(
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        null,
                        null
                )
        );
    }

    /**
     * 画面を更新
     */
    private void update() {
        updateMonthTextView(this.currentDate);
        // カテゴリーIDをキーにした辞書型
        Map<Long, CategoryTotal> categoryMap = new HashMap<>();
        int allPurchaseTotalAmount = 0;
        for (BOP purchase : loadCurrentMonthPurchaseData(this.currentDate)) {
            // キーが存在しない場合
            if (!categoryMap.containsKey(purchase.getCategoryId())) {
                BopCategory newCategory = null;
                if (this.categoryClazz == PurchaseCategory.class) {
                    newCategory = this.app.getPurchaseCategoryRepository().getDataById(purchase.getCategoryId());
                }
                else if (this.categoryClazz == IncomeCategory.class) {
                    newCategory = this.app.getIncomeCategoryRepository().getDataById(purchase.getCategoryId());
                }
                else {
                    throw new IllegalArgumentException("登録されていないクラスです");
                }

                categoryMap.put(newCategory.getId(), new CategoryTotal(newCategory));
            }
            CategoryTotal targetCategoryTotal = categoryMap.get(purchase.getCategoryId());
            if (targetCategoryTotal != null) {
                targetCategoryTotal.set(purchase);
            }
            allPurchaseTotalAmount += Math.abs(purchase.getAmount());
        }
        // 合計金額で降順に並べる
        CategoryTotal[] categoryTotalArray = MyStdlib.mergeSort(
                new ArrayList<>(categoryMap.values()).toArray(new CategoryTotal[0]),
                (a, b) -> Integer.compare(b.getTotalAmount(), a.getTotalAmount())
        );
        // 円グラフ用のデータとか作成
        float[] amountDataList = new float[categoryTotalArray.length];
        int[] colors = new int[categoryTotalArray.length];
        String[] labels = new String[categoryTotalArray.length];

        List<CategoryTotalListAdapter.CategoryTotalBinder> binderList = new ArrayList<>();

        for (int i = 0; i < categoryTotalArray.length; i++) {
            int color = categoryTotalArray[i].getCategory().getColorCode();
            int amount = categoryTotalArray[i].getTotalAmount();
            String name = categoryTotalArray[i].getCategory().getName();
            amountDataList[i] = amount;
            colors[i] = color;
            labels[i] = name;
            binderList.add(new CategoryTotalListAdapter.CategoryTotalBinder(
                    color, name, amount, (float) amount / (float) allPurchaseTotalAmount
            ));
        }
        this.pieChartView.setData(amountDataList);
        this.pieChartView.setColors(colors);
        this.pieChartView.setLabelData(labels);
        if (categoryTotalArray.length > 0) {
            this.pieChartView.setCenterText(String.format(Locale.JAPANESE, "￥%,d", allPurchaseTotalAmount));
        }
        else {
            this.pieChartView.setCenterText("データ無し");
        }


        if (this.categoryTotalListRecyclerView.getLayoutManager() == null) {
            this.categoryTotalListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        this.categoryTotalListRecyclerView.setAdapter(new CategoryTotalListAdapter(binderList));
    }

    private List<BOP> loadCurrentMonthPurchaseData(Calendar date) {
        List<BOP> monthlyBopList = new ArrayList<>();
        for (int i = 1; i <= date.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            List<T1> dailyData = MyDbManager.getBopDataByDate(this.bopClazz, date.get(Calendar.YEAR), date.get(Calendar.MONTH), i);
            if (!dailyData.isEmpty()) {
                monthlyBopList.addAll(dailyData);
            }
        }
        return monthlyBopList;
    }

    private static class CategoryTotal {
        private final BopCategory category;
        private int totalAmount;
        private List<BOP> dataList;

        public CategoryTotal(BopCategory category) {
            this.category = category;
            this.totalAmount = 0;
            this.dataList = new ArrayList<>();
        }


        public void set(BOP data) {
            this.dataList.add(data);
            this.totalAmount += data.getAmount();
        }
        public BopCategory getCategory() { return this.category; }
        public int getTotalAmount() { return this.totalAmount; }
    }
}
