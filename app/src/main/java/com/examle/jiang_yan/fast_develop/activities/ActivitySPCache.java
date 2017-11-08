package com.examle.jiang_yan.fast_develop.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiang_yan on 2016/9/19.
 */
public class ActivitySPCache extends BaseActivity {
    private static final String TAG = "ActivitySPCache";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.btn_get)
    Button btnGet;
    @Bind(R.id.btn_clear)
    Button btnClear;
    private SharedPreferences sp;

    @Override
    public void initView() {
        setContentView(R.layout.layout_sp_cache);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("缓存存储数据");
    }

    @Override
    public void initData() {
        sp = getSharedPreferences("content", MODE_PRIVATE);

    }

    @Override
    public void eventlistener() {

    }


    @OnClick({R.id.btn_save, R.id.btn_get, R.id.btn_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                String content = etContent.getText().toString().trim();
                if (!content.equals("")  ) {
                    sp.edit().putString("content", content).apply();
                    showCrouton("保存成功");
                } else {
                    showCrouton("请输入内容...");
                }

                break;
            case R.id.btn_get:
                String contentString = sp.getString("content", "没有");
                tvContent.setText(contentString);
                break;
            case R.id.btn_clear:
                sp.edit().clear().apply();
                tvContent.setText("");
                showCrouton("清除成功");
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
