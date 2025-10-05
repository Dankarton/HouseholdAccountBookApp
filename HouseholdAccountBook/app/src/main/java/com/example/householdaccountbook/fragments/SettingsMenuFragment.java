package com.example.householdaccountbook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.customviews.SettingMenuButtonView;
import com.example.householdaccountbook.data.SettingMenuFragmentKind;

public class SettingsMenuFragment extends Fragment {
    LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        linearLayout = view.findViewById(R.id.expenses_list_linearlayout);

        SettingMenuButtonView paymentEdit = view.findViewById(R.id.payment_method_edit_menu_button);
        paymentEdit.setDestination(SettingMenuFragmentKind.PAYMENT_METHOD_EDIT);

        SettingMenuButtonView expCategoryEdit = view.findViewById(R.id.expenses_category_edit_menu_button);
        expCategoryEdit.setDestination(SettingMenuFragmentKind.EXPENSES_CATEGORY_EDIT);

        SettingMenuButtonView incCategoryEdit = view.findViewById(R.id.income_category_edit_menu_button);
        incCategoryEdit.setDestination(SettingMenuFragmentKind.INCOME_CATEGORY_EDIT);
    }
}