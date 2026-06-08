package com.example.householdaccountbook.repository;

import com.example.householdaccountbook.customviews.calendar.GroupableItem;
import com.example.householdaccountbook.module.calendarentity.BopBaseUiModel;
import com.example.householdaccountbook.module.calendarentity.CalendarDisplayItem;
import com.example.householdaccountbook.module.calendarentity.DailyUiModel;
import com.example.householdaccountbook.module.calendarentity.MoneyMovementUiModel;
import com.example.householdaccountbook.module.calendarentity.TransactionUiModel;
import com.example.householdaccountbook.module.dbentity.BOP;
import com.example.householdaccountbook.module.dbentity.BopCategory;
import com.example.householdaccountbook.module.dbentity.Expenses;
import com.example.householdaccountbook.module.dbentity.HasCategory;
import com.example.householdaccountbook.module.dbentity.Income;
import com.example.householdaccountbook.module.dbentity.MoneyMovement;
import com.example.householdaccountbook.module.dbentity.PaymentMethod;
import com.example.householdaccountbook.module.dbentity.Purchase;
import com.example.householdaccountbook.module.dbentity.Wallet;

import java.util.ArrayList;
import java.util.List;

public class DataAssembler {
    // TODO だいたい完成したけど統合したら上手く動かんかも
    private static DataAssembler instance = null;
    private final RepositoryManager rm;
    private DataAssembler(RepositoryManager rm) {
        this.rm = rm;
    }
    public static void init(RepositoryManager repositoryManager) {
        if (DataAssembler.instance == null) {
            DataAssembler.instance = new DataAssembler(repositoryManager);
        }
    }
    public static DataAssembler getInstance() {
        if (DataAssembler.instance == null) {
            throw new IllegalStateException("DataAssemblerをinstanceが生成される前に使用しています。アプリ開始時のonCreate()にinit()を記述し忘れている可能性があります。");
        }
        return DataAssembler.instance;
    }
    public <T extends BOP & HasCategory> DailyUiModel assembleDailyUiModel(int year, int month, int date, List<Income> incomes, List<T> expOrPurList) {
        List<CalendarDisplayItem> bopUiModelList = new ArrayList<>();
        int totalIncome = 0;
        int totalExpenses = 0;
        for (TransactionUiModel data : assembleTransactionUiModels(incomes)) {
            bopUiModelList.add(data);
            totalIncome += data.getAmount();
        }
        for (TransactionUiModel data : assembleTransactionUiModels(expOrPurList)) {
            bopUiModelList.add(data);
            totalExpenses += data.getAmount();
        }
        return new DailyUiModel(year, month, date, totalIncome - totalExpenses, bopUiModelList, GroupableItem.PositionType.SINGLE, false);
    }
    public DailyUiModel assembleDailyUiModel(
            int year, int month, int date,
            List<Income> incomes, List<Expenses> expenses,
            List<MoneyMovement> toMoneys, List<MoneyMovement> fromMoneys) {
        List<CalendarDisplayItem> bopUiModelList = new ArrayList<>();
        int totalIncome = 0;
        int totalExpenses = 0;
        for (TransactionUiModel data : assembleTransactionUiModels(incomes)) {
            bopUiModelList.add(data);
            totalIncome += data.getAmount();
        }
        for (MoneyMovementUiModel data: assembleMoneyMovementUiModels(fromMoneys)) {
            bopUiModelList.add(data);
            totalIncome += data.getAmount();
        }
        for (TransactionUiModel data :assembleTransactionUiModels(expenses)) {
            bopUiModelList.add(data);
            totalExpenses += data.getAmount();
        }
        for (MoneyMovementUiModel data : assembleMoneyMovementUiModels(toMoneys)) {
            bopUiModelList.add(data);
            totalExpenses += data.getAmount();
        }
        return new DailyUiModel(year, month, date, totalIncome - totalExpenses, bopUiModelList, GroupableItem.PositionType.SINGLE, false);
    }

    public <T extends BOP & HasCategory> List<TransactionUiModel> assembleTransactionUiModels(List<T> dataList) {
        List<TransactionUiModel> uiList = new ArrayList<>();
        for (T data : dataList) {
            uiList.add(this.assemble(data));
        }
        return uiList;
    }
    public List<MoneyMovementUiModel> assembleMoneyMovementUiModels(List<MoneyMovement> dataList) {
        List<MoneyMovementUiModel> uiList = new ArrayList<>();
        for (MoneyMovement data : dataList) {
            uiList.add(this.assemble(data));
        }
        return uiList;
    }

    public <T extends BOP & HasCategory> TransactionUiModel assemble(T data) {
        BopCategory categoryData = this.rm.getDataById(data.getCategoryClass(), data.getCategoryId());
        long id;
        BopBaseUiModel.DataType viewType;
        String additionalMemo = "";
        if (data instanceof Expenses) {
            id = ((Expenses) data).getPurchaseId();
            viewType = BopBaseUiModel.DataType.EXPENSES;
            PaymentMethod pm = this.rm.getDataById(PaymentMethod.class, ((Expenses) data).getPaymentMethodId());
            additionalMemo = pm.getName();
        }
        else if (data instanceof Income){
            id = data.getId();
            viewType = BopBaseUiModel.DataType.INCOME;
            Wallet wallet = this.rm.getDataById(Wallet.class, ((Income) data).getWalletId());
            additionalMemo = wallet.getName();
        }
        else {
            id = data.getId();
            viewType = BopBaseUiModel.DataType.PURCHASE;
            PaymentMethod pm = this.rm.getDataById(PaymentMethod.class, ((Purchase) data).getPaymentMethodId());
            additionalMemo = pm.getName();
        }
        return new TransactionUiModel(
                viewType,
                id,
                data.getAmount(),
                data.getMemo(),
                additionalMemo,
                categoryData.getColorCode(),
                categoryData.getName(),
                GroupableItem.PositionType.SINGLE
        );
    }
    public MoneyMovementUiModel assemble(MoneyMovement data) {
        Wallet toWallet = this.rm.getDataById(Wallet.class, data.getToWalletId());
        Wallet fromWallet = this.rm.getDataById(Wallet.class, data.getFromWalletId());
        return new MoneyMovementUiModel(
                BopBaseUiModel.DataType.MONEY_MOVEMENT,
                data.getId(),
                data.getAmount(),
                data.getMemo(),
                toWallet.getName(),
                fromWallet.getName(),
                GroupableItem.PositionType.SINGLE
        );
    }
}
