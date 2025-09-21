package com.example.householdaccountbook;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.householdaccountbook.fragments.ExpensesInputFragment;
import com.example.householdaccountbook.fragments.IncomeInputFragment;

public class InputFragmentPagerAdapter extends FragmentStateAdapter {

    public InputFragmentPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new IncomeInputFragment();
        }
        else{
            return new ExpensesInputFragment();
        }
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}
