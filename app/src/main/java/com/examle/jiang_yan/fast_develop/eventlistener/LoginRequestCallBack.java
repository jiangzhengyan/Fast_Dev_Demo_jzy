package com.examle.jiang_yan.fast_develop.eventlistener;

import com.examle.jiang_yan.fast_develop.bean.PersonBean;

/**
 * 登录请求结果回调
 * Created by jiang_yan on 2016/9/21.
 */

public interface LoginRequestCallBack {
    void loginSuccess(PersonBean personBean);

    void loginFailed();
}
