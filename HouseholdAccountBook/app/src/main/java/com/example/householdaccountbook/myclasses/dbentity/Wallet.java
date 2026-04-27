package com.example.householdaccountbook.myclasses.dbentity;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;

public class Wallet extends DatabaseEntity {
    private final String name;
    private final int initAmount;
    private final int displayIndex;
    private final boolean isDefault;


    public Wallet(Long id, String name, int initAmount, int displayIndex, boolean isDefault, boolean isDeleted) {
        super(id, isDeleted);
        this.name = name;
        this.initAmount = initAmount;
        this.displayIndex = displayIndex;
        this.isDefault = isDefault;
    }
    public String getName() { return this.name; }
    public int getInitAmount() { return this.initAmount; }
    public int getDisplayIndex() { return this.displayIndex; }
    public boolean getIsDefault() { return this.isDefault; }

    @Override
    public DeleteType getDeleteType() {
        return DeleteType.LOGICAL;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        int isDefaultInteger;
        if (this.isDefault) {
            isDefaultInteger = 1;
        }
        else {
            isDefaultInteger = 0;
        }
        int isDeletedInteger;
        if(this.isDeleted()) {
            isDeletedInteger = 1;
        }
        else {
            isDeletedInteger = 0;
        }
        values.put(MyDbContract.WalletEntry.COLUMN_NAME, this.name);
        values.put(MyDbContract.WalletEntry.COLUMN_INIT_AMOUNT, this.initAmount);
        values.put(MyDbContract.WalletEntry.COLUMN_DISPLAY_INDEX, this.displayIndex);
        values.put(MyDbContract.WalletEntry.COLUMN_IS_DEFAULT, isDefaultInteger);
        values.put(MyDbContract.WalletEntry.COLUMN_IS_DELETED, isDeletedInteger);
        return values;
    }
}
