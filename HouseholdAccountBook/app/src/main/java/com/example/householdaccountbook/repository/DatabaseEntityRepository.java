package com.example.householdaccountbook.repository;

import com.example.householdaccountbook.db.MyDbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import myclasses.DatabaseEntity;

public class DatabaseEntityRepository<T extends DatabaseEntity> {
    private final Class<T> clazz;
    private final Map<Long, T> cache = new HashMap<>();
    private boolean isInitialized = false;

    public DatabaseEntityRepository(Class<T> clazz) {
        this.clazz = clazz;

    }
    public void init() {
        if (this.isInitialized) return;
        ArrayList<T> dataList = MyDbManager.getAll(clazz);
        for (T data : dataList) {
            this.cache.put(data.getId(), data);
        }
        this.isInitialized = true;
    }

    public T getDataById(long id) {
        return this.cache.get(id);
    }

    public void updateCache(T data) {
        this.cache.put(data.getId(), data);
    }

    public void removeCache(T data) {
        this.cache.remove(data.getId());
    }

    public void refresh() {
        this.clearCache();
        this.init();
    }
    public int size() {
        return this.cache.size();
    }

    public void clearCache() {
        this.cache.clear();
        isInitialized = false;
    }
}
