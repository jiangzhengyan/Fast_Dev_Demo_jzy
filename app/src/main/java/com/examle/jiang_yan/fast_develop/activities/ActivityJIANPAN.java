package com.examle.jiang_yan.fast_develop.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiang_yan on 2016/10/9.
 */
public class ActivityJIANPAN extends BaseActivity {
    private static final String TAG = "pic";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;

    @Override
    public void initView() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.layout_activity_jianpan);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void eventlistener() {
        llRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = llRoot.getRootView().getHeight();
                int height1 = llRoot.getHeight();
                int heightJianpan = height - height1;
                Log.e(TAG, "onGlobalLayout: " + height + "\n llrootheight:" + height1+"\n"+heightJianpan);

            }
        });
    }

    /**
     * 切换键盘的显示和隐藏
     * @param activity
     */
    public static void hideKeyboard_1(Activity activity) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

        }
    }

}
