package com.jzy.custommarqueetoast;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_show).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show:
                showmarquee();
                onReboot( );

                break;
        }
    }

    private void showmarquee() {

        final View marView = View.inflate(MainActivity.this, R.layout.marquee_layout, null);
        TextView mTvGiftUserName_1 = (TextView) marView.findViewById(R.id.tv_gift_user_name_1);
        TextView mTvGiftUserName_2 = (TextView) marView.findViewById(R.id.tv_gift_user_name_2);


        mTvGiftUserName_1.setText("棒棒哒");
        mTvGiftUserName_2.setText("哒哒棒");

        LinearLayout llMarquee = (LinearLayout) marView.findViewById(R.id.ll_marquee);
        RelativeLayout rlShut = (RelativeLayout) marView.findViewById(R.id.rl_shut_circle_shape);

        //跑马灯
        final MarqueeToast make = MarqueeToast.make(MainActivity.this, marView).setShowTime(4000);
        make.showMarqueeToast(llMarquee);
        //关闭
        rlShut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make.hide();
            }
        });

        mTvGiftUserName_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }


    public void onReboot( ) {
//        try {
//            Runtime.getRuntime().exec("su -c \"/system/bin/shutdown\"");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        try {
            this.enforceCallingOrSelfPermission(android.Manifest.permission.REBOOT, null);

            final long ident = Binder.clearCallingIdentity();Binder.restoreCallingIdentity(ident);
        } finally {

        }

        try {

            //获得ServiceManager类
            Class<?> ServiceManager = Class
                    .forName("android.os.ServiceManager");

            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);

            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null, Context.POWER_SERVICE);

            //获得IPowerManager.Stub类
            Class<?> cStub = Class
                    .forName("android.os.IPowerManager$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            //获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("shutdown",boolean.class,boolean.class);
            //调用shutdown()方法
            shutdown.invoke(oIPowerManager,false,true);

        } catch (Exception e) {
            Log.e(TAG,  e+"");
        }
    }

    private static final String TAG = "MainActivity";
// Runtime.getRuntime().exec("su -c \"/system/bin/shutdown\"");这条命令是关机的命令
// Runtime.getRuntime().exec("su -c \"/system/bin/reboot\"");
}
