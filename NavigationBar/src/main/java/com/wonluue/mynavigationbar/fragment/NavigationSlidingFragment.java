package com.wonluue.mynavigationbar.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by EchoWang on 2016/7/28.
 */
public class NavigationSlidingFragment extends SlidingBackFragment implements KeyEventListener {

    private static final String TAG = NavigationSlidingFragment.class.getSimpleName();
    private NavigationController mNavigationController;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStatusBarTransparent();
    }

    /**
     * 设置导航控制器
     *
     * @param controller
     */
    public void setNavigationController(NavigationController controller) {
        this.mNavigationController = controller;
        if (mNavigationController != null) {
            mNavigationController.addKeyEventListener(this);
        }
    }

    /**
     * 更新Fragment
     * @param arg
     */
    public void updateFragment(Bundle arg) {

    }



    @Override
    public void onPanelClosed(View pPanel) {
        super.onPanelClosed(pPanel);
        if (mNavigationController != null) {
            mNavigationController.finishFragment(this);
            //LogUtil.d(TAG, "finish fragment");
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!enter) {
            return null;
        }
        try {
            if (nextAnim == 0) {
                return null;
            }
            Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onPanelOpened(null);
                }
            });
            return anim;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否接收按键事件
     *
     * @param keyCode
     * @return
     */
    @Override
    public boolean isHandleKeyEvent(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mNavigationController != null) {
                mNavigationController.removeKeyEventListener(this);
               finish();
            }
            return true;
        }
        return false;
    }
}
