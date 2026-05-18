package com.example.householdaccountbook.fragments.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.adapter.FragmentPagerAdapter;
import com.example.householdaccountbook.customviews.DateSelectorCustomView;
import com.example.householdaccountbook.fragments.calendar.BaseCalendarFragment;
import com.example.householdaccountbook.fragments.calendar.BopCalendarFragment;
import com.example.householdaccountbook.fragments.calendar.WalletCalendarFragment;
import com.example.householdaccountbook.module.sharedmodel.DateSharedViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;

public class CalendarMotherFragment extends Fragment {
    private DateSelectorCustomView dateSelector;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private DateSharedViewModel dateSharedModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_calendar_mother, container, false);
        this.dateSharedModel = new ViewModelProvider(this).get(DateSharedViewModel.class);
        this.dateSharedModel.setCurrentDate(Calendar.getInstance());

        this.tabLayout = layout.findViewById(R.id.calendar_tab);
        this.viewPager = layout.findViewById(R.id.calendar_fragment_view_pager);
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
                        Log.d("CalendarMotherFragment", "Date changed: "  + dateSelector.getCurrentDate());
                        dateSharedModel.setCurrentDate(dateSelector.getCurrentDate());
                    }
                }
        );
        this.dateSelector.setDate(this.dateSharedModel.getDateLiveData().getValue());

        return layout;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        var bopCalendarFragment = new BopCalendarFragment();
        var walletCalendarFragment = new WalletCalendarFragment();
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(
                this,
                new Fragment[] { bopCalendarFragment, walletCalendarFragment },
                new String[] { "収支", "ウォレット" }
        );
        this.viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, ((tab, pos) -> tab.setText(adapter.getPageTitle(pos)))).attach();
    }
}
