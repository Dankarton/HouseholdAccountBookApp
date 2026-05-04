package com.example.householdaccountbook.fragments.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.viewmodel.DateSharedViewModel;

public class BaseCalendarFragment extends Fragment {
    private DateSharedViewModel dateShareModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dateShareModel = new ViewModelProvider(requireParentFragment()).get(DateSharedViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate()
    }
}
