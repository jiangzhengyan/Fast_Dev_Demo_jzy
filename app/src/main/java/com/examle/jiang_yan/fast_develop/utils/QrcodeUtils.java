package com.examle.jiang_yan.fast_develop.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * 生成二维码条形码
 */
public class QrcodeUtils {

	static private ImageView sweepIV;
	static private int QR_WIDTH = 200, QR_HEIGHT = 200;

	/**
	 * 生成二维码的
	 * @param url
	 * @return
	 */
	public static Bitmap createQRImage(String url) {
		try {
			// 判断URL合法性
			if (TextUtils.isEmpty(url)) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			// 显示到一个ImageView上面
			// sweepIV.setImageBitmap(bitmap);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 生成条形码
	 * @param ss
	 * @return
	 */
	public static Bitmap createBarCode(String ss){
		//条形码CODE_128
		try {
			BarcodeFormat fomt= BarcodeFormat.CODE_39;
			BitMatrix matrix=new MultiFormatWriter().encode(ss, fomt, 400, 200);
			int width=matrix.getWidth();
			int height=matrix.getHeight();
			int[] pixel=new int[width*height];
			for(int i=0;i<height;i++){
				for(int j=0;j<width;j++){
					if(matrix.get(j,i)) {
//						pixel[i*width+j]=0xFF000000;
						pixel[i * width + j] = 0xFF000000;
					}else {
						pixel[i * width + j] = 0xFFFFFFFF;
					}
				}
			}

			Bitmap bmapp=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
			bmapp.setPixels(pixel, 0, width, 0, 0, width, height);
			return bmapp;
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 保存图片
	 * @param context
	 * @param bitmap
	 */
	public static void saveBitmap(Context context, Bitmap bitmap) {
		String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/";
		File appDir = new File(sdCardDir );
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = "ToastImage" + System.currentTimeMillis() + ".png";
		File f = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(f);
			bitmap.compress(CompressFormat.PNG, 0, fos);
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
	}
}
