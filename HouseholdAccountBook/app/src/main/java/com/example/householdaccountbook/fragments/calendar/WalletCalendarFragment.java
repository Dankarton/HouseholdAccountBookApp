package com.example.householdaccountbook.fragments.calendar;

import android.util.Log;

import com.example.householdaccountbook.customviews.calendar.GroupableItem;
import com.example.householdaccountbook.module.calendarentity.BopBaseUiModel;
import com.example.householdaccountbook.module.calendarentity.CalendarDisplayItem;
import com.example.householdaccountbook.module.calendarentity.CalendarUiModel;
import com.example.householdaccountbook.module.calendarentity.DailyUiModel;
import com.example.householdaccountbook.module.calendarentity.HasGroupable;
import com.example.householdaccountbook.module.calendarentity.MoneyMovementUiModel;
import com.example.householdaccountbook.module.calendarentity.TransactionUiModel;
import com.example.householdaccountbook.module.calendarentity.WalletUiModel;
import com.example.householdaccountbook.module.dbentity.Expenses;
import com.example.householdaccountbook.module.dbentity.Income;
import com.example.householdaccountbook.module.dbentity.IncomeCategory;
import com.example.householdaccountbook.module.dbentity.MoneyMovement;
import com.example.householdaccountbook.module.dbentity.MonthlyBalanceDelta;
import com.example.householdaccountbook.module.dbentity.PaymentMethod;
import com.example.householdaccountbook.module.dbentity.PurchaseCategory;
import com.example.householdaccountbook.module.dbentity.Wallet;
import com.example.householdaccountbook.repository.RepositoryManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WalletCalendarFragment extends BaseCalendarFragment {

    @Override
    List<CalendarDisplayItem> getData(Calendar date) {
        Calendar targetDate = (Calendar) date.clone();
        List<Wallet> wallets = RepositoryManager.getInstance().getAll(Wallet.class);
        int YY = targetDate.get(Calendar.YEAR);
        int MM = targetDate.get(Calendar.MONTH);
        int startDD = targetDate.getActualMinimum(Calendar.DAY_OF_MONTH);
        int endDD = targetDate.getActualMaximum(Calendar.DAY_OF_MONTH);

        int maxAmount = 0;
        HashMap<Integer, Integer> dailyDeltaAmountHashMap = new HashMap<>();


        int[] walletCurrentAmountArray = new int[wallets.size()];
        boolean[] needlessWallets = new boolean[wallets.size()];
        int[][] walletDeltaAmountArray = new int[wallets.size()][endDD];
        ArrayList<CalendarDisplayItem>[][] dailyObjArray = new ArrayList[wallets.size()][endDD];

        for (int i = 0; i < wallets.size(); i++) {
            for (int j = 0; j < endDD; j++) {
                walletDeltaAmountArray[i][j] = 0;
                dailyObjArray[i][j] = new ArrayList<>();
            }
        }

        for (int i = 0; i < wallets.size(); i++) {
            Wallet wallet = wallets.get(i);
            List<Income> incomeList = RepositoryManager.getInstance().getIncomeDataByWalletId(wallet.getId(), YY, MM, startDD, YY, MM, endDD);
            List<Expenses> expensesList = RepositoryManager.getInstance().getExpensesDataByWalletId(wallet.getId(), YY, MM, startDD, YY, MM, endDD);
            List<MoneyMovement> comeInMmList = RepositoryManager.getInstance().getMoneyMovementDataByToWalletId(wallet.getId(), YY, MM, startDD, YY, MM, endDD);
            List<MoneyMovement> goOutMmList = RepositoryManager.getInstance().getMoneyMovementDataByFromWalletId(wallet.getId(), YY, MM, startDD, YY, MM, endDD);

//            Log.d("WalletCalendarFragment", String.format(Locale.JAPANESE, "%4d%2d%2d~%4d%2d%2d", YY, MM, startDD, YY, MM, endDD) +
//                    "Wallet[id: " + wallet.getId() + ", name: " + wallet.getName() + "] " +
//                    "Income count: " + incomeList.size() + ", Expenses count: " + expensesList.size() + ", toMM count: " + comeInMmList.size() + ", fromMM count: " + goOutMmList.size());
            // 削除済みのウォレットで金の動きがないものはスキップ
            if (wallet.isDeleted() && incomeList.isEmpty() && expensesList.isEmpty() && comeInMmList.isEmpty() && goOutMmList.isEmpty()) {
                needlessWallets[i] = true;
                continue;
            }
            else {
                needlessWallets[i] = false;
            }

            MonthlyBalanceDelta mbd = RepositoryManager.getInstance().getLatestMonthlyBalanceDelta(wallet.getId(), targetDate);
            if (mbd != null) {
                walletCurrentAmountArray[i] = mbd.getDeltaAmount() + wallet.getInitAmount();
            }
            else {
                walletCurrentAmountArray[i] = wallet.getInitAmount();
            }

            for (Income income : incomeList) {
                int index = income.getDay() - 1;
                IncomeCategory category = RepositoryManager.getInstance().getDataById(IncomeCategory.class, income.getCategoryId());
                walletDeltaAmountArray[i][index] += income.getAmount();
                dailyObjArray[i][index].add(
                        new TransactionUiModel(
                                BopBaseUiModel.DataType.INCOME,
                                income.getId(),
                                income.getAmount(),
                                income.getMemo(),
                                "",
                                category.getColorCode(),
                                category.getName(),
                                GroupableItem.PositionType.SINGLE
                        )
                );
                int deltaAmount = dailyDeltaAmountHashMap.getOrDefault(income.getDay(), 0) + income.getAmount();
                dailyDeltaAmountHashMap.put(income.getDay(), deltaAmount);
                if (maxAmount < deltaAmount) {
                    maxAmount = deltaAmount;
                }
            }
            for (Expenses expenses : expensesList) {
                int index = expenses.getDay() - 1;
                PurchaseCategory category = RepositoryManager.getInstance().getDataById(PurchaseCategory.class, expenses.getCategoryId());
                PaymentMethod paymentMethod = RepositoryManager.getInstance().getDataById(PaymentMethod.class, expenses.getPaymentMethodId());
                walletDeltaAmountArray[i][index] -= Math.abs(expenses.getAmount());
                dailyObjArray[i][index].add(
                        new TransactionUiModel(
                                BopBaseUiModel.DataType.EXPENSES,
                                expenses.getId(),
                                expenses.getAmount(),
                                expenses.getMemo(),
                                paymentMethod.getName(),
                                category.getColorCode(),
                                category.getName(),
                                GroupableItem.PositionType.SINGLE
                        )
                );
                int deltaAmount = dailyDeltaAmountHashMap.getOrDefault(expenses.getDay(), 0) - Math.abs(expenses.getAmount());
                dailyDeltaAmountHashMap.put(expenses.getDay(), deltaAmount);
                if (maxAmount < Math.abs(deltaAmount)) {
                    maxAmount = Math.abs(deltaAmount);
                }
            }

            for (MoneyMovement fromMM : comeInMmList) {
                int index = fromMM.getDay() - 1;
                Wallet toWallet = RepositoryManager.getInstance().getDataById(Wallet.class, fromMM.getToWalletId());
                Wallet fromWallet = RepositoryManager.getInstance().getDataById(Wallet.class, fromMM.getFromWalletId());
                int moveAmount =  fromMM.getAmount();
                walletDeltaAmountArray[i][index] += moveAmount;
                dailyObjArray[i][index].add(
                        new MoneyMovementUiModel(
                                BopBaseUiModel.DataType.MONEY_MOVEMENT,
                                fromMM.getId(),
                                moveAmount,
                                fromMM.getMemo(),
                                toWallet.getName(),
                                fromWallet.getName(),
                                GroupableItem.PositionType.SINGLE
                        )
                );
                // 振替は全資産の合計値は変わらないので0で更新。
                dailyDeltaAmountHashMap.put(fromMM.getDay(), 0);
            }

            for (MoneyMovement toMM : goOutMmList) {
                int index = toMM.getDay() - 1;
                Wallet toWallet = RepositoryManager.getInstance().getDataById(Wallet.class, toMM.getToWalletId());
                Wallet fromWallet = RepositoryManager.getInstance().getDataById(Wallet.class, toMM.getFromWalletId());
                int moveAmount = -1 * toMM.getAmount();
                walletDeltaAmountArray[i][index] += moveAmount;
                dailyObjArray[i][index].add(
                        new MoneyMovementUiModel(
                                BopBaseUiModel.DataType.MONEY_MOVEMENT,
                                toMM.getId(),
                                moveAmount,
                                toMM.getMemo(),
                                toWallet.getName(),
                                fromWallet.getName(),
                                GroupableItem.PositionType.SINGLE
                        )
                );
                // 振替は全資産の合計値は変わらないので0をput。
                dailyDeltaAmountHashMap.put(toMM.getDay(), 0);
            }
        }
        ArrayList<CalendarDisplayItem> dailyUiModels = new ArrayList<>();
        // 先頭にカレンダーオブジェ専用のデータクラスをセット
        dailyUiModels.add(new CalendarUiModel(targetDate, dailyDeltaAmountHashMap, maxAmount));

        for (int i = 0; i < endDD; i++) {
            ArrayList<CalendarDisplayItem> walletUiModels = new ArrayList<>();
            for (int j = 0; j < wallets.size(); j++) {
                if (needlessWallets[j]) continue;
                Wallet wallet = wallets.get(j);
                walletCurrentAmountArray[j] += walletDeltaAmountArray[j][i];
                WalletUiModel walletUiModel = new WalletUiModel(
                        wallet.getId(),
                        wallet.getName(),
                        walletDeltaAmountArray[j][i],
                        walletCurrentAmountArray[j],
                        dailyObjArray[j][i],
                        GroupableItem.PositionType.SINGLE,
                        false
                );
                walletUiModel.setListVisibleValid(!dailyObjArray[j][i].isEmpty());
                walletUiModels.add(walletUiModel);
            }
            DailyUiModel dailyUiModel = new DailyUiModel(
                    YY, MM, i + 1,
                    dailyDeltaAmountHashMap.getOrDefault(i + 1, null),
                    walletUiModels,
                    GroupableItem.PositionType.SINGLE,
                    false
            );
            dailyUiModel.setListVisibleValid(!dailyUiModel.getChildItems().isEmpty());
            dailyUiModels.add(dailyUiModel);
        }

        return dailyUiModels;
    }

    @Override
    List<CalendarDisplayItem> flatten(List<CalendarDisplayItem> beforeData) {
        ArrayList<CalendarDisplayItem> afterList = new ArrayList<>();
        for (CalendarDisplayItem data : beforeData) {
            if (data instanceof CalendarUiModel) {
                afterList.add(data);
            }
            else if (data instanceof DailyUiModel dailyData) {
                if (dailyData.isListVisible() && !dailyData.getChildItems().isEmpty()) {
                    Log.d("WalletCalendarFragment", "DailyUiModel position top");
                    dailyData.setListVisibleValid(true);
                    afterList.add(copyWithPositionType(dailyData, GroupableItem.PositionType.TOP));
                    afterList.addAll(flattenChildItems(dailyData.getChildItems()));
                }
                else {
                    dailyData.setListVisibleValid(!dailyData.getChildItems().isEmpty());
                    afterList.add(copyWithPositionType(dailyData, GroupableItem.PositionType.SINGLE));
                }
            }
        }
        return afterList;
    }
    List<CalendarDisplayItem> flattenChildItems(List<CalendarDisplayItem> childItems) {
        // TODO ネスト深いのいつか直す
        List<CalendarDisplayItem> result = new ArrayList<>();
        for (int i = 0; i < childItems.size(); i++) {
            CalendarDisplayItem child = childItems.get(i);
            if (child instanceof WalletUiModel walletData) {
                if (walletData.isListVisible() && !walletData.getChildItems().isEmpty()) {
                    walletData.setListVisibleValid(true);
                    result.add(copyWithPositionType(walletData, GroupableItem.PositionType.MIDDLE));
                    List<CalendarDisplayItem> childChildItems = walletData.getChildItems();
                    for (int j = 0; j < childChildItems.size(); j++) {
                        CalendarDisplayItem childChild = childChildItems.get(j);
                        if (childChild instanceof HasGroupable) {
                            if (i < childItems.size() - 1) {
                                result.add(copyWithPositionType(childChild, GroupableItem.PositionType.MIDDLE));
                            }
                            else {
                                result.add(copyWithPositionType(childChild, GroupableItem.PositionType.BOTTOM));
                            }
                        }
                    }
                }
                else {
                    walletData.setListVisibleValid(!walletData.getChildItems().isEmpty());
                    if (i < childItems.size() - 1) {
                        result.add(copyWithPositionType(walletData, GroupableItem.PositionType.MIDDLE));
                    }
                    else {
                        result.add(copyWithPositionType(walletData, GroupableItem.PositionType.BOTTOM));
                    }
                }
            }
        }
        return result;
    }
}
