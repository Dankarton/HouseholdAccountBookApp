package com.example.householdaccountbook.fragments.calendar;

import com.example.householdaccountbook.myclasses.calendarentity.BopBaseUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.CalendarDisplayItem;
import com.example.householdaccountbook.myclasses.calendarentity.DailyUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.MoneyMovementUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.TransactionUiModel;
import com.example.householdaccountbook.myclasses.calendarentity.WalletUiModel;
import com.example.householdaccountbook.myclasses.dbentity.Expenses;
import com.example.householdaccountbook.myclasses.dbentity.Income;
import com.example.householdaccountbook.myclasses.dbentity.IncomeCategory;
import com.example.householdaccountbook.myclasses.dbentity.MoneyMovement;
import com.example.householdaccountbook.myclasses.dbentity.PaymentMethod;
import com.example.householdaccountbook.myclasses.dbentity.PurchaseCategory;
import com.example.householdaccountbook.myclasses.dbentity.Wallet;
import com.example.householdaccountbook.repository.DataAssembler;
import com.example.householdaccountbook.repository.RepositoryManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WalletCalendarFragment extends BaseCalendarFragment {

    @Override
    List<CalendarDisplayItem> getData(Calendar targetDate) {
        // TODO 2026/05/10
        List<Wallet> wallets = RepositoryManager.getInstance().getAll(Wallet.class);
        int YY = targetDate.get(Calendar.YEAR);
        int MM = targetDate.get(Calendar.MONTH);
        int startDD = targetDate.getActualMinimum(Calendar.DAY_OF_MONTH);
        int endDD = targetDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        int[] monthlyBalanceDeltaList = new int[wallets.size()];
        boolean[] needlessWallets = new boolean[wallets.size()];
        int[][] deltaAmountArray = new int[wallets.size()][endDD];
        ArrayList<CalendarDisplayItem>[][] dailyObjArray = new ArrayList[wallets.size()][endDD];
        for (int i = 0; i < wallets.size(); i++) {
            for (int j = 0; j < endDD; j++) {
                deltaAmountArray[i][j] = 0;
                dailyObjArray[i][j] = new ArrayList<>();
            }
        }

        for (int i = 0; i < wallets.size(); i++) {
            Wallet wallet = wallets.get(i);
            List<Income> incomeList = RepositoryManager.getInstance().getIncomeDataByWalletId(wallet.getId(), YY, MM, startDD, YY, MM, endDD);
            List<Expenses> expensesList = RepositoryManager.getInstance().getExpensesDataByWalletId(wallet.getId(), YY, MM, startDD, YY, MM, endDD);
            List<MoneyMovement> comeInMmList = RepositoryManager.getInstance().getMoneyMovementDataByToWalletId(wallet.getId(), YY, MM, startDD, YY, MM, endDD);
            List<MoneyMovement> goOutMmList = RepositoryManager.getInstance().getMoneyMovementDataByFromWalletId(wallet.getId(), YY, MM, startDD, YY, MM, endDD);
            // 削除済みのウォレットで金の動きがないものはスキップ
            if (wallet.isDeleted() && incomeList.isEmpty() && expensesList.isEmpty() && comeInMmList.isEmpty() && goOutMmList.isEmpty()) {
                needlessWallets[i] = true;
                continue;
            }
            else {
                needlessWallets[i] = false;
            }
            for (Income income : incomeList) {
                int index = income.getDay() - 1;
                IncomeCategory category = RepositoryManager.getInstance().getDataById(IncomeCategory.class, income.getCategoryId());
                deltaAmountArray[i][index] += income.getAmount();
                dailyObjArray[i][index].add(
                        new TransactionUiModel(
                                BopBaseUiModel.DataType.INCOME,
                                income.getId(),
                                income.getAmount(),
                                income.getMemo(),
                                "",
                                category.getColorCode(),
                                category.getName()
                        )
                );
            }
            for (Expenses expenses : expensesList) {
                int index = expenses.getDay() - 1;
                PurchaseCategory category = RepositoryManager.getInstance().getDataById(PurchaseCategory.class, expenses.getCategoryId());
                PaymentMethod paymentMethod = RepositoryManager.getInstance().getDataById(PaymentMethod.class, expenses.getPaymentMethodId());
                deltaAmountArray[i][index] -= Math.abs(expenses.getAmount());
                dailyObjArray[i][index].add(
                        new TransactionUiModel(
                                BopBaseUiModel.DataType.EXPENSES,
                                expenses.getId(),
                                expenses.getAmount(),
                                expenses.getMemo(),
                                paymentMethod.getName(),
                                category.getColorCode(),
                                category.getName()
                        )
                );
            }
            for (MoneyMovement toMM : comeInMmList) {
                int index = toMM.getDay() - 1;
                Wallet toWallet = RepositoryManager.getInstance().getDataById(Wallet.class, toMM.getToWalletId());
                Wallet fromWallet = RepositoryManager.getInstance().getDataById(Wallet.class, toMM.getFromWalletId());
                deltaAmountArray[i][index] += toMM.getAmount();
                dailyObjArray[i][index].add(
                        new MoneyMovementUiModel(
                                BopBaseUiModel.DataType.MONEY_MOVEMENT,
                                toMM.getId(),
                                toMM.getAmount(),
                                toMM.getMemo(),
                                toWallet.getName(),
                                fromWallet.getName()
                        )
                );
            }
            for (MoneyMovement fromMM : goOutMmList) {
                int index = fromMM.getDay() - 1;
                Wallet toWallet = RepositoryManager.getInstance().getDataById(Wallet.class, fromMM.getToWalletId());
                Wallet fromWallet = RepositoryManager.getInstance().getDataById(Wallet.class, fromMM.getFromWalletId());
                deltaAmountArray[i][index] += fromMM.getAmount();
                dailyObjArray[i][index].add(
                        new MoneyMovementUiModel(
                                BopBaseUiModel.DataType.MONEY_MOVEMENT,
                                fromMM.getId(),
                                fromMM.getAmount(),
                                fromMM.getMemo(),
                                toWallet.getName(),
                                fromWallet.getName()
                        )
                );
            }
        }
        ArrayList<CalendarDisplayItem> dailyUiModels = new ArrayList<>();
        for (int i = 0; i < endDD; i++) {
            ArrayList<CalendarDisplayItem> walletUiModels = new ArrayList<>();
            for (int j = 0; j < wallets.size(); j++) {
                if (needlessWallets[j]) continue;
                Wallet wallet = wallets.get(j);
                walletUiModels.add(new WalletUiModel(
                    wallet.getId(),
                        wallet.getName(),
                        deltaAmountArray[j][i],
                        // TODO CurrentAmountを計算するようにして。
                        0,
                        dailyObjArray[j][i],
                        false
                ));
            }
            dailyUiModels.add(
                    new DailyUiModel(
                            YY, MM, i + 1,
                            0,
                            walletUiModels,
                            false
                    )
            );
        }

        return dailyUiModels;
    }

    @Override
    List<CalendarDisplayItem> flatten(List<CalendarDisplayItem> beforeData) {
        return Collections.emptyList();
    }
}
