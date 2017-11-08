package com.examle.jiang_yan.fast_develop.widgets;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Browser;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examle.jiang_yan.fast_develop.R;
import com.examle.jiang_yan.fast_develop.appdevelop.App;
import com.examle.jiang_yan.fast_develop.utils.StrUtils;

import java.util.ArrayList;

/**
 * 自动以webview
 * Created by jiang_yan on 2016/9/22.
 */

public class CustomWebViewHTML5 extends WebView {
    private static final String TAG1 = "CustomWebViewHTML5";
    private  String mUrl;
    private   String mTitle;
    private   Activity mActivity;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    private View mCustomView;
    private FrameLayout mLayout;
    private FrameLayout mBrowserFrameLayout;
    private LinearLayout wv_imgbtn_back;
    private TextView wv_tv_title;
    private FrameLayout mContentView;
    private FrameLayout mCustomViewContainer;
    private FrameLayout frame_progress;
    private TextView webview_tv_progress;
    private Context mContext;
    private MyWebChromeClient mWebChromeClient;
    private MyWebViewClient mWebViewClient;
    private boolean isRefresh = false; // 是否旋转

    public CustomWebViewHTML5(Context context) {
        super(context);
        init(context);
    }
    public CustomWebViewHTML5(Context context, Activity activity) {
        super(context);
        mActivity = activity;
        init(context);
    }
    public CustomWebViewHTML5(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }
    public CustomWebViewHTML5(Context context,Activity activity,String pTitle,String pUrl) {
        super(context);
        mActivity = activity;
        this.mTitle=pTitle;
        this.mUrl=pUrl;
        init(context);
    }
    public CustomWebViewHTML5(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public CustomWebViewHTML5(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init(context);

    }

    //初始化
    private void init(Context context) {
        mContext = context;
        mLayout = new FrameLayout(context);
        //自定义的布局
        mBrowserFrameLayout = (FrameLayout) LayoutInflater.from(context).
                inflate(R.layout.common_custom_screen, null);
        //返回
        wv_imgbtn_back = (LinearLayout) mBrowserFrameLayout.findViewById(R.id.top_bar_linear_back);
        //标题
        wv_tv_title = (TextView) mBrowserFrameLayout.findViewById(R.id.top_bar_title);
        //显示内容
        mContentView = (FrameLayout) mBrowserFrameLayout
                .findViewById(R.id.main_content);
        //全屏显示
        mCustomViewContainer = (FrameLayout) mBrowserFrameLayout
                .findViewById(R.id.fullscreen_custom_content);
        //加载的旋转动画
        frame_progress = (FrameLayout) mBrowserFrameLayout
                .findViewById(R.id.frame_progress);
        //正在加载的文字描述
        webview_tv_progress = (TextView) frame_progress
                .findViewById(R.id.webview_tv_progress);

        //把布局添加到界面
        FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mLayout.addView(mBrowserFrameLayout, COVER_SCREEN_PARAMS);
        //自定义WebChromeClient,WebViewClient
        mWebChromeClient = new MyWebChromeClient();
        mWebViewClient = new MyWebViewClient();
        setWebChromeClient(mWebChromeClient);
        setWebViewClient(mWebViewClient);
        //相关设置
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);//开启DOM
        webSettings.setDefaultTextEncodingName("utf-8");
        //web页面处理
        webSettings.setAllowFileAccess(true);//支持文件流
//        webSettings.setSupportZoom(true);//支持缩放
//        webSettings.setBuiltInZoomControls(true);//支持缩放
        webSettings.setUseWideViewPort(true);//调整到适合webview大小
        webSettings.setLoadWithOverviewMode(true);//调整到时候webview大小
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);//屏幕自适应网页,若没有这个在低分辨率手机可能会显示异常
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //为提高网页加载速度,暂时阻塞图片加载,网页加载好了,再进行图片的加载
        webSettings.setBlockNetworkImage(true);
        //开启缓存机制
        webSettings.setAppCacheEnabled(true);

        //根据网络状态进行连接
        switch (StrUtils.getAPNType(context)){
            case   StrUtils.WIFI :
            //wifi,设置无缓存
                webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            break;

            default:
                //带缓存
                webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            break;
        }
        mContentView.addView(this);

        //返回
        wv_imgbtn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAdWebPage();
            }
        });

    }

    /**
     * 关闭网页面
     */
    private void closeAdWebPage() {
        if (CustomWebViewHTML5.this.canGoBack()){
            CustomWebViewHTML5.this.goBack();
            return;
        }
        stopLoading();
        freeMemory();
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.move_pop_in,
                R.anim.move_pop_out);
    }

    /**
     * 按键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断是否是返回键
        if (keyCode==KeyEvent.KEYCODE_BACK
                &&CustomWebViewHTML5.this.canGoBack()){
            CustomWebViewHTML5.this.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public FrameLayout getlayout() {
        return  mLayout;
    }
    public boolean inCustomView(){
        return mCustomView!=null;
    }

    /**
     * 释放webview
     */
    public void releaseCustomview(){
    if (mWebChromeClient!=null){
        mWebChromeClient.onHideCustomView();
    }
    stopLoading();
}

    /**
     * 销毁webview
     */
    public void doDestory() {
        clearView();
        freeMemory();
        destroy();
    }

    /**
     * 自定义WebChromeClient
     */
    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            CustomWebViewHTML5.this.setVisibility(GONE);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomViewContainer.addView(view);
            mCustomView = view;

            mCustomViewCallback = callback;
            mCustomViewContainer.setVisibility(VISIBLE);
        }

        @Override
        public void onHideCustomView() {
            if (mCustomView == null) {
                return;
            }
            mCustomView.setVisibility(GONE);
            mCustomViewContainer.removeView(mCustomView);
            mCustomView = null;
            mCustomViewContainer.setVisibility(GONE);
            mCustomViewCallback.onCustomViewHidden();
            CustomWebViewHTML5.this.setVisibility(VISIBLE);
            super.onHideCustomView();
        }

        /**
         * 网页加载标题回调
         *
         * @param view
         * @param title
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            wv_tv_title.setText(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            ((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
            webview_tv_progress.setText("正在加载,已完成" + newProgress + "%...");
            webview_tv_progress.postInvalidate();//刷新
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }

    /**
     * 自定义webViewClient
     */
    class MyWebViewClient extends WebViewClient {
        /**
         * 加载过程中拦截加载地址的url
         *
         * @param view
         * @param url
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.startsWith("zttmall://")) {
                Uri mUri = Uri.parse(url);
                ArrayList<String> browerList = new ArrayList<>();
                browerList.add("http");
                browerList.add("https");
                browerList.add("about");
                browerList.add("javascript");
                if (browerList.contains(mUri.getScheme())) {
                    return false;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID,
                            App.getAppInstance().getApplicationContext().getPackageName());
                    try {
                        App.getAppInstance().getApplicationContext().startActivity(intent);
                        return true;
                    } catch (ActivityNotFoundException e) {
                    }
                }
                return false;
            } else {
                return true;
            }
        }

        /**
         * 页面加载过程中,加载资源回调的方法
         * @param view
         * @param url
         */
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        /**
         * 页面加载完成的回调方法
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (isRefresh){
                isRefresh=false;
            }
            //加载完成后显示界面
            frame_progress.setVisibility(GONE);
            mContentView.setVisibility(VISIBLE);
            //关闭图片加载阻塞
            view.getSettings().setBlockNetworkImage(false);
        }

        /**
         * 页面开始加载的回调
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        /**
         * 加载出错
         * @param view
         * @param request
         * @param error
         */
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.e(TAG1, "onReceivedError: --------------------------------" );
        }

        /**
         * 缩放时
         * @param view
         * @param oldScale
         * @param newScale
         */
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            CustomWebViewHTML5.this.requestFocus();
            CustomWebViewHTML5.this.requestFocusFromTouch();
        }
    }
}
