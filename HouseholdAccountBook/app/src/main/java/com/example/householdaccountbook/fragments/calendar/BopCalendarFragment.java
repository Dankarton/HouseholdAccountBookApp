package com.example.householdaccountbook.fragments.calendar;

import android.util.Log;

import com.example.householdaccountbook.customviews.calendar.GroupableItem;
import com.example.householdaccountbook.module.calendarentity.CalendarDisplayItem;
import com.example.householdaccountbook.module.calendarentity.CalendarUiModel;
import com.example.householdaccountbook.module.calendarentity.DailyUiModel;
import com.example.householdaccountbook.module.calendarentity.HasGroupable;
import com.example.householdaccountbook.module.calendarentity.MoneyMovementUiModel;
import com.example.householdaccountbook.module.calendarentity.TransactionUiModel;
import com.example.householdaccountbook.module.dbentity.Income;
import com.example.householdaccountbook.module.dbentity.MoneyMovement;
import com.example.householdaccountbook.module.dbentity.Purchase;
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
        Log.d("BopCalendarFragment", "Income count: " + incomeList.size());
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
        dailyUiList.add(new CalendarUiModel((Calendar) targetDate.clone(), dailyDeltaAmountHashMap, maxAmount));
        for (int i = startDD; i <= endDD; i++) {
            List<Income> daysIncomeList = incomeHashMap.getOrDefault(i, new ArrayList<>());
            List<Purchase> daysPurchaseList = purchaseHashMap.getOrDefault(i, new ArrayList<>());

            if (daysIncomeList.size() <= 0 && daysPurchaseList.size() <= 0) continue;
            DailyUiModel dailyUiModel = DataAssembler.getInstance().assembleDailyUiModel(
                    YY, MM, i,
                    daysIncomeList,
                    daysPurchaseList
            );
            dailyUiModel.setListVisibleValid(true);
            dailyUiList.add(dailyUiModel);
        }
        return dailyUiList;
    }

    @Override
    List<CalendarDisplayItem> flatten(List<CalendarDisplayItem> beforeData) {
        // 均した後のリスト格納用
        ArrayList<CalendarDisplayItem> afterList = new ArrayList<>();
        // TODO ネスト深すぎ。後で関数に切り離す。
        for (int i = 0; i < beforeData.size(); i++) {
            CalendarDisplayItem data = beforeData.get(i);
            if (data instanceof CalendarUiModel) {
                afterList.add(data);
            }
            else if (data instanceof DailyUiModel) {
                List<CalendarDisplayItem> childItems = ((DailyUiModel) data).getChildItems();
                if (((DailyUiModel) data).isListVisible() && !childItems.isEmpty()) {
                    ((DailyUiModel) data).setListVisibleValid(true);
                    afterList.add(copyWithPositionType(data, GroupableItem.PositionType.TOP));
                    for (int j = 0; j < childItems.size(); j++) {
                        CalendarDisplayItem child = childItems.get(j);
                        if (child instanceof HasGroupable) {
                            if (j < childItems.size() - 1) {
                                afterList.add(copyWithPositionType(child, GroupableItem.PositionType.MIDDLE));
                            }
                            else {
                                afterList.add(copyWithPositionType(child, GroupableItem.PositionType.BOTTOM));
                            }
                        }
                    }
                }
                else {
                    ((DailyUiModel) data).setListVisibleValid(!childItems.isEmpty());
                    afterList.add(copyWithPositionType(data, GroupableItem.PositionType.SINGLE));
                }
            }
        }
        return afterList;
    }
}
