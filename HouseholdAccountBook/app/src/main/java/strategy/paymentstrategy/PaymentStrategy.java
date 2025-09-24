package strategy.paymentstrategy;

import java.util.Calendar;

public interface PaymentStrategy {
    Calendar apply(Calendar closingDate);
}
