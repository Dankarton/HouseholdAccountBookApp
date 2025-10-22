package com.example.householdaccountbook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.householdaccountbook.adapter.InputFragmentPagerAdapter;
import com.example.householdaccountbook.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;

public class InputMotherFragment extends Fragment implements IncomeInputFragment.InputCompleteListener {
    ViewPager2 viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_mother, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        IncomeInputFragment incInpFragment = new IncomeInputFragment();
        ExpensesInputFragment expInpFragment = new ExpensesInputFragment();
        InputFragmentPagerAdapter adapter = new InputFragmentPagerAdapter(this, expInpFragment, incInpFragment);
        viewPager = view.findViewById(R.id.input_fragment_view_pager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = view.findViewById(R.id.expenses_or_income_tab);
        String[] tabTitleArray = {"支出", "収入"};
        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(tabTitleArray[position]))).attach();
        viewPager.setUserInputEnabled(false);
    }
    @Override
    public void onIncomeInputCompleted(int amount, Calendar date, String memo, String category) {

    }
}

