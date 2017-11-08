package com.example.addwatermark;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {

    private static final int IMAGE = 111;
    private static final int CARMERA = 222;
    private ImageView mSourImage;
    private ImageView mWartermarkImage;
    String imagePath;
    private Bitmap textBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        mSourImage = (ImageView) findViewById(R.id.sour_pic);
        mWartermarkImage = (ImageView) findViewById(R.id.wartermark_pic);
//        Bitmap sourBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sour_pic);
//        mSourImage.setImageBitmap(sourBitmap);

    }


    /**
     * 添加原图
     *
     * @param view
     */
    public void s1(View view) {

        showGetPic();
//        File cacheDirectory = getCacheDirectory(this
//                , "");
//        Log.e(TAG, "s1: "+cacheDirectory.getName() );
//        Log.e(TAG, "s1: "+cacheDirectory.getPath() );
//        Log.e(TAG, "s1: "+cacheDirectory.getAbsolutePath() );
    }

    /**
     * 展示水印图片
     *
     * @param view
     */
    public void s2(View view) {
        if (TextUtils.isEmpty(imagePath)) {
            Toast.makeText(this, "请选择原图!", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        int width = bm.getWidth();
        int height = bm.getHeight();
        Log.e(TAG, "s2: 宽 :  " + width + "    高:  " + height);
        int sizes = width / 25;
        textBitmap = ImageUtil.drawTextToLeftBottom(this, bm, "2018-12-12", sizes, Color.RED, sizes, sizes);
        saveBitmap(this,textBitmap);
        mWartermarkImage.setImageBitmap(textBitmap);
    }

    /**
     * 保存原图
     *
     * @param view
     */
    public void s3(View view) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        saveBitmap(this, bm);
    }

    /*8
   保存水印图片到相册
     */
    public void s4(View view) {
        saveBitmap(this, textBitmap);
    }


    //2017-2-13 根据网络图片url转换为bitmap
    private Bitmap getNetBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = android.graphics.BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static File getCacheDirectory(Context context,String type) {
        File appCacheDir = getExternalCacheDirectory(context,type);
        if (appCacheDir == null){
            appCacheDir = getInternalCacheDirectory(context,type);
        }

        if (appCacheDir == null){
            Log.e("getCacheDirectory","getCacheDirectory fail ,the reason is mobile phone unknown exception !");
        }else {
            if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
                Log.e("getCacheDirectory","getCacheDirectory fail ,the reason is make directory fail !");
            }
        }
        return appCacheDir;
    }
    public static File getExternalCacheDirectory(Context context,String type) {
        File appCacheDir = null;
        if( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type)){
                appCacheDir = context.getExternalCacheDir();
            }else {
                appCacheDir = context.getExternalFilesDir(type);
            }

            if (appCacheDir == null){// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(),"Android/data/"+context.getPackageName()+"/cache/"+type);
            }

            if (appCacheDir == null){
                Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is sdCard unknown exception !");
            }else {
                if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
                    Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is make directory fail !");
                }
            }
        }else {
            Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return appCacheDir;
    }
    public static File getInternalCacheDirectory(Context context,String type) {
        File appCacheDir = null;
        if (TextUtils.isEmpty(type)){
            appCacheDir = context.getCacheDir();// /data/data/app_package_name/cache
        }else {
            appCacheDir = new File(context.getFilesDir(),type);// /data/data/app_package_name/files/type
        }

        if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
            Log.e("getInternalDirectory","getInternalDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
    }

    /**
     *   存图片,返回图片路径
     * @param context  上下文
     * @param bitmap  要存的图片
     * @param dirName  存图片的文件夹名字
     * @param fileName  图片的名字
     * @return  图片存放的路径的全名
     */
    public static String saveBitmap(Context context, Bitmap bitmap,String dirName,String fileName) {
//        选择的图片路径 : /storage/emulated/0/DCIM/Camera/IMG_20170918_135342.jpg
        String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/";
        File appDir = new File(sdCardDir, "workerImage");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = "workerImage" + System.currentTimeMillis() + ".jpg";
        File f = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 通知图库更新  
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f);
        intent.setData(uri);
        context.sendBroadcast(intent);
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        return f.getPath();
    }


    private void showGetPic() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择原图");
        builder.setCancelable(true);
        builder.setPositiveButton("去相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        });
        builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "暂不支持", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private static final String TAG = "MainActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            Log.e(TAG, "onActivityResult  选择的图片路径 : " + imagePath);
            showSorImage(imagePath);
            c.close();
        }

    }

    private void showSorImage(String imagePath) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        mSourImage.setImageBitmap(bm);
    }

}
