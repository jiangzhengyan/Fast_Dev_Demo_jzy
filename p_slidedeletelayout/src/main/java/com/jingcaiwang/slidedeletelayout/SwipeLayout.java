package com.jingcaiwang.slidedeletelayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * Created by jiang_yan on 2017/11/11.
 */

public class SwipeLayout extends FrameLayout {

    private static final String TAG = "SwipeLayout";
    private ViewDragHelper viewDragHelper;
    private int scaledTouchSlop;

    public SwipeLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public enum SwipeState {
        Open, Close
    }

    private SwipeState mState = SwipeState.Close;
    private View contentView;
    private View deleteView;
    private int contentWidth;
    private int contentHeight;
    private int deleteWidth;
    private int deleteHeight;

    private void init() {
        //初始化
        viewDragHelper = ViewDragHelper.create(this, dragCallBack);
        scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }

    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        if (getChildCount() != 2)
            throw new IllegalArgumentException("只能2个子布局");
        contentView = getChildAt(0);
        deleteView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentWidth = contentView.getMeasuredWidth();
        contentHeight = contentView.getMeasuredHeight();
        deleteWidth = deleteView.getMeasuredWidth();
        deleteHeight = deleteView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        contentView.layout(0, 0, contentWidth, contentHeight);
        deleteView.layout(contentWidth, 0, contentWidth + deleteWidth, deleteHeight);
    }

    private ViewDragHelper.Callback dragCallBack = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == contentView || child == deleteView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return deleteWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == contentView) {
                if (left > 0)
                    left = 0;
                if (left < -deleteWidth)
                    left = -deleteWidth;

            } else if (child == deleteView) {

                if (left > contentWidth)
                    left = contentWidth;
                if (left < contentWidth - deleteWidth)
                    left = contentWidth - deleteWidth;
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == contentView) {
                int newLeft = deleteView.getLeft() + dx;
                deleteView.layout(newLeft, 0, newLeft + deleteWidth, deleteView.getBottom());
            } else if (changedView == deleteView) {
                int newLeft = contentView.getLeft() + dx;
                contentView.layout(newLeft, 0, newLeft + contentWidth, contentView.getBottom());
            }

            //处理打开与关闭的逻辑
            if (contentView.getLeft() < -deleteWidth / 2) {
                //说明打开了
                mState = SwipeState.Open;

            } else if (contentView.getLeft() > -deleteWidth / 2) {
                //说明关闭了
                mState = SwipeState.Close;

            }

        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            if (xvel > 2555) {
                mState = SwipeState.Close;
            } else if (xvel < -2555) {
                mState = SwipeState.Open;
            }
            if (mState == SwipeState.Open) {
//                //应该打开
                open();
            } else {
                close();
            }

        }
    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);
        //判断当前是否满足可以滑动的条件
        if (!SwipeLayoutManager.getInstance().isCanSwipe(this)) {
            //说明不满足，应该首先去关闭已经打开的
            SwipeLayoutManager.getInstance().closeSwipeLayout();

            //去拦截事件，交给onTouchEvent处理
            result = true;
        }

        return result;
    }

    private float downX, downY;//按下的x,y坐标
    private float moveX, moveY;//移动时候x,y坐标
    private float delX, delY;//抬起时候x,y偏移量
    private long downTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                moveY = event.getY();
                if (Math.abs(downX - moveX) > Math.abs(downY - moveY)) {
                    requestDisallowInterceptTouchEvent(true);
                }

                break;
            case MotionEvent.ACTION_UP:
                //根据速度限制开关
                long duration = System.currentTimeMillis() - downTime;
                delX = moveX - downX;
                delY = moveY - downY;

                break;

        }
        invalidate();
        //把触摸事件交给viewdragHelp
        viewDragHelper.processTouchEvent(event);
        return true;
    }


    /**
     * 打开
     */
    public void open() {

        SwipeLayoutManager.getInstance().setOpenSwipeLayout(SwipeLayout.this);
        viewDragHelper.smoothSlideViewTo(contentView, -deleteWidth, 0);
        //刷新整个VIew
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 关闭
     */
    public void close() {
        Log.e(TAG, "close: ");
        viewDragHelper.smoothSlideViewTo(contentView, 0, 0);
        //刷新整个VIew
        ViewCompat.postInvalidateOnAnimation(this);
        SwipeLayoutManager.getInstance().clearSwipeLayout();

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            //刷新整个VIew
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

}
