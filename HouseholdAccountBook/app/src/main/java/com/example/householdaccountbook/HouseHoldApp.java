package com.example.householdaccountbook;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.householdaccountbook.db.MyDbContract;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.module.dbentity.Income;
import com.example.householdaccountbook.module.dbentity.MoneyMovement;
import com.example.householdaccountbook.repository.DataAssembler;

import com.example.householdaccountbook.module.dbentity.IncomeCategory;
import com.example.householdaccountbook.module.dbentity.PaymentMethod;
import com.example.householdaccountbook.module.dbentity.PurchaseCategory;
import com.example.householdaccountbook.repository.RepositoryManager;

import java.util.List;

public class HouseHoldApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 管理クラス初期化インスタンス化
        MyDbManager.init(this);
        RepositoryManager.init(MyDbManager.getInstance());
        DataAssembler.init(RepositoryManager.getInstance());

        // 初回起動時の場合，プレデータをデータベースに挿入する．
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isFirstLaunch = preferences.getBoolean("is_first_launch", true);
        if (isFirstLaunch) {
            insertPreData();
            preferences.edit().putBoolean("is_first_launch", false).apply();
        }

        // テストブロック
        {
            List<Income> il = RepositoryManager.getInstance().getAll(Income.class);
            List<MoneyMovement> ml = RepositoryManager.getInstance().getAll(MoneyMovement.class);
            Log.d("App", "Income count: " + il.size());
            Log.d("App", "MM count: " + ml.size());
            for (Income data : il) {
                Log.d("App", "Income [id: " + data.getId() + ", YY: " + data.getYear() + ", MM: " + data.getMonth() + " , DD: " + data.getDay() + ", amount: " + data.getAmount() + ", walletID: " + data.getWalletId() + "]");
            }
            for (MoneyMovement data : ml) {
                Log.d("App", "MoneyMovement [id: " + data.getId() + ", YY: " + data.getYear() + ", MM: " + data.getMonth() + " , DD: " + data.getDay() + ", amount: " + data.getAmount() + ", toWalletID: " + data.getToWalletId() + ", fromWalletID: " + data.getFromWalletId());
            }
        }

    }
    private void insertPreData() {
        Log.d("HouseHoldApp", "初期データ挿入");
        for (PurchaseCategory pc : MyDbContract.PurchaseCategoryEntry.PRE_DATA_LIST) {
            MyDbManager.getInstance().setDataSafely(pc);
        }
        for (IncomeCategory ic : MyDbContract.IncomeCategoryEntry.PRE_DATA_LIST) {
            MyDbManager.getInstance().setDataSafely(ic);
        }
        MyDbManager.getInstance().upsertDatabaseSafely(MyDbContract.PaymentMethodEntry.DEFAULT_PAYMENT_METHOD);
        for (PaymentMethod pm : MyDbContract.PaymentMethodEntry.PRE_DATA_LIST) {
            MyDbManager.getInstance().setDataSafely(pm);
        }
        MyDbManager.getInstance().upsertDatabaseSafely(MyDbContract.WalletEntry.DEFAULT_WALLET);
    }
}
