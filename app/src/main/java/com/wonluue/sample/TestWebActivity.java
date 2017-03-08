package com.wonluue.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.wonluue.mynavigationbar.activity.WebActivity;

public class TestWebActivity extends WebActivity {

    @Override
    public View onAddView() {
        showLeftText();
        showWebPageTitle();
        return null;
    }


    @Override
    public Object setJsi() {
        return new JavaScriptInterface();
    }

    public class JavaScriptInterface {
        @JavascriptInterface
        public void startFunction() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
        @JavascriptInterface
        public void loginSuccess() {
            finish();
        }
    }
}
