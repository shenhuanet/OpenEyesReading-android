package com.shenhua.openeyesreading.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;
    private ViewPager mViewPager;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void add(Fragment fragment) {
        mFragments.add(fragment);
        notifyDataSetChanged();
        mViewPager.setCurrentItem(getCount() - 1, true);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setmViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }

}
