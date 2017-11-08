package com.examle.jiang_yan.fast_develop.activities;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;

/**
 * Created by jiang_yan on 2016/11/21.
 */

public class ActivityTextDemo extends BaseActivity {
    private static final String TAG = "MainActivity";
    private CharSequence mTempText;
    private int mSelectionStart;
    private int mSelectionEnd;
    private int mInputNumLimit = 3;

    @Override
    public void initView() {
        setContentView(R.layout.layout_activity_textdemo);
        setContentView(R.layout.activity_main);
        final EditText et_content = (EditText) findViewById(R.id.et_content);
        final TextView tv_num = (TextView) findViewById(R.id.tv_num);

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTempText = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = mInputNumLimit - s.length();
                tv_num.setText("还可以输入:" + number + "个字");
                mSelectionStart = et_content.getSelectionStart();
                mSelectionEnd = et_content.getSelectionEnd();
                if (mTempText.length() > mInputNumLimit) {
                    s.delete(mSelectionStart - 1, mSelectionEnd);
                    int tempSelection = mSelectionStart;
                    et_content.setText(s);
                    et_content.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void eventlistener() {

    }
}
