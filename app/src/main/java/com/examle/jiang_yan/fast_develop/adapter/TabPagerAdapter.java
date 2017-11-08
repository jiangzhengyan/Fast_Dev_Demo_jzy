package com.examle.jiang_yan.fast_develop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by jiang_yan on 2016/9/30.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    private List<String> mTabTitle;//tab的标题
    private  List<Fragment> mFragment;//fragment页面
    public TabPagerAdapter(FragmentManager fm, List<String> mTabTitle, List<Fragment> mFragment) {
        super(fm);
        this.mTabTitle=mTabTitle;
        this.mFragment=mFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle.get(position);
    }
}
