package com.example.householdaccountbook.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentPagerAdapter extends FragmentStateAdapter {
    private Fragment[] fragmentPages = {};
    private String[] pageTitle = {};
    public FragmentPagerAdapter(@NonNull Fragment motherFragment, Fragment[] fragments, String[] title) {

        super(motherFragment);
        this.fragmentPages = fragments;
        this.pageTitle = title;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return this.fragmentPages[position];
    }
    @Override
    public int getItemCount() {

        return this.fragmentPages.length;
    }

    public String getPageTitle(int position) {
        if (position < this.pageTitle.length) {
            return this.pageTitle[position];
        }
        else {
            return "-";
        }
    }
}
