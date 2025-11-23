package com.example.householdaccountbook;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.householdaccountbook.db.MyDbContract;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.repository.CacheProvider;
import com.example.householdaccountbook.repository.DatabaseEntityRepository;

import com.example.householdaccountbook.myclasses.dbentity.IncomeCategory;
import com.example.householdaccountbook.myclasses.dbentity.PaymentMethod;
import com.example.householdaccountbook.myclasses.dbentity.PurchaseCategory;

public class HouseHoldApp extends Application implements CacheProvider {
    private DatabaseEntityRepository<IncomeCategory> incomeCategoryRepository;
    private DatabaseEntityRepository<PurchaseCategory> purchaseCategoryRepository;
    private DatabaseEntityRepository<PaymentMethod> paymentMethodRepository;
    @Override
    public void onCreate() {
        super.onCreate();

        // データベース設定
        MyDbManager.setMyOpenHelper(this);

        // キャッシュ用意
        this.incomeCategoryRepository = new DatabaseEntityRepository<IncomeCategory>(IncomeCategory.class);
        this.purchaseCategoryRepository = new DatabaseEntityRepository<PurchaseCategory>(PurchaseCategory.class);
        this.paymentMethodRepository = new DatabaseEntityRepository<PaymentMethod>(PaymentMethod.class);

        this.incomeCategoryRepository.init();
        this.purchaseCategoryRepository.init();
        this.paymentMethodRepository.init();

        MyDbManager.setCacheProvider(this);

        // 初回起動時の場合，プレデータをデータベースに挿入する．
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isFirstLaunch = preferences.getBoolean("is_first_launch", true);
        if (isFirstLaunch) {
            insertPreData();
            preferences.edit().putBoolean("is_first_launch", false).apply();
        }
    }
    private void insertPreData() {
        for (PurchaseCategory pc : MyDbContract.PurchaseCategoryEntry.PRE_DATA_LIST) {
            MyDbManager.setDataSafely(pc);
        }
        for (IncomeCategory ic : MyDbContract.IncomeCategoryEntry.PRE_DATA_LIST) {
            MyDbManager.setDataSafely(ic);
        }
        MyDbManager.upsertDatabaseSafely(MyDbContract.PaymentMethodEntry.DEFAULT_PAYMENT_METHOD);
        for (PaymentMethod pm : MyDbContract.PaymentMethodEntry.PRE_DATA_LIST) {
            MyDbManager.setDataSafely(pm);
        }
    }
    public DatabaseEntityRepository<IncomeCategory> getIncomeCategoryRepository() {
        return this.incomeCategoryRepository;
    }
    public DatabaseEntityRepository<PurchaseCategory> getPurchaseCategoryRepository() {
        return this.purchaseCategoryRepository;
    }
    public DatabaseEntityRepository<PaymentMethod> getPaymentMethodRepository() {
        return this.paymentMethodRepository;
    }
}
