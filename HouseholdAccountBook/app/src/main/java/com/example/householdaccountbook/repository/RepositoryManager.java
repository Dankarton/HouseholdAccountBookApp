package com.example.householdaccountbook.repository;

import android.util.Log;

import com.example.householdaccountbook.db.MyDbContract;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.module.dbentity.DatabaseEntity;
import com.example.householdaccountbook.module.dbentity.Expenses;
import com.example.householdaccountbook.module.dbentity.Income;
import com.example.householdaccountbook.module.dbentity.IncomeCategory;
import com.example.householdaccountbook.module.dbentity.MoneyMovement;
import com.example.householdaccountbook.module.dbentity.MonthlyBalanceDelta;
import com.example.householdaccountbook.module.dbentity.PaymentMethod;
import com.example.householdaccountbook.module.dbentity.Purchase;
import com.example.householdaccountbook.module.dbentity.PurchaseCategory;
import com.example.householdaccountbook.module.dbentity.Wallet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
    public <T extends DatabaseEntity> ArrayList<T> getAll(Class<T> clazz) {
        return this.db.getAll(clazz);
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
     * @param clazz クラス
     * @param id ID
     * @return List
     * @param <T> DatabaseEntity
     */
    public <T extends DatabaseEntity> T getDataById(Class<T> clazz, long id) {
        T data = null;
        DatabaseEntityRepository<T> repository = this.cache.getDbEntityCache(clazz);
        if (repository != null) {
            data = repository.getDataById(id);
        }
        if (data == null) {
            data = this.db.getDataById(clazz, id);

            if (data != null && repository != null) {
                repository.updateCache(data);
            }
        }
        return data;
    }

    /**
     * WalletIDからIncomeを検
     * @param walletId ウォレットID
     * @param startYY 開始年
     * @param startMM 開始月
     * @param startDD 開始日
     * @param endYY 終了年
     * @param endMM 終了月
     * @param endDD 終了日
     * @return List
     */
    public ArrayList<Income> getIncomeDataByWalletId(long walletId, int startYY, int startMM, int startDD, int endYY, int endMM, int endDD) {
        return db.getDataInRangeWithWallet(Income.class, walletId, startYY, startMM, startDD, endYY, endMM, endDD);
    }

    /**
     *
     * @param walletId
     * @param startYY
     * @param startMM
     * @param startDD
     * @param endYY
     * @param endMM
     * @param endDD
     * @return
     */
    public ArrayList<Expenses> getExpensesDataByWalletId(long walletId, int startYY, int startMM, int startDD, int endYY, int endMM, int endDD) {
        return db.getDataInRangeWithWallet(Expenses.class, walletId, startYY, startMM, startDD, endYY, endMM, endDD);
    }

    /**
     *
     * @param walletId
     * @param startYY
     * @param startMM
     * @param startDD
     * @param endYY
     * @param endMM
     * @param endDD
     * @return
     */
    public ArrayList<MoneyMovement> getMoneyMovementDataByToWalletId(long walletId, int startYY, int startMM, int startDD, int endYY, int endMM, int endDD) {
        return db.getMoneyMovementInRangeWithToWallet(walletId, startYY, startMM, startDD, endYY, endMM, endDD);
    }
    public ArrayList<MoneyMovement> getMoneyMovementDataByFromWalletId(long walletId,int startYY, int startMM, int startDD, int endYY, int endMM, int endDD) {
        return db.getMoneyMovementInRangeWithFromWallet(walletId, startYY, startMM, startDD, endYY, endMM, endDD);
    }
    public MonthlyBalanceDelta getLatestMonthlyBalanceDelta(long walletId, Calendar targetDate) {
        return db.getLatestMonthlyDeltaUpTo(walletId, targetDate);
    }
    public <T extends DatabaseEntity> void setDataSafely(T data) {
        this.db.setDataSafely(data);
        DatabaseEntityRepository<?> repo = this.cache.getDbEntityCache(data.getClass());
        if (repo != null) {
            // repoとdataの型は一致してる
            handleUpdate(repo, data);
        }
    }
    public <T extends DatabaseEntity> void upsertDataSafely(T data) {
        this.db.upsertDatabaseSafely(data);
        DatabaseEntityRepository<?> repo = this.cache.getDbEntityCache(data.getClass());
        if (repo != null) {
            // repoとdataの型は一致してる
            handleUpdate(repo, data);
        }
    }
    public <T extends DatabaseEntity> void deleteDataSafely(T data) {
        this.db.deleteDataSafely(data);
        DatabaseEntityRepository<?> dbEntityRepository = this.cache.getDbEntityCache(data.getClass());
        //
        if (dbEntityRepository != null) {
            handleRemove(dbEntityRepository, data);
        }
    }
    public ArrayList<Expenses> getChildExpensesList(Purchase purchase) {
        return this.db.getData(
                Expenses.class,
                MyDbContract.ExpensesEntry.COLUMN_PURCHASE_ID + " = ?",
                new String[] { String.valueOf(purchase.getId())},
                null, null, null, null
        );
    }

    /**
     * 残高差分データを更新する関数
     * @param date
     * @param walletId
     * @param amount
     */
    public void updateMonthlyBalanceDelta(Calendar date, long walletId, int amount) {
        int targetYearMonthKey = MonthlyBalanceDelta.makeYearMonthKey(date);
        // 対象年月のデータを取得
        String targetSelection = MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_YEAR_MONTH_KEY + " = ?";
        ArrayList<MonthlyBalanceDelta> targetDateData = this.db.getData(
                MonthlyBalanceDelta.class,
                targetSelection,
                new String[]{String.valueOf(targetYearMonthKey)},
                null, null, null, null
        );
        if (targetDateData.isEmpty()) {
            // 対象年月にデータが無いときは新規作成．
            //
            // 前の月の残高差分のデータを基に対象年月の残高差分を算出
            //
            // 対象年月よりも前の年月で最も近いデータを一つだけ取得
            String beforeMonthSelection = MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_YEAR_MONTH_KEY + " < ?";
            String orderBy = MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_YEAR_MONTH_KEY + " DESC";
            ArrayList<MonthlyBalanceDelta> beforeDateData = this.db.getData(
                    MonthlyBalanceDelta.class,
                    beforeMonthSelection,
                    new String[]{String.valueOf(targetYearMonthKey)},
                    null, null, orderBy, "1"
            );
            if (beforeDateData.isEmpty()) {
                // 対象年月よりも前にデータが無いときは，対象年月をrootとする．
                Wallet wallet = getDataById(Wallet.class, walletId);
                this.db.setDataSafely(new MonthlyBalanceDelta(null, walletId, targetYearMonthKey, wallet.getInitAmount() + amount));
            } else {
                // 前の月の月からamount分変更することで対象年月の残高差分になる
                int deltaAmount = beforeDateData.get(0).getDeltaAmount() + amount;
                this.db.setDataSafely(new MonthlyBalanceDelta(null, walletId, targetYearMonthKey, deltaAmount));
            }
        } else {
            // 対象年月に残高差分のデータがすでにあった場合は，amount分金額を増減させる．
            MonthlyBalanceDelta buf = targetDateData.get(0);
            buf.setDeltaAmount(buf.getDeltaAmount() + amount);
            this.db.upsertDatabaseSafely(buf);
        }
        // 残高差分の金額が変わると後の月も影響を受けるので，対象年月よりも後の年月を全て取得
        String afterSelection = MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_YEAR_MONTH_KEY + " > ?";
        ArrayList<MonthlyBalanceDelta> afterDateData = this.db.getData(
                MonthlyBalanceDelta.class,
                afterSelection,
                new String[]{String.valueOf(targetYearMonthKey)},
                null, null, null, null
        );
        for (MonthlyBalanceDelta data : afterDateData) {
            data.setDeltaAmount(data.getDeltaAmount() + amount);
            this.db.upsertDatabaseSafely(data);
        }
    }
    /**
     * ジェネリックを維持しつつ無理やり型一致させてリポジトリのデータをアップデートする関数
     * 型不一致で簡単にエラー吐くから丁寧に使って。
     * @param repo T型のリポジトリ
     * @param data T型のデータ(キャストするためにDatabaseEntityを引数にしてる)
     * @param <T> クラス
     */
    @SuppressWarnings("unchecked")
    private <T extends DatabaseEntity> void handleUpdate(DatabaseEntityRepository<T> repo, DatabaseEntity data) {
        repo.updateCache((T) data);
    }

    /**
     * ジェネリックを維持しつつ無理やり型一致させてリポジトリからデータを除外する関数
     * 型不一致で簡単にエラー吐くから丁寧に使って。
     * @param repo T型のリポジトリ
     * @param data T型のデータ(キャストするためにDatabaseEntityを引数にしてる)
     * @param <T> クラス
     */
    @SuppressWarnings("unchecked")
    private <T extends DatabaseEntity> void handleRemove(DatabaseEntityRepository<T> repo, DatabaseEntity data) {
        repo.removeCache((T) data);
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
