package com.example.householdaccountbook.fragments.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.householdaccountbook.R;
import com.example.householdaccountbook.activities.settings.listing.SettingSelectIncomeCategoryActivity;
import com.example.householdaccountbook.activities.settings.listing.SettingSelectPaymentMethodActivity;
import com.example.householdaccountbook.activities.settings.listing.SettingSelectPurchaseCategoryActivity;
import com.example.householdaccountbook.activities.settings.listing.SettingSelectWalletActivity;
import com.example.householdaccountbook.customviews.SettingMenuButtonView;
import com.example.householdaccountbook.data.SettingMenuFragmentKind;

public class SettingsMenuFragment extends Fragment implements SettingMenuButtonView.OnClickListener {

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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = view.findViewById(R.id.expenses_list_linearlayout);

        SettingMenuButtonView walletEdit = view.findViewById(R.id.wallet_edit_menu_button);
        walletEdit.setDestination(SettingMenuFragmentKind.WALLET_EDIT);
        walletEdit.setListener(this);

        SettingMenuButtonView paymentEdit = view.findViewById(R.id.payment_method_edit_menu_button);
        paymentEdit.setDestination(SettingMenuFragmentKind.PAYMENT_METHOD_EDIT);
        paymentEdit.setListener(this);

        SettingMenuButtonView expCategoryEdit = view.findViewById(R.id.expenses_category_edit_menu_button);
        expCategoryEdit.setDestination(SettingMenuFragmentKind.EXPENSES_CATEGORY_EDIT);
        expCategoryEdit.setListener(this);

        SettingMenuButtonView incCategoryEdit = view.findViewById(R.id.income_category_edit_menu_button);
        incCategoryEdit.setDestination(SettingMenuFragmentKind.INCOME_CATEGORY_EDIT);
        incCategoryEdit.setListener(this);
    }

    @Override
    public void onClicked(SettingMenuFragmentKind type) {
        Context context = requireContext();
        switch (type) {
            case WALLET_EDIT:
                context.startActivity(new Intent(context, SettingSelectWalletActivity.class));
                break;
            case PAYMENT_METHOD_EDIT:
                context.startActivity(new Intent(context, SettingSelectPaymentMethodActivity.class));
                break;
            case EXPENSES_CATEGORY_EDIT:
                context.startActivity(new Intent(context, SettingSelectPurchaseCategoryActivity.class));
                break;
            case INCOME_CATEGORY_EDIT:
                context.startActivity(new Intent(context, SettingSelectIncomeCategoryActivity.class));
                break;
            default:
        }
    }
}