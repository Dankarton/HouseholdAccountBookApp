package com.example.householdaccountbook.fragments.edit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.ColorPaletteCustomView;
import com.example.householdaccountbook.customviews.SelectableListCustomView;
import com.example.householdaccountbook.customviews.item.ColorItemView;

import java.util.ArrayList;

import myclasses.PurchaseCategory;

public class PurchaseCategoryEditFragment extends BaseEditFragment<PurchaseCategory> {
    private EditText nameEdit;
    private SelectableListCustomView<ColorItemView, Integer> colorPalette;
    public PurchaseCategoryEditFragment(@NonNull PurchaseCategory data) {
        super(data);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();
        this.nameEdit = view.findViewById(R.id.name_edit_text);
        this.colorPalette = view.findViewById(R.id.color_palette);
        this.colorPalette.setColumnCount(4);

        ArrayList<ColorItemView> colorItemList = new ArrayList<>();
        for (String colorStr : ColorItemView.COLOR_CODES) {
            ColorItemView tmp =  new ColorItemView(context);
            int colorCode = Color.parseColor(colorStr);
            tmp.setData(colorCode);

            if (this.databaseEntityData.getColorCode() == colorCode) {
                tmp.setSelectedState(true);
            }
            colorItemList.add(tmp);
        }

        this.colorPalette.setItem(colorItemList);

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
        this.colorPalette.setListener(new SelectableListCustomView.OnItemSelectedListener() {
            @Override
            public <T1> void onItemSelected(T1 itemView) {
                saveButton.setEnabled(checkInputData());
            }
        });
        this.nameEdit.setText(this.databaseEntityData.getName());
        this.saveButton.setEnabled(false);
    }

    @Override
    protected int getContainerContentLayoutId() {
        return R.layout.fragment_bop_category_edit;
    }

    @Override
    protected void onSaveClicked() {
        if (this.listener == null) return;

        String name = nameEdit.getText().toString();
        int colorInt = Color.parseColor("#000000");
        if (this.colorPalette.getSelectedItem().getData() != null) {
            colorInt = this.colorPalette.getSelectedItem().getData();
        }
        PurchaseCategory expensesCategory = new PurchaseCategory(
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
    private boolean checkInputData() {
        boolean isNameValid = !nameEdit.getText().toString().isEmpty();
        boolean isColorValid = colorPalette.getSelectedItem() != null;
        return isNameValid && isColorValid;
    }
}