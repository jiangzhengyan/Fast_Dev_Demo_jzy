package com.examle.jiang_yan.fast_develop.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;
import com.examle.jiang_yan.fast_develop.bean.ProductInfo;
import com.examle.jiang_yan.fast_develop.dao.ProductInfoDAO;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiang_yan on 2016/10/28.
 */
public class ScanResultActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lv_scan_result)
    ListView lvScanResult;

    @Override
    public void initView() {
        setContentView(R.layout.layout_activity_scan_result);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("扫描结果");
    }

    @Override
    public void initData() {
        ProductInfoDAO productInfoDAO = new ProductInfoDAO(this);
        List<ProductInfo> all = productInfoDAO.findAll();
        ScanResultAdapter scanResultAdapter = new ScanResultAdapter(all);
        lvScanResult.setAdapter(scanResultAdapter);
    }

    @Override
    public void eventlistener() {

    }


    /**
     * 扫描结果的adapter
     */
    public class ScanResultAdapter extends BaseAdapter {
        List<ProductInfo> all;

        public ScanResultAdapter(List<ProductInfo> all) {
            this.all = all;
        }

        @Override
        public int getCount() {
            return all.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(ScanResultActivity.this, R.layout.item_scan_result, null);
            TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
            TextView tvSacnContent = (TextView) view.findViewById(R.id.tv_sacn_content);
            tvDate.setText(all.get(position).getScanDate());
            tvSacnContent.setText(all.get(position).getProductInfo());

            return view;
        }
    }
}
