package com.example.householdaccountbook.activities.settings.listing;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.activities.settings.SettingMotherActivity;
import com.example.householdaccountbook.activities.settings.edit.SettingEditWalletActivity;
import com.example.householdaccountbook.customviews.item.WalletItemView;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.fragments.listing.BaseListingFragment;
import com.example.householdaccountbook.myclasses.dbentity.Wallet;

import java.util.ArrayList;

public class SettingSelectWalletActivity extends SettingMotherActivity {
    @Override
    protected Fragment init() {
        ArrayList<WalletItemView> itemViewList = new ArrayList<>();
        for (Wallet data : MyDbManager.getAllSafely(Wallet.class)) {
            WalletItemView item = new WalletItemView(this);
            item.setData(data);
            item.setSelectedState(false);
            itemViewList.add(item);
        }
        BaseListingFragment<WalletItemView, Wallet> fragment = new BaseListingFragment<>();
        fragment.setItems(itemViewList);
        fragment.setListener(new BaseListingFragment.OnInputActionListener() {
            @Override
            public <T2> void onItemClicked(T2 data) {
                startActivity((Wallet) data);
            }

            @Override
            public void onNewCreateButtonClicked(int lastIndex) {
                startActivity(new Wallet(
                        null,
                        "",
                        0,
                        lastIndex,
                        false,
                        false
                ));
            }
        });
        return fragment;
    }

    @Override
    protected String setTitleText() {
        return "ウォレット";
    }
    private void startActivity(Wallet data) {
        Intent intent = new Intent(this, SettingEditWalletActivity.class);
        intent.putExtra("Wallet", data);
        startActivity(intent);
    }
}
