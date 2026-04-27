package com.example.householdaccountbook.myclasses.dbentity;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;

public abstract class BopCategory extends DatabaseEntity {
    private final String _name;
    private final int _colorCode;
    private final int _index;

    public BopCategory(Long id, String name, int colorCode, int index, boolean isDeleted) {
        super(id, isDeleted);
        this._name = name;
        this._colorCode = colorCode;
        this._index = index;
    }
    public ContentValues getContentValuesWithoutId() {
        ContentValues values = new ContentValues();
        int isDeletedInteger;
        if (this.isDeleted()) {
            isDeletedInteger = 1;
        }
        else {
            isDeletedInteger = 0;
        }
        values.put(MyDbContract.BaseCategoryEntry.COLUMN_NAME, this._name);
        values.put(MyDbContract.BaseCategoryEntry.COLUMN_COLOR, this._colorCode);
        values.put(MyDbContract.BaseCategoryEntry.COLUMN_IS_DELETED, isDeletedInteger);
        return values;
    }
    public String getName() {
        return this._name;
    }
    public int getColorCode() {
        return this._colorCode;
    }
    public int getIndex() {
        return this._index;
    }

    @Override
    public ContentValues getContentValues() {
        return getContentValuesWithoutId();
    }

    @Override
    public DeleteType getDeleteType() {
        return DeleteType.LOGICAL;
    }
}