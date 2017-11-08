package com.examle.jiang_yan.fast_develop.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.examle.jiang_yan.fast_develop.R;
import com.examle.jiang_yan.fast_develop.utils.RxBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiang_yan on 2016/9/22.
 */

public class ActivityEventBusTest_2 extends BaseActivity {
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.btn_send)
    Button btnSend;

    @Override
    public void initView() {
        setContentView(R.layout.layout_activity_second);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void eventlistener() {

    }



    @OnClick(R.id.btn_send)
    public void onClick() {
//        在这里发送数据
        String content = etContent.getText().toString().trim();
        RxBus.getInstance().send(content.equals("")?"发送的为空":content);
    }

}
