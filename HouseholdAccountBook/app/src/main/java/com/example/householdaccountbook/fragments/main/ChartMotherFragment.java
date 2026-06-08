package com.example.householdaccountbook.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.adapter.FragmentPagerAdapter;
import com.example.householdaccountbook.customviews.DateSelectorCustomView;
import com.example.householdaccountbook.fragments.chart.BaseChartFragment;
import com.example.householdaccountbook.module.sharedmodel.ChartDataSharedViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.example.householdaccountbook.module.dbentity.Expenses;
import com.example.householdaccountbook.module.dbentity.Income;
import com.example.householdaccountbook.module.dbentity.IncomeCategory;
import com.example.householdaccountbook.module.dbentity.Purchase;
import com.example.householdaccountbook.module.dbentity.PurchaseCategory;

import java.util.Calendar;

public class ChartMotherFragment extends Fragment {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private DateSelectorCustomView dateSelector;
    private ChartDataSharedViewModel svModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_chart_mother, container, false);
        this.svModel = new ViewModelProvider(this).get(ChartDataSharedViewModel.class);
        this.svModel.setCurrentDate(Calendar.getInstance());

        this.viewPager = layout.findViewById(R.id.chart_fragment_view_pager);
        this.tabLayout = layout.findViewById(R.id.chart_tab);
        this.dateSelector = layout.findViewById(R.id.date_select_view);
        this.dateSelector.setDisplayMode(DateSelectorCustomView.DisplayMode.MONTHLY);
        this.dateSelector.setListener(
                new DateSelectorCustomView.OnActionListener() {
                    @Override
                    public void onUpButtonClicked() { /*Do nothing*/ }
                    @Override
                    public void onBackButtonClicked() { /*Do nothing*/ }
                    @Override
                    public void onDateTextClicked() { /*Do nothing*/ }
                    @Override
                    public void onDateChanged() {
                        svModel.setCurrentDate(dateSelector.getCurrentDate());
                    }
                }
        );
        this.dateSelector.setDate(this.svModel.getDateLiveData().getValue());
        return layout;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        BaseChartFragment<Purchase, PurchaseCategory> purchaseChartFragment = new BaseChartFragment<>(Purchase.class, PurchaseCategory.class);
        BaseChartFragment<Expenses, PurchaseCategory> expensesChartFragment = new BaseChartFragment<>(Expenses.class, PurchaseCategory.class);
        BaseChartFragment<Income, IncomeCategory> incomeChartFragment = new BaseChartFragment<>(Income.class, IncomeCategory.class);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(
                this,
                new Fragment[] { purchaseChartFragment, expensesChartFragment, incomeChartFragment },
                new String[] { "支出", "支払い", "収入" }
        );
        this.viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(adapter.getPageTitle(position)))).attach();
    }
}
