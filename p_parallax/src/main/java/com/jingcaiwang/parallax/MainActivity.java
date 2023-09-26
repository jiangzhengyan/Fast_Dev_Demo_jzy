package com.jingcaiwang.parallax;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParallaxListView lv_list = (ParallaxListView) findViewById(R.id.lv_list);
        View header = View.inflate(this, R.layout.parallax_header, null);
        ImageView iv_parallax = (ImageView) header.findViewById(R.id.iv_parallax);
        lv_list.setParallaxHeaderImage(iv_parallax);
        lv_list.addHeaderView(header);
        lv_list.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 30;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(MainActivity.this);
            textView.setText(position+" ");
            textView.setPadding(15, 45, 15, 45);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }

    }

}
