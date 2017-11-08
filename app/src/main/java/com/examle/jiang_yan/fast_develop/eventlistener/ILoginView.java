package com.examle.jiang_yan.fast_develop.eventlistener;

import com.examle.jiang_yan.fast_develop.bean.PersonBean;

/**
 * Created by jiang_yan on 2016/9/21.
 */

public interface ILoginView {
    //获取用户名
    String getUserName();
    //获取密码
    String getPassword();

    void showSuccessInfo(PersonBean personBean);
    void showFailedInfo();
}
