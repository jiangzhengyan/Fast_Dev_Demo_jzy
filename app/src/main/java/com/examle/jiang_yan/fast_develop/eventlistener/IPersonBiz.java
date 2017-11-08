package com.examle.jiang_yan.fast_develop.eventlistener;

/**
 * Created by jiang_yan on 2016/9/21.
 */

public interface IPersonBiz {
    void login(String username,String password,LoginRequestCallBack valueCallBack);
}
