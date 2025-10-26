package com.example.householdaccountbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HouseholdDb.db";

    private static final String EXPENSES_SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MyDbContract.ExpensesEntry.TABLE_NAME + " (" +
                    MyDbContract.ExpensesEntry.ID + " INTEGER PRIMARY KEY," +
                    MyDbContract.ExpensesEntry.COLUMN_YEAR + " INTEGER," +
                    MyDbContract.ExpensesEntry.COLUMN_MONTH + " INTEGER," +
                    MyDbContract.ExpensesEntry.COLUMN_DAY + " INTEGER," +
                    MyDbContract.ExpensesEntry.COLUMN_AMOUNT + " INTEGER," +
                    MyDbContract.ExpensesEntry.COLUMN_CATEGORY + " TEXT," +
                    MyDbContract.ExpensesEntry.COLUMN_MEMO + " TEXT," +
                    MyDbContract.ExpensesEntry.COLUMN_PAYMENT_METHOD_ID + " INTEGER," +
                    MyDbContract.ExpensesEntry.COLUMN_PAYMENT_YEAR + " INTEGER," +
                    MyDbContract.ExpensesEntry.COLUMN_PAYMENT_MONTH + " INTEGER," +
                    MyDbContract.ExpensesEntry.COLUMN_PAYMENT_DAY + " INTEGER)";
    private static final String INCOME_SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MyDbContract.IncomeEntry.TABLE_NAME + " (" +
                    MyDbContract.IncomeEntry.ID + " INTEGER PRIMARY KEY," +
                    MyDbContract.IncomeEntry.COLUMN_YEAR + " INTEGER," +
                    MyDbContract.IncomeEntry.COLUMN_MONTH + " INTEGER," +
                    MyDbContract.IncomeEntry.COLUMN_DAY + " INTEGER," +
                    MyDbContract.IncomeEntry.COLUMN_AMOUNT + " INTEGER," +
                    MyDbContract.IncomeEntry.COLUMN_CATEGORY + " TEXT," +
                    MyDbContract.IncomeEntry.COLUMN_MEMO + " TEXT)";
    private static final String EXP_CATEGORY_SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MyDbContract.ExpensesCategoryEntry.TABLE_NAME + " (" +
                    MyDbContract.ExpensesCategoryEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MyDbContract.ExpensesCategoryEntry.COLUMN_NAME + " TEXT NOT NULL UNIQUE," +
                    MyDbContract.ExpensesCategoryEntry.COLUMN_COLOR + " TEXT NOT NULL," +
                    MyDbContract.ExpensesCategoryEntry.COLUMN_INDEX + " INTEGER," +
                    MyDbContract.ExpensesCategoryEntry.COLUMN_IS_DELETED + " INTEGER NOT NULL DEFAULT 0)";
    private static final String INC_CATEGORY_CREATE_ENTRIES =
            "CREATE TABLE " + MyDbContract.IncomeCategoryEntry.TABLE_NAME + " (" +
                    MyDbContract.IncomeCategoryEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MyDbContract.IncomeCategoryEntry.COLUMN_NAME + " TEXT NOT NULL UNIQUE," +
                    MyDbContract.IncomeCategoryEntry.COLUMN_COLOR + " TEXT NOT NULL," +
                    MyDbContract.IncomeCategoryEntry.COLUMN_INDEX + " INTEGER," +
                    MyDbContract.IncomeCategoryEntry.COLUMN_IS_DELETED + " INTEGER NOT NULL DEFAULT 0)";
    private static final String PAYMENT_METHOD_SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MyDbContract.PaymentMethodEntry.TABLE_NAME + " (" +
                    MyDbContract.PaymentMethodEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MyDbContract.PaymentMethodEntry.COLUMN_NAME + " TEXT NOT NULL UNIQUE," +
                    MyDbContract.PaymentMethodEntry.COLUMN_CLOSING_RULE_CODE + " INTEGER," +
                    MyDbContract.PaymentMethodEntry.COLUMN_CLOSING_SETTING_NUM + " INTEGER," +
                    MyDbContract.PaymentMethodEntry.COLUMN_PAYMENT_RULE_CODE + " INTEGER," +
                    MyDbContract.PaymentMethodEntry.COLUMN_PAYMENT_SETTING_NUM + " INTEGER," +
                    MyDbContract.PaymentMethodEntry.COLUMN_INDEX + " INTEGER," +
                    MyDbContract.PaymentMethodEntry.COLUMN_IS_DEFAULT + " INTEGER NOT NULL DEFAULT 0)";
    private static final String INCOME_SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MyDbContract.IncomeEntry.TABLE_NAME;
    private static final String EXPENSES_SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MyDbContract.ExpensesEntry.TABLE_NAME;
    private static final String PAYMENT_METHOD_SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MyDbContract.PaymentMethodEntry.TABLE_NAME;

    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(EXPENSES_SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(INCOME_SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(EXP_CATEGORY_SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(INC_CATEGORY_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(PAYMENT_METHOD_SQL_CREATE_ENTRIES);
        // 支払いテーブルに

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(EXPENSES_SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(INCOME_SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(PAYMENT_METHOD_SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
