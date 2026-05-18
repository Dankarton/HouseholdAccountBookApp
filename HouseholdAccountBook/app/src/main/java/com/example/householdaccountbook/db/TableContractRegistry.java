package com.example.householdaccountbook.db;

import java.util.HashMap;
import java.util.Map;

import com.example.householdaccountbook.module.dbentity.DatabaseEntity;
import com.example.householdaccountbook.module.dbentity.Expenses;
import com.example.householdaccountbook.module.dbentity.Income;
import com.example.householdaccountbook.module.dbentity.MoneyMovement;
import com.example.householdaccountbook.module.dbentity.MonthlyBalanceDelta;
import com.example.householdaccountbook.module.dbentity.Purchase;
import com.example.householdaccountbook.module.dbentity.PurchaseCategory;
import com.example.householdaccountbook.module.dbentity.IncomeCategory;
import com.example.householdaccountbook.module.dbentity.PaymentMethod;
import com.example.householdaccountbook.module.dbentity.Wallet;

/**
 * DatabaseEntityを実装したクラスとEntryの対応表管理クラス
 * ここに登録することでクラスの型を意識せずにMyDbManagerでデータベースの操作を行えるようになる．
 */
public final class TableContractRegistry {
    private static final Map<Class<? extends DatabaseEntity>, MyDbContract.TableContract<? extends DatabaseEntity>> KINDS_MAP = new HashMap<>();

    static {
        KINDS_MAP.put(Purchase.class, new MyDbContract.PurchaseEntry());
        KINDS_MAP.put(Expenses.class, new MyDbContract.ExpensesEntry());
        KINDS_MAP.put(Income.class, new MyDbContract.IncomeEntry());
        KINDS_MAP.put(MoneyMovement.class, new MyDbContract.MoneyMovementsEntry());
        KINDS_MAP.put(PurchaseCategory.class, new MyDbContract.PurchaseCategoryEntry());
        KINDS_MAP.put(IncomeCategory.class, new MyDbContract.IncomeCategoryEntry());
        KINDS_MAP.put(PaymentMethod.class, new MyDbContract.PaymentMethodEntry());
        KINDS_MAP.put(MonthlyBalanceDelta.class, new MyDbContract.MonthlyBalanceDeltaEntry());
        KINDS_MAP.put(Wallet.class, new MyDbContract.WalletEntry());
    }

    @SuppressWarnings("unchecked")
    public static <T extends DatabaseEntity> MyDbContract.TableContract<T> getContract(Class<? extends DatabaseEntity> clazz) {
         MyDbContract.TableContract<T> value = (MyDbContract.TableContract<T>) KINDS_MAP.get(clazz);
         if (value != null) {
             return value;
         }
         else {
             throw new IllegalArgumentException("DatabaseEntityを実装したクラスはTableContractRegistryに登録してください．未登録クラス: " + clazz.getSimpleName());
         }
    }
}
