package com.example.householdaccountbook.db;

import java.util.HashMap;
import java.util.Map;

import myclasses.DatabaseEntity;
import myclasses.Expenses;
import myclasses.Income;
import myclasses.Purchase;
import myclasses.PurchaseCategory;
import myclasses.IncomeCategory;
import myclasses.PaymentMethod;

public final class TableContractRegistry {
    private static final Map<Class<? extends DatabaseEntity>, MyDbContract.TableContract<? extends DatabaseEntity>> KINDS_MAP = new HashMap<>();

    static {
        KINDS_MAP.put(Purchase.class, new MyDbContract.PurchaseEntry());
        KINDS_MAP.put(Expenses.class, new MyDbContract.ExpensesEntry());
        KINDS_MAP.put(Income.class, new MyDbContract.IncomeEntry());
        KINDS_MAP.put(PurchaseCategory.class, new MyDbContract.PurchaseCategoryEntry());
        KINDS_MAP.put(IncomeCategory.class, new MyDbContract.IncomeCategoryEntry());
        KINDS_MAP.put(PaymentMethod.class, new MyDbContract.PaymentMethodEntry());
    }

    @SuppressWarnings("unchecked")
    public static <T extends DatabaseEntity> MyDbContract.TableContract<T> getContract(Class<? extends DatabaseEntity> clazz) {
         MyDbContract.TableContract<T> value = (MyDbContract.TableContract<T>) KINDS_MAP.get(clazz);
         if (value != null) {
             return value;
         }
         else {
             throw new IllegalArgumentException("登録されていないクラスです: " + clazz.getSimpleName());
         }
    }
}
