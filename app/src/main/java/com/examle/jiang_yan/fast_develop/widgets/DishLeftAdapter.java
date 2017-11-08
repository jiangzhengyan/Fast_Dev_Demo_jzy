package com.examle.jiang_yan.fast_develop.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;

import java.util.List;

public class DishLeftAdapter extends BaseAdapter {
    //标题
    private List<String> leftStr;
    //标志
    private List<Boolean> flagArray;
    private LayoutInflater inflater;

    public DishLeftAdapter(Context mContext, List<String> leftStr, List<Boolean> flagArray) {
        this.leftStr = leftStr;
        this.flagArray = flagArray;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return leftStr.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            //加载
            convertView = inflater.inflate(R.layout.lv_item_left, parent, false);
            //绑定
            holder.lv_left_item_text = (TextView) convertView.findViewById(R.id.lv_left_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        holder.lv_left_item_text.setText(leftStr.get(position));
        //根据标志位，设置背景颜色
        if (flagArray.get(position)) {
            holder.lv_left_item_text.setBackgroundColor(Color.rgb(255, 255, 255));
        } else {
            holder.lv_left_item_text.setBackgroundColor(Color.TRANSPARENT);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView lv_left_item_text;
    }
}