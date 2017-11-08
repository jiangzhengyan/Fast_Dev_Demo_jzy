package com.examle.jiang_yan.fast_develop.eventlistener;

import android.os.Handler;
import android.util.Log;

import com.examle.jiang_yan.fast_develop.bean.PersonBean;

/**
 * Created by jiang_yan on 2016/9/21.
 */

public class LoginPresenter {
    private static final String TAG = "LoginPresenter";
    private ILoginView mLoginView;
    private IPersonBiz mPersonBiz;

    private Handler mHandler = new Handler();

    public LoginPresenter(ILoginView view) {
        mLoginView = view;
        mPersonBiz = new PersonBizImp();
    }

    public void loginSystem() {
        mPersonBiz.login(mLoginView.getUserName(), mLoginView.getPassword(), new LoginRequestCallBack() {
            @Override
            public void loginSuccess(final PersonBean personBean) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoginView.showSuccessInfo(personBean);
                    }
                });
            }

            @Override
            public void loginFailed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoginView.showFailedInfo();;
                    }
                });
            }
        });
    }
}
