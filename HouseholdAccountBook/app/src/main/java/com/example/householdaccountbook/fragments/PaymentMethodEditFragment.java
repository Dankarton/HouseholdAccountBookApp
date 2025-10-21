package com.example.householdaccountbook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.adapter.EnumSpinnerAdapter;

import java.util.List;

import myclasses.OnInputActionListener;
import myclasses.PaymentMethod;

public class PaymentMethodEditFragment extends Fragment {
    OnInputActionListener<PaymentMethod> listener = null;
    EditText nameEdit;
    Spinner closingRuleSpinner;
    Spinner paymentRuleSpinner;
    TextView closingRuleSettingText;
    TextView paymentRuleSettingText;
    EditText closingRuleSettingNumEdit;
    EditText paymentRuleSettingNumEdit;

    PaymentMethod paymentMethod = null;

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
        // Viewオブジェクト取得
        nameEdit = view.findViewById(R.id.name_edit_text);
        closingRuleSpinner = view.findViewById(R.id.closing_rule_spinner);
        paymentRuleSpinner = view.findViewById(R.id.payment_rule_spinner);
        closingRuleSettingText = view.findViewById(R.id.closing_rule_setting_text);
        paymentRuleSettingText = view.findViewById(R.id.payment_rule_setting_text);
        closingRuleSettingNumEdit = view.findViewById(R.id.closing_rule_setting_num_edit);
        paymentRuleSettingNumEdit = view.findViewById(R.id.payment_rule_setting_num_edit);
        // 締め日スピナー用Adapter作成
        EnumSpinnerAdapter<PaymentMethod.ClosingRule> closingSpinnerAdapter = new EnumSpinnerAdapter<>(
                view.getContext(),
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
                view.getContext(),
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* Do nothing. */ }
        });
        // 保存ボタンが押された時の処理を記述
        // TODO 10/21 ユーザーの入力内容を確認して保存ボタンの状態を変更する処理が欲しい．型が合わないような入力だったら保存ボタンが押せないようにしたい．
        view.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyListenerCompleted(createPaymentMethodFromInput());
                view.setEnabled(false);
            }
        });
    }
    private void notifyListenerCompleted(PaymentMethod enteredData) {
        this.listener.onCompleted(enteredData);
    }
    private PaymentMethod createPaymentMethodFromInput() {
        Integer id;
        if (this.paymentMethod != null) {
            id = this.paymentMethod.getId();
        }
        else {
            id = null;
        }
        String name = nameEdit.getText().toString();
        PaymentMethod.ClosingRule closingRule = (PaymentMethod.ClosingRule) closingRuleSpinner.getSelectedItem();
        PaymentMethod.PaymentRule paymentRule = (PaymentMethod.PaymentRule) paymentRuleSpinner.getSelectedItem();
        int closingRuleCode = closingRule.getCode();
        Integer closingSettingNum;
        if (closingRule.usesSettingNum()) {
            closingSettingNum = Integer.parseInt(closingRuleSettingNumEdit.getText().toString());
        }
        else {
            closingSettingNum = null;
        }
        int paymentRuleCode = paymentRule.getCode();
        Integer paymentSettingNum;
        if (paymentRule.usesSettingNum()) {
            paymentSettingNum = Integer.parseInt(paymentRuleSettingNumEdit.getText().toString());
        }
        else {
            paymentSettingNum = null;
        }
        return new PaymentMethod(id, name, closingRuleCode, closingSettingNum, paymentRuleCode, paymentSettingNum, false);
    }
    public void setListener(OnInputActionListener<PaymentMethod> listener) {
        this.listener = listener;
    }
}
