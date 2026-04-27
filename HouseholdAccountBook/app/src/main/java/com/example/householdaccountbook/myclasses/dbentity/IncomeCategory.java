package com.example.householdaccountbook.myclasses.dbentity;

import com.example.householdaccountbook.repository.DatabaseEntityRepository;

public class IncomeCategory extends BopCategory {
    public IncomeCategory(Long id, String name, int colorCode, int index, boolean isDeleted) {
        super(id, name, colorCode, index, isDeleted);
    }
    @Override
    public void onAfterInsert(long newId) {
        super.onAfterInsert(newId);
        DatabaseEntityRepository<IncomeCategory> registry = DbEntityRepositoryRegistry.getRepository(IncomeCategory.class);
        if (registry != null) {
            registry.updateCache(this);
        }
    }

    @Override
    public void onAfterUpdate(DatabaseEntity before) {
        super.onAfterUpdate(before);
        DatabaseEntityRepository<IncomeCategory> registry = DbEntityRepositoryRegistry.getRepository(IncomeCategory.class);
        if (registry != null) {
            registry.updateCache(this);
        }
    }

    @Override
    public void onAfterDelete() {
        super.onAfterDelete();
        DatabaseEntityRepository<IncomeCategory> registry = DbEntityRepositoryRegistry.getRepository(IncomeCategory.class);
        if (registry != null) {
            registry.updateCache(this);
        }
    }
}
