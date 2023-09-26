package com.examle.jiang_yan.fast_develop.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.examle.jiang_yan.fast_develop.utils.ActivityMange;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by jiang_yan on 2016/9/19.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏标题
        getSupportActionBar().hide();

        //隐藏状态栏,
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        initData();
        eventlistener();
        //把打开的Activity添加到集合中
        ActivityMange.getInstance().addActivity(this);
        Log.e(TAG, "onCreate: ");
    }


    public abstract void initView();

    public abstract void initData();

    public abstract void eventlistener();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //返回就清除这个activity
        ActivityMange.getInstance().removeActivity(this);
        Log.i(TAG, "onBackPressed: ");
    }

    /**
     * 打开activity
     *
     * @param activityClass
     */
    public void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    /**
     * 携带数据开启activity
     *
     * @param intent
     */
    public void openActivityByIntent(Intent intent) {
        startActivity(intent);
    }

    public void showCrouton(String content) {
        Crouton.makeText(this, content, Style.ALERT).show();
    }
}