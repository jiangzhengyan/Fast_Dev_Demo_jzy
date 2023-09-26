package com.examle.jiang_yan.fast_develop.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import com.examle.jiang_yan.fast_develop.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 针对带https的接口不能访问的问题
 */
public class Https_Perssion_TrustAllHosts extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crush);

		//子线程请求网络
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.err.println("========");
				final String fStrin = get(url);
				System.err.println("========" + fStrin);
		 
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(Https_Perssion_TrustAllHosts.this, fStrin,
								Toast.LENGTH_LONG).show();
					}
				});

			}
		}).start();

	}

	String url = "https://hhap.bjyada.com/frontRS_sit/v1/firms/ATWE/devices/P2L01000000F7000101/sign";

	public static String get(String url) {
		String result = null;
		try {
			URL url_ = new URL(url);
			if (url_.getProtocol().toLowerCase().equals("https")) {
				trustAllHosts();// 信任所有
			}
		} catch (MalformedURLException e1) {

			e1.printStackTrace();
		}

		try {
			HttpGet httpRequest = new HttpGet(url);
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 信任所有主机-对于任何证书都不做检查
	 */
	@SuppressLint("TrulyRandom")
	private static void trustAllHosts() {
		 
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
