package com.wonluue.mynavigationbar.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;

import com.wonluue.mynavigationbar.utils.UiUtils;

/**
 * Created by Echo.W on 2016/8/18 0018.
 */
public class BackgroundShadowView extends View {

    private static BackgroundShadowView shadowView;
    private Activity activity;

    private BackgroundShadowView(Context context) {
        super(context);
        this.activity = (Activity) context;
        this.setBackgroundColor(Color.argb(128, 0, 0, 0));
    }

    public static BackgroundShadowView getInstance(Activity activity){
        if(shadowView == null) {
            shadowView = new BackgroundShadowView(activity);
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            params.x = params.MATCH_PARENT;
            params.y = params.MATCH_PARENT;
            activity.getWindow().addContentView(shadowView, params);

        }
        return shadowView;
    }

    public void showBackgroundShadow(){
        if(shadowView == null)
            shadowView = getInstance(activity);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(250);
        shadowView.startAnimation(alphaAnimation);
        shadowView.setVisibility(View.VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showBackgroundShadowWithBlur(float blur){
        if(shadowView == null)
            shadowView = getInstance(activity);
        shadowView.setBackground(new BitmapDrawable(UiUtils.blur(activity, getSmallSizeBitmap(getScreenImage(), 0.3f), blur)));
        showBackgroundShadow();
    }

    public void hideBackgroundShadow(){
        if(shadowView != null) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(250);
            shadowView.startAnimation(alphaAnimation);
            shadowView.setVisibility(View.GONE);
            shadowView = null;
        }
    }

    /**
     * 截图
     *
     * @return
     */
    public Bitmap getScreenImage() {
        //获取当前屏幕的大小
        int width = activity.getWindow().getDecorView().getRootView().getWidth();
        int height = activity.getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        Bitmap temBitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
        //找到当前页面的跟布局
        View view = activity.getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片
        temBitmap = view.getDrawingCache();
        return temBitmap;
    }

    /**
     * 压缩图片
     *
     * @param source
     * @param percent
     * @return
     */
    private Bitmap getSmallSizeBitmap(Bitmap source, float percent) {
        if (percent > 1 || percent <= 0) {
            throw new IllegalArgumentException("percent must be > 1 and <= 0");
        }
        Matrix matrix = new Matrix();
        matrix.setScale(percent, percent);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
