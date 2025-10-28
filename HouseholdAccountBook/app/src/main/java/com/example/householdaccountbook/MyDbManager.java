package com.example.householdaccountbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import myclasses.BopCategory;
import myclasses.DailyBop;
import myclasses.Expenses;
import myclasses.Income;
import myclasses.PaymentMethod;
import myclasses.DatabaseEntity;
import myclasses.DatabaseEntityKind;

public class MyDbManager {
    private static MyOpenHelper helper;

    public static void setOpenHelper(Context context) {
        helper = new MyOpenHelper(context);
    }

    /**
     * デフォルトの支払方法を確保する関数
     * デフォルトの支払方法(通常支払い)が不具合でデータベースから削除されても自動で補完するためのもの
     */
    public static void ensureDefaultPayments() {
        SQLiteDatabase db = helper.getWritableDatabase();
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
        SQLiteDatabase db = helper.getWritableDatabase();
        db.insert(table, null, values);
    }

    /**
     * Database内のデータを更新(データが無い場合は挿入)する関数
     *
     * @param db        SQLiteDatabase
     * @param tableName テーブル名
     * @param values    ContentValues (IDカラムは必ず必要!)
     * @return boolean 成功するとtrue，失敗するとfalse
     */
    private static boolean upsertDatabase(SQLiteDatabase db, String tableName, ContentValues values) {
        Integer id = values.getAsInteger(MyOpenHelper.ID);
        if (id == null) {
            Log.e("MyDbManager", "upsertDatabase: idがnullのため更新・挿入を中止しました．");
            return false;
        }
        int updatedRows = db.update(tableName, values, MyOpenHelper.ID + " = ?", new String[]{String.valueOf(id)});
        if (updatedRows > 0) return true;
        long result = db.insert(tableName, null, values);
        if (result == -1) {
            Log.e("MyDbManager", "upsertDatabase: insertに失敗しました．(" + tableName + ")");
            return false;
        } else {
            return true;
        }
    }
    public static boolean upsertDatabase(String tableName, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return MyDbManager.upsertDatabase(db, tableName, values);
    }

    /**
     * DataTableから一致するIDのデータを削除する関数
     *
     * @param table DataTable名
     * @param id    データID
     */
    public static void deleteRecordByID(String table, String id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        db.delete(table, "_id=?", new String[]{id});
    }

    /**
     * 購入日付からデータを検索してListにして返す関数
     *
     * @param year  購入年(nullを入れると条件は無視される)
     * @param month 購入月(nullを入れると条件は無視される)
     * @param day   購入日(nullを入れると条件は無視される)
     * @return Expensesリスト
     */
    public static ArrayList<Expenses> getExpensesByPurchaseDate(Integer year, Integer month, Integer day) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<String> selectionParts = new ArrayList<>();
        ArrayList<String> selectionArgsList = new ArrayList<>();

        if (year != null) {
            selectionParts.add(MyOpenHelper.COLUMN_YEAR + " = ?");
            selectionArgsList.add(String.valueOf(year));
        }
        if (month != null) {
            selectionParts.add(MyOpenHelper.COLUMN_MONTH + " = ?");
            selectionArgsList.add(String.valueOf(month));
        }
        if (day != null) {
            selectionParts.add(MyOpenHelper.COLUMN_DAY + " = ?");
            selectionArgsList.add(String.valueOf(day));
        }
        // 検索項目をいい感じに変換してSQLiteで検索できるようにする
        String selection = null;
        String[] selectionArgs = null;
        if (!selectionParts.isEmpty()) {
            selection = String.join(" AND ", selectionParts);
            selectionArgs = selectionArgsList.toArray(new String[0]);
        }
        String orderBy = MyOpenHelper.COLUMN_YEAR + " ASC, " + MyOpenHelper.COLUMN_MONTH + " ASC, " + MyOpenHelper.COLUMN_DAY;
        Cursor cursor = db.query(
                MyOpenHelper.EXPENSES_TABLE_NAME,
                expensesColumns,
                selection,
                selectionArgs,
                null,
                null,
                orderBy
        );
        ArrayList<Expenses> expensesList = toExpensesListBy(cursor);
        cursor.close();
        return expensesList;
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
        SQLiteDatabase db = helper.getReadableDatabase();
        String purchaseSelection = buildWhereClauseByDate(
                year, month, day,
                MyOpenHelper.COLUMN_YEAR,
                MyOpenHelper.COLUMN_MONTH,
                MyOpenHelper.COLUMN_DAY
        );
        String paymentSelection = buildWhereClauseByDate(
                year, month, day,
                MyOpenHelper.COLUMN_PAYMENT_YEAR,
                MyOpenHelper.COLUMN_PAYMENT_MONTH,
                MyOpenHelper.COLUMN_PAYMENT_DAY
        );
        String selection = "(" + purchaseSelection + ") OR (" + paymentSelection + ")";
        String orderBy = MyOpenHelper.COLUMN_YEAR + " ASC, " + MyOpenHelper.COLUMN_MONTH + " ASC, " + MyOpenHelper.COLUMN_DAY;
        Cursor cursor = db.query(
                MyOpenHelper.EXPENSES_TABLE_NAME,
                expensesColumns,
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

    /**
     * 支払日時からデータを検索してListにして返す関数
     *
     * @param year  支払年(nullを入れると条件は無視される)
     * @param month 支払月(nullを入れると条件は無視される)
     * @param day   支払日(nullを入れると条件は無視される)
     * @return ListArray
     */
    public static ArrayList<Expenses> getExpensesByPaymentDate(Integer year, Integer month, Integer day) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<String> selectionParts = new ArrayList<>();
        ArrayList<String> selectionArgsList = new ArrayList<>();

        if (year != null) {
            selectionParts.add(MyOpenHelper.COLUMN_PAYMENT_YEAR + " = ?");
            selectionArgsList.add(String.valueOf(year));
        }
        if (month != null) {
            selectionParts.add(MyOpenHelper.COLUMN_PAYMENT_MONTH + " = ?");
            selectionArgsList.add(String.valueOf(month));
        }
        if (day != null) {
            selectionParts.add(MyOpenHelper.COLUMN_PAYMENT_DAY + " = ?");
            selectionArgsList.add(String.valueOf(day));
        }
        // 検索項目をいい感じに変換してSQLiteで検索できるようにする
        String selection = null;
        String[] selectionArgs = null;
        if (!selectionParts.isEmpty()) {
            selection = String.join(" AND ", selectionParts);
            selectionArgs = selectionArgsList.toArray(new String[0]);
        }
        String orderBy = MyOpenHelper.COLUMN_PAYMENT_YEAR + " ASC, " + MyOpenHelper.COLUMN_PAYMENT_MONTH + " ASC, " + MyOpenHelper.COLUMN_PAYMENT_DAY;
        Cursor cursor = db.query(
                MyOpenHelper.EXPENSES_TABLE_NAME,
                expensesColumns,
                selection,
                selectionArgs,
                null,
                null,
                orderBy
        );
        ArrayList<Expenses> expensesList = toExpensesListBy(cursor);
        cursor.close();
        return expensesList;
    }


    public static ArrayList<Expenses> getAllExpensesData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String orderBy = "year ASC, month ASC, day ASC";
        Cursor cursor = db.query(
                MyOpenHelper.EXPENSES_TABLE_NAME,
                expensesColumns,
                null,
                null,
                null,
                null,
                orderBy);
        ArrayList<Expenses> expensesList = toExpensesListBy(cursor);
        cursor.close();
        return expensesList;
    }

    public static ArrayList<Income> getAllIncomeData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String orderBy = "year ASC, month ASC, day ASC";
        Cursor cursor = db.query(
                MyOpenHelper.INCOME_TABLE_NAME,
                new String[]{"_id", "year", "month", "day", "amount", "memo", "category"},
                null,
                null,
                null,
                null,
                orderBy);
        cursor.moveToFirst();
        ArrayList<Income> incomeList = new ArrayList<Income>();
        for (int i = 0; i < cursor.getCount(); i++) {
            Income tmp = new Income(
                    cursor.getInt(0),
                    MyStdlib.convertToCalendar(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            cursor.moveToNext();
            incomeList.add(tmp);
        }
        cursor.close();
        return incomeList;
    }

    public static ArrayList<Income> getIncomeDataByDate(Integer year, Integer month, Integer day) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = buildWhereClauseByDate(
                year, month, day,
                MyOpenHelper.COLUMN_YEAR,
                MyOpenHelper.COLUMN_MONTH,
                MyOpenHelper.COLUMN_DAY
        );
        String orderBy = MyOpenHelper.COLUMN_YEAR + " ASC, " + MyOpenHelper.COLUMN_MONTH + " ASC, " + MyOpenHelper.COLUMN_DAY;
        Cursor cursor = db.query(
                MyOpenHelper.INCOME_TABLE_NAME,
                incomeColumns,
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

    public static ArrayList<BopCategory> getAllExpensesCategoryData() {
        return getAllCategoryData(MyOpenHelper.EXPENSES_CATEGORY_TABLE_NAME);
    }

    public static ArrayList<BopCategory> getAllIncomeCategoryData() {
        return getAllCategoryData(MyOpenHelper.INCOME_CATEGORY_TABLE_NAME);
    }

    private static <T extends BopCategory>  ArrayList<T> getAllCategoryData(String tableName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                tableName,
                new String[]{
                        MyOpenHelper.ID,
                        MyOpenHelper.COLUMN_NAME,
                        MyOpenHelper.COLUMN_COLOR,
                        MyOpenHelper.COLUMN_INDEX,
                        MyOpenHelper.COLUMN_IS_DELETED
                },
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        ArrayList<T> categoryList = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            T tmp = new <T extends BopCategory>(
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5) == 1
            );
            categoryList.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        return categoryList;
    }

    /**
     * 支払方法のデータを全て取得
     *
     * @return ArrayList<PaymentMethod>
     */
    public static ArrayList<PaymentMethod> getAllPaymentMethodData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                MyOpenHelper.PAYMENT_METHOD_TABLE_NAME,
                new String[]{
                        MyOpenHelper.ID,
                        MyOpenHelper.COLUMN_NAME,
                        MyOpenHelper.COLUMN_CLOSING_RULE_CODE,
                        MyOpenHelper.COLUMN_CLOSING_DAY,
                        MyOpenHelper.COLUMN_PAYMENT_RULE_CODE,
                        MyOpenHelper.COLUMN_PAYMENT_DAY,
                        MyOpenHelper.COLUMN_INDEX,
                        MyOpenHelper.COLUMN_IS_DEFAULT
                },
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        ArrayList<PaymentMethod> paymentMethodList = new ArrayList<PaymentMethod>();
        for (int i = 0; i < cursor.getCount(); i++) {
            PaymentMethod tmp = new PaymentMethod(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getInt(7) == 1
            );
            paymentMethodList.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        return paymentMethodList;
    }

    private static ArrayList<Income> toIncomeListBy(Cursor cursor) {
        ArrayList<Income> incomeList = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Income tmp = new Income(
                    cursor.getInt(0),
                    MyStdlib.convertToCalendar(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            cursor.moveToNext();
            incomeList.add(tmp);
        }
        return incomeList;
    }

    /**
     * CursorをExpensesリストに変換する関数
     *
     * @param cursor Cursor
     * @return ArrayList
     */
    private static ArrayList<Expenses> toExpensesListBy(Cursor cursor) {
        ArrayList<Expenses> expensesList = new ArrayList<Expenses>();

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Expenses tmp = new Expenses(
                    cursor.getInt(0),
                    MyStdlib.convertToCalendar(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    MyStdlib.convertToCalendar(cursor.getInt(8), cursor.getInt(9), cursor.getInt(10))
            );
            cursor.moveToNext();
            expensesList.add(tmp);
        }
        return expensesList;
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
