package com.tencent.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by jiang_yan on 2017/9/26.
 */

public class MyIntentService  extends IntentService {

    private static final String TAG = "IntentService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }


    /**
     * 处理子线程代码,自动创建worker线程来处理,此操作完成之后会自动调用onDestory(),
     * 不用专门去关闭服务
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        //可以执行一些耗时操作

        int n=1;
        while (n<4){
            Log.e(TAG, "onHandleIntent: 处理子线程");

            synchronized (this) {
                n++;
                Log.e(TAG, "onHandleIntent: "+n );
                try {
                    wait(2222);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } }
        }
    }
}
