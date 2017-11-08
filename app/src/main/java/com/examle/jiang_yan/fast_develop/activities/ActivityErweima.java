package com.examle.jiang_yan.fast_develop.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiang_yan on 2016/10/9.
 */
public class ActivityErweima extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_sao)
    Button btnSao;
    @Bind(R.id.btn_result)
    Button btnResult;

    @Override
    public void initView() {
        setContentView(R.layout.layout_activity_erweima);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("二维码");
    }

    @Override
    public void initData() {

    }

    @Override
    public void eventlistener() {

    }



    @OnClick({R.id.btn_sao, R.id.btn_result})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sao:
                openActivity(CaptureActivity.class);
                break;
            case R.id.btn_result:
                openActivity(ScanResultActivity.class);
                break;
        }
    }
}
