package com.lidchanin.shoppinglist.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Alexander Destroyed on 12.10.2017.
 */

public class ThemePagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public ThemePagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getArguments().getString("List");
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
