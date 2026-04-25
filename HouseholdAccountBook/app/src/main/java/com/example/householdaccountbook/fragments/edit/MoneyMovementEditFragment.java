package com.example.householdaccountbook.fragments.edit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.SelectableListCustomView;
import com.example.householdaccountbook.customviews.item.WalletItemView;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.myclasses.dbentity.MoneyMovement;
import com.example.householdaccountbook.myclasses.dbentity.Wallet;

import java.util.ArrayList;
import java.util.Calendar;

public class MoneyMovementEditFragment extends BaseEditFragment<MoneyMovement> {
    TextView dateTextView;
    Calendar currentDate;
    EditText amountEditText;
    EditText memoEditText;
    SelectableListCustomView<WalletItemView, Wallet> fromWalletList;
    SelectableListCustomView<WalletItemView, Wallet> toWalletList;


    public MoneyMovementEditFragment(@NonNull MoneyMovement data) { super(data); }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = requireContext();
        //
        // Viewオブジェクトの登録
        //
        this.dateTextView = view.findViewById(R.id.date_text);
        this.memoEditText = view.findViewById(R.id.memo_edit_text);
        this.amountEditText = view.findViewById(R.id.amount_edit_text);
        this.fromWalletList = view.findViewById(R.id.from_wallet_list_custom_view);
        this.toWalletList = view.findViewById(R.id.to_wallet_list_custom_view);

        this.fromWalletList.setColumnCount(2);
        this.toWalletList.setColumnCount(2);

        ArrayList<Wallet> walletList = MyDbManager.getAllSafely(Wallet.class);
        ArrayList<WalletItemView> fromWalletItemViews = new ArrayList<>();
        ArrayList<WalletItemView> toWalletItemViews = new ArrayList<>();
        for (Wallet wallet : walletList) {
            WalletItemView fromTmp = new WalletItemView(context);
            WalletItemView toTmp = new WalletItemView(context);
            fromTmp.setData(wallet);
            toTmp.setData(wallet);
            if (wallet.getId() == this.databaseEntityData.getFromWalletId()) {
                fromTmp.setSelectedState(true);
            }
            if (wallet.getId() == this.databaseEntityData.getToWalletId()) {
                toTmp.setSelectedState(true);
            }
            fromWalletItemViews.add(fromTmp);
            toWalletItemViews.add(toTmp);
        }
        this.fromWalletList.setItem(fromWalletItemViews);
        this.toWalletList.setItem(toWalletItemViews);
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
        // From,toウォレットのアイテムリストの選択アイテムが変更された時のイベント
        var itemListListener = new SelectableListCustomView.OnItemSelectedListener() {
            @Override
            public <T1> void onItemSelected(T1 itemView) {
                saveButton.setEnabled(checkInputData());
            }
        };
        this.fromWalletList.setListener(itemListListener);
        this.toWalletList.setListener(itemListListener);
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
    @Override
    protected int getContainerContentLayoutId() {
        return R.layout.fragment_money_movement_edit;
    }

    @Override
    protected void onSaveClicked() {
        int amount = Integer.parseInt(this.amountEditText.getText().toString());
        MoneyMovement newMovements = new MoneyMovement(
                this.databaseEntityData.getId(),
                this.currentDate,
                amount,
                this.memoEditText.getText().toString(),
                this.fromWalletList.getSelectedItem().getData().getId(),
                this.toWalletList.getSelectedItem().getData().getId()
        );
        if (this.listener != null) this.listener.onSaveButtonClicked(newMovements);
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
        boolean isAmountValid = MyStdlib.canConvertToInteger(this.amountEditText.getText().toString());
        boolean isFromWalletValid = this.fromWalletList.getSelectedItem() != null;
        boolean isToWalletValid = this.toWalletList.getSelectedItem() != null;
        return isAmountValid && isFromWalletValid && isToWalletValid;
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
        this.amountEditText.setText("");
        this.memoEditText.setText("");
        this.fromWalletList.deselect();
        this.toWalletList.deselect();
    }
}
