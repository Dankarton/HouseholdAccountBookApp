package com.example.householdaccountbook.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.householdaccountbook.MyDbManager;
import com.example.householdaccountbook.MyOpenHelper;
import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.CategoryItemView;
import com.example.householdaccountbook.customviews.ItemListCustomView;

import java.util.ArrayList;
import java.util.Calendar;

import myclasses.BopCategory;
import myclasses.Expenses;
import myclasses.PaymentMethod;

public class ExpensesInputFragment extends Fragment {
    public interface InputCompleteListener{
        void onExpensesInputCompleted();
    }
    InputCompleteListener listener = null;
    TextView dateTextView;
    Calendar currentDate;
    Spinner categorySpinner;
    Spinner paymentMethodSpinner;
    PaymentMethod[] paymentMethodArray;
    EditText memoEditText;
    EditText amountEditText;
    Button addButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        dateTextView = view.findViewById(R.id.exp_date_text);
        categorySpinner = view.findViewById(R.id.expenses_category_spinner);
        paymentMethodSpinner = view.findViewById(R.id.payment_method_spinner);
        memoEditText = view.findViewById(R.id.exp_memo_edit_text);
        amountEditText = view.findViewById(R.id.exp_amount_edit_text);
        addButton = view.findViewById(R.id.expenses_add_button);
        ArrayList<BopCategory> categories = MyDbManager.getAllExpensesCategoryData();
        String[] categoryArray = {"食費", "日用品", "通信費", "通販", "グッズ", "サブスク"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                view.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                categoryArray
        );
        categorySpinner.setAdapter(categoryAdapter);
        // 支払方法をスピナーに登録する
        ArrayList<PaymentMethod> paymentMethods = MyDbManager.getAllPaymentMethodData();
        String[] pmStrings = new String[paymentMethods.size()];
        paymentMethodArray = new PaymentMethod[paymentMethods.size()];
        for (int i = 0; i < pmStrings.length; i++) {
            pmStrings[i] = paymentMethods.get(i).getName();
            paymentMethodArray[i] = paymentMethods.get(i);
        }
        ArrayAdapter<String> paymentMethodAdapter = new ArrayAdapter<>(
                view.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                pmStrings
        );
        paymentMethodSpinner.setAdapter(paymentMethodAdapter);

        // テスト中
        ItemListCustomView<CategoryItemView> itemListCustomView = view.findViewById(R.id.category_list_custom_view);
        ArrayList<BopCategory> categoryList = new ArrayList<>();
        categoryList.add(new BopCategory(1, "食費", Color.parseColor("#FF0000")));
        categoryList.add(new BopCategory(2, "燃料", Color.parseColor("#00FF00")));
        categoryList.add(new BopCategory(3, "サブスク", Color.parseColor("#0000FF")));
        ArrayList<CategoryItemView> itemViews = new ArrayList<>();
        for (int i = 0; i < categoryList.size(); i++) {
            CategoryItemView tmp = new CategoryItemView(view.getContext());
            tmp.setData(categoryList.get(i));
            itemViews.add(tmp);
        }
        itemListCustomView.setItem(itemViews);
        //

        currentDate = Calendar.getInstance();
        setDateUpButtonEvent(view);
        setDateDownButtonEvent(view);
        setAddButtonEvent(view);
        setMemoEditTextEvent();
        setAmountEditText();
        updateDateTextView();
        addButton.setEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expenses_input, container, false);
    }

    private void setDateUpButtonEvent(View view){
        ImageButton dateUpButton = view.findViewById(R.id.exp_date_up_button);
        dateUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                currentDate.add(Calendar.DAY_OF_MONTH, 1);
                updateDateTextView();
                changeToTrueAddButtonEnabled();
            }
        });
    }
    private void setDateDownButtonEvent(View view){
        ImageButton dateUpButton = view.findViewById(R.id.exp_date_down_button);
        dateUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                currentDate.add(Calendar.DAY_OF_MONTH, -1);
                updateDateTextView();
                changeToTrueAddButtonEnabled();
            }
        });
    }

    private void setAddButtonEvent(View view){
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String inpAmountText = amountEditText.getText().toString();
                // 金額が数字に変換出来ない時
                if (!MyStdlib.canConvertToInteger(inpAmountText)){ return; }
                int amount = Integer.parseInt(inpAmountText);
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                String memo = memoEditText.getText().toString();
                String category = (String)categorySpinner.getSelectedItem();
                PaymentMethod paymentMethod = paymentMethodArray[paymentMethodSpinner.getSelectedItemPosition()];
                Calendar paymentDate = paymentMethod.getPaymentDate(MyStdlib.convertToCalendar(year, month, day));
                MyDbManager.setRecordToDataBase(
                        MyOpenHelper.EXPENSES_TABLE_NAME,
                        Expenses.convertContentValues(
                                year,
                                month,
                                day,
                                amount,
                                memo,
                                category,
                                paymentMethod.getId(),
                                paymentDate.get(Calendar.YEAR),
                                paymentDate.get(Calendar.MONTH),
                                paymentDate.get(Calendar.DATE)
                                )
                );
                if(null != listener) {
                    listener.onExpensesInputCompleted();
                }
                addButton.setEnabled(false);
            }
        });
    }
    private void setMemoEditTextEvent() {
        memoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeToTrueAddButtonEnabled();
            }
        });
    }
    private void setAmountEditText() {
        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeToTrueAddButtonEnabled();
            }
        });
    }
    public void attachListener(ExpensesInputFragment.InputCompleteListener listener) {
        this.listener = listener;
    }
    private void updateDateTextView(){
        dateTextView.setText(
                MyStdlib.convertCalendarToString(
                        currentDate.get(Calendar.YEAR),
                        currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DATE),
                        currentDate.get(Calendar.DAY_OF_WEEK)
                )
        );
    }
    private void changeToTrueAddButtonEnabled() {
        Log.d("MyTestLog", "" + amountEditText.getText().toString().isEmpty());
        if (amountEditText.getText().toString().isEmpty()){
            addButton.setEnabled(false);
            return;
        }
        if (!MyStdlib.canConvertToInteger(amountEditText.getText().toString())){
            addButton.setEnabled(false);
            return;
        }
        addButton.setEnabled(true);
    }
}