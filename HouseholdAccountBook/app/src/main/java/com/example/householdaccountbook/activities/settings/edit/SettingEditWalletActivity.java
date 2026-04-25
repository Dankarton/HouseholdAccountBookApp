package com.example.householdaccountbook.activities.settings.edit;

import com.example.householdaccountbook.fragments.edit.WalletEditFragment;
import com.example.householdaccountbook.myclasses.dbentity.Wallet;

public class SettingEditWalletActivity extends SettingEditBaseActivity<Wallet, WalletEditFragment> {

    @Override
    protected WalletEditFragment createFragment(Wallet data) {
        return new WalletEditFragment(data);
    }

    @Override
    protected String getExtraKey() {
        return "Wallet";
    }

    @Override
    protected Class<Wallet> getEntityClass() {
        return Wallet.class;
    }

    @Override
    protected String setTitleText() {
        return "ウォレット追加・編集";
    }
}
