package com.jingcaiwang.b;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jingcaiwang.Ad;
import com.jingcaiwang.R;
import com.jingcaiwang.transformer.SinaLikeTransformer;

import java.util.ArrayList;
import java.util.List;

public class ActivityGravityBanner extends Activity {
    private GravityBannerViewPager mViewPager;
    private TextView mTvTitle;
    private LinearLayout mLlDotLayout;
    private ArrayList<Ad> mDataList = new ArrayList<>();
    private SensorManager mSm;
    private static final String TAG = "ActivityGravityBanner";
    private int mUpItem;
    private int mOffscreenPageLimit = 12;
    private float mSensorGravityX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        mViewPager = (GravityBannerViewPager) findViewById(R.id.viewPager);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mLlDotLayout = (LinearLayout) findViewById(R.id.llDotLayout);

        mDataList.add(new Ad(R.mipmap.a, "希望一切事情都像长胖那么容易"));
        mDataList.add(new Ad(R.mipmap.b, "你认真的样子就像天桥上贴膜的。"));
        mDataList.add(new Ad(R.mipmap.c, "睡不到我，你的人生还有什么意义。"));
        mDataList.add(new Ad(R.mipmap.d, "为了节约用水，今晚可以一起洗澡吗？"));
        mDataList.add(new Ad(R.mipmap.e, "你的名字是最短的情诗。"));
        mDataList.add(new Ad(R.mipmap.f, "有空一起睡觉啊？"));
        mDataList.add(new Ad(R.mipmap.g, "来，有事床上聊。"));
        mViewPager.setAdapter(new MysPagerAdapter(this, mDataList));
        mViewPager.setOnPageChangeListener(new GravityBannerViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateTitleAndDot();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setOnTouchUpItemListener(new GravityBannerViewPager.OnTouchUpItemListener() {
            @Override
            public void onTouchUpItem(int upItem) {
                mUpItem = upItem;
            }
        });
        mViewPager.setOffscreenPageLimit(mOffscreenPageLimit);
//        mViewPager.setPageTransformer(true,new Transformer_1());
//        mViewPager.setPageTransformer(true,new Transformer_2());
//        mViewPager.setPageTransformer(true,new Transformer_3());
//        mViewPager.setPageTransformer(true,new DepthTransforma tion());
//        mViewPager.setPageTransformer(true,new RotateDownPageTransformer(mViewPager));
//        mViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
//        mViewPager.setPageTransformer(true,new MyTransformation());
        mViewPager.setPageTransformer(true, new SinaLikeTransformer());
        //初始化点
        initDot();
        updateTitleAndDot();
        mViewPager.setCurrentItem(mDataList.size() * 100000);
        mUpItem = mViewPager.getCurrentItem();
        systemSensorManager();

    }

    /**
     * 系统感应
     */
    private void systemSensorManager() {
        mSm = (SensorManager) getSystemService(SENSOR_SERVICE);//感应
        //获取所有感应
        List<Sensor> sensorList = mSm.getSensorList(Sensor.TYPE_ALL);
        Sensor defaultSensor = mSm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSm.registerListener(sensorEventListener, defaultSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    int mTempItem;
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (mViewPager.isTouch())
                return;
            mSensorGravityX = event.values[SensorManager.DATA_X];
            int round = Math.round(mSensorGravityX);
            int currentItem = mViewPager.getCurrentItem();
            int futureItem = mUpItem + round;
            int dataSkipItemCount = futureItem - currentItem;
            if (Math.abs(dataSkipItemCount) > mOffscreenPageLimit)
                futureItem = (dataSkipItemCount >= 0) ? currentItem + mOffscreenPageLimit : currentItem - mOffscreenPageLimit;
            mViewPager.setCurrentItem(futureItem);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    /**
     * 初始化所有的点
     */
    private void initDot() {
        for (int i = 0; i < mDataList.size(); i++) {
            View view = new View(this);
            LayoutParams params = new LayoutParams(10, 10);
            params.leftMargin = 10;
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.dot_black);
            mLlDotLayout.addView(view);
        }
    }

    /**
     * 根据当前ViewPager选中的页来设置标题和点的图片
     */
    protected void updateTitleAndDot() {
        int currentItem = mViewPager.getCurrentItem() % mDataList.size();
        mTvTitle.setText(mDataList.get(currentItem).getTitle());
        for (int i = 0; i < mDataList.size(); i++) {
            View child = mLlDotLayout.getChildAt(i);
            child.setBackgroundResource(i == currentItem ? R.drawable.dot_white
                    : R.drawable.dot_black);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSm != null) {
            mSm.unregisterListener(sensorEventListener);
        }
    }


}