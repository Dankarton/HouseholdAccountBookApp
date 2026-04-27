package com.example.householdaccountbook.repository;

import com.example.householdaccountbook.myclasses.BopBaseUiModel;
import com.example.householdaccountbook.myclasses.DailyUiModel;
import com.example.householdaccountbook.myclasses.MoneyMovementUiModel;
import com.example.householdaccountbook.myclasses.TransactionUiModel;
import com.example.householdaccountbook.myclasses.dbentity.BOP;
import com.example.householdaccountbook.myclasses.dbentity.BopCategory;
import com.example.householdaccountbook.myclasses.dbentity.Expenses;
import com.example.householdaccountbook.myclasses.dbentity.HasCategory;
import com.example.householdaccountbook.myclasses.dbentity.Income;
import com.example.householdaccountbook.myclasses.dbentity.MoneyMovement;
import com.example.householdaccountbook.myclasses.dbentity.Wallet;

import java.util.ArrayList;
import java.util.Calendar;
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
        List<BopBaseUiModel> bopUiModelList = new ArrayList<>();
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
        return new DailyUiModel(year, month, date, totalIncome, totalExpenses, bopUiModelList);
    }
    public DailyUiModel assembleDailyUiModel(
            int year, int month, int date,
            List<Income> incomes, List<Expenses> expenses,
            List<MoneyMovement> toMoneys, List<MoneyMovement> fromMoneys) {
        List<BopBaseUiModel> bopUiModelList = new ArrayList<>();
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
        return new DailyUiModel(year, month, date, totalIncome, totalExpenses, bopUiModelList);
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
        BopBaseUiModel.ViewType viewType;
        if (data instanceof Expenses) {
            id = ((Expenses) data).getPurchaseId();
            viewType = BopBaseUiModel.ViewType.EXPENSES;
        }
        else {
            id = data.getId();
            if (data instanceof Income) {
                viewType = BopBaseUiModel.ViewType.INCOME;
            }
            else {
                viewType = BopBaseUiModel.ViewType.PURCHASE;
            }
        }
        return new TransactionUiModel(
                viewType,
                id,
                data.getAmount(),
                data.getMemo(),
                categoryData.getColorCode(),
                categoryData.getName()
        );
    }
    public MoneyMovementUiModel assemble(MoneyMovement data) {
        Wallet toWallet = this.rm.getDataById(Wallet.class, data.getToWalletId());
        Wallet fromWallet = this.rm.getDataById(Wallet.class, data.getFromWalletId());
        return new MoneyMovementUiModel(
                BopBaseUiModel.ViewType.MONEY_MOVEMENT,
                data.getId(),
                data.getAmount(),
                data.getMemo(),
                toWallet.getName(),
                fromWallet.getName()
        );
    }
}
