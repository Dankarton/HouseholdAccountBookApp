package com.example.householdaccountbook.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.adapter.FragmentPagerAdapter;
import com.example.householdaccountbook.fragments.chart.BaseChartFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import myclasses.Expenses;
import myclasses.Income;
import myclasses.IncomeCategory;
import myclasses.Purchase;
import myclasses.PurchaseCategory;

public class ChartMotherFragment extends Fragment {
    ViewPager2 viewPager;
    TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_chart_mother, container, false);
        this.viewPager = layout.findViewById(R.id.chart_fragment_view_pager);
        this.tabLayout = layout.findViewById(R.id.chart_tab);
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
