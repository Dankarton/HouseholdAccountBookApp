package com.example.householdaccountbook.myclasses.dbentity;

import android.content.ContentValues;

import java.io.Serializable;

/**
 * データベース操作時に使うインターフェース
 * --※※※--新しく実装したクラスがあったらTableContractRegistryに必ず登録するように--※※※--
 */
public abstract class DatabaseEntity implements Serializable {
    public enum DeleteType {
        HARD,
        LOGICAL
    }
    private Long id;
    private boolean isDeleted;

    protected DatabaseEntity(Long id, boolean isDeleted) {
        this.id = id;
        this.isDeleted = isDeleted;
    }
    /**
     * データベースのID取得用.新規追加前でIDが割り当てられてない場合はnullを返すようにする
     * @return id
     */
    public final Long getId() { return this.id; }

    public final boolean isDeleted() { return isDeleted; }

    /**
     * データのContentValue取得．IDは除外するように！
     * @return ContentValues
     */
    public abstract ContentValues getContentValues();

    /**
     * 通常削除か論理削除かを返すメソッド．論理削除が必要なエンティティはこれをオーバーライドしてLOGICALにすること．
     * @return DeleteType
     */
    public DeleteType getDeleteType() { return DeleteType.HARD; }

    public void onBeforeInsert() {}
    public void onAfterInsert(long newId) { this.id = newId; }
    public void onBeforeUpdate() {}
    public void onAfterUpdate(DatabaseEntity before) {}
    public void onBeforeDelete() {}
    public void onAfterDelete() { this.isDeleted = true; }
}
