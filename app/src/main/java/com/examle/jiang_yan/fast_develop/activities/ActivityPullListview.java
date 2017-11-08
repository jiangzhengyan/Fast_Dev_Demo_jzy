package com.examle.jiang_yan.fast_develop.activities;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.examle.jiang_yan.fast_develop.R;
import com.examle.jiang_yan.fast_develop.base.BaseAutoLoadMoreAdapter;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableRecyclerView;
import com.jingchen.pulltorefresh.WrapRecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiang_yan on 2016/9/19.
 */
public class ActivityPullListview extends BaseActivity {
    private static final String TAG = "ActivityPullListview";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_recylerview)
    PullableRecyclerView rvRecylerview;
    @Bind(R.id.pull_layout)
    PullToRefreshLayout pullLayout;
    private WrapRecyclerView rvPullableView;
    private ArrayList<String> mData = new ArrayList<>();
    private RectylerViewAdapter mAdapter;
    private int mRequestCount; // 请求次数

    @Override
    public void initView() {
        setContentView(R.layout.layout_pull_listview);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("下拉刷新上拉加载的recylerview");
        pullLayout.setPullUpEnable(false);
        //recylerview
        rvPullableView = (WrapRecyclerView) pullLayout.getPullableView();

        //添加头部布局
        View header = View.inflate(this, R.layout.header, null);
//        rvPullableView.addHeaderView(header);
        //设置属性
        rvPullableView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void initData() {
        //添加模拟数据
        for (int i = 0; i < 12; i++) {
            mData.add("这是第" + i + "条!");
        }
        mAdapter = new RectylerViewAdapter(this, rvPullableView, mData);
        rvPullableView.setAdapter(mAdapter);
    }

    @Override
    public void eventlistener() {
        //下拉刷新的监听
        pullLayout.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                Log.e(TAG, "onRefresh: ");
                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 3000);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                Log.e(TAG, "onLoadMore: ");
            }
        });

        mAdapter.setOnLoadmoreListener(new RectylerViewAdapter.OnLoadmoreListener() {
            @Override
            public void onLoadmore() {
                mRequestCount++;
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (3 == mRequestCount) {
                            // 请求失败时
                            mAdapter.onFailed();
                            return;
                        }
                        if (5 == mRequestCount) {
                            // 没有更多内容时
                            mAdapter.onNothing();
                            return;
                        }
                        int size = mData.size();
                        for (int i = size; i < size + 5; i++) {
                            mData.add("这里是item " + i);
                        }
                        mAdapter.notifyDataChanged();
                        Log.i(TAG, "加载更多成功");
                    }
                }.sendEmptyMessageDelayed(0, 3000);
            }
        });
        mAdapter.setOnItemClickListener(new RectylerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ActivityPullListview.this,
                        " Click on " + mData.get(position),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(
                        ActivityPullListview.this,
                        "LongClick on "
                                + mData.get(position),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    class RectylerViewAdapter extends BaseAutoLoadMoreAdapter<String> {
        public RectylerViewAdapter(Context context, WrapRecyclerView recyclerView, ArrayList<String> data) {
            super(context, recyclerView, data);
        }

        @Override
        public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            View inflate = View.inflate(ActivityPullListview.this, R.layout.item_recylerview, null);
            MyBaseViewHolder myBaseViewHolder = new MyBaseViewHolder(inflate);
            return myBaseViewHolder;
        }

        @Override
        public int getItemHeight(RecyclerView.ViewHolder holder) {
            holder.itemView.measure(0, 0);
            return holder.itemView.getMeasuredHeight();
        }

        @Override
        public void onBindBaseViewHolder(BaseViewHolder holder, int position) {
            MyBaseViewHolder myBaseViewHolder = (MyBaseViewHolder) holder;
            myBaseViewHolder.tv_name.setText(mData.get(position));
        }

        class MyBaseViewHolder extends BaseViewHolder {

            TextView tv_name;

            public MyBaseViewHolder(View v) {
                super(v);
                tv_name = (TextView) v.findViewById(R.id.tv_name);
            }
        }
    }
}
