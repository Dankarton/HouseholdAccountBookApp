package com.example.householdaccountbook.db;

import android.database.Cursor;

import com.example.householdaccountbook.MyStdlib;

import myclasses.DatabaseEntity;
import myclasses.Expenses;
import myclasses.ExpensesCategory;
import myclasses.Income;
import myclasses.IncomeCategory;
import myclasses.PaymentMethod;
import myclasses.Purchase;

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
    }

    public static final class IncomeEntry extends BaseBopEntry implements TableContract<Income> {
        public static final String TABLE_NAME = "IncomeDb";
        public static final String[] COLUMNS = {
                IncomeEntry.ID,             // 0
                IncomeEntry.COLUMN_YEAR,    // 1
                IncomeEntry.COLUMN_MONTH,   // 2
                IncomeEntry.COLUMN_DAY,     // 3
                IncomeEntry.COLUMN_AMOUNT,  // 4
                IncomeEntry.COLUMN_MEMO,    // 5
                IncomeEntry.COLUMN_CATEGORY_ID // 6
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
                    cursor.getInt(0),
                    MyStdlib.convertToCalendar(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getInt(6)
            );
        }
    }

    public static final class PurchaseEntry extends BaseBopEntry implements TableContract<Purchase> {
        public static final String TABLE_NAME = "PurchaseDb";
        public static final String COLUMN_PAYMENT_METHOD_ID = "payment_method_id";
        public static final String COLUMN_IS_SAME_DAY = "is_same_day";
        public static final String[] COLUMNS = {
                PurchaseEntry.ID,
                PurchaseEntry.COLUMN_YEAR,
                PurchaseEntry.COLUMN_MONTH,
                PurchaseEntry.COLUMN_DAY,
                PurchaseEntry.COLUMN_AMOUNT,
                PurchaseEntry.COLUMN_MEMO,
                PurchaseEntry.COLUMN_CATEGORY_ID,
                PurchaseEntry.COLUMN_PAYMENT_METHOD_ID,
                PurchaseEntry.COLUMN_IS_SAME_DAY
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
                    cursor.getInt(0),
                    MyStdlib.convertToCalendar(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getInt(7),
                    cursor.getInt(8) == 1
            );
        }
    }

    public static final class ExpensesEntry extends BaseBopEntry implements TableContract<Expenses> {
        public static final String TABLE_NAME = "ExpensesDb";
        public static final String COLUMN_PAYMENT_METHOD_ID = "payment_method_id";
        public static final String COLUMN_PAYMENT_YEAR = "payment_year";
        public static final String COLUMN_PAYMENT_MONTH = "payment_month";
        public static final String COLUMN_PAYMENT_DAY = "payment_day";

        public static final String[] COLUMNS = {
                ExpensesEntry.ID,
                ExpensesEntry.COLUMN_YEAR,
                ExpensesEntry.COLUMN_MONTH,
                ExpensesEntry.COLUMN_DAY,
                ExpensesEntry.COLUMN_AMOUNT,
                ExpensesEntry.COLUMN_MEMO,
                ExpensesEntry.COLUMN_CATEGORY_ID,
                ExpensesEntry.COLUMN_PAYMENT_METHOD_ID,
                ExpensesEntry.COLUMN_PAYMENT_YEAR,
                ExpensesEntry.COLUMN_PAYMENT_MONTH,
                ExpensesEntry.COLUMN_PAYMENT_DAY
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
                    cursor.getInt(0),
                    MyStdlib.convertToCalendar(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getInt(7),
                    MyStdlib.convertToCalendar(cursor.getInt(8), cursor.getInt(9), cursor.getInt(10))
            );
        }

        ;
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
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4) == 1
            );
        }
    }

    public static final class ExpensesCategoryEntry extends BaseCategoryEntry implements TableContract<ExpensesCategory> {
        public static final String TABLE_NAME = "ExpensesCategoryDb";

        @Override
        public String getTableName() {
            return ExpensesCategoryEntry.TABLE_NAME;
        }

        @Override
        public String getIdColumnName() {
            return ExpensesCategoryEntry.ID;
        }

        @Override
        public String[] getColumns() {
            return ExpensesCategoryEntry.COLUMNS;
        }

        @Override
        public ExpensesCategory fromCursor(Cursor cursor) {
            return new ExpensesCategory(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4) == 1
            );
        }
    }

    public static final class PaymentMethodEntry implements TableContract<PaymentMethod> {
        public static final String TABLE_NAME = "PaymentMethodDb";
        public static final String ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CLOSING_RULE_CODE = "closing_rule_code";
        public static final String COLUMN_CLOSING_SETTING_NUM = "closing_day";
        public static final String COLUMN_PAYMENT_RULE_CODE = "payment_rule_code";
        public static final String COLUMN_PAYMENT_SETTING_NUM = "payment_setting_num";
        public static final String COLUMN_INDEX = "list_index";
        public static final String COLUMN_IS_DEFAULT = "is_default";

        public static final String[] COLUMNS = {
                PaymentMethodEntry.ID,
                PaymentMethodEntry.COLUMN_NAME,
                PaymentMethodEntry.COLUMN_CLOSING_RULE_CODE,
                PaymentMethodEntry.COLUMN_CLOSING_SETTING_NUM,
                PaymentMethodEntry.COLUMN_PAYMENT_RULE_CODE,
                PaymentMethodEntry.COLUMN_PAYMENT_SETTING_NUM,
                PaymentMethodEntry.COLUMN_INDEX,
                PaymentMethodEntry.COLUMN_IS_DEFAULT
        };

        public static final PaymentMethod DEFAULT_PAYMENT_METHOD = new PaymentMethod(
                0,
                "通常支払い",
                PaymentMethod.ClosingRule.None.getCode(), null,
                PaymentMethod.PaymentRule.SameDay.getCode(), null,
                0,
                true
        );

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
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getInt(7) == 1
            );
        }
    }
}
