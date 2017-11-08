package com.examle.jiang_yan.fast_develop.activities;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.examle.jiang_yan.fast_develop.R;

/**
 * Created by jiang_yan on 2016/10/20.
 */
public class ActivityImageSwitcher extends BaseActivity implements ViewSwitcher.ViewFactory, View.OnTouchListener {
    private static final String TAG = "MainActivity";
    /**
     * ImagaSwitcher 的引用
     */
    private ImageSwitcher mImageSwitcher;
    /**
     * 图片id数组
     */
    private int[] imgIds;
    /**
     * 当前选中的图片id序号
     */
    private int currentPosition;
    /**
     * 按下点的X坐标
     */
    private float downX;
    /**
     * 装载点点的容器
     */
    private LinearLayout linearLayout;
    /**
     * 点点数组
     */
    private ImageView[] tips;


    @Override
    public void initView() {
        setContentView(R.layout.layout_activity_image_swiycher);
        //imageSwitch
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.is_image);
        //装载点的LinearLayout
        linearLayout = (LinearLayout) findViewById(R.id.ll_view_group);
        //图片的id
        imgIds = new int[]{R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e};

        mImageSwitcher.setFactory(this);
        mImageSwitcher.setOnTouchListener(this);
        mImageSwitcher.setImageResource(imgIds[0]);
        //初始化点
        initDot();
        setcurrentDot(0);
    }

    @Override
    public void initData() {

    }

    @Override
    public void eventlistener() {

    }

    /**
     * 更新 点
     *
     * @param dotID
     */
    private void setcurrentDot(int dotID) {
        int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == dotID) {
                View childAt = linearLayout.getChildAt(dotID);
                childAt.setBackgroundColor(Color.WHITE);
            } else {
                View childAt = linearLayout.getChildAt(i);
                childAt.setBackgroundColor(Color.RED);
            }
        }
    }

    private void initDot() {
        for (int i = 0; i < imgIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundColor(Color.RED);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(12, 12));
            layoutParams.rightMargin = 31;
            layoutParams.leftMargin = 31;
            linearLayout.addView(imageView, layoutParams);
        }
    }

    /**
     * 重写switcher的factory的方法
     *
     * @return
     */
    @Override
    public View makeView() {

        final ImageView i = new ImageView(this);
        i.setBackgroundColor(Color.YELLOW);
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT));
        return i;
    }

    /**
     * ontouchlistener的方法
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                downX = event.getX();
                Log.e(TAG, "onTouch: " + downX);
                break;
            case MotionEvent.ACTION_MOVE:
                //移动

                break;
            case MotionEvent.ACTION_UP:
                //抬起
                float lastX = event.getX();
                //抬起的时候的X坐标大于按下的时候就显示上一张图片
                if (lastX > downX) {
                    if (currentPosition > 0) {
                        //设置动画
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_in));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_out));
                        currentPosition--;
                        mImageSwitcher.setImageResource(imgIds[currentPosition % imgIds.length]);
                        setcurrentDot(currentPosition);
                    } else {
                        Toast.makeText(getApplication(), "已经是第一张", Toast.LENGTH_SHORT).show();
                    }
                }

                if (lastX < downX) {
                    if (currentPosition < imgIds.length - 1) {
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_in));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_out));
                        currentPosition++;
                        mImageSwitcher.setImageResource(imgIds[currentPosition]);
                        setcurrentDot(currentPosition);
                    } else {
                        Toast.makeText(getApplication(), "到了最后一张", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }


        return true;
    }


}
