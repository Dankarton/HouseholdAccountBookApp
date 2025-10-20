package com.example.householdaccountbook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    OnInputActionListener listener;
    Spinner closingRuleSpinner;
    Spinner paymentRuleSpinner;
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
        closingRuleSpinner = view.findViewById(R.id.closing_rule_spinner);
        paymentRuleSpinner = view.findViewById(R.id.payment_rule_spinner);
        closingRuleSettingNumEdit = view.findViewById(R.id.closing_rule_setting_num_edit);
        paymentRuleSettingNumEdit = view.findViewById(R.id.payment_rule_setting_num_edit);

        PaymentMethod.ClosingRule[] closingRules = PaymentMethod.ClosingRule.values();
        EnumSpinnerAdapter<PaymentMethod.ClosingRule> closingSpinnerAdapter = new EnumSpinnerAdapter<>(
                view.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                closingRules,
                new EnumSpinnerAdapter.LabelProvider<PaymentMethod.ClosingRule>() {
                    @Override
                    public String getLabel(PaymentMethod.ClosingRule item) {
                        return item.getNameText();
                    }
                }
        );
        PaymentMethod.PaymentRule[] paymentRules = PaymentMethod.PaymentRule.values();
        EnumSpinnerAdapter<PaymentMethod.PaymentRule> paymentSpinnerAdapter = new EnumSpinnerAdapter<>(
                view.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                paymentRules,
                new EnumSpinnerAdapter.LabelProvider<PaymentMethod.PaymentRule>() {
                    @Override
                    public String getLabel(PaymentMethod.PaymentRule item) {
                        return item.getText();
                    }
                }
        );
        // TODO スピナー作成中

    }

    public void setListener(OnInputActionListener listener) {
        this.listener = listener;
    }
}
