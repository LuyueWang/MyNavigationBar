package com.wonluue.mynavigationbar.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.utils.StatusBarUtil;
import com.wonluue.mynavigationbar.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by Alex on 2016/8/1 0001.
 */
public abstract class LocalActivity extends SwipeBackActivity {
    private AppBarLayout toolbarDefault;
    private FrameLayout flToolbarDefault;
    private LinearLayout llToolbarLeft;
    private ImageView ivToolbarLeftImg;
    private TextView tvToolbarLeftText;
    private LinearLayout llToolbarCenter;
    private TextView tvToolbarCenterText;
    private LinearLayout llToolbarRight;
    private ImageView ivToolbarRightImg;
    private TextView tvToolbarRightText;

    protected FrameLayout localFrame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_local);

        initToolbarView();

        localFrame = (FrameLayout) findViewById(R.id.local_frame);
        localFrame.addView(createView());
    }

    /**
     * 加载toolbar布局
     */
    protected void initToolbarView() {
        toolbarDefault = (AppBarLayout) findViewById(R.id.toolbar_default);
        flToolbarDefault = (FrameLayout) findViewById(R.id.fl_toolbar_default);
        llToolbarLeft = (LinearLayout) findViewById(R.id.ll_toolbar_left);
        ivToolbarLeftImg = (ImageView) findViewById(R.id.iv_toolbar_left_img);
        tvToolbarLeftText = (TextView) findViewById(R.id.tv_toolbar_left_text);
        llToolbarCenter = (LinearLayout) findViewById(R.id.ll_toolbar_center);
        tvToolbarCenterText = (TextView) findViewById(R.id.tv_toolbar_center_text);
        llToolbarRight = (LinearLayout) findViewById(R.id.ll_toolbar_right);
        ivToolbarRightImg = (ImageView) findViewById(R.id.iv_toolbar_right_img);
        tvToolbarRightText = (TextView) findViewById(R.id.tv_toolbar_right_text);

        toolbarDefault.setPadding(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);

        ivToolbarLeftImg.setVisibility(View.VISIBLE);// 显示左边图标
        tvToolbarCenterText.setVisibility(View.VISIBLE);// 显示标题文字
        //tvToolbarLeftText.setVisibility(View.VISIBLE);// 显示左边文字

        ivToolbarLeftImg.setImageResource(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        tvToolbarCenterText.setText("新界面");
        //tvToolbarLeftText.setText("返回");
        //tvToolbarLeftText.setTextSize(14);

        ivToolbarLeftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                UiUtils.hideInputMethod(LocalActivity.this);
            }
        });
    }

    public void showOrhindTitleBar(boolean isShow) {
        if (isShow) {
            toolbarDefault.setVisibility(View.VISIBLE);
        } else {
            toolbarDefault.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title) {
        if (tvToolbarCenterText != null) {
            tvToolbarCenterText.setText(title);
            tvToolbarCenterText.setVisibility(View.VISIBLE);
        }
    }

    public void setTitle(int resId) {
        if (tvToolbarCenterText != null) {
            tvToolbarCenterText.setText(resId);
            tvToolbarCenterText.setVisibility(View.VISIBLE);
        }
    }

    public abstract View createView();
}
