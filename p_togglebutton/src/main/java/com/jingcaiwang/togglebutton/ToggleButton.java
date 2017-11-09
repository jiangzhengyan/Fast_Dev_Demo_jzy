package com.jingcaiwang.togglebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jiang_yan on 2017/11/8 .
 */

public class ToggleButton extends View {
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
    private Bitmap toggleBg = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_background);
    //滑 块
    private Bitmap slideImage = BitmapFactory.decodeResource(getResources(), R.mipmap.slide_image);


    /**
     * 定义开关的状态
     */
    public enum ToggleState {
        On, Off
    }

    // 当前开关的状态
    private ToggleState mState = ToggleState.Off;
    private static final String TAG = "ToggleButton";

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: " + toggleBg.getWidth());
        if (toggleBg != null) {
            setMeasuredDimension(toggleBg.getWidth(), toggleBg.getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景宽度
        int toggleBgWidth = toggleBg.getWidth();
        //滑块长度
        int slideImageWidth = slideImage.getWidth();
        Log.e(TAG, "onDraw: " + toggleBgWidth);
        Log.e(TAG, "onDraw: " + slideImageWidth);

        //画背景
        canvas.drawBitmap(toggleBg, 0, 0, null);
        //画滑块
        if (isSliding) {
            float left = currentX - slideImageWidth / 2;
            if (left < 0)
                left = 0;
            if (left > toggleBgWidth - slideImageWidth)
                left = toggleBgWidth - slideImageWidth;

            canvas.drawBitmap(slideImage, left, 0, null);
        } else {
            if (mState == ToggleState.On) {
                //开
                //画到右边
                canvas.drawBitmap(slideImage, toggleBgWidth - slideImageWidth, 0, null);
            } else {
                //关
                //画到左边
                canvas.drawBitmap(slideImage, 0, 0, null);

            }
        }
    }

    private boolean isSliding = false;//是否正在滑动
    private float currentX;//当前x位置

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_MOVE:
                isSliding = true;
                //当前滑到的位置
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                isSliding = false;
                int l = toggleBg.getWidth() / 2;
                if (currentX < l && mState != ToggleState.Off) {
                    //应该是关闭
                    mState = ToggleState.Off;
                    if (listener != null) {
                        listener.onToggleStateChange(mState);
                    }
                } else if (currentX >= l && mState != ToggleState.On) {
                    //应该是开
                    mState = ToggleState.On;
                    if (listener != null) {
                        listener.onToggleStateChange(mState);
                    }
                }
                break;

        }
        invalidate();
//        return super.onTouchEvent(event);
        return true;
    }

    private OnToggleStateChangeListener listener;

    public void setOnToggleStateChangeListener(OnToggleStateChangeListener listener) {
        this.listener = listener;
    }

    //定义接口回调
    public interface OnToggleStateChangeListener {
        void onToggleStateChange(ToggleState state);
    }
}
