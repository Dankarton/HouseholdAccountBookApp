package com.example.householdaccountbook.fragments.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.myclasses.dbentity.DatabaseEntity;
import com.example.householdaccountbook.myclasses.dbentity.Wallet;

public class WalletEditFragment extends BaseEditFragment<Wallet> {
    EditText nameEdit;
    EditText initAmountEdit;

    public WalletEditFragment(@NonNull Wallet wallet) {
        super(wallet);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameEdit = view.findViewById(R.id.name_edit_text);
        initAmountEdit = view.findViewById(R.id.balance_amount_edit_text);
    }
    private boolean checkInputData() {
        return checkValidName() && checkValidInitAmount();
    }
    private boolean checkValidName() {
        return !nameEdit.getText().toString().isEmpty();
    }
    private boolean checkValidInitAmount() {
        if (initAmountEdit.isEnabled()) {
            String numText = initAmountEdit.getText().toString();
            if (MyStdlib.canConvertToInteger(numText)) {
                if (Integer.parseInt(numText) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected int getContainerContentLayoutId() {
        return R.layout.fragment_wallet_edit;
    }

    @Override
    protected void onSaveClicked() {
        String name = this.nameEdit.getText().toString();
        int initAmount = Integer.parseInt(this.initAmountEdit.getText().toString());
        Wallet wallet = new Wallet(
                this.databaseEntityData.getId(),
                name,
                initAmount,
                this.databaseEntityData.getDisplayIndex(),
                this.databaseEntityData.getIsDefault(),
                this.databaseEntityData.getIsDeleted()
        );
        this.listener.onSaveButtonClicked(wallet);
    }

    @Override
    protected void onDeleteClicked() {
        new AlertDialog.Builder(requireContext())
                .setTitle("確認")
                .setMessage("このデータを削除してもよろしいですか？")
                .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDeleteButtonClicked(databaseEntityData);
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
}
