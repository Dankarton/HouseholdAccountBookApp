package com.example.householdaccountbook.repository;

import com.example.householdaccountbook.db.MyDbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.example.householdaccountbook.myclasses.dbentity.DatabaseEntity;

public class DatabaseEntityRepository<T extends DatabaseEntity> {
    private final Class<T> clazz;
    private final Map<Long, T> cache = new HashMap<>();
    private boolean isInitialized = false;

    public DatabaseEntityRepository(Class<T> clazz) {
        this.clazz = clazz;
    }
    public void init(MyDbManager db) {
        if (this.isInitialized) return;
        ArrayList<T> dataList = db.getAll(clazz);
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

    public void refresh(MyDbManager db) {
        this.clearCache();
        this.init(db);
    }
    public int size() {
        return this.cache.size();
    }
    public Set<Long> keySet() { return this.cache.keySet(); }

    public void clearCache() {
        this.cache.clear();
        isInitialized = false;
    }
}
