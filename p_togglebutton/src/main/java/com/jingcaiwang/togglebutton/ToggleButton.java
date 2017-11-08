package com.jingcaiwang.togglebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jiang_yan on 2017/11/8 .
 */

public class ToggleButton extends View{
    public ToggleButton(Context context) {
        super(context);
    }

    public ToggleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //定义滑动的背景
    private Bitmap toggleBg= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
    //滑 块
    private Bitmap slideImage;


}
