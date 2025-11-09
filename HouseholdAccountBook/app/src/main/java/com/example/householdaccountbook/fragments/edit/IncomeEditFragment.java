package com.example.householdaccountbook.fragments.edit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.ItemListCustomView;
import com.example.householdaccountbook.customviews.item.CategoryItemView;
import com.example.householdaccountbook.db.MyDbManager;

import java.util.ArrayList;
import java.util.Calendar;

import myclasses.BopCategory;
import myclasses.Income;
import myclasses.IncomeCategory;
import myclasses.PurchaseCategory;

public class IncomeEditFragment extends BaseEditFragment<Income> {
    TextView dateTextView;
    Calendar currentDate;
    EditText memoEditText;
    EditText amountEditText;
    ItemListCustomView<CategoryItemView<IncomeCategory>, IncomeCategory> categoryList;

    public IncomeEditFragment(@NonNull Income data) {
        super(data);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = requireContext();
        //
        // Viewオブジェクトの登録
        //
        this.dateTextView = view.findViewById(R.id.exp_date_text);
        this.memoEditText = view.findViewById(R.id.memo_edit_text);
        this.amountEditText = view.findViewById(R.id.amount_edit_text);
        this.categoryList = view.findViewById(R.id.category_list_custom_view);
        //
        // カテゴリーリストにItemViewを挿入する操作
        //
        ArrayList<IncomeCategory> categoryList = MyDbManager.getAll(IncomeCategory.class);
        ArrayList<CategoryItemView<IncomeCategory>> categoryItemViews = new ArrayList<>();
        for (IncomeCategory category : categoryList) {
            CategoryItemView<IncomeCategory> tmp = new CategoryItemView<>(context);
            tmp.setData(category);
            // 編集するデータのカテゴリーと同じ場合だった場合，選択状態にする．
            if (category.getId() == this.databaseEntityData.getCategoryId()) {
                tmp.setSelectedState(true);
            }
            categoryItemViews.add(tmp);
        }
        this.categoryList.setItem(categoryItemViews);
        //
        // イベントリスナー登録
        //
        // 日付プラスボタンが押された時のイベント
        view.findViewById(R.id.date_up_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentDate.add(Calendar.DAY_OF_MONTH, 1);
                        updateDateTextView();
                        saveButton.setEnabled(checkInputData());
                    }
                }
        );
        // 日付マイナスボタンが押された時のイベント
        view.findViewById(R.id.date_down_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentDate.add(Calendar.DAY_OF_MONTH, -1);
                        updateDateTextView();
                        saveButton.setEnabled(checkInputData());
                    }
                }
        );
        // メモ，金額のテキストエディターが変更された時のイベント
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                saveButton.setEnabled(checkInputData());
            }
        };
        this.memoEditText.addTextChangedListener(watcher);
        this.amountEditText.addTextChangedListener(watcher);
        // カテゴリー，支払い方法のアイテムリストの選択アイテムが変更された時のイベント
        ItemListCustomView.OnItemSelectedListener itemListListener = new ItemListCustomView.OnItemSelectedListener() {
            @Override
            public <T1> void onItemSelected(T1 itemView) {
                saveButton.setEnabled(checkInputData());
            }
        };
        this.categoryList.setListener(itemListListener);
        //
        // 初期値設定
        //

        this.memoEditText.setText(this.databaseEntityData.getMemo());
        if (this.databaseEntityData.getAmount() == 0) {
            this.amountEditText.setText("");
        }
        else {
            this.amountEditText.setText(String.valueOf(this.databaseEntityData.getAmount()));
        }
        this.currentDate = this.databaseEntityData.getDate();
        updateDateTextView();
    }

    /**
     * ベースコンテナに取り付けるFragmentのID
     * @return int FragmentのID
     */
    @Override
    protected int getContainerContentLayoutId() {
        return R.layout.fragment_income_edit;
    }

    @Override
    protected void onSaveClicked() {
        int amount = Integer.parseInt(this.amountEditText.getText().toString());
        Income newIncome = new Income(
                this.databaseEntityData.getId(),
                this.currentDate,
                amount,
                this.memoEditText.getText().toString(),
                this.categoryList.getSelectedItem().getData().getId()
        );
        if (this.listener != null) this.listener.onSaveButtonClicked(newIncome);
    }

    private boolean checkInputData() {
        boolean isMemoValid = !this.memoEditText.getText().toString().isEmpty();
        boolean isAmountValid = MyStdlib.canConvertToInteger(this.amountEditText.getText().toString());
        boolean isCategoryValid = this.categoryList.getSelectedItem() != null;
        return isMemoValid && isAmountValid && isCategoryValid;
    }

    @Override
    protected void onDeleteClicked() {
        new AlertDialog.Builder(requireContext())
                .setTitle("確認")
                .setMessage("このデータを削除してもよろしいですか？")
                .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) listener.onDeleteButtonClicked(databaseEntityData);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void updateDateTextView() {
        this.dateTextView.setText(
                MyStdlib.convertCalendarToString(
                        this.currentDate.get(Calendar.YEAR),
                        this.currentDate.get(Calendar.MONTH),
                        this.currentDate.get(Calendar.DATE),
                        this.currentDate.get(Calendar.DAY_OF_WEEK)
                )
        );
    }

    public void reset() {
        this.memoEditText.setText("");
        this.amountEditText.setText("");
        this.categoryList.deselect();
    }
}
