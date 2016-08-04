package com.wonluue.mynavigationbar.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.utils.NetworkHelpers;
import com.wonluue.mynavigationbar.utils.StatusBarUtil;
import com.wonluue.mynavigationbar.utils.UiUtils;

/**
 * Created by Alex on 2016/8/2 0001.
 * 原则上作为二级页面使用
 */
public abstract class WebActivity extends LocalActivity {

    private ProgressBar pbLoadPage;
    private WebView webView;
    private TextView tvMsg;
    private LinearLayout llNoNetwork;
    private FrameLayout onlineFrame;

    private boolean isShowWebPageTitle = false;

    @Override
    public View onCreatView() {
        View view = View.inflate(this,R.layout.activity_web,null);

        pbLoadPage = (ProgressBar) view.findViewById(R.id.pb_load_page);
        webView = (WebView) view.findViewById(R.id.webView);
        tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        llNoNetwork = (LinearLayout) view.findViewById(R.id.ll_no_network);
        onlineFrame = (FrameLayout) view.findViewById(R.id.online_frame);

        tvToolbarCenterText.setMaxEms(11);
        tvToolbarCenterText.setEllipsize(TextUtils.TruncateAt.END);
        tvToolbarCenterText.setSingleLine(true);
        tvToolbarCenterText.setMaxLines(1);


        llNoNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });

        loadUrl();

        if(onAddView() != null) {
            onlineFrame.addView(onAddView());
        }
        return view;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(NetworkHelpers.isNetworkConnected(this)){
            llNoNetwork.setVisibility(View.GONE);
        }else {
            llNoNetwork.setVisibility(View.VISIBLE);
        }
    }

    public void loadUrl(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(), "login");
        WebViewClient wvc = new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                llNoNetwork.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                llNoNetwork.setVisibility(View.GONE);
            }
        };
        webView.setWebViewClient(wvc);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pbLoadPage.setVisibility(View.GONE);
                } else {
                    if (View.GONE == pbLoadPage.getVisibility()) {
                        pbLoadPage.setVisibility(View.VISIBLE);
                    }
                    pbLoadPage.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(isShowWebPageTitle) {
                    tvToolbarCenterText.setText(title);
                }
            }
        });
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Uri uri = Uri.parse(s);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView.loadUrl(url != null?url:"");
    }

    public class JavaScriptInterface{
        @JavascriptInterface
        public void startFunction() {

        }
    }

    /**
     * 设置标题
     */
    public void showWebPageTitle() {
        isShowWebPageTitle = true;
        if (tvToolbarCenterText != null) {
            tvToolbarCenterText.setVisibility(View.VISIBLE);
        }
    }

    public abstract View onAddView();
}
