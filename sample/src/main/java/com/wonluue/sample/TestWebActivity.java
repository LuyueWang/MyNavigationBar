package com.wonluue.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wonluue.mynavigationbar.activity.WebActivity;

public class TestWebActivity extends WebActivity {

    @Override
    public View onAddView() {
        showLeftText();
        showWebPageTitle();
        return null;
    }

}
