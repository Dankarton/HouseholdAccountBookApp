package com.example.householdaccountbook.fragments.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.adapter.CalendarUiAdapter;
import com.example.householdaccountbook.myclasses.calendarentity.BopBaseUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.CalendarDisplayItem;
import com.example.householdaccountbook.repository.RepositoryManager;
import com.example.householdaccountbook.viewmodel.DateSharedViewModel;

import java.util.Calendar;
import java.util.List;

public abstract class BaseCalendarFragment extends Fragment {
    private DateSharedViewModel dateShareModel;
    private RecyclerView recyclerView;
    private CalendarUiAdapter calendarUiAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dateShareModel = new ViewModelProvider(requireParentFragment()).get(DateSharedViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_base_calendar, container, false);
        this.recyclerView = layout.findViewById(R.id.item_list);
        this.calendarUiAdapter = new CalendarUiAdapter();
        this.calendarUiAdapter.setListener(new CalendarUiAdapter.OnActionListener() {
            @Override
            public void onListableButtonClicked() {
                calendarUiAdapter.setData(flatten(calendarUiAdapter.getData()));
            }

            @Override
            public void onMoreActionButtonClicked(BopBaseUiModel.DataType type, long id) {

            }
        });
        this.recyclerView.setAdapter(this.calendarUiAdapter);
        return layout;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.dateShareModel.getDateLiveData().observe(
                getViewLifecycleOwner(),
                date -> this.calendarUiAdapter.setData(getData(date))
        );
    }
    abstract List<CalendarDisplayItem> getData(Calendar targetDate);
    abstract List<CalendarDisplayItem> flatten(List<CalendarDisplayItem> beforeData);
}
