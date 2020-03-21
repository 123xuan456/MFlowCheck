package com.mtm.flowcheck.utils.printer.js_pdf;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mtm.flowcheck.utils.base64.BASE64Encoder;

import java.io.UnsupportedEncodingException;


/**
 * @author D&LL
 * @date 2018/7/22
 * @description
 */
public class WebViewHelper {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SetJavaScriptEnabled")
    public static void WebViewSetting( WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
//        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //加载错误的时候会回调
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                if (webResourceRequest.isForMainFrame()) {
                    webView.setVisibility(View.GONE);
                }
            }

        });

    }

    public static void WebViewLoadPDF(WebView webView, String docPath) {
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
