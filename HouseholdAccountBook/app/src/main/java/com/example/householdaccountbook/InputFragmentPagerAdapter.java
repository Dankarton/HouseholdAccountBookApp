package com.example.householdaccountbook;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.householdaccountbook.fragments.ExpensesInputFragment;
import com.example.householdaccountbook.fragments.IncomeInputFragment;

public class InputFragmentPagerAdapter extends FragmentStateAdapter {
    private Fragment fragmentPage1 = null;
    private Fragment fragmentPage2 = null;
    public InputFragmentPagerAdapter(@NonNull Fragment fragment, Fragment f1, Fragment f2) {

        super(fragment);
        setFragments(f1, f2);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
//        if (this.fragmentPage1 == null || this.fragmentPage2 == null) {
//
//        }
        if (position == 0){
            return this.fragmentPage1;
        }
        else{
            return this.fragmentPage2;
        }
    }
    @Override
    public int getItemCount() {
        return 2;
    }

    private void setFragments(Fragment f1, Fragment f2) {
        this.fragmentPage1 = f1;
        this.fragmentPage2 = f2;
    }
}
