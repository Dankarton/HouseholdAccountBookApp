package com.example.householdaccountbook.module.dbentity;

public interface HasCategory {
    long getCategoryId();
    Class<? extends BopCategory> getCategoryClass();
}
