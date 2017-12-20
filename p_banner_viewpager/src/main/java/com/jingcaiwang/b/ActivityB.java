package com.jingcaiwang.b;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Scroller;
import android.widget.TextView;

import com.jingcaiwang.Ad;
import com.jingcaiwang.MainPagerAdapter;
import com.jingcaiwang.R;
import com.jingcaiwang.transformer.SinaLikeTransformer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ActivityB extends Activity {
    private MyViewPager viewPager;
    private TextView tv_title;
    private LinearLayout dot_layout;

    private ArrayList<Ad> list = new ArrayList<Ad>();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //让ViewPager选中下一页
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

            //继续发送延时消息，达到不断轮播的效果
            handler.sendEmptyMessageDelayed(0, 2500);
        }
    };
    private SensorManager sm;
    private static final String TAG = "ActivityB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        viewPager = (MyViewPager) findViewById(R.id.viewPager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        dot_layout = (LinearLayout) findViewById(R.id.dot_layout);

        list.add(new Ad(R.mipmap.a, "希望一切事情都像长胖那么容易"));
        list.add(new Ad(R.mipmap.b, "你认真的样子就像天桥上贴膜的。"));
        list.add(new Ad(R.mipmap.c, "睡不到我，你的人生还有什么意义。"));
        list.add(new Ad(R.mipmap.d, "为了节约用水，今晚可以一起洗澡吗？"));
        list.add(new Ad(R.mipmap.e, "你的名字是最短的情诗。"));
        list.add(new Ad(R.mipmap.f, "有空一起睡觉啊？"));
        list.add(new Ad(R.mipmap.g, "来，有事床上聊。"));

        systemSensorManager();

        viewPager.setAdapter(new BPagerAdapter(this, list));

        viewPager.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {
            /**
             * when当前页发送改变的时候调用
             */
            @Override
            public void onPageSelected(int position) {
//				Log.e("tag", "onPageSelected position: "+position);
                updateTitleAndDot();
            }

            /**
             * 当Page滚动的时候会调用
             */
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
//				Log.e("tag", "onPageScrolled: "+positionOffsetPixels);
            }

            /**
             * 当Page滚动状态改变的时候调用
             */
            @Override
            public void onPageScrollStateChanged(int state) {
//				Log.e("tag", "onPageScrollStateChanged state: "+state);
            }
        });
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        stop();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        start();
//                        break;
//
//                }
//                return false;
//            }
//        });
        //设置左右可以缓存的页数，但是一般不用，因为会造成内存过大的问题
        viewPager.setOffscreenPageLimit(3);
//        viewPager.setPageTransformer(true,new Transformer_1());
//        viewPager.setPageTransformer(true,new Transformer_2());
//        viewPager.setPageTransformer(true,new Transformer_3());
//        viewPager.setPageTransformer(true,new DepthTransforma tion());
//        viewPager.setPageTransformer(true,new RotateDownPageTransformer(viewPager));
//        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
//        viewPager.setPageTransformer(true,new MyTransformation());
        viewPager.setPageTransformer(true, new SinaLikeTransformer());


        //动态创建点，并且添加到dot_layout
        initDot();

        //刚进来的时候就去调用更新标题的方法
        updateTitleAndDot();

        viewPager.setCurrentItem(list.size() * 100000);
        //发送延时消息
//        handler.sendEmptyMessageDelayed(0, 2500);
//        try {
//            Field field = MyViewPager.class.getDeclaredField("mScroller");
//            field.setAccessible(true);
//            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(),
//                    new AccelerateInterpolator());
//            field.set(viewPager, scroller);
//            scroller.setmDuration(800);
//        } catch (Exception e) {
//
//        }
    }

    /**
     * 系统感应
     */
    private void systemSensorManager() {
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);//感应
        //获取所有感应
        List<Sensor> sensorList1 = sm.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < sensorList1.size(); i++) {
            Log.e(TAG, "onCreate:sensorList1  " + sensorList1.get(i).getName());
        }
        Sensor defaultSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(sensorEventListener,defaultSensor  , SensorManager.SENSOR_DELAY_NORMAL);
    }
    private float x, y, z;
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            float[] values = event.values;
            x =  values[SensorManager.DATA_X];
            y =  values[SensorManager.DATA_Y];
            z =  values[SensorManager.DATA_Z];
            Log.e(TAG, "onSensorChanged:X : "+x );
            Log.e(TAG, "onSensorChanged:Y : "+y );
            Log.e(TAG, "onSensorChanged:Z : "+z );


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

            Log.e(TAG, "onAccuracyChanged: "+sensor.getName() );
        }
    };

    /**
     * 初始化所有的点
     */
    private void initDot() {
        for (int i = 0; i < list.size(); i++) {
            View view = new View(this);
            LayoutParams params = new LayoutParams(10, 10);
            params.leftMargin = 10;
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.dot_black);
            dot_layout.addView(view);
        }
    }

    /**
     * 根据当前ViewPager选中的页来设置标题和点的图片
     */
    protected void updateTitleAndDot() {
        int currentItem = viewPager.getCurrentItem() % list.size();
        tv_title.setText(list.get(currentItem).getTitle());
        for (int i = 0; i < list.size(); i++) {
            View child = dot_layout.getChildAt(i);
            child.setBackgroundResource(i == currentItem ? R.drawable.dot_white
                    : R.drawable.dot_black);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }

    /**
     * 停止
     */
    private void stop() {
        handler.removeMessages(0);
    }

    /**
     * 开始
     */
    private void start() {
        handler.sendEmptyMessageDelayed(0, 2500);
    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 1500;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }
}