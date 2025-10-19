package com.example.householdaccountbook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.R;

import myclasses.OnInputActionListener;
import myclasses.PaymentMethod;

public class PaymentMethodEditFragment extends Fragment {
    OnInputActionListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_method_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PaymentMethod.ClosingRule[] methods = PaymentMethod.ClosingRule.values();
        ArrayAdapter<PaymentMethod.ClosingRule> adapter = new ArrayAdapter<>(
                view.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                methods
        );

    }

    public void setListener(OnInputActionListener listener) {
        this.listener = listener;
    }
}
