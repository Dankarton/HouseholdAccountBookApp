package com.example.householdaccountbook.myclasses.dbentity;

import android.content.ContentValues;

import com.example.householdaccountbook.db.MyDbContract;

public class Wallet extends DatabaseEntity {
    private final String name;
    private final int initAmount;
    private final int displayIndex;

    public Wallet(Long id, String name, int initAmount, int displayIndex) {
        super(id);
        this.name = name;
        this.initAmount = initAmount;
        this.displayIndex = displayIndex;
    }
    public String getName() { return this.name; }
    public int getInitAmount() { return this.initAmount; }
    public int getDisplayIndex() { return this.displayIndex; }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MyDbContract.WalletEntry.COLUMN_NAME, this.name);
        values.put(MyDbContract.WalletEntry.COLUMN_INIT_AMOUNT, this.initAmount);
        values.put(MyDbContract.WalletEntry.COLUMN_DISPLAY_INDEX, this.displayIndex);
        return values;
    }
}
