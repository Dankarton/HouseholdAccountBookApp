package com.example.householdaccountbook.activities.settings.listing;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.householdaccountbook.activities.settings.SettingMotherActivity;
import com.example.householdaccountbook.activities.settings.edit.SettingEditPurchaseCategoryActivity;
import com.example.householdaccountbook.customviews.item.CategoryItemView;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.fragments.listing.BaseListingFragment;

import java.util.ArrayList;

import myclasses.PurchaseCategory;

public class SettingSelectPurchaseCategoryActivity extends SettingMotherActivity {
    @Override
    protected Fragment init() {
        // カテゴリーリスト作成
        ArrayList<CategoryItemView<PurchaseCategory>> categoryViewList = new ArrayList<>();
        for (PurchaseCategory data : MyDbManager.getAllSafely(PurchaseCategory.class)) {
            CategoryItemView<PurchaseCategory> item = new CategoryItemView<>(this);
            item.setData(data);
            item.setSelectedState(false);
            categoryViewList.add(item);
        }
        BaseListingFragment<CategoryItemView<PurchaseCategory>, PurchaseCategory> fragment = new BaseListingFragment<>();
        fragment.setItems(categoryViewList);
        fragment.setListener(new BaseListingFragment.OnInputActionListener() {
            @Override
            public <T2> void onItemClicked(T2 data) {
                startEditActivity((PurchaseCategory) data);
            }

            @Override
            public void onNewCreateButtonClicked(int lastIndex) {
                startEditActivity(new PurchaseCategory(
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
        return "支出カテゴリ";
    }

    private void startEditActivity(PurchaseCategory data) {
        Intent intent = new Intent(this, SettingEditPurchaseCategoryActivity.class);
        intent.putExtra("PurchaseCategory", data);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.reloadFragment(init());
    }
}
