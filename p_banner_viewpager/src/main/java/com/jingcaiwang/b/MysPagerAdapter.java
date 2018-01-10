package com.jingcaiwang.b;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.jingcaiwang.Ad;

import java.util.ArrayList;

public class BPagerAdapter extends MyPagerAdapter {
    protected Context context;
    protected ArrayList<Ad> list;

    public BPagerAdapter(Context context, ArrayList<Ad> list) {
        super();
        this.context = context;
        this.list = list;
    }

    /**
     * 返回多少页
     */
    @Override
    public int getCount() {
//		return list.size();
        return Integer.MAX_VALUE;
    }

    /**
     * 用来判断instantiateItem方法返回的对象是否是View对象
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 是用来创建Page的，相当于BaseAdapter的getView方法，
     * 一般在该方法中，需要加载View对象，给VIew绑定数据，返回View
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//		Log.e("tag", "position:"+position);  实例化 item
        //1.加载View对象
        ImageView imageView = new ImageView(context);
        //设置图片去铺满整个ImageView
        imageView.setScaleType(ScaleType.FIT_XY);
        //2.给ImageView绑定数据
//		position        list=5
//		   5     5%5=0     0
//		   6	 6%5=1	   1
//		   7	 7%5=2	   2
//		   8			   3
        Ad ad = list.get(position % list.size());
        imageView.setImageResource(ad.getIconResId());

        //注意：需要将imageView添加到ViewPager中
        container.addView(imageView);

        return imageView;
    }

    /**
     * 用来销毁每一页的，从ViewPager中移除每一页的View对象
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
