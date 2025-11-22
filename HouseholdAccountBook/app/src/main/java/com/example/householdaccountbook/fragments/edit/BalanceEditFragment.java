package com.example.householdaccountbook.fragments.edit;

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

public class BalanceEditFragment extends Fragment {
    public interface OnInputActionListener {
        void onSaveButtonClicked(int amount);
    }
    private OnInputActionListener listener = null;
    private Button saveButton;
    private EditText amountEditText;
    private Integer amount = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance_edit, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.saveButton = view.findViewById(R.id.save_button);
        this.amountEditText = view.findViewById(R.id.balance_amount_edit_text);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });
        this.amountEditText.setText(String.valueOf(this.amount));
    }
    public void setListener(OnInputActionListener listener) { this.listener = listener; }

    public void setItems(int amount) {
        this.amount = amount;

    }

    private void onSave() {
        if (this.listener != null) {
            String amountStr = this.amountEditText.getText().toString();
            if (MyStdlib.canConvertToInteger(amountStr)) {
                this.listener.onSaveButtonClicked(Integer.parseInt(amountStr));
            }
        }
    }
}
