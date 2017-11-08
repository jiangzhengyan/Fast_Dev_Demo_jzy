package com.examle.jiang_yan.fast_develop.utils;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 补货app异常
 */
public class CrushHandler implements UncaughtExceptionHandler {
    private static CrushHandler instance;
    private String path = "";
    private Context mContext;
    private static final String TAG = "CrushHandler";
    private CrushHandler() {
    }

    public static CrushHandler getInstance() {
        if(instance == null) {
            Class var0 = CrushHandler.class;
            synchronized(CrushHandler.class) {
                instance = new CrushHandler();
            }
        }

        return instance;
    }

    public void setPath(String path) {
        this.path = path + "err" + File.separator;
        File file = new File(this.path);
        if(!file.exists()) {
            file.mkdirs();
        }

    }

    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, "uncaughtException: "+"线程 : "+thread.getName()+"Throwable : "+ex.getMessage().toString());

        (new Thread() {
            public void run() {
                Looper.prepare();
                Builder dialog = new Builder(CrushHandler.this.mContext);
                dialog.setTitle("提示");
                dialog.setMessage("程序出现异常，需要重新启动");
                dialog.setPositiveButton("重启", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = CrushHandler.this.mContext.getPackageManager().getLaunchIntentForPackage(CrushHandler.this.mContext.getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        CrushHandler.this.mContext.startActivity(intent);
                        Process.killProcess(Process.myPid());
                    }
                });
                dialog.setCancelable(false);
                dialog.create().show();
                Looper.loop();
            }
        }).start();
        ex.printStackTrace();
     //保存异常信息
    }



    private void saveSD(String key, String value) {
        try {
            byte[] byteArray = value.getBytes("GBK");
            FileOutputStream fos = new FileOutputStream(this.path + key, false);
            DataOutputStream out = new DataOutputStream(fos);
            out.write(byteArray);
            out.close();
        } catch (IOException var6) {
            ;
        }

    }

    private String getStackTraceText(Throwable ex) {
        try {
            StringWriter e = new StringWriter();
            PrintWriter pw = new PrintWriter(e);
            ex.printStackTrace(pw);
            pw.close();
            return e.toString();
        } catch (Exception var4) {
            ex.printStackTrace();
            return "";
        }
    }

    public void init(Context context) {

            this.mContext = context;
            Thread.setDefaultUncaughtExceptionHandler(this);

    }
}
