package myclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.example.householdaccountbook.MyDbContract;

/**
 * DatabaseEntityの種類と対応するデータベーステーブル名を管理する列挙型クラス
 * --※※※--新しくDatabaseEntityを実装したクラスがあったら必ず登録するように--※※※--
 */
public enum DatabaseEntityKind {
    EXPENSES(Expenses.class, MyDbContract.ExpensesEntry.TABLE_NAME, MyDbContract.ExpensesEntry.ID, MyDbContract.ExpensesEntry.COLUMNS),
    INCOME(Income.class, MyDbContract.IncomeEntry.TABLE_NAME, MyDbContract.IncomeEntry.ID, MyDbContract.IncomeEntry.COLUMNS),
    EXPENSES_CATEGORY(ExpensesCategory.class, MyDbContract.ExpensesCategoryEntry.TABLE_NAME, MyDbContract.ExpensesCategoryEntry.ID, MyDbContract.ExpensesCategoryEntry.COLUMNS),
    INCOME_CATEGORY(IncomeCategory.class, MyDbContract.IncomeCategoryEntry.TABLE_NAME, MyDbContract.IncomeCategoryEntry.ID, MyDbContract.IncomeCategoryEntry.COLUMNS),
    PAYMENT_METHOD(PaymentMethod.class, MyDbContract.PaymentMethodEntry.TABLE_NAME, MyDbContract.PaymentMethodEntry.ID, MyDbContract.PaymentMethodEntry.COLUMNS);

    // フィールド
    // クラス
    private final Class<? extends DatabaseEntity> entityClass;
    // 保存先のテーブル名
    private final String tableName;
    // idカラム名
    private final String idColumnName;
    // カラム配列
    private final String[] columns;
    // メソッド
    /**
     * コンストラクタ
     * @param entityClass クラス
     * @param tableName テーブル名
     * @param idColumnName idカラム名
     */
    private DatabaseEntityKind(Class<? extends DatabaseEntity> entityClass, String tableName, String idColumnName, String[] columns) {
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.idColumnName = idColumnName;
        this.columns = columns;
    }

    /**
     * クラス取得
     * @return Class
     */
    public Class<? extends DatabaseEntity> getEntityClass() {
        return this.entityClass;
    }
    /**
     * テーブル名取得
     */
    public String getTableName() {
        return this.tableName;
    }
    /**
     * idカラム名取得
     */
    public String getIdColumnName() {
        return this.idColumnName;
    }
    /**
     *
     */
    public String[] getColumns() {
        return this.columns;
    }
    // ユーティリティ
    /**
     * クラスから対応するEnumを取得
     * @param entityClass Class extends DatabaseEntity
     * @return DatabaseEntityKind
     */
    public static DatabaseEntityKind fromClass(Class<? extends DatabaseEntity> entityClass) {
        for (DatabaseEntityKind kind : values()) {
            if (kind.entityClass.equals(entityClass)) {
                return kind;
            }
        }
        throw new IllegalArgumentException("DatabaseEntityKind fromEntity 登録されていないクラスを検索しています: " + entityClass.getName());
    }
}
