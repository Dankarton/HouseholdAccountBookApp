package strategy.closingstrategy;

import java.util.Calendar;

/**
 * 締め日無し
 */
public class NoneClosingRule implements ClosingStrategy {
    @Override
    public Calendar apply(Calendar purchaseDate) {
        return (Calendar) purchaseDate.clone();
    }
    @Override
    public String getName() {
        return "無し";
    }
    @Override
    public boolean usesSettingNum() {
        return false;
    }
}
