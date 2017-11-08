package com.examle.jiang_yan.fast_develop.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiang_yan on 2016/10/10.
 */
public class ResultActivity extends BaseActivity {
    @Bind(R.id.btn_back)
    Button btnBack;
    @Bind(R.id.scan_title)
    TextView scanTitle;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.relativeLayout1)
    RelativeLayout relativeLayout1;
    @Bind(R.id.result)
    TextView result;

    @Override
    public void initView() {
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        String resultString = this.getIntent().getStringExtra("result");
        result.setText(resultString);
    }

    @Override
    public void eventlistener() {

    }


    @OnClick(R.id.btn_back)
    public void onClick() {
        finish();
    }
}
