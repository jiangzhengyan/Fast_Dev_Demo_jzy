package com.jingcaiwang.b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jingcaiwang.Ad;
import com.jingcaiwang.MainPagerAdapter;
import com.jingcaiwang.R;
import com.jingcaiwang.transformer.DepthTransformation;
import com.jingcaiwang.transformer.Transformer_1;
import com.jingcaiwang.transformer.Transformer_2;
import com.jingcaiwang.transformer.Transformer_3;

import java.util.ArrayList;
public class ActivityB  extends Activity  {
    private ViewPager viewPager;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        //1.初始化VIew
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        dot_layout = (LinearLayout) findViewById(R.id.dot_layout);

        //2.准备数据
        list.add(new Ad(R.mipmap.a, "巩俐不低俗，我就不能低俗"));
        list.add(new Ad(R.mipmap.b, "朴树又回来了，再唱经典老歌引百万人大合唱"));
        list.add(new Ad(R.mipmap.c, "揭秘北京电影如何升级"));
        list.add(new Ad(R.mipmap.d, "乐视网TV版大派送"));
        list.add(new Ad(R.mipmap.e, "热血屌丝的反杀"));

        //3.给ViewPager填充数据
        viewPager.setAdapter(new MainPagerAdapter(this, list));

        //4.给ViewPager添加滑动改变的监听器
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
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
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stop();
                        break;
                    case MotionEvent.ACTION_UP:
                        start();
                        break;

                }
                return false;
            }
        });
        //设置左右可以缓存的页数，但是一般不用，因为会造成内存过大的问题
		viewPager.setOffscreenPageLimit(4);
//        viewPager.setPageTransformer(true,new Transformer_1());
//        viewPager.setPageTransformer(true,new Transformer_2());
//        viewPager.setPageTransformer(true,new Transformer_3());
        viewPager.setPageTransformer(true,new DepthTransformation());
        viewPager.setPageTransformer(true,new DepthTransformation());

        //动态创建点，并且添加到dot_layout
        initDot();

        //刚进来的时候就去调用更新标题的方法
        updateTitleAndDot();

        //让ViewPager一进来就选中一个比较大的页,
        //注意：此处的数值不要设置过大，因为在加载网络图片的时候去造成ViewPager卡顿
        viewPager.setCurrentItem(list.size() * 100000);
        //发送延时消息
        handler.sendEmptyMessageDelayed(0, 2500);
    }

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
     *  停止
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


}