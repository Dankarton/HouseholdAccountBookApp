package com.example.householdaccountbook.db;

import android.database.Cursor;
import android.graphics.Color;

import com.example.householdaccountbook.MyStdlib;

import com.example.householdaccountbook.myclasses.dbentity.DatabaseEntity;
import com.example.householdaccountbook.myclasses.dbentity.Expenses;
import com.example.householdaccountbook.myclasses.dbentity.MoneyMovement;
import com.example.householdaccountbook.myclasses.dbentity.MonthlyBalanceDelta;
import com.example.householdaccountbook.myclasses.dbentity.PurchaseCategory;
import com.example.householdaccountbook.myclasses.dbentity.Income;
import com.example.householdaccountbook.myclasses.dbentity.IncomeCategory;
import com.example.householdaccountbook.myclasses.dbentity.PaymentMethod;
import com.example.householdaccountbook.myclasses.dbentity.Purchase;
import com.example.householdaccountbook.myclasses.dbentity.Wallet;

public final class MyDbContract {
    public interface TableContract<T extends DatabaseEntity> {
        String getTableName();

        String getIdColumnName();

        String[] getColumns();

        T fromCursor(Cursor cursor);
    }

    private MyDbContract() { /*インスタンス化防止*/ }

    public static abstract class BaseBopEntry {
        private BaseBopEntry() { /*インスタンス化防止*/ }

        public static final String ID = "_id";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_MEMO = "memo";
        public static final String COLUMN_CATEGORY_ID = "category_id";

        public String getColumnAmount() { return COLUMN_AMOUNT; }
    }

    public static final class IncomeEntry extends BaseBopEntry implements TableContract<Income> {
        public static final String TABLE_NAME = "IncomeDb";
        public static final String COLUMN_WALLET_ID = "wallet_id";

        public static final String[] COLUMNS = {
                IncomeEntry.ID,             // 0
                IncomeEntry.COLUMN_YEAR,    // 1
                IncomeEntry.COLUMN_MONTH,   // 2
                IncomeEntry.COLUMN_DAY,     // 3
                IncomeEntry.COLUMN_AMOUNT,  // 4
                IncomeEntry.COLUMN_MEMO,    // 5
                IncomeEntry.COLUMN_CATEGORY_ID, // 6
                IncomeEntry.COLUMN_WALLET_ID
        };

        @Override
        public String getTableName() {
            return IncomeEntry.TABLE_NAME;
        }

        @Override
        public String getIdColumnName() {
            return IncomeEntry.ID;
        }

        @Override
        public String[] getColumns() {
            return IncomeEntry.COLUMNS;
        }

        @Override
        public Income fromCursor(Cursor cursor) {
            return new Income(
                    cursor.getLong(0),
                    MyStdlib.convertToCalendar(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getLong(7)
            );
        }
    }

    public static final class PurchaseEntry extends BaseBopEntry implements TableContract<Purchase> {
        public static final String TABLE_NAME = "PurchaseDb";
        public static final String COLUMN_PAYMENT_METHOD_ID = "payment_method_id";
        public static final String COLUMN_PAYMENT_TIMING_CODE = "payment_timing_code";
        public static final String[] COLUMNS = {
                PurchaseEntry.ID,
                PurchaseEntry.COLUMN_YEAR,
                PurchaseEntry.COLUMN_MONTH,
                PurchaseEntry.COLUMN_DAY,
                PurchaseEntry.COLUMN_AMOUNT,
                PurchaseEntry.COLUMN_MEMO,
                PurchaseEntry.COLUMN_CATEGORY_ID,
                PurchaseEntry.COLUMN_PAYMENT_METHOD_ID,
        };

        @Override
        public String getTableName() {
            return PurchaseEntry.TABLE_NAME;
        }

        @Override
        public String getIdColumnName() {
            return PurchaseEntry.ID;
        }

        @Override
        public String[] getColumns() {
            return PurchaseEntry.COLUMNS;
        }

        @Override
        public Purchase fromCursor(Cursor cursor) {
            return new Purchase(
                    cursor.getLong(0),
                    MyStdlib.convertToCalendar(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getInt(7)
            );
        }
    }

    public static final class ExpensesEntry extends BaseBopEntry implements TableContract<Expenses> {
        public static final String TABLE_NAME = "ExpensesDb";
        public static final String COLUMN_PAYMENT_METHOD_ID = "payment_method_id";
        public static final String COLUMN_PURCHASE_ID = "purchase_id";
        public static final String COLUMN_WALLET_ID = "wallet_id";

        public static final String[] COLUMNS = {
                ExpensesEntry.ID,
                ExpensesEntry.COLUMN_YEAR,
                ExpensesEntry.COLUMN_MONTH,
                ExpensesEntry.COLUMN_DAY,
                ExpensesEntry.COLUMN_AMOUNT,
                ExpensesEntry.COLUMN_MEMO,
                ExpensesEntry.COLUMN_CATEGORY_ID,
                ExpensesEntry.COLUMN_PAYMENT_METHOD_ID,
                ExpensesEntry.COLUMN_PURCHASE_ID,
                ExpensesEntry.COLUMN_WALLET_ID
        };

        @Override
        public String getTableName() {
            return ExpensesEntry.TABLE_NAME;
        }

        @Override
        public String getIdColumnName() {
            return ExpensesEntry.ID;
        }

        @Override
        public String[] getColumns() {
            return ExpensesEntry.COLUMNS;
        }

        @Override
        public Expenses fromCursor(Cursor cursor) {
            return new Expenses(
                    cursor.getLong(0),
                    MyStdlib.convertToCalendar(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getInt(7),
                    cursor.getInt(8),
                    cursor.getInt(9)
            );
        }
    }
    public static final class MoneyMovementsEntry extends BaseBopEntry implements TableContract<MoneyMovement> {
        private MoneyMovementsEntry() { /*インスタンス防止*/ }

        public static final String TABLE_NAME = "MoneyMovementsDb";
        public static final String COLUMN_FROM_WALLET_ID = "from_wallet_id";
        public static final String COLUMN_TO_WALLET_ID = "to_wallet_id";

        public static final String[] COLUMNS = {
                MoneyMovementsEntry.ID,
                MoneyMovementsEntry.COLUMN_YEAR,
                MoneyMovementsEntry.COLUMN_MONTH,
                MoneyMovementsEntry.COLUMN_DAY,
                MoneyMovementsEntry.COLUMN_AMOUNT,
                MoneyMovementsEntry.COLUMN_MEMO,
                MoneyMovementsEntry.COLUMN_CATEGORY_ID,
                MoneyMovementsEntry.COLUMN_FROM_WALLET_ID,
                MoneyMovementsEntry.COLUMN_TO_WALLET_ID
        };


        @Override
        public String getTableName() {
            return MoneyMovementsEntry.TABLE_NAME;
        }

        @Override
        public String getIdColumnName() {
            return MoneyMovementsEntry.ID;
        }

        @Override
        public String[] getColumns() {
            return MoneyMovementsEntry.COLUMNS;
        }

        @Override
        public MoneyMovement fromCursor(Cursor cursor) {
            return new MoneyMovement(
                    cursor.getLong(0),
                    MyStdlib.convertToCalendar(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getLong(6),
                    cursor.getLong(7)
                    );
        }
    }
    public static abstract class BaseCategoryEntry {
        private BaseCategoryEntry() { /*インスタンス化防止*/ }

        public static final String ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COLOR = "color_code_text";
        public static final String COLUMN_INDEX = "list_index";
        public static final String COLUMN_IS_DELETED = "is_deleted";
        public static final String[] COLUMNS = {
                BaseCategoryEntry.ID,
                BaseCategoryEntry.COLUMN_NAME,
                BaseCategoryEntry.COLUMN_COLOR,
                BaseCategoryEntry.COLUMN_INDEX,
                BaseCategoryEntry.COLUMN_IS_DELETED
        };
    }

    public static final class IncomeCategoryEntry extends BaseCategoryEntry implements TableContract<IncomeCategory> {
        public static final String TABLE_NAME = "IncomeCategoryDb";

        @Override
        public String getTableName() {
            return IncomeCategoryEntry.TABLE_NAME;
        }

        @Override
        public String getIdColumnName() {
            return IncomeCategoryEntry.ID;
        }

        @Override
        public String[] getColumns() {
            return IncomeCategoryEntry.COLUMNS;
        }

        @Override
        public IncomeCategory fromCursor(Cursor cursor) {
            return new IncomeCategory(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4) == 1
            );
        }

        public static IncomeCategory[] PRE_DATA_LIST = {
                new IncomeCategory(null, "給料", Color.parseColor("#009944"), 0, false),
                new IncomeCategory(null, "アルバイト", Color.parseColor("#9DC93A"), 1, false),
                new IncomeCategory(null, "お小遣い", Color.parseColor("#FFF100"), 2, false),
                new IncomeCategory(null, "ボーナス", Color.parseColor("#ef845c"), 3, false)
        };
    }

    public static final class PurchaseCategoryEntry extends BaseCategoryEntry implements TableContract<PurchaseCategory> {
        public static final String TABLE_NAME = "PurchaseCategoryDb";

        @Override
        public String getTableName() {
            return PurchaseCategoryEntry.TABLE_NAME;
        }

        @Override
        public String getIdColumnName() {
            return PurchaseCategoryEntry.ID;
        }

        @Override
        public String[] getColumns() {
            return PurchaseCategoryEntry.COLUMNS;
        }

        @Override
        public PurchaseCategory fromCursor(Cursor cursor) {
            return new PurchaseCategory(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4) == 1
            );
        }

        public static PurchaseCategory[] PRE_DATA_LIST = {
                new PurchaseCategory(null, "食費", Color.parseColor("#f39800"), 0, false),
                new PurchaseCategory(null, "日用品費", Color.parseColor("#009944"), 1, false),
                new PurchaseCategory(null, "被服費", Color.parseColor("#0068B7"), 2, false),
                new PurchaseCategory(null, "美容費", Color.parseColor("#E5004F"), 3, false),
                new PurchaseCategory(null, "交際費", Color.parseColor("#FFF100"), 4, false),
                new PurchaseCategory(null, "趣味費", Color.parseColor("#36318F"), 5, false),
                new PurchaseCategory(null, "交通費", Color.parseColor("#a44a0a"), 6, false),
                new PurchaseCategory(null, "教育費", Color.parseColor("#e60012"), 7, false),
                new PurchaseCategory(null, "医療費", Color.parseColor("#B6D56A"), 8, false),
                new PurchaseCategory(null, "住居費", Color.parseColor("#EB6EA5"), 9, false),
                new PurchaseCategory(null, "水道光熱費", Color.parseColor("#00B9EF"), 10, false),
                new PurchaseCategory(null, "通信費", Color.parseColor("#d1d1d1"), 11, false),
                new PurchaseCategory(null, "保険料", Color.parseColor("#e8a06c"), 12, false),
                new PurchaseCategory(null, "雑費", Color.parseColor("#5a5a5a"), 13, false),
        };
    }

    public static final class PaymentMethodEntry implements TableContract<PaymentMethod> {
        public static final String TABLE_NAME = "PaymentMethodDb";
        public static final String ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CLOSING_RULE_CODE = "closing_rule_code";
        public static final String COLUMN_CLOSING_SETTING_NUM = "closing_day";
        public static final String COLUMN_PAYMENT_RULE_CODE = "payment_rule_code";
        public static final String COLUMN_PAYMENT_SETTING_NUM = "payment_setting_num";
        public static final String COLUMN_WALLET_ID = "wallet_id";
        public static final String COLUMN_INDEX = "list_index";
        public static final String COLUMN_IS_DEFAULT = "is_default";

        public static final String[] COLUMNS = {
                PaymentMethodEntry.ID,
                PaymentMethodEntry.COLUMN_NAME,
                PaymentMethodEntry.COLUMN_CLOSING_RULE_CODE,
                PaymentMethodEntry.COLUMN_CLOSING_SETTING_NUM,
                PaymentMethodEntry.COLUMN_PAYMENT_RULE_CODE,
                PaymentMethodEntry.COLUMN_PAYMENT_SETTING_NUM,
                PaymentMethodEntry.COLUMN_WALLET_ID,
                PaymentMethodEntry.COLUMN_INDEX,
                PaymentMethodEntry.COLUMN_IS_DEFAULT
        };

        public static final PaymentMethod DEFAULT_PAYMENT_METHOD = new PaymentMethod(
                (long) 0,
                "現金",
                PaymentMethod.ClosingRule.None.getCode(), null,
                PaymentMethod.PaymentRule.SameDay.getCode(), null,
                (long) 0,
                0,
                true
        );
        public static final PaymentMethod[] PRE_DATA_LIST = {
                new PaymentMethod(
                        null,
                        "クレジットカード",
                        PaymentMethod.ClosingRule.EndOfMonth.getCode(), null,
                        PaymentMethod.PaymentRule.FixedDay.getCode(), 27,
                        (long) 0,
                        1,
                        false
                )
        };

        @Override
        public String getTableName() {
            return PaymentMethodEntry.TABLE_NAME;
        }

        @Override
        public String getIdColumnName() {
            return PaymentMethodEntry.ID;
        }

        @Override
        public String[] getColumns() {
            return PaymentMethodEntry.COLUMNS;
        }

        @Override
        public PaymentMethod fromCursor(Cursor cursor) {
            return new PaymentMethod(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getInt(7),
                    cursor.getInt(8) == 1
            );
        }
    }
    public static final class WalletEntry implements TableContract<Wallet> {
        public static final String TABLE_NAME = "WalletDb";
        public static final String ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_INIT_AMOUNT = "init_amount";
        public static final String COLUMN_DISPLAY_INDEX = "display_index";
        public static final String COLUMN_IS_DEFAULT = "is_default";
        public static final String COLUMN_IS_DELETED = "is_deleted";
        public static final String[] COLUMNS = {
                WalletEntry.ID,
                WalletEntry.COLUMN_NAME,
                WalletEntry.COLUMN_INIT_AMOUNT,
                WalletEntry.COLUMN_DISPLAY_INDEX,
                WalletEntry.COLUMN_IS_DEFAULT,
                WalletEntry.COLUMN_IS_DELETED,
        };
        public static final Wallet DEFAULT_WALLET = new Wallet(
                (long) 0,
                "現金",
                0,
                0,
                true,
                false
        );
        @Override
        public String getTableName() {
            return WalletEntry.TABLE_NAME;
        }

        @Override
        public String getIdColumnName() {
            return WalletEntry.ID;
        }

        @Override
        public String[] getColumns() {
            return WalletEntry.COLUMNS;
        }

        @Override
        public Wallet fromCursor(Cursor cursor) {
            return new Wallet(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4) == 1,
                    cursor.getInt(5)== 1
            );
        }
    }

    public static final class MonthlyBalanceDeltaEntry implements TableContract<MonthlyBalanceDelta> {
        public static final String TABLE_NAME = "MonthlyBalanceDeltaDb";
        public static final String ID = "_id";
        public static final String COLUMN_WALLET_ID = "wallet_id";
        public static final String COLUMN_YEAR_MONTH_KEY = "year_month_key";
        public static final String COLUMN_DELTA_AMOUNT = "delta_amount";

        public static final String[] COLUMNS = {
                MonthlyBalanceDeltaEntry.ID,
                MonthlyBalanceDeltaEntry.COLUMN_WALLET_ID,
                MonthlyBalanceDeltaEntry.COLUMN_YEAR_MONTH_KEY,
                MonthlyBalanceDeltaEntry.COLUMN_DELTA_AMOUNT
        };

        @Override
        public String getTableName() {
            return MonthlyBalanceDeltaEntry.TABLE_NAME;
        }

        @Override
        public String getIdColumnName() {
            return MonthlyBalanceDeltaEntry.ID;
        }

        @Override
        public String[] getColumns() {
            return MonthlyBalanceDeltaEntry.COLUMNS;
        }

        @Override
        public MonthlyBalanceDelta fromCursor(Cursor cursor) {
            return new MonthlyBalanceDelta(
                    cursor.getLong(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3)
            );
        }
    }
}
