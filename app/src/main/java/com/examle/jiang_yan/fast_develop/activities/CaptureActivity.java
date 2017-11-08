package com.examle.jiang_yan.fast_develop.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.examle.jiang_yan.fast_develop.R;
import com.examle.jiang_yan.fast_develop.dao.ProductInfoDAO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.DecodeFormatManager;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.BitmapLuminanceSource;
import com.zxing.view.RGBLuminanceSource;
import com.zxing.view.ViewfinderView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by jiang_yan on 2016/10/10.
 */

public class CaptureActivity extends AppCompatActivity implements Callback {

    private static final String TAG = "CaptureActivity";
    private static final long VIBRATE_DURATION = 200L;
    @Bind(R.id.preview_view)
    SurfaceView previewView;
    @Bind(R.id.viewfinder_view)
    ViewfinderView viewfinderView;
    @Bind(R.id.btn_back)
    Button btnBack;
    @Bind(R.id.scan_title)
    TextView scanTitle;
    @Bind(R.id.prompt1)
    TextView prompt1;
    @Bind(R.id.prompt2)
    TextView prompt2;
    @Bind(R.id.photo)
    Button photo;
    @Bind(R.id.flash)
    Button flash;
    @Bind(R.id.myqrcode)
    Button myqrcode;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private boolean isOpen;
    private String photoPath;
    private ProgressDialog mProgress;
    private Bitmap scanBitmap;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private boolean playBeep;
    private boolean vibrate;
    private CaptureActivityHandler handler;
    private MediaPlayer mediaPlayer;
    private static final float BEEP_VOLUME = 0.50f;
    private Parameters parameters;


    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //成功
                    mProgress.dismiss();
                    String resultString = msg.obj.toString();
                    Log.e(TAG, "扫描结果: " + resultString);
                    if (resultString.equals("")) {
                        Toast.makeText(CaptureActivity.this, "扫描失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(CaptureActivity.this, ResultActivity.class);
                        intent.putExtra("result", resultString);
                        startActivity(intent);
                    }
                    break;
                case 2:
                    //扫描失败
                    mProgress.dismiss();
                    Toast.makeText(CaptureActivity.this, "解析错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private AlertDialog.Builder builderDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_activity_capture);
        isOpen = false;
        ButterKnife.bind(this);
        CameraManager.init(getApplication());//初始化摄像头
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        //扫描结果的对话框
        builderDialog = new AlertDialog.Builder(this,3);
    }

    /**
     * 全部的点击事件
     *
     * @param view
     */
    @OnClick({R.id.btn_back, R.id.photo, R.id.flash, R.id.myqrcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                //返回
                finish();
                break;
            case R.id.photo:
                pickPictureFromAblum();
                break;
            case R.id.flash:
                //灯
                turnLight();
                break;
            case R.id.myqrcode:
                Intent intent = new Intent(CaptureActivity.this, MyQrActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 打开闪关灯
     */
    private void turnLight() {
        if (!isOpen) {
            parameters = CameraManager.get().camera.getParameters();
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            CameraManager.get().camera.setParameters(parameters);
            isOpen = true;
            Drawable dr = this.getResources().getDrawable(R.drawable.qrcode_scan_btn_flash_down);
            flash.setBackgroundDrawable(dr);
        } else {
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            CameraManager.get().camera.setParameters(parameters);
            isOpen = false;
            Drawable dr = this.getResources().getDrawable(R.drawable.qrcode_scan_btn_flash_nor);
            flash.setBackgroundDrawable(dr);
        }
    }

    /**
     * 从相册中选择照片进行扫描,打开手机相册
     */
    private void pickPictureFromAblum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(intent, "选择二维码照片");
        this.startActivityForResult(wrapperIntent, 1);
    }

    /**
     * //得到返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case 1:
                    //获取照片的路径
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        Log.e(TAG, "二维码路径: " + photoPath);
                    }
                    cursor.close();
                    //创建扫描进度
                    mProgress = new ProgressDialog(this);
                    mProgress.setMessage("正在扫描");
                    mProgress.setCancelable(false);
                    mProgress.show();

                    //创建子线程进行扫描
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //得到扫描结果,通过handler发送到主线程
                            Result result = analyzeBitmap(photoPath);
                            Log.e(TAG, "run: " + result);
                            if (result != null) {
                                Message m = mHandler.obtainMessage();
                                m.what = 1;
                                m.obj = result.getText();
                                mHandler.sendMessage(m);
                            } else {
                                Message m = mHandler.obtainMessage();
                                m.what = 2;
                                m.obj = "Scan failed!";
                                mHandler.sendMessage(m);
                            }
                        }
                    }).start();

                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 扫描二维码图片的方法
     *
     * @param path
     * @return
     */
    private Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); // 设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 100);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析二维码图片工具类
     *
     * @param path
     */
    public Result analyzeBitmap(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        /**
         * 首先判断图片的大小,若图片过大,则执行图片的裁剪操作,防止OOM
         */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap mBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 400);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        mBitmap = BitmapFactory.decodeFile(path, options);

        MultiFormatReader multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        // 设置继续的字符编码格式为UTF8
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(mBitmap))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "analyzeBitmap: " + rawResult);
        if (rawResult != null) {
            return rawResult;
        } else {
            return null;
        }
    }

    /**
     * 返回handler
     *
     * @return
     */
    public Handler getHandler() {
        return handler;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    /**
     * 处理二维码
     *
     * @param result
     * @param barcode
     */
    @SuppressLint("NewApi")
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        String msg = result.getText();
        Intent intent = new Intent(CaptureActivity.this, ResultActivity.class);
        if (msg == null || "".equals(msg)) {
            msg = "无法识别";
        }
        intent.putExtra("result", msg);
//        startActivity(intent);
        Log.e(TAG, "扫描结果: " + msg);
        //在这里可以实现连续扫描

        showResultAndSave(msg);

    }
    ProductInfoDAO productInfoDAO = new ProductInfoDAO(CaptureActivity.this);

    private void showResultAndSave(final String msg) {
        if (builderDialog!=null&&builderDialog.create().isShowing() ){
            builderDialog.create().dismiss();
        }
        builderDialog.setMessage(msg).setTitle("扫描结果").setPositiveButton("存储", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确定
                if (productInfoDAO!=null){
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(new Date());
                    boolean insert = productInfoDAO.insert(msg,currentDate);
                    if (insert){
                        Crouton.makeText(CaptureActivity.this,"存储成功", Style.ALERT).show();
                    }else {
                        Crouton.makeText(CaptureActivity.this,"存储失败", Style.ALERT).show();
                    }
                }

                handler.restartPreviewAndDecode();

            }
        }).setNegativeButton("放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消
                dialog.dismiss();
                Crouton.makeText(CaptureActivity.this,"存储取消", Style.ALERT).show();
                handler.restartPreviewAndDecode();
            }
        }).create().show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = previewView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        //判断手机是否是调成了不是响铃模式,若不是响铃模式就不播放声音了.
        //有点不太妥,即使有的手机是静音模式,但是声音一样是有的,比如魅族的m2
        //判断声音的大小比较合适
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//            playBeep = false;
//        }
        if (audioService.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inactivityTimer.shutdown();
    }

    /**
     * 初始化声音震动
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            //声音资料
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beeegun);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    /**
     * 声音播放完成的监听
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 播放声音和震动
     */
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * 初始化相机
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }
}
