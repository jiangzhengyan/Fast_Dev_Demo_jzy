package com.examle.jiang_yan.fast_develop.activities;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.examle.jiang_yan.fast_develop.R;

import java.util.ArrayList;

/**
 * Created by jiang_yan on 2016/10/20.
 */
public class ActivitySpinner extends BaseActivity {
    private static final String TAG = "ActivitySpinner";

    @Override
    public void initView() {
        setContentView(R.layout.layout_activity_spinner);
        Spinner sp_spinner = (Spinner) findViewById(R.id.sp_spinner);
//        sp_spinner.setBackgroundResource(R.mipmap.ic_launcher);
        sp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //处理选择的点击事件
                Log.e(TAG, "onItemClick: " + "posion:" + position + "   id:" + id);
                Toast.makeText(ActivitySpinner.this, "你选了" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ActivitySpinner.this, "什么也没有选择", Toast.LENGTH_SHORT).show();
            }
        });


        MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter();
        sp_spinner.setAdapter(mySpinnerAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void eventlistener() {

    }

    private class MySpinnerAdapter extends BaseAdapter {
        ArrayList<String> strings;

        public MySpinnerAdapter() {
            strings = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                strings.add("利用adapter适配器的选项 " + i);
            }
        }

        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public String getItem(int position) {
            return strings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText(strings.get(position));
            textView.setTextColor(Color.parseColor("#121212"));
            textView.setTextSize(23);
            textView.setPadding(12, 12, 12, 12);
            return textView;
        }
    }
}
