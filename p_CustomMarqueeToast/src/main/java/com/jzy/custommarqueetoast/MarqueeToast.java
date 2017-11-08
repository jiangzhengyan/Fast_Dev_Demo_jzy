package com.jzy.custommarqueetoast;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.MediaRouteActionProvider;
import android.content.Context;
import android.os.health.SystemHealthManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;

/**
 * Created by jiangzhengyan on 17/8/28.
 *
 * The class will show the Crouton toast by selfdesign,
 * in big gift method be used
 * attention must be helped by Crouton
 */
public final  class MarqueeToast {
    private static final String TAG = "MarqueeToast";

    private Context mContext;
    private Crouton make;
    private int mWidth = 0;
    private boolean mIsFirstShow = true;
    private View mCustomView;
    private long mShowTime = 3000;//动画显示时间

    private MarqueeToast(Context context, View customView) {
        this.mContext = context;
        this.mCustomView = customView;
    }

    /**
     *
     * @param context
     * @param customView   自定义的布局
     * @return
     */
    public static MarqueeToast make(Context context, View customView) {

        return new MarqueeToast(context, customView);
    }

    /**
     * 跑马灯的布局
     * @param marqueeShowView
     */
    public void showMarqueeToast(final View marqueeShowView) {

        make = Crouton.make((Activity) mContext, mCustomView);
        make.setConfiguration(new Configuration.Builder().setDuration((int) mShowTime).build());
        make.setLifecycleCallback(new LifecycleCallback() {
            @Override
            public void onDisplayed() {
                showToastAnim(marqueeShowView);
            }

            @Override
            public void onRemoved() {
            }
        });
        make.show();
    }


    /**
     *  跑马灯动画
     * @param marqueeShowView
     */
    private void showToastAnim(final View marqueeShowView) {
        ViewTreeObserver viewTreeObserver = marqueeShowView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWidth = marqueeShowView.getWidth();
                if (mIsFirstShow && mWidth > 0) {
                    mIsFirstShow = false;
                    Log.e(TAG, "onGlobalLayout:  mWidth  :  "+ mWidth);
                    WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);

                    int width = wm.getDefaultDisplay().getWidth();
                    int height = wm.getDefaultDisplay().getHeight();
                    Log.e(TAG, "onGlobalLayout: pm :"+width+"     h : " +height);
                    final  ObjectAnimator    mOa = ObjectAnimator.ofFloat(marqueeShowView, "translationX", mWidth,-mWidth-width);
                    mOa.setDuration(mShowTime);
                    mOa.start();
                    mOa.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mIsFirstShow = true;
                            make.hide();
                            if (mOa != null) {
                                mOa.cancel();
                            }
                        }
                    });
                }
                marqueeShowView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }


    /**
     * set animation show time
     * @param showTime
     */
    public MarqueeToast setShowTime(int showTime) {
        this.mShowTime = showTime;
        return this;
    }

    /**
     * 隐藏吐司
     */
    public void hide() {
        make.hide();
    }
}
