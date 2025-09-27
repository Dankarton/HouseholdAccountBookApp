package com.example.householdaccountbook.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.Calendar;

import myclasses.Income;

public class IncomeInputFragment extends Fragment {
    TextView dateTextView;
    Calendar currentDate;
    Spinner categorySpinner;
    EditText memoEditText;
    EditText amountEditText;
    Button addButton;
    private MyOpenHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        helper = new MyOpenHelper(view.getContext());
        dateTextView = view.findViewById(R.id.inc_date_text);
        categorySpinner = view.findViewById(R.id.income_category_spinner);
        memoEditText = view.findViewById(R.id.inc_memo_edit_text);
        amountEditText = view.findViewById(R.id.inc_amount_edit_text);
        addButton = view.findViewById(R.id.income_add_button);
        String[] categoryArray = {"給料", "アルバイト", "おこづかい", "副業", "賞与"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                view.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                categoryArray
        );
        categorySpinner.setAdapter(adapter);
        currentDate = Calendar.getInstance();
        setDateUpButtonEvent(view);
        setDateDownButtonEvent(view);
        setMemoEditTextEvent();
        setAmountEditText();
        setAddButtonEvent();
        updateDateTextView();
        addButton.setEnabled(false);
    }

    private void checkList() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("IncomeDb", new String[] {"_id", "year", "month", "day", "amount", "memo", "category"},
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            String strBuilder =
                    "id:" + cursor.getInt(0) +
                            ", y:" +
                            cursor.getInt(1) +
                            ", m:" +
                            cursor.getInt(2) +
                            ", d:" +
                            cursor.getInt(3) +
                            ", pay:" +
                            cursor.getInt(4) +
                            ", memo:" +
                            cursor.getString(5) +
                            ", category:" +
                            cursor.getString(6);
            Log.d("IncomeInputFragment", strBuilder);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void setDateUpButtonEvent(View view){
        ImageButton dateUpButton = view.findViewById(R.id.inc_date_up_button);
        dateUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                currentDate.add(Calendar.DAY_OF_MONTH, 1);
                updateDateTextView();
                changeToTrueAddButtonEnabled();
            }
        });
    }

    /**
     * 日付を＋1するボタンが押された時のイベントハンドラ関数
     * @param view View
     */
    private void setDateDownButtonEvent(View view){
        ImageButton dateUpButton = view.findViewById(R.id.inc_date_down_button);
        dateUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                currentDate.add(Calendar.DAY_OF_MONTH, -1);
                updateDateTextView();
                changeToTrueAddButtonEnabled();
            }
        });
    }

    /**
     * 保存ボタンのイベントハンドラ関数
     */
    private void setAddButtonEvent() {
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String inpAmountText = amountEditText.getText().toString();
                if (!MyStdlib.canConvertToInteger(inpAmountText)) { return; }
                int amount = Integer.parseInt(inpAmountText);
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH) + 1;
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                String memo = memoEditText.getText().toString();
                String category = (String) categorySpinner.getSelectedItem();
                MyDbManager.setRecordToDataBase("IncomeDb", Income.convertContentValues(year, month, day, amount, memo, category));
                addButton.setEnabled(false);
            }
        });
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

    /**
     * 保存ボタンが押せる状態かどうか判断して変更する関数
     * メモが未入力だったり，金額が未入力だったら押せないようにする．
     */
    private void changeToTrueAddButtonEnabled() {
        if (amountEditText.getText().toString().isEmpty()) {
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