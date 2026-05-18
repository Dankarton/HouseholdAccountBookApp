package com.example.householdaccountbook.fragments.chart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

import com.example.householdaccountbook.module.dbentity.BOP;
import com.example.householdaccountbook.module.dbentity.BopCategory;
import com.example.householdaccountbook.module.dbentity.HasCategory;
import com.example.householdaccountbook.module.dbentity.IncomeCategory;
import com.example.householdaccountbook.module.dbentity.PurchaseCategory;
import com.example.householdaccountbook.module.sharedmodel.ChartDataSharedViewModel;
import com.example.householdaccountbook.repository.RepositoryManager;

public class BaseChartFragment<T1 extends BOP & HasCategory, T2 extends BopCategory> extends Fragment {

    private final Class<T1> bopClazz;
    private final Class<T2> categoryClazz;
    private HouseHoldApp app;
    private PieChartView pieChartView;
    private RecyclerView categoryTotalListRecyclerView;

    private ChartDataSharedViewModel viewModel;

    public BaseChartFragment(Class<T1> bopClazz, Class<T2> categoryClazz) {
        this.bopClazz = bopClazz;
        this.categoryClazz = categoryClazz;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.viewModel = new ViewModelProvider(requireParentFragment()).get(ChartDataSharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_base_chart, container, false);
        this.app = (HouseHoldApp) layout.getContext().getApplicationContext();
        this.pieChartView = layout.findViewById(R.id.pie_chart);
        this.categoryTotalListRecyclerView = layout.findViewById(R.id.category_total_list_recycler_view);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewModel.getDateLiveData().observe(
                getViewLifecycleOwner(),
                date -> update(date)
        );
    }

    /**
     * 画面を更新
     */
    private void update(Calendar targetDate) {
        // カテゴリーIDをキーにした辞書型
        Map<Long, CategoryTotal> categoryMap = new HashMap<>();
        int allPurchaseTotalAmount = 0;

        int YY = targetDate.get(Calendar.YEAR);
        int MM = targetDate.get(Calendar.MONTH);
        int startDD = targetDate.getActualMinimum(Calendar.DAY_OF_MONTH);
        int endDD = targetDate.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (T1 bop : RepositoryManager.getInstance().getBopDataInRange(this.bopClazz, YY, MM, startDD, YY, MM, endDD)) {
            // キーが存在しない場合
            if (!categoryMap.containsKey(bop.getCategoryId())) {
                BopCategory newCategory = null;
                newCategory = RepositoryManager.getInstance().getDataById(this.categoryClazz, bop.getCategoryId());

                categoryMap.put(newCategory.getId(), new CategoryTotal(newCategory));
            }
            CategoryTotal targetCategoryTotal = categoryMap.get(bop.getCategoryId());
            if (targetCategoryTotal != null) {
                targetCategoryTotal.set(bop);
            }
            allPurchaseTotalAmount += Math.abs(bop.getAmount());
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
