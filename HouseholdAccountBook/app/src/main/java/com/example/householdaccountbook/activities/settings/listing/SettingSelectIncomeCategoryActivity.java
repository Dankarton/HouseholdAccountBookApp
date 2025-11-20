package com.example.householdaccountbook.activities.settings.listing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.activities.settings.SettingMotherActivity;
import com.example.householdaccountbook.activities.settings.edit.SettingEditIncomeCategoryActivity;
import com.example.householdaccountbook.customviews.item.CategoryItemView;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.fragments.listing.BaseListingFragment;

import java.util.ArrayList;

import myclasses.BopCategory;
import myclasses.IncomeCategory;

public class SettingSelectIncomeCategoryActivity extends SettingMotherActivity {
    @Override
    protected Fragment init() {
        ArrayList<CategoryItemView<IncomeCategory>> itemViewList = new ArrayList<>();
        for (IncomeCategory data : MyDbManager.getAllSafely(IncomeCategory.class)) {
            CategoryItemView<IncomeCategory> item = new CategoryItemView<>(this);
            item.setData(data);
            item.setSelectedState(false);
            itemViewList.add(item);

        }
        BaseListingFragment<CategoryItemView<IncomeCategory>, IncomeCategory> fragment = new BaseListingFragment<>();
        fragment.setItems(itemViewList);
        fragment.setListener(new BaseListingFragment.OnInputActionListener() {
            @Override
            public <T2> void onItemClicked(T2 data) {
                startEditActivity((IncomeCategory) data);
            }

            @Override
            public void onNewCreateButtonClicked(int lastIndex) {
                startEditActivity(new IncomeCategory(
                        null,
                        "",
                        -1,
                        lastIndex,
                        false
                ));
            }
        });
        return fragment;
    }
    @Override
    protected String setTitleText() {
        return "収入カテゴリ";
    }
    private void startEditActivity(IncomeCategory data) {
        Intent intent = new Intent(this, SettingEditIncomeCategoryActivity.class);
        intent.putExtra("IncomeCategory", data);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.reloadFragment(init());
    }
}
