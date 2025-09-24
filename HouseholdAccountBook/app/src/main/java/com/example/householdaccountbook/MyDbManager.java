package com.example.householdaccountbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import myclasses.Expenses;
import myclasses.Income;
import myclasses.PaymentMethod;

public class MyDbManager {
    private static MyOpenHelper helper;

    public static void setOpenHelper(Context context) {

        helper = new MyOpenHelper(context);
    }

    public static void ensureDefaultPayments() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(
                MyOpenHelper.PAYMENT_METHOD_TABLE_NAME,
                new String[]{"COUNT(*)"},
                "name = ?",
                new String[]{"通常支払い"},
                null,
                null,
                null
        );
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        if (count == 0) {
            MyDbManager.setRecordToDataBase(
                    MyOpenHelper.PAYMENT_METHOD_TABLE_NAME,
                    PaymentMethod.getContentValues(
                            "通常支払い",
                            PaymentMethod.ClosingRule.None.getCode(), null,
                            PaymentMethod.PaymentRule.SameDay.getCode(), null,
                            true
                    )
            );
        }
    }
    public static void setRecordToDataBase(String table, ContentValues values){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.insert(table, null, values);
    }

    /**
     * DataTableから一致するIDのデータを削除する関数
     * @param table DataTable名
     * @param id データID
     */
    public static void deleteRecordByID(String table, String id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        db.delete(table, "_id=?", new String[]{ id });
    }

    /**
     * 購入日付からデータを検索してListにして返す関数
     * @param year 購入年(nullを入れると条件は無視される)
     * @param month 購入月(nullを入れると条件は無視される)
     * @param day 購入日(nullを入れると条件は無視される)
     * @return Expensesリスト
     */
    public static ArrayList<Expenses> getExpensesByPurchaseDate(Integer year, Integer month, Integer day) {
        SQLiteDatabase db =helper.getReadableDatabase();
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
                new String[] {
                        MyOpenHelper.ID,
                        MyOpenHelper.COLUMN_YEAR,
                        MyOpenHelper.COLUMN_MONTH,
                        MyOpenHelper.COLUMN_DAY,
                        MyOpenHelper.COLUMN_AMOUNT,
                        MyOpenHelper.COLUMN_MEMO,
                        MyOpenHelper.COLUMN_CATEGORY,
                        MyOpenHelper.COLUMN_PAYMENT_METHOD_ID,
                        MyOpenHelper.COLUMN_PAYMENT_YEAR,
                        MyOpenHelper.COLUMN_PAYMENT_MONTH,
                        MyOpenHelper.COLUMN_PAYMENT_DAY
                },
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
     * 支払日時からデータを検索してListにして返す関数
     * @param year 支払年(nullを入れると条件は無視される)
     * @param month 支払月(nullを入れると条件は無視される)
     * @param day 支払日(nullを入れると条件は無視される)
     * @return ListArray
     */
    public static ArrayList<Expenses> getExpensesByPaymentDate(Integer year, Integer month, Integer day) {
        SQLiteDatabase db =helper.getReadableDatabase();
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
                new String[] {
                        MyOpenHelper.ID,
                        MyOpenHelper.COLUMN_YEAR,
                        MyOpenHelper.COLUMN_MONTH,
                        MyOpenHelper.COLUMN_DAY,
                        MyOpenHelper.COLUMN_AMOUNT,
                        MyOpenHelper.COLUMN_MEMO,
                        MyOpenHelper.COLUMN_CATEGORY,
                        MyOpenHelper.COLUMN_PAYMENT_METHOD_ID,
                        MyOpenHelper.COLUMN_PAYMENT_YEAR,
                        MyOpenHelper.COLUMN_PAYMENT_MONTH,
                        MyOpenHelper.COLUMN_PAYMENT_DAY
                },
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
                new String[] {
                        MyOpenHelper.ID,
                        MyOpenHelper.COLUMN_YEAR,
                        MyOpenHelper.COLUMN_MONTH,
                        MyOpenHelper.COLUMN_DAY,
                        MyOpenHelper.COLUMN_AMOUNT,
                        MyOpenHelper.COLUMN_MEMO,
                        MyOpenHelper.COLUMN_CATEGORY,
                        MyOpenHelper.COLUMN_PAYMENT_METHOD_ID,
                        MyOpenHelper.COLUMN_PAYMENT_YEAR,
                        MyOpenHelper.COLUMN_PAYMENT_MONTH,
                        MyOpenHelper.COLUMN_PAYMENT_DAY
                },
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
                new String[] {"_id", "year", "month", "day", "amount", "memo", "category"},
                null,
                null,
                null,
                null,
                orderBy);
        cursor.moveToFirst();
        ArrayList<Income> incomeList = new ArrayList<Income>();
        for (int i = 0; i < cursor.getCount(); i++){
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

    public static ArrayList<PaymentMethod> getAllPaymentMethodData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                MyOpenHelper.PAYMENT_METHOD_TABLE_NAME,
                new String[] {
                        MyOpenHelper.ID,
                        MyOpenHelper.COLUMN_NAME,
                        MyOpenHelper.COLUMN_CLOSING_RULE_CODE,
                        MyOpenHelper.COLUMN_CLOSING_DAY,
                        MyOpenHelper.COLUMN_PAYMENT_RULE_CODE,
                        MyOpenHelper.COLUMN_PAYMENT_DAY,
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
                    cursor.getInt(6)
            );
            paymentMethodList.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        return paymentMethodList;
    }
    /**
     * CursorをExpensesリストに変換する関数
     * @param cursor Cursor
     * @return ArrayList
     */
    private static ArrayList<Expenses> toExpensesListBy(Cursor cursor) {
        ArrayList<Expenses> expensesList = new ArrayList<Expenses>();

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
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

    /**
     * データテーブル内にあるデータをログに表示する関数
     * コードテスト用
     */
    public static void checkExpensesDbList(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("ExpensesDb", new String[] {"_id", "year", "month", "day", "amount", "memo", "category"},
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            String strBuilder =
                    "id:" + cursor.getInt(0) +
                            ", y:" +
                            cursor.getInt(1) +
                            ", m:" +
                            cursor.getInt(2) +
                            ", d:" +
                            cursor.getInt(3) +
                            ", pay:" +
                            cursor.getInt(4) +
                            ", memo:" +
                            cursor.getString(5) +
                            ", category:" +
                            cursor.getString(6);
            Log.d("ExpensesInputFragment", strBuilder);
            cursor.moveToNext();
        }
        cursor.close();
    }
}
