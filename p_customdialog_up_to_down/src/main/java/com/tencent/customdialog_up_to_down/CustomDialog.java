package com.tencent.customdialog_up_to_down;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by jiangzhengyan on  2017-5-15
 */
public class CustomDialog {
    private Context mContext;
    private int mTheme;
    private Dialog mDialog;


    public CustomDialog(Context mContext, int theme) {
        this.mContext = mContext;
        this.mTheme = theme;
        initDialog();
    }


    private void initDialog() {
        mDialog = new Dialog(mContext, mTheme);
        //获得dialog的window窗口
        Window window = mDialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.ToUp_dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

    }

    //将自定义布局加载到dialog上
    public void setContentView(View contentView) {
        if (mDialog != null) {
            mDialog.setContentView(contentView);
        }
    }

    //显示  dialog
    public void show() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    //dismiss
    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

}
