package com.example.householdaccountbook.fragments.edit;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.item.CategoryItemView;
import com.example.householdaccountbook.customviews.ItemListCustomView;
import com.example.householdaccountbook.customviews.item.PaymentMethodItemView;
import com.example.householdaccountbook.db.MyDbManager;

import java.util.ArrayList;
import java.util.Calendar;

import myclasses.BopCategory;
import myclasses.Expenses;
import myclasses.ExpensesCategory;
import myclasses.PaymentMethod;

public class ExpensesEditFragment extends BaseEditFragment<Expenses> {
    TextView dateTextView;
    Calendar currentDate;
    EditText memoEditText;
    EditText amountEditText;
    ItemListCustomView<CategoryItemView, BopCategory> categoryList;
    ItemListCustomView<PaymentMethodItemView, PaymentMethod> paymentMethodList;

    public ExpensesEditFragment(@NonNull Expenses data) {
        super(data);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = requireContext();
        this.dateTextView = view.findViewById(R.id.exp_date_text);
        this.memoEditText = view.findViewById(R.id.exp_memo_edit_text);
        this.amountEditText = view.findViewById(R.id.exp_amount_edit_text);
        this.categoryList = view.findViewById(R.id.category_list_custom_view);
        this.paymentMethodList = view.findViewById(R.id.payment_method_edit_menu_button);

        ArrayList<ExpensesCategory> categoryList = MyDbManager.getAll(ExpensesCategory.class);
        ArrayList<CategoryItemView> itemViews = new ArrayList<>();
        for (int i = 0; i < categoryList.size(); i++) {
            CategoryItemView tmp = new CategoryItemView(context);
            ExpensesCategory ec = categoryList.get(i);
            tmp.setData(ec);
            if (ec.getId() == this.databaseEntityData.getCategoryId()) {
                tmp.setSelectedState(true);
            }
            itemViews.add(tmp);
        }
    }
    @Override
    protected int getContainerContentLayoutId() {
        return R.layout.fragment_expenses_edit;
    }

    @Override
    protected void onSaveClicked() {

    }

    @Override
    protected void onDeleteClicked() {

    }
}
