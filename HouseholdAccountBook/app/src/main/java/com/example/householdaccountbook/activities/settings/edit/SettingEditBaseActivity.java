package com.example.householdaccountbook.activities.settings.edit;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.activities.settings.SettingMotherActivity;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.fragments.edit.BaseEditFragment;

import com.example.householdaccountbook.myclasses.dbentity.DatabaseEntity;

public abstract class SettingEditBaseActivity<T extends DatabaseEntity, F extends BaseEditFragment<T>> extends SettingMotherActivity {
    // DatabaseEntityインターフェースはSerializableを継承しているので，TにSerializableが継承されていることを明示しなくてもいい
    protected abstract F createFragment(T data);
    protected abstract String getExtraKey();
    protected abstract Class<T> getEntityClass();
    @Override
    protected Fragment init() {
        T data = getIntent().getSerializableExtra(getExtraKey(), getEntityClass());
        F fragment = createFragment(data);
        fragment.setListener(new BaseEditFragment.OnInputActionListener<T>() {
            @Override
            public void onSaveButtonClicked(T data) {
                Log.d("SettingEditBaseActivity", "save button clicked");
                // IDが無い場合(新規追加の場合)
                if (data.getId() == null) {
                    Log.d("SettingEditBaseActivity", "set data");
                    MyDbManager.setDataSafely(data);
                }
                // 編集の場合
                else {
                    Log.d("SettingEditBaseActivity", "upsert");
                    MyDbManager.upsertDatabaseSafely(data);
                }
                finish();
            }

            @Override
            public void onDeleteButtonClicked(T data) {
                if (data.getId() != null) {
                    MyDbManager.deleteDataSafely(data);
                }
                finish();
            }
        });
        return fragment;
    }
}
