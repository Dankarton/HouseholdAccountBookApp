package strategy.paymentstrategy;

import java.util.Calendar;

/**
 * 締め日から任意日数後に支払うルール
 */
public class AfterClosingPaymentRule implements PaymentStrategy {
    private final int afterDayNum;

    public AfterClosingPaymentRule(int afterDayNum) {
        this.afterDayNum = afterDayNum;
    }

    @Override
    public Calendar apply(Calendar closingDate) {
        Calendar paymentDate = (Calendar) closingDate.clone();
        // 指定日数分進める
        paymentDate.add(Calendar.DATE, this.afterDayNum);
        return paymentDate;
    }
    @Override
    public String getName() { return "締め日の指定日後に支払い"; }
    @Override
    public boolean usesSettingNum() { return true; }
}
