package com.examle.jiang_yan.fast_develop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.widget.TextView;
import android.widget.Toast;

import com.examle.jiang_yan.fast_develop.R;
import com.examle.jiang_yan.fast_develop.widgets.CustomWebViewHTML5;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * 自定义封装webview   h5
 * Created by jiang_yan on 2016/9/19.
 */
public class ActivityHTML5WebViewCustomAD extends AppCompatActivity {

    private CustomWebViewHTML5 mWebView;
    //http://www.zttmall.com/Wapshop/Topic.aspx?TopicId=18
    private String ad_url = "http://www.zttmall.com/Wapshop/Topic.aspx?TopicId=18";
    private String title = "百度一下你就知道";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mWebView = new CustomWebViewHTML5(this, ActivityHTML5WebViewCustomAD.this, title, ad_url);
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //准备javascript注入
        mWebView.addJavascriptInterface(new Js2JavaInterface(), "Js2JavaInterface");
        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        } else {
            if (ad_url != null) {
                mWebView.loadUrl(ad_url);
            }
        }
        setContentView(mWebView.getlayout());
    }

    /**
     * 保存状态实例的状态
     *
     * @param outState
     * @param outPersistentState
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (mWebView != null) {
            mWebView.saveState(outState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWebView != null) {
            mWebView.stopLoading();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.doDestory();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if (mWebView != null) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                mWebView.releaseCustomview();
            }
        }
        super.onBackPressed();
    }

    public class Js2JavaInterface {
        private Context context;
        private String TAG = "Js2JavaInterface";

        @JavascriptInterface
        public void showProduct(String productId) {
            Crouton.makeText(ActivityHTML5WebViewCustomAD.this,
                    productId != null ? "点击商品的ID为:" + productId : "点击商品的ID为空",
                    Style.ALERT).show();
        }
    }


}
