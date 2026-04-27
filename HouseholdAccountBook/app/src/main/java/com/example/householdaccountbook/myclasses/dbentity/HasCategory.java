package com.example.householdaccountbook.myclasses.dbentity;

public interface HasCategory {
    long getCategoryId();
    Class<? extends BopCategory> getCategoryClass();
}
