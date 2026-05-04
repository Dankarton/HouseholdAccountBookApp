package com.example.householdaccountbook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

/**
 * MotherFragmentと複数の子Fragmentとかで日付データを共有するためのモデル
 * ---未来の自分へ---
 * AndroidSDKにあるLiveDataクラスはデータが書き換えられると自動で通達がいく仕組みが備わってるので
 * Fragment内にわざわざListenerを設定し無くても良くなるから使ってる。
 * ちなみに紐づけられたオーナーのライフサイクルに従うから、キャッシュみたいに常に保持する必要はないけど、
 * 画面が生きてる間は保持したいデータとかにViewModelを使うといい(らしい)
 */
public class DateSharedViewModel extends ViewModel {
    private final MutableLiveData<Calendar> currentDate = new MutableLiveData<>();

    public LiveData<Calendar> getDateLiveData() {
        return this.currentDate;
    }
    public void setCurrentDate(Calendar date) {
        this.currentDate.setValue((Calendar) date.clone());
    }
}
