package com.example.householdaccountbook.fragments.edit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.adapter.EnumSpinnerAdapter;

import com.example.householdaccountbook.customviews.SelectableListCustomView;
import com.example.householdaccountbook.customviews.item.WalletItemView;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.myclasses.dbentity.PaymentMethod;
import com.example.householdaccountbook.myclasses.dbentity.Wallet;

import java.util.ArrayList;

public class PaymentMethodEditFragment extends BaseEditFragment<PaymentMethod> {
    EditText nameEdit;
    Spinner closingRuleSpinner;
    Spinner paymentRuleSpinner;
    TextView closingRuleSettingText;
    TextView paymentRuleSettingText;
    EditText closingRuleSettingNumEdit;
    EditText paymentRuleSettingNumEdit;
    SelectableListCustomView<WalletItemView, Wallet> walletListCV;

    public PaymentMethodEditFragment(@NonNull PaymentMethod paymentMethodData) {
        super(paymentMethodData);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Viewオブジェクト取得
        nameEdit = view.findViewById(R.id.name_edit_text);
        closingRuleSpinner = view.findViewById(R.id.closing_rule_spinner);
        paymentRuleSpinner = view.findViewById(R.id.payment_rule_spinner);
        closingRuleSettingText = view.findViewById(R.id.closing_rule_setting_text);
        paymentRuleSettingText = view.findViewById(R.id.payment_rule_setting_text);
        closingRuleSettingNumEdit = view.findViewById(R.id.closing_rule_setting_num_edit);
        paymentRuleSettingNumEdit = view.findViewById(R.id.payment_rule_setting_num_edit);
        walletListCV = view.findViewById(R.id.wallet_list_custom_view);
        saveButton = view.findViewById(R.id.save_button);
        deleteButton = view.findViewById(R.id.delete_button);
        Context context = view.getContext();
        // 締め日スピナー用Adapter作成
        EnumSpinnerAdapter<PaymentMethod.ClosingRule> closingSpinnerAdapter = new EnumSpinnerAdapter<>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                PaymentMethod.ClosingRule.values(),
                new EnumSpinnerAdapter.LabelProvider<PaymentMethod.ClosingRule>() {
                    @Override
                    public String getLabel(PaymentMethod.ClosingRule item) {
                        return item.getNameText();
                    }
                }
        );
        // 支払日スピナー用Adapter作成
        EnumSpinnerAdapter<PaymentMethod.PaymentRule> paymentSpinnerAdapter = new EnumSpinnerAdapter<>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                PaymentMethod.PaymentRule.values(),
                new EnumSpinnerAdapter.LabelProvider<PaymentMethod.PaymentRule>() {
                    @Override
                    public String getLabel(PaymentMethod.PaymentRule item) {
                        return item.getText();
                    }
                }
        );
        // Adapterセット
        closingRuleSpinner.setAdapter(closingSpinnerAdapter);
        paymentRuleSpinner.setAdapter(paymentSpinnerAdapter);
        // 締め日スピナーのアイテムが選ばれた時の追加処理を記述
        closingRuleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PaymentMethod.ClosingRule selectedRule = (PaymentMethod.ClosingRule) parent.getItemAtPosition(position);
                closingRuleSettingText.setText(selectedRule.getSettingNumText());
                closingRuleSettingNumEdit.setEnabled(selectedRule.usesSettingNum());
                saveButton.setEnabled(checkInputData());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* Do nothing. */ }
        });
        // 支払日スピナーのアイテムが選ばれた時の追加処理を記述
        paymentRuleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PaymentMethod.PaymentRule selectedRule = (PaymentMethod.PaymentRule) parent.getItemAtPosition(position);
                paymentRuleSettingText.setText(selectedRule.getSettingNumText());
                paymentRuleSettingNumEdit.setEnabled(selectedRule.usesSettingNum());
                saveButton.setEnabled(checkInputData());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* Do nothing. */ }
        });
        // ウォレットのセレクタブルリストにデータ入力
        ArrayList<WalletItemView> walletItemViews = new ArrayList<>();
        for (Wallet wallet : MyDbManager.getAllSafely(Wallet.class)) {
            var tmp = new WalletItemView(context);
            tmp.setData(wallet);
            if (wallet.getId() == this.databaseEntityData.getWalletId()) {
                tmp.setSelectedState(true);
            }
            walletItemViews.add(tmp);
        }
        this.walletListCV.setItem(walletItemViews);

        this.walletListCV.setListener(new SelectableListCustomView.OnItemSelectedListener() {
            @Override
            public <T1> void onItemSelected(T1 itemView) {
                saveButton.setEnabled(checkInputData());
            }
        });
        // Editが変更された時などに実行される処理を記述するWatcher
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { /* Do nothing */ }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { /* Do nothing */ }

            @Override
            public void afterTextChanged(Editable editable) {
                saveButton.setEnabled(checkInputData());
            }
        };
        // Editが変更された時に追加処理を行えるようにwatcherをセット
        nameEdit.addTextChangedListener(watcher);
        closingRuleSettingNumEdit.addTextChangedListener(watcher);
        paymentRuleSettingNumEdit.addTextChangedListener(watcher);
        // 初期値設定
        if (this.databaseEntityData.getName() != null) {
            this.nameEdit.setText(this.databaseEntityData.getName());
        } else {
            this.nameEdit.setText("");
        }
        this.closingRuleSpinner.setSelection(this.databaseEntityData.getClosingRule().ordinal());
        this.paymentRuleSpinner.setSelection(this.databaseEntityData.getPaymentRule().ordinal());
        if (this.databaseEntityData.getClosingSettingNum() != null) {
            this.closingRuleSettingNumEdit.setText(String.valueOf(this.databaseEntityData.getClosingSettingNum()));
        } else {
            this.closingRuleSettingNumEdit.setText("");
        }
        if (this.databaseEntityData.getPaymentSettingNum() != null) {
            this.paymentRuleSettingNumEdit.setText(String.valueOf(this.databaseEntityData.getPaymentSettingNum()));
        } else {
            this.paymentRuleSettingNumEdit.setText("");
        }
        // データがデフォルト値だった場合は削除ボタンを無効化
        if (this.databaseEntityData.isDefault()) {
            this.deleteButton.setEnabled(false);
        }
    }

    private void notifyListenerCompleted(PaymentMethod enteredData) {
        this.listener.onSaveButtonClicked(enteredData);
    }

    /**
     * ユーザーが入力したデータが正しく揃っているか確認する関数
     *
     * @return すべてそろっているtrue，一つでも適合しないとfalse
     */
    private boolean checkInputData() {
        // 支払い方法名が入力されているか検証
        boolean isNameValid = !nameEdit.getText().toString().isEmpty();
        // 締め日設定値が正しく入力されているか検証
        boolean isClosingSettingNumValid = false;
        if (closingRuleSettingNumEdit.isEnabled()) {
            String numText = closingRuleSettingNumEdit.getText().toString();
            if (MyStdlib.canConvertToInteger(numText)) {
                if (Integer.parseInt(numText) > 0) {
//                    Log.d("PaymentMethodEditFragment", "closingRuleSetting: true 正常");
                    isClosingSettingNumValid = true;
                } else {
//                    Log.d("PaymentMethodEditFragment", "closingRuleSetting: ありえない数値");
                }
            } else {
//                Log.d("PaymentMethodEditFragment", "closingRuleSetting: 入力が数値じゃない");
            }
        } else {  // EditのEnabledがFalseの時は，settingNumの入力がそもそも必要ない項目
            isClosingSettingNumValid = true;
        }
        // 支払日設定値が正しく入力されているか検証
        boolean isPaymentSettingNumValid = false;
        if (paymentRuleSettingNumEdit.isEnabled()) {
            String numText = paymentRuleSettingNumEdit.getText().toString();
            if (MyStdlib.canConvertToInteger(numText)) {
                if (Integer.parseInt(numText) > 0) {
//                    Log.d("PaymentMethodEditFragment", "paymentRuleSetting: true 正常");
                    isPaymentSettingNumValid = true;
                } else {
//                    Log.d("PaymentMethodEditFragment", "paymentRuleSetting: ありえない数値");
                }
            } else {
//                Log.d("PaymentMethodEditFragment", "paymentRuleSetting: 入力が数値じゃない");
            }
        } else {
//            Log.d("PaymentMethodEditFragment", "paymentRuleSetting: true 設置値は使わない");
            isPaymentSettingNumValid = true;
        }
        boolean isWalletValid = this.walletListCV.getSelectedItem() != null;
        return isNameValid && isClosingSettingNumValid && isPaymentSettingNumValid && isWalletValid;
    }

    @Override
    protected int getContainerContentLayoutId() {
        return R.layout.fragment_payment_method_edit;
    }

    @Override
    protected void onSaveClicked() {
        String name = nameEdit.getText().toString();
        PaymentMethod.ClosingRule closingRule = (PaymentMethod.ClosingRule) closingRuleSpinner.getSelectedItem();
        PaymentMethod.PaymentRule paymentRule = (PaymentMethod.PaymentRule) paymentRuleSpinner.getSelectedItem();
        int closingRuleCode = closingRule.getCode();
        Integer closingSettingNum;
        if (closingRule.usesSettingNum()) {
            closingSettingNum = Integer.parseInt(closingRuleSettingNumEdit.getText().toString());
        } else {
            closingSettingNum = null;
        }
        int paymentRuleCode = paymentRule.getCode();
        Integer paymentSettingNum;
        if (paymentRule.usesSettingNum()) {
            paymentSettingNum = Integer.parseInt(paymentRuleSettingNumEdit.getText().toString());
        } else {
            paymentSettingNum = null;
        }
        PaymentMethod paymentMethod = new PaymentMethod(
                this.databaseEntityData.getId(),
                name, closingRuleCode, closingSettingNum,
                paymentRuleCode, paymentSettingNum,
                this.walletListCV.getSelectedItem().getData().getId(),
                this.databaseEntityData.getIndex(),
                false
        );
        Log.d("PaymentMethodEditFragment",
                "ID:" + paymentMethod.getId() +
                        ", Name:" + paymentMethod.getName() +
                "Wallet ID:" + paymentMethod.getWalletId());
        this.listener.onSaveButtonClicked(paymentMethod);
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
