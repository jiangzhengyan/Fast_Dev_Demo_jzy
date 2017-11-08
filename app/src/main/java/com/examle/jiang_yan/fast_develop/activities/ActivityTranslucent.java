package com.examle.jiang_yan.fast_develop.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiang_yan on 2016/9/19.
 */
public class ActivityTranslucent extends BaseActivity {
    private static final String TAG = "activityTranslucent";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.linear_bar)
    LinearLayout linearBar;
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.btn)
    Button btn;

    @Override
    public void initView() {
        setContentView(R.layout.layout_translucent);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("沉浸式");


    }

    @Override
    public void initData() {
        //当系统版本>=4.4时可以使用沉浸式状态栏
        int mode = getIntent().getIntExtra("mode", 0);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明单行蓝
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            switch (mode) {
                case 0:
                    //系统的
                    linearBar.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearBar.getLayoutParams();
                    layoutParams.height=getstatusBarHeight();
                    linearBar.setLayoutParams(layoutParams);
                    tv.setText("系统方法设置状态栏高度");
                    break;
                case 1:
                    //第三方的
                    break;
            }

        }
    }

    @Override
    public void eventlistener() {
        getstatusBarHeight();
    }

    private int getstatusBarHeight() {
        try {
            //反射获取状态栏的高度
            Class<?> aClass = Class.forName("com.android.internal.R$dimen");
            Object o = aClass.newInstance();
            Field status_bar_height = aClass.getField("status_bar_height");
            int height = Integer.parseInt(status_bar_height.get(o).toString());//17104913
            Log.e(TAG, "getstatusBarHeight: " + height);
            //转化成像素值
            int dimensionPixelSize = getResources().getDimensionPixelSize(height);//50
            Log.e(TAG, "getstatusBarHeight: " + dimensionPixelSize);

            return dimensionPixelSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


}
