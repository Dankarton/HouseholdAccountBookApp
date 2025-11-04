package com.example.householdaccountbook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.householdaccountbook.MyStdlib;

import java.util.ArrayList;

import myclasses.BOP;
import myclasses.DailyBop;
import myclasses.Expenses;
import myclasses.ExpensesCategory;
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
    }

    public static void setMyOpenHelper(Context context) {
        MyOpenHelperContainer.setHelper(new MyOpenHelper(context));
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
     * @return 成功true，失敗false
     */
    public static <T extends DatabaseEntity> boolean setData(T data) {
        if (data.getId() != null) {
            throw new IllegalArgumentException(
                    "データにIDが設定されています．既存データを謝って登録している可能性があります: id = " + data.getId()
            );
        }
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getWritableDatabase();
        MyDbContract.TableContract<T> contract = TableContractRegistry.getContract(data.getClass());
        long resultNum = db.insert(contract.getTableName(), null, data.getContentValues());
        if (resultNum == -1) {
            Log.d("MyDbManager.setData", "データの挿入に失敗しました．");
            return false;
        } else {
            return true;
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
        Integer id = data.getId();
        if (id == null) {
            Log.e("MyDbManager", "upsertDatabase: idがnullのため更新・挿入を中止しました．");
            return false;
        }
        // dataのクラスから対応するTableContractを取得
        final MyDbContract.TableContract<T> tableContract = TableContractRegistry.getContract(data.getClass());
        final String tableName = tableContract.getTableName();
        ContentValues dataValues = data.getContentValues();
        dataValues.put(tableContract.getIdColumnName(), data.getId());
        int updatedRows = db.update(
                tableName,
                dataValues,
                tableContract.getIdColumnName() + " = ?",
                new String[]{String.valueOf(id)}
        );
        if (updatedRows > 0) return true;
        long result = db.insert(tableName, null, dataValues);
        if (result == -1) {
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
    public static <T extends DatabaseEntity> boolean upsertDatabase(T data) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getWritableDatabase();

        return MyDbManager.upsertDatabase(db, data);
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
            deleteData(
                    MyDbContract.ExpensesEntry.TABLE_NAME,
                    MyDbContract.ExpensesEntry.COLUMN_PURCHASE_ID + " = ?",
                    new String[]{String.valueOf(data.getId())}
            );
            deleteData(data);
        } else if (data.getClass() == ExpensesCategory.class) {
            // カテゴリは過去の購入日などが参照する可能性があるため見かけ上の削除
            ExpensesCategory beforeCategory = (ExpensesCategory) data;
            upsertDatabase(
                    new ExpensesCategory(
                            beforeCategory.getId(),
                            beforeCategory.getName(),
                            beforeCategory.getColorCode(),
                            beforeCategory.getIndex(),
                            true
                    )
            );
        } else if (data.getClass() == IncomeCategory.class) {
            // カテゴリは過去の購入日などが参照する可能性があるため見かけ上の削除
            IncomeCategory beforeCategory = (IncomeCategory) data;
            upsertDatabase(
                    new ExpensesCategory(
                            beforeCategory.getId(),
                            beforeCategory.getName(),
                            beforeCategory.getColorCode(),
                            beforeCategory.getIndex(),
                            true
                    )
            );
        } else if (data.getClass() == PaymentMethod.class) {

        } else {
            deleteData(data);
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
    private static <T extends DatabaseEntity> void deleteData(Class<T> clazz, int id) {
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
     * データ削除(低レベル操作，依存関係などを無視して指定されたデータを削除する)
     * アプリケーションのデータ構造を維持したまま削除するなら deleteDataSafely を使うように!
     *
     * @param tableName   テーブル名
     * @param whereClause where句
     * @param whereArgs   args
     */
    private static void deleteData(String tableName, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getWritableDatabase();
        int deletedRowNum = db.delete(tableName, whereClause, whereArgs);
        if (deletedRowNum == 0) {
            Log.d("MyDbManager.deleteData", "削除されたデータが0件です．TableName: " + tableName);
        }
    }

    /**
     * 引数で指定した日付と購入日もしくは支払日が一致する支出データを取得
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return Expensesリスト
     */
    public static ArrayList<Expenses> getExpensesByPurchaseOrPaymentDate(Integer year, Integer month, Integer day) {

        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getReadableDatabase();
        String purchaseSelection = buildWhereClauseByDate(
                year, month, day,
                MyDbContract.ExpensesEntry.COLUMN_YEAR,
                MyDbContract.ExpensesEntry.COLUMN_MONTH,
                MyDbContract.ExpensesEntry.COLUMN_DAY
        );
        String paymentSelection = buildWhereClauseByDate(
                year, month, day,
                MyDbContract.ExpensesEntry.COLUMN_PAYMENT_YEAR,
                MyDbContract.ExpensesEntry.COLUMN_PAYMENT_MONTH,
                MyDbContract.ExpensesEntry.COLUMN_PAYMENT_DAY
        );
        String selection = "(" + purchaseSelection + ") OR (" + paymentSelection + ")";
        String orderBy = MyDbContract.ExpensesEntry.COLUMN_YEAR + " ASC, "
                + MyDbContract.ExpensesEntry.COLUMN_MONTH + " ASC, "
                + MyDbContract.ExpensesEntry.COLUMN_DAY + " ASC";
        Cursor cursor = db.query(
                MyDbContract.ExpensesEntry.TABLE_NAME,
                MyDbContract.ExpensesEntry.COLUMNS,
                selection,
                null,
                null,
                null,
                orderBy
        );
        ArrayList<Expenses> expensesList = new ArrayList<>();
        MyDbContract.ExpensesEntry entry = new MyDbContract.ExpensesEntry();
        if (cursor.moveToFirst()) {
            do {
                expensesList.add(entry.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        ;
        cursor.close();
        return expensesList;
    }

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

    public static DailyBop getDailyData(int year, int month, int day) {
        ArrayList<Expenses> expensesList = MyDbManager.getExpensesByPurchaseOrPaymentDate(year, month, day);
        ArrayList<Income> incomeList = MyDbManager.getIncomeDataByDate(year, month, day);
        // 収入も支出もない場合
        if (expensesList.isEmpty() && incomeList.isEmpty()) {
            return null;
        }
        ArrayList<Expenses> purchaseList = new ArrayList<>();
        ArrayList<Expenses> paymentList = new ArrayList<>();
        for (int i = 0; i < expensesList.size(); i++) {
            Expenses exp = expensesList.get(i);
            // 購入日と支払日が同じとき
            if (exp.isSameDay()) {
                purchaseList.add(exp);
                paymentList.add(exp);
            }
            // 対象日が支払日の時
            else if (MyStdlib.isSameDay(exp.getPaymentDate(), MyStdlib.convertToCalendar(year, month, day))) {
                paymentList.add(exp);
            }
            // 購入日だけと支払日じゃない時
            else {
                purchaseList.add(exp);
            }
        }
        return new DailyBop(year, month, day, incomeList, purchaseList, paymentList);
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
