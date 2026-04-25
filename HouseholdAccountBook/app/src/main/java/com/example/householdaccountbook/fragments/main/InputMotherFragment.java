package com.example.householdaccountbook.fragments.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.householdaccountbook.adapter.FragmentPagerAdapter;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.fragments.edit.BaseEditFragment;
import com.example.householdaccountbook.fragments.edit.IncomeEditFragment;
import com.example.householdaccountbook.fragments.edit.MoneyMovementEditFragment;
import com.example.householdaccountbook.fragments.edit.PurchaseEditFragment;
import com.example.householdaccountbook.myclasses.dbentity.MoneyMovement;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.example.householdaccountbook.myclasses.dbentity.Income;
import com.example.householdaccountbook.myclasses.dbentity.Purchase;

public class InputMotherFragment extends Fragment {
    ViewPager2 viewPager;
    TabLayout tabLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_mother, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.input_fragment_view_pager);
        tabLayout = view.findViewById(R.id.expenses_or_income_tab);
        makeTabLayoutFragment();
    }
    private void makeTabLayoutFragment() {
        Purchase insPurchaseData = new Purchase();
        Income insIncomeData = new Income();
        MoneyMovement insMoneyMovementData = new MoneyMovement();

        var purchaseEditFragment = new PurchaseEditFragment(insPurchaseData);
        var incomeEditFragment = new IncomeEditFragment(insIncomeData);
        var movementEditFragment = new MoneyMovementEditFragment(insMoneyMovementData);
        purchaseEditFragment.setListener(new BaseEditFragment.OnInputActionListener<Purchase>() {
            @Override
            public void onSaveButtonClicked(Purchase data) {

                MyDbManager.setDataSafely(data);
                purchaseEditFragment.reset();
            }

            @Override
            public void onDeleteButtonClicked(Purchase data) {
                // 新規登録時は削除ボタンは使わない
            }
        });
        incomeEditFragment.setListener(new BaseEditFragment.OnInputActionListener<Income>() {
            @Override
            public void onSaveButtonClicked(Income data) {

                MyDbManager.setDataSafely(data);
                incomeEditFragment.reset();
            }

            @Override
            public void onDeleteButtonClicked(Income data) {
                // 新規登録時は削除ボタンは使わない
            }
        });
        movementEditFragment.setListener(new BaseEditFragment.OnInputActionListener<MoneyMovement>() {
            @Override
            public void onSaveButtonClicked(MoneyMovement data) {
                MyDbManager.setDataSafely(data);
                movementEditFragment.reset();
            }

            @Override
            public void onDeleteButtonClicked(MoneyMovement data) {
                // 新規登録時は削除ボタンは使わない
            }
        });
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(
                this,
                new Fragment[]{ purchaseEditFragment, incomeEditFragment, movementEditFragment },
                new String[] { "支出", "収入", "振替" }
        );
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(adapter.getPageTitle(position)))).attach();
        // 入力中に遷移するのがメンドいのでスワイプでの移動を禁止
        viewPager.setUserInputEnabled(false);
    }
}

