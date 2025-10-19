package strategy.paymentstrategy;

import java.util.Calendar;

/**
 * 締め日の翌月の最終日に支払うルール
 */
public class EndOfMonthPaymentRule implements PaymentStrategy {
    @Override
    public Calendar apply(Calendar closingDate) {
        Calendar paymentDate = (Calendar) closingDate.clone();
        int lastDay = paymentDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        paymentDate.add(Calendar.MONTH, 1);
        paymentDate.set(Calendar.DATE, lastDay);
        return paymentDate;
    }

    @Override
    public String getName() {
        return "締め日の翌月末に支払い";
    }

    @Override
    public boolean usesSettingNum() {
        return false;
    }
}
