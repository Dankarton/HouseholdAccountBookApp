package strategy.closingstrategy;

import java.util.Calendar;

/**
 * 固定日が締め日
 */
public class FixedDayClosingRule implements ClosingStrategy {
    private final int fixedDay;
    public FixedDayClosingRule(int fixedDay) {
        this.fixedDay = fixedDay;
    }
    @Override
    public Calendar apply(Calendar purchaseDate) {
        Calendar closingDate = (Calendar) purchaseDate.clone();
        if (closingDate.get(Calendar.DATE) <= this.fixedDay) {
            closingDate.set(Calendar.DATE, this.fixedDay);
        }
        else {
            closingDate.add(Calendar.MONTH, 1);;
            closingDate.set(Calendar.DATE, this.fixedDay);
        }
        return closingDate;
    }
    @Override
    public String getName() {
        return "指定日";
    }
    @Override
    public boolean usesSettingNum() {
        return true;
    }
}
