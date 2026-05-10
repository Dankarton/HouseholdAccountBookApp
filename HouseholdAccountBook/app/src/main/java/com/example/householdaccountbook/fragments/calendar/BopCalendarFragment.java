package com.example.householdaccountbook.fragments.calendar;

import com.example.householdaccountbook.myclasses.calendarentity.CalendarDisplayItem;
import com.example.householdaccountbook.myclasses.calendarentity.CalendarUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.DailyUiModel;
import com.example.householdaccountbook.myclasses.dbentity.Income;
import com.example.householdaccountbook.myclasses.dbentity.Purchase;
import com.example.householdaccountbook.repository.DataAssembler;
import com.example.householdaccountbook.repository.RepositoryManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BopCalendarFragment extends BaseCalendarFragment {
    @Override
    List<CalendarDisplayItem> getData(Calendar targetDate) {
        int YY = targetDate.get(Calendar.YEAR);
        int MM = targetDate.get(Calendar.MONTH);
        int startDD = targetDate.getActualMinimum(Calendar.DAY_OF_MONTH);
        int endDD = targetDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 年月で範囲指定してデータを取得
        List<Income> incomeList = RepositoryManager.getInstance().getBopDataInRange(Income.class, YY, MM, startDD, YY, MM, endDD);
        List<Purchase> purchaseList = RepositoryManager.getInstance().getBopDataInRange(Purchase.class, YY, MM, startDD, YY, MM, endDD);
        // カレンダーオブジェで使うメンバ変数とか
        int maxAmount = 0;
        HashMap<Integer, Integer> dailyDeltaAmountHashMap = new HashMap<>();
        //
        HashMap<Integer, List<Income>> incomeHashMap = new HashMap<>();
        HashMap<Integer, List<Purchase>> purchaseHashMap = new HashMap<>();

        for (Income data : incomeList) {
            // 差額計算
            int deltaAmount = dailyDeltaAmountHashMap.getOrDefault(data.getDay(), 0) + data.getAmount();
            dailyDeltaAmountHashMap.put(data.getDay(), deltaAmount);
            if (maxAmount < deltaAmount) {
                maxAmount = deltaAmount;
            }
            incomeHashMap.computeIfAbsent(data.getDay(), k -> new ArrayList<>()).add(data);
        }
        for (Purchase data : purchaseList) {
            int deltaAmount = dailyDeltaAmountHashMap.getOrDefault(data.getDay(), 0) - Math.abs(data.getAmount());
            dailyDeltaAmountHashMap.put(data.getDay(), deltaAmount);
            if (maxAmount < Math.abs(deltaAmount)) {
                maxAmount = Math.abs(deltaAmount);
            }
            purchaseHashMap.computeIfAbsent(data.getDay(), k -> new ArrayList<>()).add(data);
        }
        List<CalendarDisplayItem> dailyUiList = new ArrayList<>();
        // 先頭にカレンダーオブジェ専用のデータクラスをセット
        dailyUiList.add(new CalendarUiModel(targetDate, dailyDeltaAmountHashMap, maxAmount));
        for (int i = startDD; i <= endDD; i++) {
            dailyUiList.add(
                    DataAssembler.getInstance().assembleDailyUiModel(
                            YY, MM, i,
                            incomeHashMap.getOrDefault(i, new ArrayList<>()),
                            purchaseHashMap.getOrDefault(i, new ArrayList<>())
                    )
            );
        }
        return dailyUiList;
    }

    @Override
    List<CalendarDisplayItem> flatten(List<CalendarDisplayItem> beforeData) {
        // 均した後のリスト格納用
        ArrayList<CalendarDisplayItem> afterList = new ArrayList<>();

        for (CalendarDisplayItem data : beforeData) {
            if (data instanceof CalendarUiModel) {
                afterList.add(data);
            }
            else if (data instanceof DailyUiModel) {
                afterList.add(data);
                if (((DailyUiModel) data).isListVisible()) {
                    afterList.addAll(((DailyUiModel) data).getChildItems());
                }
            }
        }
        return afterList;
    }
}
