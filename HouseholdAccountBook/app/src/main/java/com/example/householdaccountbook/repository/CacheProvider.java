package com.example.householdaccountbook.repository;

import myclasses.IncomeCategory;
import myclasses.PaymentMethod;
import myclasses.PurchaseCategory;

public interface CacheProvider {
    DatabaseEntityRepository<IncomeCategory> getIncomeCategoryRepository();
    DatabaseEntityRepository<PurchaseCategory> getPurchaseCategoryRepository();
    DatabaseEntityRepository<PaymentMethod> getPaymentMethodRepository();
}
