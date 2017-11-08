package com.examle.jiang_yan.fast_develop.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiang_yan on 2016/10/9.
 */
public class ActivitySelectPic extends BaseActivity {
    private static final String TAG = "PIC";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_select)
    TextView tvSelect;
    @Bind(R.id.iv_pic)
    ImageView ivPic;

    @Override
    public void initView() {
        setContentView(R.layout.layout_activity_select_pic);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("从相册选择照片");
    }

    @Override
    public void initData() {

    }

    @Override
    public void eventlistener() {

    }


    @OnClick({R.id.tv_title, R.id.tv_select, R.id.iv_pic})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select:
                selectPic();//选择相册照片
                break;
        }
    }

    /**
     * 从手机相册选择照片
     */
    private void selectPic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(intent, "请你选择照片");
        startActivityForResult(chooserIntent, 1);
    }

    /**
     * 返回选择相册照片的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            //Intent { dat=content://media/external/images/media/124893 flg=0x1 (has extras) }
            ContentResolver contentResolver = getContentResolver();
            Uri uriData = data.getData();
            Log.e(TAG, "onActivityResult: " + uriData);
//            Cursor query(Uri, String[], String, String[], String)
            Cursor cursor = contentResolver.query(uriData, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                //照片的路径
                String pic_patch = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Log.e(TAG, "onActivityResult: " + pic_patch);
                Bitmap bitmap = BitmapFactory.decodeFile(pic_patch);
                ivPic.setImageBitmap(bitmap);
            }
            //关闭cursor
            if (cursor!=null){
                cursor.close();
            }
        }
    }
}
