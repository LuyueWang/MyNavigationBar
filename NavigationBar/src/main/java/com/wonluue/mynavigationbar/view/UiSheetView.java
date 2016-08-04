package com.wonluue.mynavigationbar.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.utils.UiUtils;

/**
 * Created by Alex on 2016/8/3 0003.
 */
public class UiSheetView {

    private PopupWindow mUiSheetView;
    private View view;// 布局
    private View shadow;// 背景阴影
    private TextView tvSheetViewTitle;// 显示标题
    private TextView tvSheetViewMsg;// 显示信息
    private LinearLayout llSheetViewDo;// 显示操作栏
    private TextView tvSheetViewCancel;// 取消按钮
    private View linInTitle;
    private View viewSheetViewLine;// 取消和确认按钮分割线
    private TextView tvSheetViewSure;// 确认按钮

    private FrameLayout flSheetViewAdd;// 自定义布局容器

    private Activity activity;

    private boolean isOutsideClose = true;// 默认点击其他地方可以关闭

    private boolean isShow;// 记录是否显示
    private OnDismissListener mOnDismissListener;

    public static final int CENTER = 0;
    public static final int BOTTOM = 1;
    private int local = BOTTOM;

    public UiSheetView(@NonNull Activity activity,int location) {
        super();
        this.activity = activity;
        this.local = location;
        if(location == CENTER){
            view = View.inflate(activity, R.layout.layout_center_sheet_view, null);
        }else {
            view = View.inflate(activity, R.layout.layout_sheet_view, null);
        }
        tvSheetViewTitle = (TextView) view.findViewById(R.id.tv_sheet_view_title);
        tvSheetViewMsg = (TextView) view.findViewById(R.id.tv_sheet_view_msg);
        llSheetViewDo = (LinearLayout) view.findViewById(R.id.ll_sheet_view_do);
        tvSheetViewCancel = (TextView) view.findViewById(R.id.tv_sheet_view_cancel);
        viewSheetViewLine = view.findViewById(R.id.view_sheet_view_line);
        linInTitle = view.findViewById(R.id.line_in_title);
        tvSheetViewSure = (TextView) view.findViewById(R.id.tv_sheet_view_sure);
        flSheetViewAdd = (FrameLayout) view.findViewById(R.id.fl_sheet_view_add);

        shadow = new View(activity);
        shadow.setBackgroundColor(Color.argb(100, 0, 0, 0));

    }

    /**
     * 返回显示状态
     * @return
     */
    public boolean isShow() {
        return isShow;
    }

    /**
     * 设置弹窗标题
     * @param title
     * @return
     */
    public UiSheetView setTitle(String title) {
        tvSheetViewTitle.setText(title);
        tvSheetViewTitle.setVisibility(View.VISIBLE);
        return this;
    }
    /**
     * 设置弹窗标题
     * @param titleId
     * @return
     */
    public UiSheetView setTitle(int titleId) {
        tvSheetViewTitle.setText(titleId);
        tvSheetViewTitle.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置弹窗显示的信息
     * @param message
     * @return
     */
    public UiSheetView setMessage(String message) {
        tvSheetViewMsg.setText(message);
        tvSheetViewMsg.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置弹窗显示的信息
     * @param messageId
     * @return
     */
    public UiSheetView setMessage(int messageId) {
        tvSheetViewMsg.setText(messageId);
        tvSheetViewMsg.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 确认按钮设置
     * @param text 文本
     * @param listener 操作
     * @return
     */
    public UiSheetView setPositiveButton(String text, View.OnClickListener listener) {
        tvSheetViewSure.setText(text);
        tvSheetViewSure.setOnClickListener(listener);
        tvSheetViewSure.setVisibility(View.VISIBLE);
        llSheetViewDo.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 确认按钮设置
     * @param textId 文本
     * @param listener 操作
     * @return
     */
    public UiSheetView setPositiveButton(int textId, View.OnClickListener listener) {
        tvSheetViewSure.setText(textId);
        tvSheetViewSure.setOnClickListener(listener);
        tvSheetViewSure.setVisibility(View.VISIBLE);
        llSheetViewDo.setVisibility(View.VISIBLE);
        return this;

    }

    /**
     * 取消按钮设置
     * @param text 文本
     * @param listener 操作
     * @return
     */
    public UiSheetView setNegativeButton(String text, View.OnClickListener listener) {
        tvSheetViewCancel.setText(text);
        tvSheetViewCancel.setOnClickListener(listener);
        tvSheetViewCancel.setVisibility(View.VISIBLE);
        llSheetViewDo.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 取消按钮设置
     * @param textId 文本
     * @param listener 操作
     * @return
     */
    public UiSheetView setNegativeButton(int textId, View.OnClickListener listener) {
        tvSheetViewCancel.setText(textId);
        tvSheetViewCancel.setOnClickListener(listener);
        tvSheetViewCancel.setVisibility(View.VISIBLE);
        llSheetViewDo.setVisibility(View.VISIBLE);
        return this;

    }

    /**
     * 设置自定义View
     * @param view
     * @return
     */
    public UiSheetView setView(View view){
        if(view != null) {
            linInTitle.setVisibility(View.VISIBLE);
            flSheetViewAdd.removeAllViews();
            flSheetViewAdd.addView(view);
        }
        return this;
    }

    /**
     * 设置为点击空白处
     * @param flag true:消失；false:不消失
     * @return
     */
    public UiSheetView setCancelable(boolean flag) {
        this.isOutsideClose = flag;
        return this;
    }

    public void show() {

        if (tvSheetViewSure.getVisibility() == View.VISIBLE && tvSheetViewCancel.getVisibility() == View.VISIBLE) {
            viewSheetViewLine.setVisibility(View.VISIBLE);
        } else if (tvSheetViewSure.getVisibility() == View.GONE && tvSheetViewCancel.getVisibility() == View.GONE) {
            llSheetViewDo.setVisibility(View.GONE);
        } else {
            viewSheetViewLine.setVisibility(View.GONE);
        }

        dismiss();

        mUiSheetView = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mUiSheetView.setInputMethodMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
        mUiSheetView.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (isOutsideClose) {
            mUiSheetView.setFocusable(true);
            mUiSheetView.setBackgroundDrawable(new BitmapDrawable());
            mUiSheetView.setOutsideTouchable(true);
        } else {
            shadow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mUiSheetView.setFocusable(false);
            //mUiSheetView.setBackgroundDrawable(new BitmapDrawable());
            mUiSheetView.setOutsideTouchable(false);
        }
        mUiSheetView.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                alphaAnimation.setDuration(250);
                shadow.startAnimation(alphaAnimation);
                shadow.setVisibility(View.GONE);
                if(mOnDismissListener != null)
                    mOnDismissListener.onDismiss();
            }
        });
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.x = params.MATCH_PARENT;
        params.y = params.MATCH_PARENT;
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(250);
        shadow.startAnimation(alphaAnimation);
        shadow.setVisibility(View.VISIBLE);

        activity.getWindow().addContentView(shadow, params);
        if(local == CENTER) {
            mUiSheetView.setAnimationStyle(R.style.SheetViewCenterAnim);
            mUiSheetView.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }else {
            mUiSheetView.setAnimationStyle(R.style.SheetViewAnim);
            mUiSheetView.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        isShow = true;

    }

    public void dismiss() {
        if (mUiSheetView != null) {
            isShow = false;
            mUiSheetView.dismiss();
            mUiSheetView = null;
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

}