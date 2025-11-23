package com.example.householdaccountbook.repository;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import com.example.householdaccountbook.myclasses.dbentity.DatabaseEntity;
import com.example.householdaccountbook.myclasses.dbentity.IncomeCategory;
import com.example.householdaccountbook.myclasses.dbentity.PaymentMethod;
import com.example.householdaccountbook.myclasses.dbentity.PurchaseCategory;

public class DbEntityRepositoryRegistry {
    public static final Map<Class<?>, DatabaseEntityRepository<?>> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put(PurchaseCategory.class, new DatabaseEntityRepository<>(PurchaseCategory.class));
        REGISTRY.put(IncomeCategory.class, new DatabaseEntityRepository<>(IncomeCategory.class));
        REGISTRY.put(PaymentMethod.class, new DatabaseEntityRepository<>(PaymentMethod.class));
    }

    @SuppressWarnings("unchecked")
    public static <T extends DatabaseEntity> DatabaseEntityRepository<T> getRepository(Class<T> clazz) {
        DatabaseEntityRepository<T> value =(DatabaseEntityRepository<T>) DbEntityRepositoryRegistry.REGISTRY.get(clazz);
        if (value != null) {
            return value;
        }
        else {
            Log.w("DbEntityRepositoryRegistry.getRepository", "Repositoryが存在しません．未登録クラス: " + clazz.getSimpleName() + "\n登録が不要なクラスであればこの警告は無視してください．");
            return null;
        }
    }
}
