package myclasses;

import com.example.householdaccountbook.MyDbContract;

/**
 * DatabaseEntityの種類と対応するデータベーステーブル名を管理する列挙型クラス
 * --※※※--新しくDatabaseEntityを実装したクラスがあったら必ず登録するように--※※※--
 */
public enum DatabaseEntityKind {
    EXPENSES(Expenses.class, MyDbContract.ExpensesEntry.TABLE_NAME),
    INCOME(Income.class, MyDbContract.IncomeEntry.TABLE_NAME),
    EXPENSES_CATEGORY(ExpensesCategory.class, MyDbContract.ExpensesCategoryEntry.TABLE_NAME),
    INCOME_CATEGORY(IncomeCategory.class, MyDbContract.IncomeCategoryEntry.TABLE_NAME),
    PAYMENT_METHOD(PaymentMethod.class, MyDbContract.PaymentMethodEntry.TABLE_NAME);

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
     */
    private DatabaseEntityKind(Class<? extends DatabaseEntity> entityClass, String tableName) {
        this.entityClass = entityClass;
        this.tableName = tableName;
    }

    /**
     * クラス取得
     * @return Class
     */
    public Class<? extends DatabaseEntity> getEntityClass() {
        return this.entityClass;
    }
    public String getTableName() {
        return this.tableName;
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
