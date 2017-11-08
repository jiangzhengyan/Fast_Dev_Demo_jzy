package com.examle.jiang_yan.fast_develop.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;
import com.examle.jiang_yan.fast_develop.adapter.TabPagerAdapter;
import com.examle.jiang_yan.fast_develop.fragment.TabFragment_1;
import com.examle.jiang_yan.fast_develop.fragment.TabFragment_2;
import com.examle.jiang_yan.fast_develop.fragment.TabFragment_3;
import com.examle.jiang_yan.fast_develop.fragment.TabFragment_4;
import com.examle.jiang_yan.fast_develop.fragment.TabFragment_5;
import com.examle.jiang_yan.fast_develop.fragment.TabFragment_6;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiang_yan on 2016/9/19.
 */
public class ActivityTabLayout extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tab_title)
    TabLayout tabTitle;
    @Bind(R.id.vp_find_fragment_pager)
    ViewPager vpFindFragmentPager;
private ArrayList<String> mTabTitle=new ArrayList<>();
private ArrayList<Fragment> mTabFragment=new ArrayList<>();

    @Override
    public void initView() {
        setContentView(R.layout.layout_activity_tab);
        ButterKnife.bind(this);
        for (int i=0;i<6;i++){
            mTabTitle.add(getResources().getStringArray(R.array.tab_title)[i]);
        }
        //初始化Fragment
        TabFragment_1 tabFragment_1 = new TabFragment_1();
        TabFragment_2 tabFragment_2 = new TabFragment_2();
        TabFragment_3 tabFragment_3 = new TabFragment_3();
        TabFragment_4 tabFragment_4 = new TabFragment_4();
        TabFragment_5 tabFragment_5 = new TabFragment_5();
        TabFragment_6 tabFragment_6 = new TabFragment_6();
        mTabFragment.add(tabFragment_1);
        mTabFragment.add(tabFragment_2);
        mTabFragment.add(tabFragment_3);
        mTabFragment.add(tabFragment_4);
        mTabFragment.add(tabFragment_5);
        mTabFragment.add(tabFragment_6);
    }

    @Override
    public void initData() {
        TabPagerAdapter tabPagerAdapter =
                new TabPagerAdapter(getSupportFragmentManager(), mTabTitle, mTabFragment);
        vpFindFragmentPager.setAdapter(tabPagerAdapter);
        tabTitle.setTabGravity(TabLayout.GRAVITY_CENTER);

        //关联Tab和ViewPager
        tabTitle.setupWithViewPager(vpFindFragmentPager);
    }

    @Override
    public void eventlistener() {

    }


}
