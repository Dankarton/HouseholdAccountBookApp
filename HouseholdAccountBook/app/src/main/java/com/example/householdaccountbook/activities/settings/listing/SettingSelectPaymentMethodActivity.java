package com.example.householdaccountbook.activities.settings.listing;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.activities.settings.SettingMotherActivity;
import com.example.householdaccountbook.activities.settings.edit.SettingEditPaymentMethodActivity;
import com.example.householdaccountbook.customviews.item.PaymentMethodItemView;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.fragments.listing.BaseListingFragment;
import com.example.householdaccountbook.fragments.listing.PaymentMethodListingFragment;

import java.util.ArrayList;

import myclasses.PaymentMethod;

public class SettingSelectPaymentMethodActivity extends SettingMotherActivity {
    @Override
    protected Fragment init() {
//        PaymentMethodListingFragment fragment = new PaymentMethodListingFragment();
//        fragment.setListener(this);
//        this.attachedFragment = fragment;
//        return fragment;

        return getPaymentMethodListingFragment();
    }

    @NonNull
    private BaseListingFragment<PaymentMethodItemView, PaymentMethod> getPaymentMethodListingFragment() {
        ArrayList<PaymentMethodItemView> methodViewList = new ArrayList<>();
        for (PaymentMethod data : MyDbManager.getAll(PaymentMethod.class)) {
            PaymentMethodItemView item = new PaymentMethodItemView(this);
            item.setData(data);
            item.setSelectedState(false);
            methodViewList.add(item);
        }
        BaseListingFragment<PaymentMethodItemView, PaymentMethod> fragment = new BaseListingFragment<>();
        fragment.setItems(methodViewList);
        fragment.setListener(new BaseListingFragment.OnInputActionListener() {
            @Override
            public <T2> void onItemClicked(T2 data) {
                startEditActivity((PaymentMethod) data);
            }

            @Override
            public void onNewCreateButtonClicked(int lastIndex) {
                startEditActivity(new PaymentMethod(
                        null,
                        "",
                        -1,
                        null,
                        -1,
                        null,
                        lastIndex,
                        false
                ));
            }
        });
        return fragment;
    }

    private void startEditActivity(PaymentMethod data) {
        Intent intent = new Intent(this, SettingEditPaymentMethodActivity.class);
        intent.putExtra("PaymentMethod", data);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.reloadFragment(getPaymentMethodListingFragment());
    }
}
