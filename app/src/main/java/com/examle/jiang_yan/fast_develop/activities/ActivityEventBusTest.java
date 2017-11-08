package com.examle.jiang_yan.fast_develop.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;
import com.examle.jiang_yan.fast_develop.utils.RxBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by jiang_yan on 2016/9/19.
 */
public class ActivityEventBusTest extends BaseActivity {
    public static final String TAG = "EventBusTest";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_receive)
    TextView tvReceive;
    @Bind(R.id.btn_goto)
    Button btnGoto;
    private Subscription subscribe;

    @Override
    public void initView() {
        setContentView(R.layout.layout_activity_rxbus);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("RxBus事件总线");
        subscribeRxBus();//订阅消息
    }

    /**
     * 订阅消息
     */
    private void subscribeRxBus() {
        //接收传过来的数据
        subscribe = RxBus.getInstance().toObserverable().subscribe(new Action1<Object>() {

            @Override
            public void call(Object o) {
                //接收传过来的数据
                String name = Thread.currentThread().getName();
                if (o instanceof CharSequence) {
                    String reveiveContent = (String) o;
                    Log.e(TAG, "call: " + reveiveContent);
                    tvReceive.setText(reveiveContent);
                }
                Log.e(TAG, "call: " + name);

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void eventlistener() {

    }

    

    @OnClick(R.id.btn_goto)
    public void onClick() {
        openActivity(ActivityEventBusTest_2.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅
        if (!subscribe.isUnsubscribed())
        subscribe.unsubscribe();
    }
}
