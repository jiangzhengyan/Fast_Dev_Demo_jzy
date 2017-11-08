package com.examle.jiang_yan.fast_develop.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.examle.jiang_yan.fast_develop.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiang_yan on 2016/9/19.
 */
public class ActivityTextDrawablesTest extends BaseActivity {
    String[] mTitles = new String[]{"SAMPLE_RECT"
            , "SAMPLE_ROUND_RECT", "SAMPLE_ROUND"
            , "SAMPLE_RECT_BORDER", "SAMPLE_ROUND_RECT_BORDER"
            , "SAMPLE_ROUND_BORDER"
            , "SAMPLE_MULTIPLE_LETTERS",
            "SAMPLE_FONT", "SAMPLE_SIZE", "SAMPLE_ANIMATION", "SAMPLE_MISC"};
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lv_textdrawable)
    ListView lvTextdrawable;
    private MyTextDrawableAdapter myTextDrawableAdapter;

    @Override
    public void initView() {
        setContentView(R.layout.layout_activity_text);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("TextDrawable");
    }

    @Override
    public void initData() {
        //设置适配器
        if (myTextDrawableAdapter == null) {
            myTextDrawableAdapter = new MyTextDrawableAdapter(mTitles);
            lvTextdrawable.setAdapter(myTextDrawableAdapter);
        } else {
            myTextDrawableAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void eventlistener() {

    }

    class MyTextDrawableAdapter extends BaseAdapter {
        private String[] mTitles;

        public MyTextDrawableAdapter(String[] mTitles) {
            this.mTitles = mTitles;
        }

        @Override
        public int getCount() {
            return mTitles.length;
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
            ColorGenerator mGenerator = ColorGenerator.DEFAULT;
             //颜色处理工具
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_text_drawable, null);
            }
            ViewHolder holder = ViewHolder.getHolder(convertView);
            holder.lvItemText.setText(mTitles[position]);
            TextDrawable drawable = null;
            //设置文字数据
            switch (position) {
                case 0:  //SAMPLE_RECT
                    drawable = TextDrawable.builder().buildRect("R", Color.BLUE);
                    break;
                case 1:  //SAMPLE_ROUND_RECT
                    drawable = TextDrawable.builder().buildRoundRect("S", Color.CYAN, 10);
                    break;
                case 2:  //SAMPLE_ROUND
                    drawable = TextDrawable.builder().buildRound("圆", Color.LTGRAY);
                    break;
                case 3:  //SAMPLE_RECT_BORDER
                    drawable = TextDrawable.builder().beginConfig()
                            .withBorder(5)
                            .endConfig()
                            .buildRect("粗", Color.RED);
                    break;
                case 4:  //SAMPLE_ROUND_RECT_BORDER
                    drawable = TextDrawable.builder()
                            .beginConfig()
                            .withBorder(5)
                            .endConfig()
                            .buildRoundRect("S", Color.argb(220, 122, 122, 1), 10);
                    break;
                case 5:  //SAMPLE_ROUND_BORDER
                    drawable = TextDrawable.builder()
                            .beginConfig().withBorder(5).endConfig()
                            .buildRound("圆", Color.LTGRAY);
                    break;
                case 6:  //SAMPLE_MULTIPLE_LETTERS
                    drawable = TextDrawable.builder()
                            .beginConfig()
                            .fontSize(40)
                            .toUpperCase()
                            .endConfig()
                            .buildRect("AK", mGenerator.getColor("AK"));
                    break;
                case 7:  //SAMPLE_FONT
                    drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(Color.BLACK)
                            .useFont(Typeface.SERIF)
                            .bold()
                            .toUpperCase()
                            .endConfig()
                            .buildRect("a", Color.RED);
                    break;
                case 8:  //SAMPLE_SIZE
                    drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(Color.BLACK)
                            .fontSize(30) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()
                            .buildRect("a", Color.RED);
                    break;
                case 9:  //SAMPLE_ANIMATION
                    TextDrawable.IBuilder builder = TextDrawable.builder()
                            .rect();
                    AnimationDrawable animationDrawable = new AnimationDrawable();
                    for (int i = 'A'; i < 'Z'; i++) {
                        TextDrawable frame = builder.build(""+(char)i, mGenerator.getRandomColor());
                        animationDrawable.addFrame(frame, 1000);
                    }
                    animationDrawable.setOneShot(false);
                    animationDrawable.start();
                    holder.lvItemImage.setImageDrawable(animationDrawable);
                    break;
                case 10: //SAMPLE_MISC
                    drawable = TextDrawable.builder()
                            .buildRect("M", mGenerator.getColor("Misc"));
                    break;
            }
            //设置文字
            if (drawable != null)
                holder.lvItemImage.setImageDrawable(drawable);
            return convertView;
        }
    }

    public static class ViewHolder {

        private final ImageView lvItemImage;
        private final TextView lvItemText;

        public ViewHolder(View convertView) {
            lvItemImage = (ImageView) convertView.findViewById(R.id.lv_item_img);
            lvItemText = (TextView) convertView.findViewById(R.id.lv_item_text);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }

}
