package strategy.paymentstrategy;

import java.util.Calendar;

public interface PaymentStrategy {
    String getName();
    boolean usesSettingNum();
    Calendar apply(Calendar closingDate);
}
