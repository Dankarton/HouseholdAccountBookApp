package com.example.householdaccountbook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.R;

import java.util.List;

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
        ){

        };

    }

    public void setListener(OnInputActionListener listener) {
        this.listener = listener;
    }
    private class EnumSpinnerAdapter<T extends Enum<T>> extends ArrayAdapter<T> {
        // TODO 内部クラスじゃなくて別でファイル宣言したほうがいいかも
        private final LabelProvider<T> labelProvider;
        public EnumSpinnerAdapter(@NonNull Context context, int resource, @NonNull T[] objects, @NonNull LabelProvider<T> labelProvider) {
            super(context, resource, objects);
            this.labelProvider = labelProvider;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView text = view.findViewById(android.R.id.text1);
            text.setText(labelProvider.getLabel(getItem(position)));
            return view;
        }
        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView text = view.findViewById(android.R.id.text1);
            text.setText(labelProvider.getLabel(getItem(position)));
            return view;
        }

        public interface LabelProvider<T> {
            String getLabel(T item);
        }
    }
}
