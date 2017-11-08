package com.examle.jiang_yan.fast_develop.appdevelop;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by jiang_yan on 2016/9/20.
 */

public class App extends Application {
    Context mApplicationContext = getApplicationContext();
    private static final App app = new App();
    public static final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();
    }
    //私有化构造方法
    private App() {
    }
    //App的实例
    public synchronized static App getAppInstance() {
        return app;
    }
    //    获取上下文
    public Context getApplicationContext(){
        return mApplicationContext;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(TAG, "onLowMemory: " );
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e(TAG, "onTrimMemory: " );
    }
}
