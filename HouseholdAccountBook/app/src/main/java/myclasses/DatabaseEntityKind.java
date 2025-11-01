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
    EXPENSES(Expenses.class, new MyDbContract.ExpensesEntry()),
    INCOME(Income.class, new MyDbContract.IncomeEntry()),
    EXPENSES_CATEGORY(ExpensesCategory.class, new MyDbContract.ExpensesCategoryEntry()),
    INCOME_CATEGORY(IncomeCategory.class, new MyDbContract.IncomeCategoryEntry()),
    PAYMENT_METHOD(PaymentMethod.class, new MyDbContract.PaymentMethodEntry());

    // フィールド
    // クラス
    private final Class<? extends DatabaseEntity> entityClass;
    private final MyDbContract.TableContract<? extends DatabaseEntity> tableContract;
    /**
     * コンストラクタ
     * @param entityClass クラス
     * @param tableName テーブル名
     * @param idColumnName idカラム名
     */
    private DatabaseEntityKind(Class<? extends DatabaseEntity> entityClass, MyDbContract.TableContract<? extends DatabaseEntity> tableContract) {
        this.entityClass = entityClass;
        this.tableContract = tableContract;
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
    public MyDbContract.TableContract<? extends DatabaseEntity> getTableContract() { return this.tableContract; }
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
