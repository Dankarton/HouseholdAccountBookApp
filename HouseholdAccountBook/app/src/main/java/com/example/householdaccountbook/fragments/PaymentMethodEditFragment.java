package com.example.householdaccountbook.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.MyStdlib;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.adapter.EnumSpinnerAdapter;

import myclasses.PaymentMethod;

public class PaymentMethodEditFragment extends Fragment {
    OnInputActionListener listener = null;
    EditText nameEdit;
    Spinner closingRuleSpinner;
    Spinner paymentRuleSpinner;
    TextView closingRuleSettingText;
    TextView paymentRuleSettingText;
    EditText closingRuleSettingNumEdit;
    EditText paymentRuleSettingNumEdit;
    Button saveButton;
    Button deleteButton;

    PaymentMethod paymentMethod = null;

    public interface OnInputActionListener {
        public void onSaveButtonClicked(PaymentMethod data);
        public void onDeleteButtonClicked(PaymentMethod data);
    }


    public PaymentMethodEditFragment() { /*Do nothing*/ }

    public PaymentMethodEditFragment(PaymentMethod paymentMethodData) {
        this.paymentMethod = paymentMethodData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_method_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();
        // Viewオブジェクト取得
        nameEdit = view.findViewById(R.id.name_edit_text);
        closingRuleSpinner = view.findViewById(R.id.closing_rule_spinner);
        paymentRuleSpinner = view.findViewById(R.id.payment_rule_spinner);
        closingRuleSettingText = view.findViewById(R.id.closing_rule_setting_text);
        paymentRuleSettingText = view.findViewById(R.id.payment_rule_setting_text);
        closingRuleSettingNumEdit = view.findViewById(R.id.closing_rule_setting_num_edit);
        paymentRuleSettingNumEdit = view.findViewById(R.id.payment_rule_setting_num_edit);
        saveButton = view.findViewById(R.id.save_button);
        deleteButton = view.findViewById(R.id.delete_button);
        // 締め日スピナー用Adapter作成
        EnumSpinnerAdapter<PaymentMethod.ClosingRule> closingSpinnerAdapter = new EnumSpinnerAdapter<>(
                context,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
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
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
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
                checkInputData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* Do nothing. */ }
        });
        // Editが変更された時などに実行される処理を記述するWatcher
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { /* Do nothing */ }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { /* Do nothing */ }

            @Override
            public void afterTextChanged(Editable editable) {
                checkInputData();
            }
        };
        // Editが変更された時に追加処理を行えるようにwatcherをセット
        nameEdit.addTextChangedListener(watcher);
        closingRuleSettingNumEdit.addTextChangedListener(watcher);
        paymentRuleSettingNumEdit.addTextChangedListener(watcher);
        // 保存ボタンが押された時の処理を記述
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyListenerCompleted(createPaymentMethodFromInput());
                view.setEnabled(false);
            }
        });
        // 削除ボタンが押された時の処理を記述
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("確認")
                        .setMessage("このデータを削除してもよろしいですか？")
                        .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listener.onDeleteButtonClicked(paymentMethod);
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
        });
        // ボタンの状態を変更
        this.saveButton.setEnabled(false);
        if (this.paymentMethod.getId() == null){
            this.deleteButton.setEnabled(false);
            this.deleteButton.setVisibility(View.GONE);
        }
        // 初期値設定
        if (this.paymentMethod.getName() != null) {
            this.nameEdit.setText(this.paymentMethod.getName());
        }
        else {
            this.nameEdit.setText("");
        }
        this.closingRuleSpinner.setSelection(this.paymentMethod.getClosingRule().ordinal());
        this.paymentRuleSpinner.setSelection(this.paymentMethod.getPaymentRule().ordinal());
        if (this.paymentMethod.getClosingDay() != null) {
            this.closingRuleSettingNumEdit.setText(String.valueOf(this.paymentMethod.getClosingDay()));
        }
        else {
            this.closingRuleSettingNumEdit.setText("");
        }
        if (this.paymentMethod.getPaymentDay() != null) {
            this.paymentRuleSettingNumEdit.setText(String.valueOf(this.paymentMethod.getPaymentDay()));
        }
        else {
            this.paymentRuleSettingNumEdit.setText("");
        }
    }

    private void notifyListenerCompleted(PaymentMethod enteredData) {
        this.listener.onSaveButtonClicked(enteredData);
    }

    private PaymentMethod createPaymentMethodFromInput() {
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
        return new PaymentMethod(this.paymentMethod.getId(), name, closingRuleCode, closingSettingNum, paymentRuleCode, paymentSettingNum, this.paymentMethod.getIndex(), false);
    }

    private void checkInputData() {
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
                }
                else {
//                    Log.d("PaymentMethodEditFragment", "closingRuleSetting: ありえない数値");
                }
            }
            else {
//                Log.d("PaymentMethodEditFragment", "closingRuleSetting: 入力が数値じゃない");
            }
        } else {
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
                }
                else {
//                    Log.d("PaymentMethodEditFragment", "paymentRuleSetting: ありえない数値");
                }
            }
            else {
//                Log.d("PaymentMethodEditFragment", "paymentRuleSetting: 入力が数値じゃない");
            }
        } else {
//            Log.d("PaymentMethodEditFragment", "paymentRuleSetting: true 設置値は使わない");
            isPaymentSettingNumValid = true;
        }

        saveButton.setEnabled(isNameValid && isClosingSettingNumValid && isPaymentSettingNumValid);
    }

    public void setListener(OnInputActionListener listener) {
        this.listener = listener;
    }
}
