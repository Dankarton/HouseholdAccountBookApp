package com.example.householdaccountbook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.householdaccountbook.repository.CacheProvider;

import java.util.ArrayList;
import java.util.Calendar;

import myclasses.BOP;
import myclasses.DailyBop;
import myclasses.Expenses;
import myclasses.MonthlyBalanceDelta;
import myclasses.PurchaseCategory;
import myclasses.Income;
import myclasses.IncomeCategory;
import myclasses.PaymentMethod;
import myclasses.DatabaseEntity;
import myclasses.Purchase;

public class MyDbManager {
    // フィールド

    /**
     * MyOpenHelper格納用
     * ヘルパーセット前に使おうとするとエラーを出すようにしたかっただけ．
     */
    private static class MyOpenHelperContainer {
        private static MyOpenHelper helper = null;

        /**
         * Helperセット
         *
         * @param helper MyOpenHelper
         */
        public static void setHelper(MyOpenHelper helper) {
            MyOpenHelperContainer.helper = helper;
        }

        /**
         * Helper取得
         *
         * @return MyOpenHelper
         */
        public static MyOpenHelper getHelper() {
            if (MyOpenHelperContainer.helper != null) {
                return MyOpenHelperContainer.helper;
            } else {
                throw new NullPointerException(
                        "MyDbManagerにMyOpenHelperがセットされる前に，データベースの参照を行おうとしています．"
                );
            }
        }

        public boolean existHelper() {
            return MyOpenHelperContainer.helper != null;
        }
    }

    private static CacheProvider cacheProvider = null;

    public static void setMyOpenHelper(Context context) {
        MyOpenHelperContainer.setHelper(new MyOpenHelper(context));
    }

    public static void setCacheProvider(CacheProvider provider) {
        MyDbManager.cacheProvider = provider;
    }


    /**
     * デフォルトの支払方法を確保する関数
     * デフォルトの支払方法(通常支払い)が不具合でデータベースから削除されても自動で補完するためのもの
     */
    public static void ensureDefaultPayments() {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getWritableDatabase();
        // デフォルトの支払い方法のIDで検索をかける
        Cursor cursor = db.query(
                MyDbContract.PaymentMethodEntry.TABLE_NAME,
                new String[]{"COUNT(*)"},
                MyDbContract.PaymentMethodEntry.ID + " = ?",
                new String[]{
                        String.valueOf(MyDbContract.PaymentMethodEntry.DEFAULT_PAYMENT_METHOD.getId())
                },
                null,
                null,
                null
        );
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        // 件数が1以上だったら登録する必要はないので終了
        if (count > 0) return;
        // デフォルトの支払方法を新規作成
        ArrayList<PaymentMethod> newlyList = MyDbManager.getAll(PaymentMethod.class);
        // リストの一番最初に来るように登録
        ContentValues values = MyDbContract.PaymentMethodEntry.DEFAULT_PAYMENT_METHOD.getContentValues();
        values.put(
                MyDbContract.PaymentMethodEntry.ID,
                MyDbContract.PaymentMethodEntry.DEFAULT_PAYMENT_METHOD.getId()
        );
        db.insert(MyDbContract.PaymentMethodEntry.TABLE_NAME, null, values);
        // 他のPaymentMethodのインデックスを更新
        for (int i = 1; i < newlyList.size(); i++) {
            // 先頭はデフォルトの支払が来るためindexを+1してる
            newlyList.get(i).setIndex(i + 1);
            upsertDatabase(db, newlyList.get(i));
        }
    }

    /**
     * データの新規追加
     *
     * @param data データクラス
     * @param <T>  extends DatabaseEntity
     * @return id番号
     */
    private static <T extends DatabaseEntity> long setData(SQLiteDatabase db, MyDbContract.TableContract<T> contract, T data) {
        if (data.getId() != null) {
            throw new IllegalArgumentException(
                    "データにIDが設定されています．既存データを謝って登録している可能性があります: id = " + data.getId()
            );
        }
        return db.insert(contract.getTableName(), null, data.getContentValues());
    }

    private static <T extends DatabaseEntity> long setData(T data) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getWritableDatabase();
        return setData(db, TableContractRegistry.getContract(data.getClass()), data);
    }

    /**
     * アプリ内のデータ構造を考慮して新規データを追加する関数
     *
     * @param data データ
     * @param <T>  DatabaseEntity
     */
    public static <T extends DatabaseEntity> void setDataSafely(T data) {
        if (data.getId() != null) {
            throw new IllegalArgumentException(
                    "データにIDが設定されています．既存データを謝って登録している可能性があります: id = " + data.getId()
            );
        }
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getWritableDatabase();
        Long newId = null;
        if (data.getClass() == Purchase.class) {
            newId = setData(db, TableContractRegistry.getContract(data.getClass()), data);
            if (newId == -1) {
                Log.d("MyDbManager.setData", "データの挿入に失敗しました．");
                return;
            }
            Log.d("MyDbManager.setDataSafely", "Purchase categoryId: " + ((Purchase) data).getCategoryId() + ", paymentMethodId: " + ((Purchase) data).getPaymentMethodId());
            //
            // Purchaseの子オブジェクトExpensesを生成する処理
            //
            // Dbに登録されたIDデータを新たに含んだPurchase
            Purchase newPurchase = new Purchase(
                    newId,
                    ((Purchase) data).getDate(),
                    ((Purchase) data).getAmount(),
                    ((Purchase) data).getMemo(),
                    ((Purchase) data).getCategoryId(),
                    ((Purchase) data).getPaymentMethodId(),
                    ((Purchase) data).getPaymentTiming()
            );
            // キャッシュから支払い方法を取得してExpensesを生成
            ArrayList<Expenses> newExpList = cacheProvider.getPaymentMethodRepository()
                    .getDataById(newPurchase.getPaymentMethodId())
                    .makeExpenses(newPurchase);
            // Expensesリストセット
            for (Expenses exp : newExpList) {
                setDataSafely(exp);
            }
        } else if (data.getClass() == Income.class) {
            setData(data);
            updateMonthlyBalanceDelta(((Income) data).getDate(), Math.abs(((Income) data).getAmount()));
        } else if (data.getClass() == Expenses.class) {
            Log.d("setDataSafely", "amount: " + ((Expenses) data).getAmount());
            setData(data);
            updateMonthlyBalanceDelta(((Expenses) data).getDate(), -1 * Math.abs(((Expenses) data).getAmount()));
        } else {
            // それ以外のデータはそのままセット
            newId = setData(db, TableContractRegistry.getContract(data.getClass()), data);
        }
        //
        // 新しく追加されたデータをキャッシュに登録
        //
        if (data.getClass() == IncomeCategory.class) {
            IncomeCategory incomeCategory = new IncomeCategory(
                    newId,
                    ((IncomeCategory) data).getName(),
                    ((IncomeCategory) data).getColorCode(),
                    ((IncomeCategory) data).getIndex(),
                    ((IncomeCategory) data).isDeleted()
            );
            cacheProvider.getIncomeCategoryRepository().updateCache(incomeCategory);
        } else if (data.getClass() == PurchaseCategory.class) {
            PurchaseCategory purchaseCategory = new PurchaseCategory(
                    newId,
                    ((PurchaseCategory) data).getName(),
                    ((PurchaseCategory) data).getColorCode(),
                    ((PurchaseCategory) data).getIndex(),
                    ((PurchaseCategory) data).isDeleted()
            );
            cacheProvider.getPurchaseCategoryRepository().updateCache(purchaseCategory);
        } else if (data.getClass() == PaymentMethod.class) {
            PaymentMethod paymentMethod = new PaymentMethod(
                    newId,
                    ((PaymentMethod) data).getName(),
                    ((PaymentMethod) data).getClosingRule().getCode(),
                    ((PaymentMethod) data).getClosingSettingNum(),
                    ((PaymentMethod) data).getPaymentRule().getCode(),
                    ((PaymentMethod) data).getPaymentSettingNum(),
                    ((PaymentMethod) data).getIndex(),
                    ((PaymentMethod) data).isDefault()
            );
            cacheProvider.getPaymentMethodRepository().updateCache(paymentMethod);
        }
    }

    /**
     * Database内の対応IDのデータを更新(データが無い場合は挿入)する関数
     *
     * @param db   SQLiteDatabase
     * @param data データクラス
     * @param <T>  DatabaseEntityを継承したデータクラス
     * @return boolean 成功するとtrue，失敗するとfalse
     */
    private static <T extends DatabaseEntity> boolean upsertDatabase(SQLiteDatabase db, T data) {
        Long id = data.getId();
        if (id == null) {
            Log.e("MyDbManager", "upsertDatabase: idがnullのため更新・挿入を中止しました．");
            return false;
        }
        // dataのクラスから対応するTableContractを取得
        final MyDbContract.TableContract<T> tableContract = TableContractRegistry.getContract(data.getClass());
        final String tableName = tableContract.getTableName();
        ContentValues dataValues = data.getContentValues();
        dataValues.put(tableContract.getIdColumnName(), data.getId());
        // アップデート
        int updatedRows = db.update(
                tableName,
                dataValues,
                tableContract.getIdColumnName() + " = ?",
                new String[]{String.valueOf(id)}
        );
        // アップデートに成功したら終了
        if (updatedRows > 0) return true;
        // データが存在しない場合，挿入に切り替え
        long result = db.insert(tableName, null, dataValues);
        if (result == -1) {
            // 挿入にも失敗した場合
            Log.e("MyDbManager", "upsertDatabase: insertに失敗しました．(" + tableName + ")");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Database内の対応IDのデータを更新(データが無い場合は同IDで挿入)する関数
     *
     * @param data データ
     * @param <T>  DatabaseEntityを継承したデータクラス
     * @return boolean 成功するとtrue,失敗するとfalse
     */
    private static <T extends DatabaseEntity> boolean upsertDatabase(T data) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getWritableDatabase();
        return MyDbManager.upsertDatabase(db, data);
    }

    /**
     * データ構造を考慮した安全なデータ更新
     *
     * @param data データ
     * @param <T>  DatabaseEntityを継承したデータクラス
     */
    public static <T extends DatabaseEntity> void upsertDatabaseSafely(T data) {
        if (data.getId() == null) {
            Log.e("MyDbManager", "upsertDatabase: idがnullのため更新・挿入を中止しました．");
            return;
        }
        if (data.getClass() == Purchase.class) {
            // Purchaseに付随するExpensesデータも削除する必要があるので，Purchase諸々，一度すべて削除
            deleteDataSafely(data);
            // Purchaseの子オブジェクトExpensesを生成
            ArrayList<Expenses> newExpList = cacheProvider.getPaymentMethodRepository()
                    .getDataById(((Purchase) data).getPaymentMethodId())
                    .makeExpenses((Purchase) data);
            // Purchase挿入
            upsertDatabase(data);
            // Expenses挿入
            for (Expenses exp : newExpList) {
                setDataSafely(exp);
            }
        } else if (data.getClass() == Income.class) {
            // 過去の収入データを取得
            Income beforeIncome = getDataById(Income.class, data.getId());
            int deltaAmount;
            if (beforeIncome != null) {
                // 過去のデータとの変更差分
                deltaAmount = ((Income) data).getAmount() - beforeIncome.getAmount();
            } else {
                // 過去データが無かった時
                deltaAmount = ((Income) data).getAmount();
            }
            upsertDatabase(data);
            updateMonthlyBalanceDelta(((Income) data).getDate(), deltaAmount);

        } else if (data.getClass() == Expenses.class) {
            Expenses beforeExpenses = getDataById(Expenses.class, data.getId());
            int deltaAmount;
            if (beforeExpenses != null) {
                deltaAmount = Math.abs(((Expenses) data).getAmount()) - Math.abs(beforeExpenses.getAmount());
            } else {
                deltaAmount = ((Expenses) data).getAmount();
            }
            upsertDatabase(data);
            updateMonthlyBalanceDelta(((Expenses) data).getDate(), -1 * deltaAmount);
        } else {
            // それ以外のデータはただ更新するだけ．
            upsertDatabase(data);
        }
        // キャッシュの更新
        if (data.getClass() == IncomeCategory.class) {
            cacheProvider.getIncomeCategoryRepository().updateCache((IncomeCategory) data);
        } else if (data.getClass() == PurchaseCategory.class) {
            cacheProvider.getPurchaseCategoryRepository().updateCache((PurchaseCategory) data);
        } else if (data.getClass() == PaymentMethod.class) {
            cacheProvider.getPaymentMethodRepository().updateCache((PaymentMethod) data);
        }
    }

    /**
     * アプリケーションのデータ構造を考慮した安全な削除処理
     * (DatabaseEntityインターフェース利用)
     *
     * @param data <T extends DatabaseEntity>
     */
    public static <T extends DatabaseEntity> void deleteDataSafely(T data) {
        if (data.getClass() == Purchase.class) {
            // 購入日に付随する全ての支払日も削除
            ArrayList<Expenses> deleteExpDataList = getData(
                    Expenses.class,
                    MyDbContract.ExpensesEntry.COLUMN_PURCHASE_ID + " = ?",
                    new String[]{String.valueOf(data.getId())},
                    null, null, null, null
            );
            // 支払の削除は残高差分が変動するため，deleteDataSafelyを使ってデータの依存関係を維持して削除
            for (Expenses exp : deleteExpDataList) {
                deleteDataSafely(exp);
            }
            deleteData(data);
        } else if (data.getClass() == PurchaseCategory.class) {
            // カテゴリは過去の購入日などが参照する可能性があるため見かけ上の削除
            ((PurchaseCategory) data).setIsDeleted(true);
            upsertDatabase(data);
        } else if (data.getClass() == IncomeCategory.class) {
            // カテゴリは過去の購入日などが参照する可能性があるため見かけ上の削除
            ((IncomeCategory) data).setIsDeleted(true);
            upsertDatabase(data);
        } else if (data.getClass() == Income.class) {
            // 収入のデータが削除されると，残高差分はマイナス側に移動させる．
            updateMonthlyBalanceDelta(((Income) data).getDate(), -1 * Math.abs(((Income) data).getAmount()));
            deleteData(data);
        } else if (data.getClass() == Expenses.class) {
            // 支出のデータが削除されると，残高差分はプラス側に移動させる．
            updateMonthlyBalanceDelta(((Expenses) data).getDate(), Math.abs(((Expenses) data).getAmount()));
            deleteData(data);
        } else {
            deleteData(data);
        }
        // キャッシュの更新
        if (data.getClass() == IncomeCategory.class) {
            // 支払い方法はデータ本体ごと消えるので削除
            cacheProvider.getIncomeCategoryRepository().removeCache((IncomeCategory) data);
        } else if (data.getClass() == PurchaseCategory.class) {
            // カテゴリはデータ本体は削除されないのでキャッシュに行う操作はデータ更新になる
            cacheProvider.getPurchaseCategoryRepository().updateCache((PurchaseCategory) data);
        } else if (data.getClass() == PaymentMethod.class) {
            // カテゴリはデータ本体は削除されないのでキャッシュに行う操作はデータ更新になる
            cacheProvider.getPaymentMethodRepository().updateCache((PaymentMethod) data);
        }
    }

    /**
     * データ削除(低レベル操作，依存関係などを無視して指定されたデータを削除する)
     * アプリケーションのデータ構造を維持したまま削除するなら deleteDataSafely を使うように!
     */
    private static <T extends DatabaseEntity> void deleteData(T data) {
        if (data.getId() == null) {
            Log.d("MyDbManager.deleteData", "IDがnullです．データベースに追加前のデータを削除することはできません．");
            return;
        }
        deleteData(data.getClass(), data.getId());
    }

    /**
     * データ削除(低レベル操作，依存関係などを無視して指定されたデータを削除する)
     * アプリケーションのデータ構造を維持したまま削除するなら deleteDataSafely を使うように!
     *
     * @param clazz データクラス
     * @param id    ID
     * @param <T>   DatabaseEntity
     */
    private static <T extends DatabaseEntity> void deleteData(Class<T> clazz, long id) {
        MyDbContract.TableContract<T> classKind = TableContractRegistry.getContract(clazz);

        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getReadableDatabase();
        int deletedRowNum = db.delete(
                classKind.getTableName(),
                classKind.getIdColumnName() + " = ?",
                new String[]{String.valueOf(id)}
        );
        if (deletedRowNum == 0) {
            Log.d("MyDbManager.deleteData", "削除されたデータが0件です．Class: " + classKind.getClass().getSimpleName() + "，id: " + id);
        }
    }

    /**
     * Idからデータを取得する関数
     *
     * @param clazz クラス
     * @param id    ID
     * @param <T>   DatabaseEntity
     * @return データ
     */
    public static <T extends DatabaseEntity> T getDataById(Class<T> clazz, Long id) {
        if (id == null) {
            Log.d("MyDbManager.getDataById", "IDがnullです．検索が行えません．" + clazz.getSimpleName());
            return null;
        }
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getReadableDatabase();
        MyDbContract.TableContract<T> contract = TableContractRegistry.getContract(clazz);
        Cursor cursor = db.query(
                contract.getTableName(),
                contract.getColumns(),
                contract.getIdColumnName() + " = " + id,
                null,
                null,
                null,
                null
        );
        T data = null;
        if (cursor.moveToFirst()) {
            data = contract.fromCursor(cursor);
        }
        cursor.close();
        return data;
    }

    /**
     * 収支データを日付から取得する関数
     *
     * @param clazz BOPを継承したクラス(Purchase, Expenses, Income)
     * @param year  年
     * @param month 月
     * @param day   日
     * @param <T>   BOP
     * @return ArrayList
     */
    public static <T extends BOP> ArrayList<T> getBopDataByDate(Class<T> clazz, Integer year, Integer month, Integer day) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getReadableDatabase();
        MyDbContract.TableContract<T> contract = TableContractRegistry.getContract(clazz);
        String selection = buildWhereClauseByDate(
                year, month, day,
                MyDbContract.BaseBopEntry.COLUMN_YEAR,
                MyDbContract.BaseBopEntry.COLUMN_MONTH,
                MyDbContract.BaseBopEntry.COLUMN_DAY
        );
        String orderBy = MyDbContract.BaseBopEntry.COLUMN_YEAR + " ASC, "
                + MyDbContract.BaseBopEntry.COLUMN_MONTH + " ASC, "
                + MyDbContract.BaseBopEntry.COLUMN_DAY + " ASC";
        Cursor cursor = db.query(
                contract.getTableName(),
                contract.getColumns(),
                selection,
                null,
                null,
                null,
                orderBy
        );
        ArrayList<T> bopDataList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                bopDataList.add(contract.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        ;
        cursor.close();
        return bopDataList;
    }

    /**
     * 過去からdateまでの中で最も新しい残高差分のデータを返す関数．
     * @param date
     * @return
     */
    public static MonthlyBalanceDelta getLatestMonthlyDeltaUpTo(Calendar date) {
        ArrayList<MonthlyBalanceDelta> dataList = getData(
                MonthlyBalanceDelta.class,
                MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_YEAR_MONTH_KEY + " <= ?",
                new String[]{String.valueOf(MonthlyBalanceDelta.makeYearMonthKey(date))},
                null,
                null,
                MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_YEAR_MONTH_KEY + " DESC",
                "1"
                );
        if (dataList.isEmpty()) {
            return null;
        }
        else {
            return dataList.get(0);
        }
    }

    public static DailyBop getDailyData(int year, int month, int day) {
        ArrayList<Purchase> purchaseList = MyDbManager.getBopDataByDate(Purchase.class, year, month, day);
        ArrayList<Expenses> expensesList = MyDbManager.getBopDataByDate(Expenses.class, year, month, day);
        ArrayList<Income> incomeList = MyDbManager.getBopDataByDate(Income.class, year, month, day);
        // 収入も支出もない場合
        if ((purchaseList.isEmpty() && expensesList.isEmpty()) && incomeList.isEmpty()) {
            return null;
        }
        return new DailyBop(year, month, day, incomeList, purchaseList, expensesList);
    }

    /**
     * アプリのデータ構造を考慮して全てのデータを取得する
     *
     * @param clazz クラス
     * @param <T>   DatabaseEntityを実装したクラス
     * @return リスト
     */
    public static <T extends DatabaseEntity> ArrayList<T> getAllSafely(Class<T> clazz) {
        if (clazz == PurchaseCategory.class || clazz == IncomeCategory.class) {
            // 論理削除されてないデータだけを取得
            String selection = MyDbContract.BaseCategoryEntry.COLUMN_IS_DELETED + " = ?";
            return getData(clazz, selection, new String[]{"0"}, null, null, null, null);
        } else {
            return getAll(clazz);
        }
    }

    /**
     * 全てのデータ取得 (TableContractインターフェース利用)
     *
     * @param clazz データクラス
     * @param <T>   DatabaseEntity継承済み
     * @return ArrayList
     */
    public static <T extends DatabaseEntity> ArrayList<T> getAll(Class<T> clazz) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getReadableDatabase();
        MyDbContract.TableContract<T> contract = TableContractRegistry.getContract(clazz);
        ArrayList<T> result = new ArrayList<>();
        Cursor cursor = db.query(
                contract.getTableName(),
                contract.getColumns(),
                null,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            do {
                result.add(contract.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        ;
        cursor.close();
        return result;
    }

    /**
     * 詳細な指定を出来るようにしたデータ取得関数
     *
     * @param clazz     クラス
     * @param selection 　SELECT句
     * @param orderBy   ORDER句
     * @param <T>       DatabaseEntity
     * @return ArrayList
     */
    private static <T extends DatabaseEntity> ArrayList<T> getData(Class<T> clazz, String selection,
                                                                   String[] selectionArgs, String groupBy,
                                                                   String having, String orderBy, String limit) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getReadableDatabase();
        MyDbContract.TableContract<T> contract = TableContractRegistry.getContract(clazz);
        ArrayList<T> result = new ArrayList<>();
        Cursor cursor = db.query(
                contract.getTableName(),
                contract.getColumns(),
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy,
                limit
        );
        if (cursor.moveToFirst()) {
            do {
                result.add(contract.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        ;
        cursor.close();
        return result;
    }

    /**
     * 残高差分データを更新する関数
     *
     * @param date   収支の変更があった日付
     * @param amount 変更分の金額
     */
    private static void updateMonthlyBalanceDelta(Calendar date, int amount) {
        int targetYearMonthKey = MonthlyBalanceDelta.makeYearMonthKey(date);
        // 対象年月のデータを取得
        String targetSelection = MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_YEAR_MONTH_KEY + " = ?";
        ArrayList<MonthlyBalanceDelta> targetDateData = getData(
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
            ArrayList<MonthlyBalanceDelta> beforeDateData = getData(
                    MonthlyBalanceDelta.class,
                    beforeMonthSelection,
                    new String[]{String.valueOf(targetYearMonthKey)},
                    null, null, orderBy, "1"
            );
            if (beforeDateData.isEmpty()) {
                // 対象年月よりも前にデータが無いときは，対象年月がrootとする．
                setData(new MonthlyBalanceDelta(null, targetYearMonthKey, amount));
            } else {
                // 前の月の月からamount分変更することで対象年月の残高差分になる
                int deltaAmount = beforeDateData.get(0).getDeltaAmount() + amount;
                setData(new MonthlyBalanceDelta(null, targetYearMonthKey, deltaAmount));
            }
        } else {
            // 対象年月に残高差分のデータがすでにあった場合は，amount分金額を増減させる．
            MonthlyBalanceDelta buf = targetDateData.get(0);
            buf.setDeltaAmount(buf.getDeltaAmount() + amount);
            upsertDatabase(buf);
        }
        // 残高差分の金額が変わると後の月も影響を受けるので，対象年月よりも後の年月を全て取得
        String afterSelection = MyDbContract.MonthlyBalanceDeltaEntry.COLUMN_YEAR_MONTH_KEY + " > ?";
        ArrayList<MonthlyBalanceDelta> afterDateData = getData(
                MonthlyBalanceDelta.class,
                afterSelection,
                new String[]{String.valueOf(targetYearMonthKey)},
                null, null, null, null
        );
        for (MonthlyBalanceDelta data : afterDateData) {
            data.setDeltaAmount(data.getDeltaAmount() + amount);
            upsertDatabase(data);
        }
    }

    /**
     * 日付を昇順に並べるためのWhere句を作るための関数
     *
     * @param year        年
     * @param month       月
     * @param day         日
     * @param yearColumn  年のカラム名
     * @param monthColumn 月のカラム名
     * @param dayColumn   日のカラム名
     * @return Where句
     */
    private static String buildWhereClauseByDate(Integer year, Integer month, Integer day, String yearColumn, String monthColumn, String dayColumn) {
        ArrayList<String> selectionParts = new ArrayList<>();

        if (year != null) {
            selectionParts.add(yearColumn + " = " + year);
        }
        if (month != null) {
            selectionParts.add(monthColumn + " = " + month);
        }
        if (day != null) {
            selectionParts.add(dayColumn + " = " + day);
        }
        // 検索項目をいい感じに変換してSQLiteで検索できるようにする
        String selection = null;
        if (!selectionParts.isEmpty()) {
            selection = String.join(" AND ", selectionParts);
        }
        return selection;
    }
}
