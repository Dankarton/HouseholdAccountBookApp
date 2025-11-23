package com.example.householdaccountbook.myclasses.dbentity;

import com.example.householdaccountbook.repository.DatabaseEntityRepository;
import com.example.householdaccountbook.repository.DbEntityRepositoryRegistry;

public class PurchaseCategory extends BopCategory {
    public PurchaseCategory(Long id, String name, int colorCode, int index, boolean isDeleted) {
        super(id, name, colorCode, index, isDeleted);
    }

    @Override
    public void onAfterInsert(long newId) {
        super.onAfterInsert(newId);
        DatabaseEntityRepository<PurchaseCategory> registry = DbEntityRepositoryRegistry.getRepository(PurchaseCategory.class);
        if (registry != null) {
            registry.updateCache(this);
        }
    }

    @Override
    public void onAfterUpdate(DatabaseEntity before) {
        super.onAfterUpdate(before);
        DatabaseEntityRepository<PurchaseCategory> repository = DbEntityRepositoryRegistry.getRepository(PurchaseCategory.class);
        if (repository != null) {
            repository.updateCache(this);
        }
    }

    @Override
    public void onAfterDelete() {
        super.onAfterDelete();
        DatabaseEntityRepository<PurchaseCategory> repository = DbEntityRepositoryRegistry.getRepository(PurchaseCategory.class);
        if (repository != null) {
            repository.updateCache(this);
        }
    }
}
