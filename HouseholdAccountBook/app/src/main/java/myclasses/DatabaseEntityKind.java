package myclasses;

import com.example.householdaccountbook.MyDbContract;

/**
 * DatabaseEntityの種類と対応するデータベーステーブル名を管理する列挙型クラス
 * --※※※--新しくDatabaseEntityを実装したクラスがあったら必ず登録するように--※※※--
 */
public enum DatabaseEntityKind {
    EXPENSES(Expenses.class, MyDbContract.ExpensesEntry.TABLE_NAME, MyDbContract.ExpensesEntry.ID),
    INCOME(Income.class, MyDbContract.IncomeEntry.TABLE_NAME, MyDbContract.IncomeEntry.ID),
    EXPENSES_CATEGORY(ExpensesCategory.class, MyDbContract.ExpensesCategoryEntry.TABLE_NAME, MyDbContract.ExpensesCategoryEntry.ID),
    INCOME_CATEGORY(IncomeCategory.class, MyDbContract.IncomeCategoryEntry.TABLE_NAME, MyDbContract.IncomeCategoryEntry.ID),
    PAYMENT_METHOD(PaymentMethod.class, MyDbContract.PaymentMethodEntry.TABLE_NAME, MyDbContract.PaymentMethodEntry.ID);

    // フィールド
    // クラス
    private final Class<? extends DatabaseEntity> entityClass;
    // 保存先のテーブル名
    private final String tableName;

    // メソッド
    /**
     * コンストラクタ
     * @param entityClass クラス
     * @param tableName テーブル名
     * @param idColumnName idカラム名
     */
    private DatabaseEntityKind(Class<? extends DatabaseEntity> entityClass, String tableName, String idColumnName) {
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.idColumnName = idColumnName;
    }

    /**
     * クラス取得
     * @return Class
     */
    public Class<? extends DatabaseEntity> getEntityClass() {
        return this.entityClass;
    }
    /*
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

    // ユーティリティ
    /**
     * クラスから対応するEnumを取得
     * @param entity Class extends DatabaseEntity
     * @return DatabaseEntityKind
     */
    public static DatabaseEntityKind fromEntity(DatabaseEntity entity) {
        for (DatabaseEntityKind kind : values()) {
            if (kind.entityClass.equals(entity.getClass())) {
                return kind;
            }
        }
        throw new IllegalArgumentException("DatabaseEntityKind fromEntity 登録されていないクラスを検索しています: " + entity.getClass().getName());
    }
}
