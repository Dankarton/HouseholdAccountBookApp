package com.example.householdaccountbook.strategy.closingstrategy;

import java.util.Calendar;

public interface ClosingStrategy {
    public Calendar apply(Calendar purchaseDate);
}
