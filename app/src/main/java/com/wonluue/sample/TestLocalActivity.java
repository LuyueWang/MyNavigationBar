package com.wonluue.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wonluue.mynavigationbar.activity.LocalActivity;
import com.wonluue.mynavigationbar.activity.NavigationBarActivity;
import com.wonluue.mynavigationbar.view.UiSheetView;


public class TestLocalActivity extends LocalActivity {
    private UiSheetView uiSheetView;
    @Override
    public View onCreatView() {
        TextView tv = new TextView(this);
        tv.setText("可以滑动左侧返回");
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(16);

        setTitle("更多");
        showLeftText();

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return tv;
    }


    @Override
    public void onBackPressed() {
        if(uiSheetView != null && uiSheetView.isShow()){
            finish();
            return;
        }
        super.onBackPressed();
    }
}
