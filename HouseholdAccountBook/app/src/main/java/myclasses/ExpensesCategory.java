package myclasses;

import com.example.householdaccountbook.MyDbContract;

public class ExpensesCategory extends BopCategory {
    public ExpensesCategory(Integer id, String name, int colorCode, int index, boolean isDeleted) {
        super(id, name, colorCode, index, isDeleted);
    }
}
