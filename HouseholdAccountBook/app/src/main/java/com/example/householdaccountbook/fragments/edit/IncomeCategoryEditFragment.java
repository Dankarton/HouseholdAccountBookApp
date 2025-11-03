package com.example.householdaccountbook.fragments.edit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.ColorPaletteCustomView;

import myclasses.IncomeCategory;

public class IncomeCategoryEditFragment extends BaseEditFragment<IncomeCategory> {
    private EditText nameEdit;
    private ColorPaletteCustomView colorPalette;

    public IncomeCategoryEditFragment(@NonNull IncomeCategory data) {
        super(data);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.nameEdit = view.findViewById(R.id.name_edit_text);
        this.colorPalette = view.findViewById(R.id.color_palette);
        this.nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { /*Do nothing*/ }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { /*Do nothing*/ }

            @Override
            public void afterTextChanged(Editable editable) {
                saveButton.setEnabled(checkInputData());
            }
        });
        colorPalette.setOnColorSelectedListener(new ColorPaletteCustomView.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int colorInt) {
                saveButton.setEnabled(checkInputData());
            }
        });
    }

    @Override
    protected int getContainerContentLayoutId() {
        return R.layout.fragment_bop_category_edit;
    }

    @Override
    protected void onSaveClicked() {
        // リスナーが登録されてなかったら終了
        if (this.listener == null) return;
        //
        String name = nameEdit.getText().toString();
        int colorInt = colorPalette.getSelectedColor();
        IncomeCategory incomeCategory = new IncomeCategory(
                this.databaseEntityData.getId(),
                name,
                colorInt,
                this.databaseEntityData.getIndex(),
                this.databaseEntityData.isDeleted()
        );
        this.listener.onSaveButtonClicked(incomeCategory);
    }

    @Override
    protected void onDeleteClicked() {
        this.listener.onDeleteButtonClicked(this.databaseEntityData);
    }

    /**
     * ユーザー入力のデータが正しく揃っているか確認する関数
     *
     * @return 全て揃ってるとtrue，一つでも適合しないデータがあるとfalse
     */
    private boolean checkInputData() {
        boolean isNameValid = !nameEdit.getText().toString().isEmpty();
        boolean isColorValid = colorPalette.isSelected();
        return isNameValid && isColorValid;
    }
}