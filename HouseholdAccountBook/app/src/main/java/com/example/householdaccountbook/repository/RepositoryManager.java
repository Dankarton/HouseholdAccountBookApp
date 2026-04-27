package com.example.householdaccountbook.repository;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.householdaccountbook.db.MyDbContract;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.myclasses.DailyUiModel;
import com.example.householdaccountbook.myclasses.dbentity.BOP;
import com.example.householdaccountbook.myclasses.dbentity.DatabaseEntity;
import com.example.householdaccountbook.myclasses.dbentity.Expenses;
import com.example.householdaccountbook.myclasses.dbentity.HasDate;
import com.example.householdaccountbook.myclasses.dbentity.Income;
import com.example.householdaccountbook.myclasses.dbentity.IncomeCategory;
import com.example.householdaccountbook.myclasses.dbentity.PaymentMethod;
import com.example.householdaccountbook.myclasses.dbentity.PurchaseCategory;
import com.example.householdaccountbook.myclasses.dbentity.Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepositoryManager {
    private static RepositoryManager instance;
    private final MyDbManager db;
    private final AppCache cache;

    private RepositoryManager(MyDbManager db) {
        this.db = db;
        this.cache = new AppCache(db);

    }
    public static void init(MyDbManager db) {
        if (RepositoryManager.instance == null) {
            RepositoryManager.instance = new RepositoryManager(db);
        }
    }
    public static RepositoryManager getInstance() {
        if (RepositoryManager.instance == null) {
            throw new IllegalStateException("RepositoryManagerをinstanceが生成される前に使用しています。アプリ開始時のonCreate()にinit()を記述し忘れている可能性があります。");
        }
        return RepositoryManager.instance;
    }

    /**
     * データ構造を考慮し、論理削除されたデータを除外して取得する
     * @param clazz クラス
     * @return リスト
     * @param <T> DatabaseEntityを実装したクラス
     */
    public <T extends DatabaseEntity> ArrayList<T> getAllActive(Class<T> clazz) {
        return this.db.getAllSafely(clazz);
    }

    /**
     * BOP データを範囲指定で取得する関数
     * @param clazz 対象クラス(BOPを継承していること)
     * @param startYY 開始年
     * @param startMM 開始月
     * @param startDD 開始日
     * @param endYY 終了年
     * @param endMM 終了月
     * @param endDD 終了日
     * @return ArrayList<BOP>
     * @param <T> BOP
     */
    public <T extends DatabaseEntity, HasDate> ArrayList<T> getBopDataInRange(Class<T> clazz, int startYY, int startMM, int startDD, int endYY, int endMM, int endDD) {
        return this.db.getDataInRange(clazz, startYY, startMM, startDD, endYY, endMM, endDD);
    }

    /**
     * DatabaseEntityをID検索から取得する関数。データがキャッシュにあれば、そこから優先的に取得。
     * @param clazz
     * @param id
     * @return
     * @param <T>
     */
    public <T extends DatabaseEntity> T getDataById(Class<T> clazz, long id) {
        T data = null;
        DatabaseEntityRepository<T> repository = this.cache.getDbEntityCache(clazz);
        if (repository != null) {
            data = repository.getDataById(id);
        }
        if (data == null) {
            data = this.db.getDataByIdupd(clazz, id);

            if (data != null && repository != null) {
                repository.updateCache(data);
            }
        }
        return data;
    }
    public ArrayList<Income> getIncomeDataByWalletId(long walletId, int startYY, int startMM, int startDD, int endYY, int endMM, int endDD) {
        return db.getDataInRangeWithWallet(Income.class, walletId, startYY, startMM, startDD, endYY, endMM, endDD);
    }
    public ArrayList<Expenses> getExpensesDataByWalletId(long walletId, int startYY, int startMM, int startDD, int endYY, int endMM, int endDD) {
        return db.getDataInRangeWithWallet(Expenses.class, walletId, startYY, startMM, startDD, endYY, endMM, endDD);
    }



    /**
     * キャッシュ管理専用クラス
     */
    private class AppCache {
        // TODO Class<?>じゃなくてDatabaseEntityを継承したClassってこと明記した方がいいかも
        public final HashMap<Class<? extends DatabaseEntity>, DatabaseEntityRepository<? extends DatabaseEntity>> repositoryMap;

        public AppCache(MyDbManager db) {
            this.repositoryMap = new HashMap<>();
            // レジストリ登録作業
            this.repositoryMap.put(IncomeCategory.class, new DatabaseEntityRepository<>(IncomeCategory.class));
            this.repositoryMap.put(PurchaseCategory.class, new DatabaseEntityRepository<>(PurchaseCategory.class));
            this.repositoryMap.put(PaymentMethod.class, new DatabaseEntityRepository<>(PaymentMethod.class));
            this.repositoryMap.put(Wallet.class, new DatabaseEntityRepository<>(Wallet.class));
            // 初期化
            for (var val : this.repositoryMap.values()) {
                val.init(db);
            }
        }
        public <T extends DatabaseEntity> DatabaseEntityRepository<T> getDbEntityCache(Class<T> clazz) {
            DatabaseEntityRepository<T> value = (DatabaseEntityRepository<T>) this.repositoryMap.get(clazz);
            if (value != null) {
                return value;
            }
            else {
                Log.w("RepositoryManager", "Repositoryが存在しません．未登録クラス: " + clazz.getSimpleName() + "\n登録が不要なクラスであればこの警告は無視してください．");
                return null;
            }
        }
    }
}
