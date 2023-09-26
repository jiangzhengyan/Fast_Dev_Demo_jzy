package com.jingcaiwang.slidedeletelayout;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //寻找listview
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.e(TAG, "onItemClick: " + position);
            }
        });
        initData();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
    }

    /**
     * listview的adapter
     */
    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 69;
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
            ViewHoldr viewHoldr;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_swipe, null);
                  viewHoldr = new ViewHoldr(convertView);
                  convertView.setTag(viewHoldr);
            }else {
                viewHoldr = (ViewHoldr) convertView.getTag();
            }
            viewHoldr.tv_name.setText("进老唐的第:   "+ position);
            return convertView;
        }
    }


    class ViewHoldr{
        TextView tv_name;
        public ViewHoldr(View view){
            tv_name=(TextView) view.findViewById(R.id.tv_name);
        }


    }
}
