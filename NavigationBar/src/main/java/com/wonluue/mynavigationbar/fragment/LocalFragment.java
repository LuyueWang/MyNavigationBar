package com.wonluue.mynavigationbar.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.utils.UiUtils;

/**
 * Created by EchoWang on 2016/7/28.
 */
public abstract class LocalFragment extends NavigationSlidingFragment {

    private AppBarLayout toolbarDefault;
    protected FrameLayout flToolbarDefault;

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
    public View onCreateView(ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_local, null);
        mRootView = view;
        setStatusBarTransparent();
        initViews(view);
        localFrame = (FrameLayout) view.findViewById(R.id.local_frame);
        localFrame.addView(createView());
        return view;
    }

    private  void initViews(View view) {

        toolbarDefault = (AppBarLayout) view.findViewById(R.id.toolbar_default);
        flToolbarDefault = (FrameLayout) view.findViewById(R.id.fl_toolbar_default);

        llToolbarLeft = (LinearLayout) view.findViewById(R.id.ll_toolbar_left);
        llToolbarCenter = (LinearLayout) view.findViewById(R.id.ll_toolbar_center);
        llToolbarRight = (LinearLayout) view.findViewById(R.id.ll_toolbar_right);
        tvToolbarLeftText = (TextView) view.findViewById(R.id.tv_toolbar_left_text);
        tvToolbarCenterText = (TextView) view.findViewById(R.id.tv_toolbar_center_text);
        tvToolbarRightText = (TextView) view.findViewById(R.id.tv_toolbar_right_text);
        ivToolbarLeftImg = (ImageView) view.findViewById(R.id.iv_toolbar_left_img);
        ivToolbarRightImg = (ImageView) view.findViewById(R.id.iv_toolbar_right_img);

        ivToolbarLeftImg.setVisibility(View.VISIBLE);// 显示左边图标
        tvToolbarLeftText.setVisibility(View.VISIBLE);// 显示左边文字
        tvToolbarCenterText.setVisibility(View.VISIBLE);// 显示标题文字

        ivToolbarLeftImg.setImageResource(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        tvToolbarLeftText.setText(R.string.fragment_finish);
        tvToolbarCenterText.setText("默认Fragment");

        ivToolbarLeftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                UiUtils.hideInputMethod(getActivity());
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