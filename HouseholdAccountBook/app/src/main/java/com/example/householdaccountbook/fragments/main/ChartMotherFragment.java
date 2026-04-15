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
import com.example.householdaccountbook.fragments.chart.BaseChartFragment;
import com.example.householdaccountbook.viewmodel.ChartDataSharedViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.example.householdaccountbook.myclasses.dbentity.Expenses;
import com.example.householdaccountbook.myclasses.dbentity.Income;
import com.example.householdaccountbook.myclasses.dbentity.IncomeCategory;
import com.example.householdaccountbook.myclasses.dbentity.Purchase;
import com.example.householdaccountbook.myclasses.dbentity.PurchaseCategory;

import java.util.Calendar;

public class ChartMotherFragment extends Fragment {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private TextView monthTextView;
    private ChartDataSharedViewModel svModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.svModel = new ViewModelProvider(this).get(ChartDataSharedViewModel.class);
        this.svModel.setCurrentDate(Calendar.getInstance());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_chart_mother, container, false);
        this.viewPager = layout.findViewById(R.id.chart_fragment_view_pager);
        this.tabLayout = layout.findViewById(R.id.chart_tab);
        this.monthTextView = layout.findViewById(R.id.month_text_view);
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

        view.findViewById(R.id.month_down_button).setOnClickListener(bv -> {
            Calendar buf = this.svModel.getDateLiveData().getValue();
            buf.add(Calendar.MONTH, -1);
            this.svModel.setCurrentDate(buf);
            updateMonthTextView(buf);
        });

        view.findViewById(R.id.month_up_button).setOnClickListener(bv -> {
            Calendar buf = this.svModel.getDateLiveData().getValue();
            buf.add(Calendar.MONTH, 1);
            this.svModel.setCurrentDate(buf);
            updateMonthTextView(buf);
        });
        Calendar currentDate = this.svModel.getDateLiveData().getValue();
        if (currentDate == null) {
            currentDate = Calendar.getInstance();
            this.svModel.setCurrentDate(currentDate);
        }
        updateMonthTextView(currentDate);
    }
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
}
