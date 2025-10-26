package myclasses;

import com.example.householdaccountbook.MyOpenHelper;

public class ExpensesCategory extends BopCategory {
    public ExpensesCategory(Integer id, String name, int colorCode, int index, boolean isDeleted) {
        super(id, name, colorCode, index, isDeleted);
    }
    @Override
    public String getDatabaseName() {
        return MyOpenHelper.EXPENSES_CATEGORY_TABLE_NAME;
    }
}
