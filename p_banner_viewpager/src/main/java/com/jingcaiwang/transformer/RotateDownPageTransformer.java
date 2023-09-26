package com.jingcaiwang.transformer;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * Created by wang on 2016/10/24.
 * 角度旋转
 */

public class RotateDownPageTransformer implements ViewPager.PageTransformer {
    private static final float ROT_MAX = 20.0f;
    private static final int radiusMultiple = 2;
    private int radius;

    public RotateDownPageTransformer(ViewPager viewPager) {
        radius = viewPager.getLayoutParams().height / radiusMultiple;
        int margin = (int) (Math.tan(ROT_MAX / 90f) * radius);
        viewPager.setPageMargin(margin);
    }

    @Override
    public void transformPage(View page, float position) {
        float mRot = ROT_MAX * position;
        float translationY = radius * position * (ROT_MAX / 90f);
        translationY = Math.abs(translationY);

        page.setPivotX(page.getMeasuredWidth() * 0.5f);
        page.setPivotY(radius);
        page.setRotation(mRot);
        page.setTranslationY(translationY);
//        if (position < -1) {
//            page.setRotation(0);
//        } else if (position <= 1) {
//            page.setPivotX(page.getMeasuredWidth() * 0.5f);
//            page.setPivotY(page.getMeasuredHeight());
//            page.setRotation(mRot);
//        } else {
//            page.setRotation(0);
//        }
    }
}
