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

import myclasses.ExpensesCategory;

public class ExpensesCategoryEditFragment extends BaseEditFragment<ExpensesCategory> {
    private EditText nameEdit;
    private ColorPaletteCustomView colorPalette;
    public ExpensesCategoryEditFragment(@NonNull ExpensesCategory data) {
        super(data);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (this.listener == null) return;

        String name = nameEdit.getText().toString();
        int colorInt = colorPalette.getSelectedColor();
        ExpensesCategory expensesCategory = new ExpensesCategory(
                this.databaseEntityData.getId(),
                name,
                colorInt,
                this.databaseEntityData.getIndex(),
                this.databaseEntityData.isDeleted()
        );
        this.listener.onSaveButtonClicked(expensesCategory);
    }

    @Override
    protected void onDeleteClicked() {
        this.listener.onDeleteButtonClicked(this.databaseEntityData);
    }
    private boolean checkInputData() {
        boolean isNameValid = !nameEdit.getText().toString().isEmpty();
        boolean isColorValid = colorPalette.isSelected();
        return isNameValid && isColorValid;
    }
}