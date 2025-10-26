package myclasses;

import com.example.householdaccountbook.MyDbContract;
import com.example.householdaccountbook.MyOpenHelper;

public class IncomeCategory extends BopCategory {
    public IncomeCategory(Integer id, String name, int colorCode, int index, boolean isDeleted) {
        super(id, name, colorCode, index, isDeleted);
    }
    @Override
    public String getDatabaseName() {
        return MyDbContract.IncomeCategoryEntry.TABLE_NAME;
    }
}
