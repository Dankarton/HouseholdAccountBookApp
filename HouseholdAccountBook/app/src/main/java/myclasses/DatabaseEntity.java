package myclasses;

import android.content.ContentValues;

public interface DatabaseEntity {
    /**
     * データベースのID取得用.新規追加前でIDが割り当てられてない場合はnullを返すようにする
     * @return Integer: id
     */
    Integer getId();

    /**
     * 登録先のテーブル名を取得
     * @return String: テーブル名
     */
    String getDatabaseName();

    /**
     * データのContentValue取得．IDは除外するように！
     * @return ContentValues
     */
    ContentValues getContentValues();
}
