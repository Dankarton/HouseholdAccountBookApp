package com.example.householdaccountbook;

import android.app.Application;
import android.util.Log;

import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.repository.CacheProvider;
import com.example.householdaccountbook.repository.DatabaseEntityRepository;

import myclasses.IncomeCategory;
import myclasses.PaymentMethod;
import myclasses.PurchaseCategory;

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

        Log.d("HouseHoldApp", "incomeCategoryRepository size: " + this.incomeCategoryRepository.size());
        Log.d("HouseHoldApp", "purchaseCategoryRepository size: " + this.purchaseCategoryRepository.size());
        Log.d("HouseHoldApp", "paymentMethodRepository size: " + this.paymentMethodRepository.size());
        MyDbManager.setCacheProvider(this);
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
