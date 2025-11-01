package com.example.householdaccountbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import myclasses.BOP;
import myclasses.BopCategory;
import myclasses.DailyBop;
import myclasses.Expenses;
import myclasses.Income;
import myclasses.PaymentMethod;
import myclasses.DatabaseEntity;
import myclasses.DatabaseEntityKind;

public class MyDbManager {
    private static class MyOpenHelperContainer {
        private static MyOpenHelper helper = null;
        public static void setHelper(MyOpenHelper helper) {
            MyOpenHelperContainer.helper = helper;
        }
        public static MyOpenHelper getHelper() {
            if (MyOpenHelperContainer.helper != null) {
                return MyOpenHelperContainer.helper;
            }
            else {
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
                    MyDbContract.PaymentMethodEntry.DEFAULT_PAYMENT_METHOD.getId().toString()
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
        ArrayList<PaymentMethod> newlyList = MyDbManager.getAllPaymentMethodData();
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
            upsertDatabase(
                    db,
                    MyDbContract.PaymentMethodEntry.TABLE_NAME,
                    newlyList.get(i).getContentValues()
            );
        }
    }

    public static void setRecordToDataBase(String table, ContentValues values) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getWritableDatabase();
        db.insert(table, null, values);
    }

    /**
     * Database内の対応IDのデータを更新(データが無い場合は挿入)する関数
     *
     * @param db        SQLiteDatabase
     * @param tableContract テーブル情報
     * @param data データクラス
     * @return boolean 成功するとtrue，失敗するとfalse
     * @param <T> DatabaseEntityを継承したデータクラス
     */
    private static <T extends DatabaseEntity> boolean upsertDatabase(SQLiteDatabase db, MyDbContract.TableContract<T> tableContract, T data) {
        Integer id = data.getId();
        if (id == null) {
            Log.e("MyDbManager", "upsertDatabase: idがnullのため更新・挿入を中止しました．");
            return false;
        }
        final String tableName = tableContract.getTableName();
        ContentValues dataValues = data.getContentValues();
        dataValues.put(tableContract.getIdColumnName(), data.getId());
        int updatedRows = db.update(
                tableName,
                dataValues,
                tableContract.getIdColumnName() + " = ?",
                new String[]{String.valueOf( id )}
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
     * @param tableContract テーブル情報
     * @param data データ
     * @return boolean 成功するとtrue,失敗するとfalse
     * @param <T> DatabaseEntityを継承したデータクラス
     */
    public static <T extends DatabaseEntity> boolean upsertDatabase(MyDbContract.TableContract<T> tableContract, T data) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getWritableDatabase();
        return MyDbManager.upsertDatabase(db, tableContract, data);
    }

    /**
     * DataTableから一致するIDのデータを削除する関数
     *
     * @param table DataTable名
     * @param id    データID
     */
    public static void deleteRecordByID(String table, Integer id) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getReadableDatabase();
        db.delete(table, "_id=?", new String[]{id});
    }

    /**
     * データ削除 (DatabaseEntityインターフェース利用)
     * 
     * @param data <T extends DatabaseEntity>
     */
    public static <T extends DatabaseEntity> void deleteData(T data) {
        DatabaseEntityKind classKind = DatabaseEntityKind.fromClass(data.getClass());
        Integer id = data.getId();
        if (id != null) {
            SQLiteDatabase db = MyOpenHelperContainer.getHelper().getReadableDatabase();
            db.delete(
                classKind.getTableName(), 
                classKind.getIdColumnName() + " = ?",
                new String[]{ String.valueOf(id) }
            );
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
        ArrayList<Expenses> expensesList = toExpensesListBy(cursor);
        cursor.close();
        return expensesList;
    }

    public static ArrayList<Income> getIncomeDataByDate(Integer year, Integer month, Integer day) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getReadableDatabase();
        String selection = buildWhereClauseByDate(
                year, month, day,
                MyDbContract.IncomeEntry.COLUMN_YEAR,
                MyDbContract.IncomeEntry.COLUMN_MONTH,
                MyDbContract.IncomeEntry.COLUMN_DAY
        );
        String orderBy = MyDbContract.IncomeEntry.COLUMN_YEAR + " ASC, "
                + MyDbContract.IncomeEntry.COLUMN_MONTH + " ASC, "
                + MyDbContract.IncomeEntry.COLUMN_DAY + " ASC";
        Cursor cursor = db.query(
                MyDbContract.IncomeEntry.TABLE_NAME,
                MyDbContract.IncomeEntry.COLUMNS,
                selection,
                null,
                null,
                null,
                orderBy
        );
        ArrayList<Income> incomesList = toIncomeListBy(cursor);
        cursor.close();
        return incomesList;
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
     * @param entry MyDbContract.TableContract
     * @return ArrayList
     * @param <T> DatabaseEntity継承済み
     */
    public static <T extends DatabaseEntity> ArrayList<T> getAll(MyDbContract.TableContract<T> entry) {
        SQLiteDatabase db = MyOpenHelperContainer.getHelper().getReadableDatabase();
        ArrayList<T> result = new ArrayList<>();
        Cursor cursor = db.query(
                entry.getTableName(),
                entry.getColumns(),
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            result.add(entry.fromCursor(cursor));
        }
        cursor.close();
        return result;
    }
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
