package com.wonluue.mynavigationbar.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.wonluue.mynavigationbar.MyApplication;

/**
 * Created by Jarlene on 2016/1/16.
 */
public class UiUtils {
    /**
     * dp转px
     */
    public static int dip2px(float dpValue) {
        final float scale = MyApplication.context .getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     */
    public static int px2dip(Context ctx, float pxValue) {
        final float scale = MyApplication.context .getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 得到窗口的宽度
     */
    public static int getWindowWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 得到窗口的高度
     */
    public static int getWindowHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

}
