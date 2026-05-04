package com.example.householdaccountbook.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.DateSelectorCustomView;
import com.example.householdaccountbook.viewmodel.DateSharedViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class CalendarFragment extends Fragment {
    private DateSelectorCustomView dateSelector;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RecyclerView dailyRecordList;
    private DateSharedViewModel dateSharedModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_calendar_mother, container, false);
        this.dateSharedModel = new ViewModelProvider(this).get(DateSharedViewModel.class);
        this.dateSharedModel.setCurrentDate(Calendar.getInstance());
        return layout;
    }
}
