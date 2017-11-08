package com.examle.jiang_yan.fast_develop.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;
import com.examle.jiang_yan.fast_develop.bean.PersonBean;
import com.examle.jiang_yan.fast_develop.eventlistener.ILoginView;
import com.examle.jiang_yan.fast_develop.eventlistener.LoginPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiang_yan on 2016/9/19.
 * name
 */
public class ActivityMVPTest extends BaseActivity implements ILoginView {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ed_username)
    EditText edUsername;
    @Bind(R.id.ed_password)
    EditText edPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    private LoginPresenter mLoginPresenter;

    @Override
    public void initView() {
        setContentView(R.layout.layout_activitymvp);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("MVP");
        mLoginPresenter = new LoginPresenter(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void eventlistener() {

        //点击登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.loginSystem();
            }
        });
    }


    @Override
    public String getUserName() {
        return edUsername.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return edPassword.getText().toString().trim();
    }

    @Override
    public void showSuccessInfo(PersonBean personBean) {
        showCrouton("登录成功");
    }

    @Override
    public void showFailedInfo() {
        showCrouton("登录失败.....");

    }
}
