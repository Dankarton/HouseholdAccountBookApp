package strategy.closingstrategy;

import java.util.Calendar;

public interface ClosingStrategy {
    public Calendar apply(Calendar purchaseDate);
    String getName();
    boolean usesSettingNum();
}
