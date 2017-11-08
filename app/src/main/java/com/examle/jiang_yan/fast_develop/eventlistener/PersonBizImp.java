package com.examle.jiang_yan.fast_develop.eventlistener;

import android.util.Log;

import com.examle.jiang_yan.fast_develop.bean.PersonBean;

/**
 * Created by jiang_yan on 2016/9/21.
 */

public class PersonBizImp implements IPersonBiz {
    private static final String TAG1 = "PersonBizIpl";

    @Override
    public void login(final String username, final String password,final LoginRequestCallBack valueCallBack) {
        Log.d(TAG1,"username:"+username+",password:"+password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //进行开始登录,这边应该进行请求服务器，进行数据验证
                if(username.equals("jiang")&&password.equals("12345")){
                    valueCallBack.loginSuccess(new PersonBean(username,password));
                }else{
                    valueCallBack.loginFailed();
                }
            }
        }).start();
    }
}
