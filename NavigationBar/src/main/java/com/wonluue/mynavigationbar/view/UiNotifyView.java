package com.wonluue.mynavigationbar.view;

import android.app.Activity;
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

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.utils.StatusBarUtil;

import static com.wonluue.mynavigationbar.R.id.cardView;

/**
 * Created by Echo.W on 2016/8/3 0003.
 */
public class UiNotifyView {

    private PopupWindow mUiNotifyView;
    private View view;// 布局
    private LinearLayout llNotifyBg;
    private ImageView ivNotifyViewIcon;
    private TextView tvNotifyViewMsg;

    private Activity activity;

    private boolean isShow;// 记录是否显示
    private OnDismissListener mOnDismissListener;

    public static final int DEFAULT = 0;
    public static final int LIGHT = 1;
    public static final int DARK = 2;
    private int theme = DEFAULT;

    private Handler mHandler;
    private static final int MSG_HIND_TIP = 0x2001;

    public UiNotifyView(@NonNull Activity activity, int theme) {
        super();
        this.activity = activity;
        this.theme = theme;
        view = View.inflate(activity, R.layout.layout_notify_view, null);

        llNotifyBg = (LinearLayout) view.findViewById(R.id.ll_notify_bg);
        ivNotifyViewIcon = (ImageView) view.findViewById(R.id.iv_tip_view_icon);
        tvNotifyViewMsg = (TextView) view.findViewById(R.id.tv_tip_view_msg);

        llNotifyBg.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        tvNotifyViewMsg.setTextColor(activity.getResources().getColor(R.color.color_white));
        if (theme == LIGHT) {
            llNotifyBg.setBackgroundColor(Color.rgb(255, 255, 255));
            tvNotifyViewMsg.setTextColor(activity.getResources().getColor(R.color.color_sheet_view_msg));
        } else if (theme == DARK) {
            llNotifyBg.setBackgroundColor(Color.rgb(63, 63, 63));
            tvNotifyViewMsg.setTextColor(activity.getResources().getColor(R.color.color_white));
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_HIND_TIP:
                        if (mUiNotifyView != null) {
                            dismiss();
                        }
                        break;
                }
            }
        };

    }

    public static UiNotifyView makeTip(Activity activity, CharSequence text, int icon, int theme) {
        UiNotifyView tip = new UiNotifyView(activity, theme);
        tip.setMessage(text);
        tip.setIcon(icon);
        return tip;
    }

    public static UiNotifyView makeTip(Activity activity, int text, int icon, int theme) {
        UiNotifyView tip = new UiNotifyView(activity, theme);
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

    public UiNotifyView setIcon(int icon) {
        ivNotifyViewIcon.setImageResource(icon);
        return this;
    }

    /**
     * 设置弹窗显示的信息
     *
     * @param message
     * @return
     */
    public UiNotifyView setMessage(CharSequence message) {
        tvNotifyViewMsg.setText(message);
        return this;
    }

    /**
     * 设置弹窗显示的信息
     *
     * @param messageId
     * @return
     */
    public UiNotifyView setMessage(int messageId) {
        tvNotifyViewMsg.setText(messageId);
        return this;
    }

    public void show() {

        dismiss();

        mUiNotifyView = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mUiNotifyView.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mOnDismissListener != null)
                    mOnDismissListener.onDismiss();
            }
        });

        mUiNotifyView.setAnimationStyle(R.style.NotifyViewAnim);
        mUiNotifyView.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP, 0, 0);
        isShow = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);
                mHandler.sendEmptyMessage(MSG_HIND_TIP);
            }
        }).start();
    }

    public void dismiss() {
        if (mUiNotifyView != null) {
            isShow = false;
            mUiNotifyView.dismiss();
            mUiNotifyView = null;
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