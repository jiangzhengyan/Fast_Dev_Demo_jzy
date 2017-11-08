package com.examle.jiang_yan.fast_develop.activities;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;
import com.examle.jiang_yan.fast_develop.widgets.DishHaveHeaderListView;
import com.examle.jiang_yan.fast_develop.widgets.DishLeftAdapter;
import com.examle.jiang_yan.fast_develop.widgets.DishRightAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by jiang_yan on 2017/6/27.
 */

public class ActivityOrder extends BaseActivity{

//    @Bind(R.id.tv_title)
//    TextView tvTitle;
//左边的ListView
private ListView lv_left;
    //左边ListView的Adapter
    private DishLeftAdapter dishLeftAdapter;
    //左边的数据存储
    private List<String> leftStr;
    //左边数据的标志
    private List<Boolean> flagArray;
    //右边的ListView
    private DishHaveHeaderListView lv_right;
    //右边的ListView的Adapter
    private DishRightAdapter rightAdapter;
    //右边的数据存储
    private List<List<String>> rightStr;
    //是否滑动标志位
    private Boolean isScroll = false;
    @Override
    public void initView() {
        setContentView(R.layout.layout_activity_elema_order);

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("饿了吗");



        //初始化控件
        initViews();
        //初始化数据
        initDatas();
        lv_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isScroll = false;
                for (int i = 0; i < leftStr.size(); i++) {
                    if (i == position) {
                        flagArray.set(i, true);
                    } else {
                        flagArray.set(i, false);
                    }
                }
                //更新
                dishLeftAdapter.notifyDataSetChanged();
                int rightSection = 0;
                for (int i = 0; i < position; i++) {
                    //查找
                    rightSection += rightAdapter.getCountForSection(i) + 1;
                }
                //显示到rightSection所代表的标题
                lv_right.setSelection(rightSection);
            }
        });
        lv_right.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_right.getLastVisiblePosition() == (lv_right.getCount() - 1)) {
                            lv_left.setSelection(ListView.FOCUS_DOWN);
                        }
                        // 判断滚动到顶部
                        if (lv_right.getFirstVisiblePosition() == 0) {
                            lv_left.setSelection(0);
                        }
                        break;
                }

            }

            int y = 0;
            int x = 0;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    for (int i = 0; i < rightStr.size(); i++) {
                        if (i == rightAdapter.getSectionForPosition(lv_right.getFirstVisiblePosition())) {
                            flagArray.set(i, true);
                            //获取当前标题的标志位
                            x = i;
                        } else {
                            flagArray.set(i, false);
                        }
                    }
                    if (x != y) {
                        dishLeftAdapter.notifyDataSetChanged();
                        //将之前的标志位赋值给y，下次判断
                        y = x;
                    }
                } else {
                    isScroll = true;
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


    private void initViews() {
        lv_left = (ListView) findViewById(R.id.lv_left);
        leftStr = new ArrayList<>();
        flagArray = new ArrayList<>();
        dishLeftAdapter = new DishLeftAdapter(  this, leftStr, flagArray);
        lv_left.setAdapter(dishLeftAdapter);
        lv_right = (DishHaveHeaderListView) findViewById(R.id.lv_right);
        rightStr = new ArrayList<List<String>>();
        rightAdapter = new DishRightAdapter( this, leftStr, rightStr);
        lv_right.setAdapter(rightAdapter);
    }

    private void initDatas() {
        //左边相关数据

        leftStr.add("面食类");
        leftStr.add("盖饭");
        leftStr.add("寿司");
        leftStr.add("烧烤");
        leftStr.add("酒水");
        leftStr.add("凉菜");
        leftStr.add("小吃");
        leftStr.add("粥");
        leftStr.add("菜类");
        leftStr.add("汤类");
        flagArray.add(true);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        dishLeftAdapter.notifyDataSetChanged();

        //右边相关数据
        //面食类
        List<String> food1 = new ArrayList<>();
        food1.add("热干面");
        food1.add("臊子面");
        food1.add("烩面");
        food1.add("干面");
        //盖饭
        List<String> food2 = new ArrayList<>();
        food2.add("番茄鸡蛋");
        food2.add("红烧排骨");
        food2.add("活塞排骨");
        food2.add("农家小炒肉");
        //寿司
        List<String> food3 = new ArrayList<>();
        food3.add("芝士");
        food3.add("丑小丫");
        food3.add("金枪鱼");
        food3.add("利鱼");
        //烧烤
        List<String> food4 = new ArrayList<>();
        food4.add("羊肉串");
        food4.add("烤鸡翅");
        food4.add("烤羊排");
        food4.add("烤狗排");
        //酒水
        List<String> food5 = new ArrayList<>();
        food5.add("长城干红");
        food5.add("闯天涯");
        food5.add("燕京鲜啤");
        food5.add("青岛鲜啤");
        //凉菜
        List<String> food6 = new ArrayList<>();
        food6.add("拌粉丝");
        food6.add("大拌菜");
        food6.add("菠菜花生");
        //小吃
        List<String> food7 = new ArrayList<>();
        food7.add("小食组");
        food7.add("紫薯");
        //粥
        List<String> food8 = new ArrayList<>();
        food8.add("小米粥");
        food8.add("大米粥");
        food8.add("南瓜粥");
        food8.add("玉米粥");
        food8.add("紫米粥");
        //包子类
        List<String> food9 = new ArrayList<>();
        food9.add("菜包子");
        food9.add("肉包子");
        food9.add("好吃包子");
        food9.add("没有包子");
        food9.add("hi包子");

        //汤类
        List<String> food10 = new ArrayList<>();
        food10.add("西汤");
        food10.add("南瓜汤");
        food10.add("我喝汤");
        food10.add("羊汤");
        rightStr.add(food1);
        rightStr.add(food2);
        rightStr.add(food3);
        rightStr.add(food4);
        rightStr.add(food5);
        rightStr.add(food6);
        rightStr.add(food7);
        rightStr.add(food8);
        rightStr.add(food9);
        rightStr.add(food10);
        rightAdapter.notifyDataSetChanged();
    }
}
