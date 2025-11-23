package com.example.householdaccountbook.repository;

import com.example.householdaccountbook.myclasses.dbentity.IncomeCategory;
import com.example.householdaccountbook.myclasses.dbentity.PaymentMethod;
import com.example.householdaccountbook.myclasses.dbentity.PurchaseCategory;

public interface CacheProvider {
    DatabaseEntityRepository<IncomeCategory> getIncomeCategoryRepository();
    DatabaseEntityRepository<PurchaseCategory> getPurchaseCategoryRepository();
    DatabaseEntityRepository<PaymentMethod> getPaymentMethodRepository();
}
