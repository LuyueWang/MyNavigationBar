package com.wonluue.mynavigationbar.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wonluue.mynavigationbar.R;

/**
 * Created by Echo.W on 2016/8/3 0003.
 */
public class UiTipView {

    private PopupWindow mUiTipView;
    private View view;// 布局
    private BackgroundShadowView shadow;// 背景阴影
    private CardView cardView;
    private ImageView ivTipViewIcon;
    private TextView tvTipViewMsg;

    private Activity activity;

    private boolean isShow;// 记录是否显示
    private OnDismissListener mOnDismissListener;

    public static final int LIGHT = 0;
    public static final int DARK = 1;
    private int theme = LIGHT;

    private Handler mHandler;
    private static final int MSG_HIND_TIP = 0x2001;

    public UiTipView(@NonNull Activity activity, int theme) {
        super();
        this.activity = activity;
        this.theme = theme;
        view = View.inflate(activity, R.layout.layout_tip_view, null);

        cardView = (CardView) view.findViewById(R.id.cardView);
        ivTipViewIcon = (ImageView) view.findViewById(R.id.iv_tip_view_icon);
        tvTipViewMsg = (TextView) view.findViewById(R.id.tv_tip_view_msg);
        if (theme == LIGHT) {
            cardView.setCardBackgroundColor(Color.rgb(255, 255, 255));
            tvTipViewMsg.setTextColor(activity.getResources().getColor(R.color.color_sheet_view_msg));
        } else {
            cardView.setCardBackgroundColor(Color.rgb(63, 63, 63));
            tvTipViewMsg.setTextColor(activity.getResources().getColor(R.color.color_white));
        }

        shadow = BackgroundShadowView.getInstance(activity);
        //shadow.setBackgroundColor(Color.argb(128, 0, 0, 0));

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_HIND_TIP:
                        if (mUiTipView != null) {
                            dismiss();
                        }
                        break;
                }
            }
        };

    }

    public static UiTipView makeTip(Activity activity, CharSequence text, int icon, int theme) {
        UiTipView tip = new UiTipView(activity, theme);
        tip.setMessage(text);
        tip.setIcon(icon);
        return tip;
    }

    public static UiTipView makeTip(Activity activity, int text, int icon, int theme) {
        UiTipView tip = new UiTipView(activity, theme);
        tip.setMessage(text);
        tip.setIcon(icon);
        return tip;
    }

    /**
     * 返回显示状态
     *
     * @return
     */
    private boolean isShow() {
        return isShow;
    }

    public UiTipView setIcon(int icon) {
        ivTipViewIcon.setImageResource(icon);
        return this;
    }

    /**
     * 设置弹窗显示的信息
     *
     * @param message
     * @return
     */
    public UiTipView setMessage(CharSequence message) {
        tvTipViewMsg.setText(message);
        return this;
    }

    /**
     * 设置弹窗显示的信息
     *
     * @param messageId
     * @return
     */
    public UiTipView setMessage(int messageId) {
        tvTipViewMsg.setText(messageId);
        return this;
    }

    /**
     * 默认背景显示
     */
    public void show() {
        show(0f);
    }

    /**
     * 高斯模糊背景，仅支持API16及以上
     * @param blur >0 && <=25
     */
    public void show(float blur) {

        dismiss();

        mUiTipView = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mUiTipView.setInputMethodMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
        mUiTipView.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        shadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mUiTipView.setFocusable(true);
        mUiTipView.setBackgroundDrawable(new BitmapDrawable());
        mUiTipView.setOutsideTouchable(true);

        mUiTipView.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (shadow != null)
                    shadow.hideBackgroundShadow();
                if (mOnDismissListener != null)
                    mOnDismissListener.onDismiss();
            }
        });

        if (!isShow) {
            if (blur <= 0) {
                shadow.showBackgroundShadow();
            } else {
                shadow.showBackgroundShadowWithBlur(blur >= 25 ? 25 : blur);
            }
        }

        mUiTipView.setAnimationStyle(R.style.SheetViewCenterAnim);
        mUiTipView.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        isShow = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1200);
                mHandler.sendEmptyMessage(MSG_HIND_TIP);
            }
        }).start();
    }

    public void dismiss() {
        if (mUiTipView != null) {
            isShow = false;
            mUiTipView.dismiss();
            shadow.hideBackgroundShadow();
            mUiTipView = null;
            System.gc();
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

}