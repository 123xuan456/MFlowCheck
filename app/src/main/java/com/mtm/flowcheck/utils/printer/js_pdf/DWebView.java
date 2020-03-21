package com.mtm.flowcheck.utils.printer.js_pdf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;


import com.mtm.flowcheck.R;
import com.mtm.flowcheck.utils.base64.BASE64Encoder;

import java.io.UnsupportedEncodingException;


/**
 * @author DeMon
 * @date 2018/12/8
 * @email 757454343@qq.com
 * @description
 */
public class DWebView extends FrameLayout {
    private WebView webView;
    boolean isSetting = true;

    public DWebView(Context context) {
        this(context, null);
    }

    @SuppressLint("NewApi")
    public DWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public DWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.widget_webview, this);
        webView = (WebView) findViewById(R.id.webView);
//        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.DWebView);
//        isSetting = mTypedArray.getBoolean(R.styleable.DWebView_dwv_isSetting, true);
        if (isSetting) {
            WebViewSetting();
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
//        mTypedArray.recycle();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SetJavaScriptEnabled")
    private void WebViewSetting() {
        WebSettings settings = webView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
    }



    public void LoadPDF(String docPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//api >= 19
            webView.loadUrl("file:///android_asset/pdf/pdf.html?" + docPath);
        } else {
            if (!TextUtils.isEmpty(docPath)) {
                byte[] bytes = null;
                try {// 获取以字符编码为utf-8的字符
                    bytes = docPath.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (bytes != null) {
                    docPath = new BASE64Encoder().encode(bytes);// BASE64转码
                }
            }
            webView.loadUrl("file:///android_asset/pdf/pdf.html?" + docPath);
        }
    }
}
