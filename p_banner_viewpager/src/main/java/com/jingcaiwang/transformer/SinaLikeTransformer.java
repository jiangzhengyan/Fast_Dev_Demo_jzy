package com.jingcaiwang.transformer;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.jingcaiwang.b.MyViewPager;

/**
 * Created by jiang_yan on 2017/12/7.
 */

public class SinaLikeTransformer implements MyViewPager.PageTransformer {
    private static final String TAG = "SinaLikeTransformer";
    @Override
    public void transformPage(View view, float position) {
        Log.e(TAG, "transformPage:  " + "  position:"+position);
        float scale = 0.4f;
        float scaleValue = 1 - Math.abs(position) * scale;
        Log.e(TAG, "transformPage:scaleValue: "+scaleValue );
        view.setScaleX(scaleValue);
        view.setScaleY(scaleValue);
        view.setAlpha(scaleValue);
        view.setPivotX(view.getWidth() * (1 - position - (position > 0 ? 1 : -1) * 0.75f) * scale);
        view.setElevation(position > -0.25 && position < 0.25 ? 1 : 0);
    }
}