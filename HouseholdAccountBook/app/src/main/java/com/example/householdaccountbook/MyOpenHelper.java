package com.example.householdaccountbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // このアプリのデータベース名
    private static final String DATABASE_NAME = "HouseholdDb.db";
    // 支出テーブル
    public static final String EXPENSES_TABLE_NAME = "ExpensesDb";
    // 収入テーブル
    public static final String INCOME_TABLE_NAME = "IncomeDb";
    // 支出カテゴリテーブル
    public static final String EXPENSES_CATEGORY_TABLE_NAME = "ExpCategoryDb";
    // 収入カテゴリテーブル
    public static final String INCOME_CATEGORY_TABLE_NAME = "IncCategoryDb";
    // 支払方法テーブル
    public static final String PAYMENT_METHOD_TABLE_NAME = "PaymentMethodDb";

    public static final String ID = "_id";                     // ID

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IS_DEFAULT = "is_default";
    // 支出，収入で使う共通カラム
    public static final String COLUMN_YEAR = "year";           // 年
    public static final String COLUMN_MONTH = "month";         // 月
    public static final String COLUMN_DAY = "day";             // 日
    public static final String COLUMN_AMOUNT = "amount";       // 金額
    public static final String COLUMN_CATEGORY = "category";   // カテゴリ
    public static final String COLUMN_MEMO = "memo";           // メモ

    public static final String COLUMN_PAYMENT_YEAR = "payment_year";
    public static final String COLUMN_PAYMENT_MONTH = "payment_month";
    public static final String COLUMN_PAYMENT_DAY = "payment_day";

    public static final String COLUMN_CLOSING_DAY = "closing_day";
    public static final String COLUMN_CLOSING_RULE_CODE = "closing_rule_code";
    public static final String COLUMN_PAYMENT_RULE_CODE = "payment_rule_code";
    public static final String COLUMN_PAYMENT_METHOD_ID = "payment_method_id"; // 支払方法のID (支出の方のカラムが持つ情報)

    public static final String COLUMN_COLOR = "color_code_text";

    private static final String EXPENSES_SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EXPENSES_TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    COLUMN_YEAR + " INTEGER," +
                    COLUMN_MONTH + " INTEGER," +
                    COLUMN_DAY + " INTEGER," +
                    COLUMN_AMOUNT + " INTEGER," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_MEMO + " TEXT," +
                    COLUMN_PAYMENT_METHOD_ID + " INTEGER," +
                    COLUMN_PAYMENT_YEAR + " INTEGER," +
                    COLUMN_PAYMENT_MONTH + " INTEGER," +
                    COLUMN_PAYMENT_DAY + " INTEGER)";
    private static final String INCOME_SQL_CREATE_ENTRIES =
            "CREATE TABLE " + INCOME_TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    COLUMN_YEAR + " INTEGER," +
                    COLUMN_MONTH + " INTEGER," +
                    COLUMN_DAY + " INTEGER," +
                    COLUMN_AMOUNT + " INTEGER," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_MEMO + " TEXT)";
    private static final String EXP_CATEGORY_SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EXPENSES_CATEGORY_TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT NOT NULL UNIQUE," +
                    COLUMN_COLOR + " TEXT NOT NULL)";
    private static final String INC_CATEGORY_CREATE_ENTRIES =
            "CREATE TABLE " + INCOME_CATEGORY_TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT NOT NULL UNIQUE," +
                    COLUMN_COLOR + " TEXT NOT NULL)";
    private static final String PAYMENT_METHOD_SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PAYMENT_METHOD_TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT NOT NULL UNIQUE," +
                    COLUMN_CLOSING_RULE_CODE + " INTEGER," +
                    COLUMN_CLOSING_DAY + " INTEGER," +
                    COLUMN_PAYMENT_RULE_CODE + " INTEGER," +
                    COLUMN_PAYMENT_DAY + " INTEGER," +
                    COLUMN_IS_DEFAULT + " INTEGER NOT NULL DEFAULT 0)";
    private static final String INCOME_SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + INCOME_TABLE_NAME;
    private static final String EXPENSES_SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + EXPENSES_TABLE_NAME;
    private static final String PAYMENT_METHOD_SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PAYMENT_METHOD_TABLE_NAME;

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
