package myclasses;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;

import java.io.Serializable;

public abstract class BopCategory implements DatabaseEntity, Serializable {
    private final Long _id;
    private final String _name;
    private final int _colorCode;
    private final int _index;
    private boolean _isDeleted;

    public BopCategory(Long id, String name, int colorCode, int index, boolean isDeleted) {
        this._id = id;
        this._name = name;
        this._colorCode = colorCode;
        this._index = index;
        this._isDeleted = isDeleted;
    }
    public ContentValues getContentValuesWithoutId() {
        ContentValues values = new ContentValues();
        int isDeletedInteger;
        if (this._isDeleted) {
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
    @Override
    public Long getId() {
        return this._id;
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
    public boolean isDeleted() { return this._isDeleted; }
    @Override
    public ContentValues getContentValues() {
        return getContentValuesWithoutId();
    }

    public void setIsDeleted(boolean isDeleted) { this._isDeleted = isDeleted; }
}
