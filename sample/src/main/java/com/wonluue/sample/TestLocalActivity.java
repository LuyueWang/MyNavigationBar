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
        tv.setText("点我");
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(32);

        setTitle("更多");
        showLeftText();

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSheetView();
            }
        });
        return tv;
    }

    public void showSheetView(){
        uiSheetView = new UiSheetView(this,UiSheetView.BOTTOM);
        uiSheetView.setTitle("温馨提示");
        uiSheetView.setMessage("今天心情很好，明天也很好，后退会更好，因为后天周五了");
        uiSheetView.setNegativeButton("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiSheetView.dismiss();
            }
        });
        uiSheetView.setPositiveButton("打开猫小贷", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestLocalActivity.this, TestWebActivity.class);
                intent.putExtra("back_name","猫小贷");
                intent.putExtra("url","http://www.maoxiaodai.cn");
                startActivity(intent);
                uiSheetView.dismiss();
            }
        });
        uiSheetView.setCancelable(false);
        uiSheetView.show();

        uiSheetView.setOnDismissListener(new UiSheetView.OnDismissListener() {
            @Override
            public void onDismiss() {
                Toast.makeText(TestLocalActivity.this, "给你一个命", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(uiSheetView != null && uiSheetView.isShow()){
            uiSheetView.dismiss();
            return;
        }
        super.onBackPressed();
    }
}
