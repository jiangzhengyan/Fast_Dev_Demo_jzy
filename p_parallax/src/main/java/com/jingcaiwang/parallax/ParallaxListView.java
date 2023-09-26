package com.jingcaiwang.parallax;

import android.animation.ValueAnimator;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by jiang_yan on 2017/11/16.
 */

public class ParallaxListView extends ListView {
    private static final String TAG = "ParallaxListView";
    private int parallaxTrueHeight;

    public ParallaxListView(Context context) {
        super(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private ImageView parallaxImage;//头部的图
    private int parallaxImageOriginalHeight;//头部的图的原始高度

    public void setParallaxHeaderImage(@NonNull final ImageView parallaxImage) {
        this.parallaxImage = parallaxImage;

        //设定最大高度为图片的真实高度
        parallaxTrueHeight = parallaxImage.getDrawable().getIntrinsicHeight();
        Log.e(TAG, "setParallaxHeaderImage: " + parallaxTrueHeight);
        parallaxImage
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(new

                                                   ViewTreeObserver.
                                                           OnGlobalLayoutListener() {
                                                       @Override
                                                       public void onGlobalLayout() {
                                                           //移除布局的监听
                                                           parallaxImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                                           parallaxImageOriginalHeight = parallaxImage.getMeasuredHeight();
                                                           Log.e(TAG, "onGlobalLayout: " + parallaxImageOriginalHeight);
                                                       }
                                                   });
    }

    /**
     * listview滑到头调用的
     *
     * @param deltaX
     * @param deltaY         继续滑动的距离, 正:底部到头, 负:顶部到头
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX
     * @param maxOverScrollY 到头之后可以继续滑动的距离
     * @param isTouchEvent   true : 手指滑到头的  false : 惯性滑到头
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
//        Log.e(TAG, "overScrollBy: deltaY:" + deltaY
//                + "\r\nscrollY:" + scrollY
//                + " \r\nscrollRangeY: " + scrollRangeY
//                + " \r\nmaxOverScrollY: " + maxOverScrollY
//                + " \r\nisTouchEvent: " + isTouchEvent);
        //如果是顶部到头,并且是手指拖到头的,才让header高度增加
        if (deltaY < 0 && isTouchEvent) {
//            int newHeight = parallaxImageOriginalHeight - deltaY;
            int newHeight = parallaxImage.getHeight() - deltaY / 3;
            //限制滑动的最大值
            if (newHeight > parallaxTrueHeight)
                newHeight = parallaxTrueHeight;
            ViewGroup.LayoutParams layoutParams = parallaxImage.getLayoutParams();
            layoutParams.height = newHeight;
            parallaxImage.setLayoutParams(layoutParams);
//            parallaxImage.requestLayout();
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                ValueAnimator valueAnimator = ValueAnimator.ofInt(parallaxImage.getHeight(), parallaxImageOriginalHeight);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int animatedValue = (int) animation.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = parallaxImage.getLayoutParams();
                        layoutParams.height = animatedValue;
                        parallaxImage.setLayoutParams(layoutParams);
                    }
                });
                valueAnimator.setInterpolator(new OvershootInterpolator());
                valueAnimator.setDuration(400);
                valueAnimator.start();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
