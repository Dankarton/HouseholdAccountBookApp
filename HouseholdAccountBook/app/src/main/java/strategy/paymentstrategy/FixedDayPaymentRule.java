package strategy.paymentstrategy;

import java.util.Calendar;

/**
 * 締め日の翌月の任意日に支払うルール
 */
public class FixedDayPaymentRule implements PaymentStrategy {
    private final int fixedDay;

    public FixedDayPaymentRule(int fixedDay) {
        this.fixedDay = fixedDay;
    }
    @Override
    public Calendar apply(Calendar closingDate) {
        Calendar paymentDate = (Calendar) closingDate.clone();
        paymentDate.add(Calendar.MONTH, 1);
        paymentDate.set(Calendar.DATE, this.fixedDay);
        return paymentDate;
    }
    @Override
    public String getName() { return "締め日の翌月指定日に支払い"; }
    @Override
    public boolean usesSettingNum() { return true; }
}
