package strategy.closingstrategy;

import java.util.Calendar;

/**
 * 月末が締め日
 */
public class EndOfMonthClosingRule implements ClosingStrategy {

    @Override
    public Calendar apply(Calendar purchaseDate) {
        Calendar closingDate = (Calendar) purchaseDate.clone();
        int lastDay = closingDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        closingDate.set(Calendar.DATE, lastDay);
        return closingDate;
    }
    @Override
    public String getName() {
        return "月末";
    }
    @Override
    public boolean usesSettingNum() {
        return true;
    }
}
