package strategy.paymentstrategy;

import java.util.Calendar;

/**
 * 購入日と支払日が同じ時のルール
 */
public class SameDayPaymentRule implements PaymentStrategy {
    @Override
    public Calendar apply(Calendar closingDate) {
        return (Calendar) closingDate.clone();
    }
}
