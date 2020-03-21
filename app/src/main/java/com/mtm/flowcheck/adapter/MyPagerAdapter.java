package com.mtm.flowcheck.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private List<String> list;
    private List<Fragment> fragments;
    public MyPagerAdapter(FragmentManager fm, List<String> datas, ArrayList<Fragment> fragments) {
        super(fm);
        this.list=datas;
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}
