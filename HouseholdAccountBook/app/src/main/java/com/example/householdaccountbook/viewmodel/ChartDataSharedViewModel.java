package com.example.householdaccountbook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class ChartDataSharedViewModel extends ViewModel {
    private final MutableLiveData<Calendar> currentDate = new MutableLiveData<>();

    public LiveData<Calendar> getDateLiveData() {
        return this.currentDate;
    }
    public void setCurrentDate(Calendar date) {
        this.currentDate.setValue((Calendar) date.clone());
    }
}
