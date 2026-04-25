package com.example.householdaccountbook.activities.settings.edit;

import com.example.householdaccountbook.fragments.edit.MoneyMovementEditFragment;
import com.example.householdaccountbook.myclasses.dbentity.MoneyMovement;

public class SettingEditMoneyMovementActivity extends SettingEditBaseActivity<MoneyMovement, MoneyMovementEditFragment> {
    @Override
    protected MoneyMovementEditFragment createFragment(MoneyMovement data) {
        return new MoneyMovementEditFragment(data);
    }

    @Override
    protected String getExtraKey() {
        return "MoneyMovement";
    }

    @Override
    protected Class<MoneyMovement> getEntityClass() {
        return MoneyMovement.class;
    }

    @Override
    protected String setTitleText() {
        return "振替データ編集";
    }
}
