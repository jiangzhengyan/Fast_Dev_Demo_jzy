package com.examle.jiang_yan.fast_develop.activities;

import android.content.Context;
import android.os.Handler;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiang_yan on 2016/9/19.
 */
public class ActivityGalleryIndicator extends BaseActivity {
    private static final String TAG = "ActivityGalleryIndicator";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.vp_view_pager)
    ViewPager vpViewPager;
    @Bind(R.id.ll_dot)
    LinearLayout llDot;
    private ViewPagerAdapter mViewPagerAdapter;
    int[] pic = {R.mipmap.a, R.mipmap.b,
            R.mipmap.c, R.mipmap.d, R.mipmap.e};
    Handler mHandler = new Handler();
    private long time = 1500;//轮播的时间
    private int nextItemPosition;//将要展示的图片位置

    @Override
    public void initView() {
        setContentView(R.layout.activity_big_picture);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("轮播图");
        //初始化点的数据
        initDot();
        mViewPagerAdapter = new ViewPagerAdapter(this, pic);
        vpViewPager.setAdapter(mViewPagerAdapter);
        vpViewPager.setCurrentItem(500000);
    }

    /**
     * 初始化点
     */
    private void initDot() {
        for (int i = 0; i < pic.length; i++) {
            View view = new View(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(66, 66);
            layoutParams.leftMargin = 30;
            view.setLayoutParams(layoutParams);
            view.setBackgroundResource(i==0?R.drawable.dot_select:R.drawable.dot_state);
            llDot.addView(view);
        }
    }

    @Override
    public void initData() {
        //开始轮播
        start();
    }

    @Override
    public void eventlistener() {
        //控制手势
        vpViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:

                        stop();
                        break;
                    case MotionEvent.ACTION_UP:

                        start();
                        break;
                }
                return false;
            }
        });

        vpViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateDot();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 开始轮播
     */
    private void start() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, time);
                int currentItem = vpViewPager.getCurrentItem();
                nextItemPosition = currentItem + 1;
                vpViewPager.setCurrentItem(nextItemPosition);
                //更新点的位置和状态
                updateDot();
            }
        }, time);

    }
    /**
     * /更新点的位置和状态
     */
    private void updateDot() {
        //1.获取当前选中的页
        int currentItem = vpViewPager.getCurrentItem() % pic.length;

        //3.让对应位置的点变白色,还要让其他的点变黑色
        for (int i = 0; i < pic.length; i++) {
            View child = llDot.getChildAt(i);
            child.setBackgroundResource(i == currentItem ? R.drawable.dot_select
                    : R.drawable.dot_state);
        }
    }

    /**
     * 停止轮播
     */
    private void stop() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * viewpager的适配器
     */
    class ViewPagerAdapter extends PagerAdapter {
        private Context mContext;
        private int[] mPic;

        public ViewPagerAdapter(Context context, int[] pic) {
            this.mContext = context;
            this.mPic = pic;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(mPic[position % pic.length]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }
}
