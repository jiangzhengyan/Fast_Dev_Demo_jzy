package com.examle.jiang_yan.fast_develop.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiang_yan on 2016/9/19.
 */
public class ActivityMange {


    private List<Activity> mLists = new ArrayList<>();//存放activity

    private ActivityMange() {

    }

    private static ActivityMange mActivityMange = null;

    /**
     * 线程安全的单例模式
     *
     * @return
     */
    public static ActivityMange getInstance() {
        if (mActivityMange == null) {
            synchronized (ActivityMange.class) {
                if (mActivityMange == null) {
                    mActivityMange = new ActivityMange();
                }
            }

        }
        return mActivityMange;
    }

    /**
     * 添加Activity到集合中
     *
     * @param mActivity
     */
    public void addActivity(Activity mActivity) {
        if (mActivity != null) {
            mLists.add(mActivity);
        }
    }

    /**
     * 移除activity
     * @param mActivity
     */
    public void removeActivity(Activity mActivity) {
        if (mActivity != null&&mLists.contains(mActivity)) {
            mLists.remove(mActivity);
            mActivity.finish();
        }
    }

    /**
     * 清空所有的activity
     */
    public void clearAllActivity() {
        if (mLists!=null&&mLists.size()>0) {
            for (int i=0;i<mLists.size();i++){
                mLists.get(i).finish();
            }
        }
    }



}
