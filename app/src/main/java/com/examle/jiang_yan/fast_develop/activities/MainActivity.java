package com.examle.jiang_yan.fast_develop.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;
import com.examle.jiang_yan.fast_develop.eventlistener.OnItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {


    private static final String TAG = "MainActivity";
    @Bind(R.id.rv_recylerview)
    RecyclerView rvRecylerview;
    private MainAdapter mMainAdapter;
    private ArrayList<String> mLists = new ArrayList<>();
    private Class[] mClassItems;

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //全屏,状态栏在全屏之下
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //全屏,状态栏在全屏之上
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void initData() {
        //填充列表数据
        String[] stringArray = getResources().getStringArray(R.array.main_list);
        for (int i = 0; i < stringArray.length; i++) {
            mLists.add(stringArray[i]);
        }
/**
 * 点击事件响应的activity
 */
        mClassItems = new Class[]{ActivityGalleryIndicator.class, ActivityPullListview.class,
                ActivitySPCache.class, ActivityCrashTest.class
                , ActivityTranslucent.class, ActivityMVPTest.class,
                ActivityAnnotationsTest_.class, ActivityAVLoadingIndicator.class,
                ActivityEventBusTest.class, ActivityTextDrawablesTest.class,
                ActivityHTML5WebViewCustomAD.class, ActivityBaseAdapterTest.class,
                ActivityVolleyTest.class, ActivityMainFrame.class,
                ActivityRecyclerDemo.class, ActivityCardView.class, ActivityViewGragHelper.class,
                ActivityMainInfo.class, ActivityTabLayout.class, ActivityOkhttpDemo.class, ActivitySelectPic.class,
                ActivityJIANPAN.class, ActivityErweima.class, ActivityImageSwitcher.class, ActivitySpinner.class, ActivityTextDemo.class,ActivityOrder.class
        };


        //垂直的方向,默认的
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //Gridlayout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rvRecylerview.setLayoutManager(gridLayoutManager);
        mMainAdapter = new MainAdapter(this, mLists);
        rvRecylerview.setAdapter(mMainAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    /**
     * 条目的点击事件
     */
    @Override
    public void eventlistener() {

        mMainAdapter.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void itemClickListenr(View view, int position) {
                Log.e(TAG, "itemClickListenr: " + position);
                if (position != 4) {
                    openActivity(mClassItems[position]);
                } else {
                    new AlertDialog.Builder(MainActivity.this).setTitle("" +
                            "请选择使用的:").setSingleChoiceItems(new String[]{"系统", "第三方的"}
                            , 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, ActivityTranslucent.class);
                                    intent.putExtra("mode", which);
                                    Log.e(TAG, "onClick: " + which);
                                    openActivityByIntent(intent);
                                    dialog.dismiss();
                                }
                            }).create().show();

                }

            }

            @Override
            public void itemLongClickListenr(View view, int position) {
                Log.e(TAG, "itemLongClickListenr: " + position);
            }
        });

    }


    /**
     * 数据适配器
     */
    class MainAdapter extends RecyclerView.Adapter {
        private ArrayList<String> mLists;
        private Context mContext;

        public MainAdapter(Context mContext, ArrayList<String> mLists) {
            this.mLists = mLists;
            this.mContext = mContext;

        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = View.inflate(mContext, R.layout.item_main, null);

            MainViewHolder mainViewHolder = new MainViewHolder(inflate);


            return mainViewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof MainViewHolder) {
                ((MainViewHolder) holder).mtvMainItem.setText("" + (position + 1) + ":" + mLists.get(position));

                //添加点击条目的点击事件
                //长按事件------点击事件
                if (listener != null) {
                    ((MainViewHolder) holder).mRlMainItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.itemClickListenr(((MainViewHolder) holder).mRlMainItem, position);
                        }
                    });
                    ((MainViewHolder) holder).mRlMainItem.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            listener.itemLongClickListenr(((MainViewHolder) holder).mRlMainItem, position);
                            return true;
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return mLists.size();


        }

        OnItemClickListener listener;

        /**
         * 点击事件
         *
         * @param listener
         */
        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }


        /**
         * ViewHolder
         */
        class MainViewHolder extends RecyclerView.ViewHolder {

            private TextView mtvMainItem;
            private RelativeLayout mRlMainItem;

            public MainViewHolder(View itemView) {
                super(itemView);
                mtvMainItem = (TextView) itemView.findViewById(R.id.tv_main_item);
                mRlMainItem = (RelativeLayout) itemView.findViewById(R.id.rl_main_item);
            }
        }
    }

}
