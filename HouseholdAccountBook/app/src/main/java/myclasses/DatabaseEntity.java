package myclasses;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

/**
 * データベース操作時に使うインターフェース
 * --※※※--新しく実装したクラスがあったらDatabaseEntityKindに必ず登録するように--※※※--
 */
public interface DatabaseEntity extends Serializable {
    /**
     * データベースのID取得用.新規追加前でIDが割り当てられてない場合はnullを返すようにする
     * @return Integer: id
     */
    Integer getId();
    /**
     * データのContentValue取得．IDは除外するように！
     * @return ContentValues
     */
    ContentValues getContentValues();
}
