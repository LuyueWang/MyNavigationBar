package com.wonluue.mynavigationbar.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.activity.NavigationBarActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xiongwenlong01 on 2016/1/8.
 */
public class NavigationController {

    private long mLastCloseTs = 0;

    private FragmentManager mFragmentManager;
    private LinkedList<BaseFragment> mFragments;
    private LinkedList<KeyEventListener> mKeyEventListeners;


    public NavigationController(FragmentActivity activity) {
        mFragmentManager = activity.getSupportFragmentManager();
        mFragments = new LinkedList<BaseFragment>();
        mKeyEventListeners = new LinkedList<KeyEventListener>();
    }


    /**
     * 添加事件点击回调
     * @param listener
     */
    public void addKeyEventListener(KeyEventListener listener) {
        if (listener == null) {
            return;
        }
        if (!mKeyEventListeners.contains(listener)) {
            mKeyEventListeners.add(listener);
        }
    }

    public void removeKeyEventListener(KeyEventListener listener) {
        if (listener == null) {
            return;
        }
        if (mKeyEventListeners.contains(listener)) {
            mKeyEventListeners.remove(listener);
        }
    }

    /**
     * 分发点击事件
     *
     * @param keyCode
     * @return
     */
    public boolean dispatchKeyEvent(int keyCode) {
        if (mKeyEventListeners != null && mKeyEventListeners.size() > 0) {
            KeyEventListener topListener = mKeyEventListeners.getLast();
            return topListener.isHandleKeyEvent(keyCode);
        }
        return false;
    }

    /**
     * 显示fragment
     * @param fragment
     * @param hasAnim
     */
    public void showFragment(NavigationSlidingFragment fragment, boolean hasAnim) {

        BaseFragment topFragment = getTopFragment();
        if (fragment != null) {
            String key = ((Object) fragment).getClass().getSimpleName();
            if (topFragment != null) {
                String topKey = ((Object) topFragment).getClass().getSimpleName();
                if (key.equalsIgnoreCase(topKey)) {
                    reloadFragment(fragment, fragment.getArguments());
                } else {
                    showFragment(fragment, hasAnim, true);
                }
            } else {
                showFragment(fragment, hasAnim, true);
            }
        }

    }


    /**
     * 显示fragment
     * 显示出来的Fragment会在最上面，这个时候，下面所有的Fragment都会调用OnPause；
     * @param fragment
     * @param hasAnim
     */
    public synchronized void showFragment(BaseFragment fragment, boolean hasAnim,
                                          boolean enableSliding) {
        if (mFragmentManager == null || fragment == null) {
            return;
        }
        long currentTs = System.currentTimeMillis();
        if ((hasAnim && (currentTs - mLastCloseTs > 0 && currentTs - mLastCloseTs < 100))) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (fragment instanceof NavigationSlidingFragment) {
            if (hasAnim) {
                transaction.setCustomAnimations(R.anim.right2left, -1);
            }
            NavigationSlidingFragment hSlidingFragment = (NavigationSlidingFragment) fragment;
            hSlidingFragment.setSlideable(enableSliding);
            hSlidingFragment.setNavigationController(this);
        }


        transaction.add(R.id.parent_layout, fragment);
        transaction.commitAllowingStateLoss();
        mFragments.add(fragment);
        try {
            if (mFragments.size() > 1) {
                Fragment topFragment = mFragments.get(mFragments.size() - 2);
                View view = topFragment.getView();
                if (view != null) {
                    view.setEnabled(false);
                }
                topFragment.onPause();
                FragmentManager childManager = topFragment.getChildFragmentManager();
                List<Fragment> listFragment = childManager.getFragments();
                if (listFragment != null && listFragment.size() > 0) {
                    for (Fragment f : listFragment) {
                        if (f != null) {
                            try {
                                f.onPause();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 结束Fragment
     * 结束的Fragment之后，下面所有的Fragment都会onResume
     * @param fragment
     */
    public void finishFragment(BaseFragment fragment) {
        if (mFragmentManager == null || fragment == null) {
            return;
        }
        long currentTs = System.currentTimeMillis();
        if (currentTs - mLastCloseTs > 0 && currentTs - mLastCloseTs < 100) {
            return;
        }
        mLastCloseTs = currentTs;
        mFragments.remove(fragment);
        if (fragment instanceof NavigationSlidingFragment) {
            NavigationSlidingFragment hSlidingFragment = (NavigationSlidingFragment) fragment;
            hSlidingFragment.setNavigationController(null);
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();

        try {
            if (mFragments.size() > 0) {
                Fragment topFragment = mFragments.get(mFragments.size() - 1);
                View view = topFragment.getView();
                if (view != null) {
                    view.setEnabled(true);
                }
                // No activity问题，发现Fragment生命周期异常，先会调用onResume，然后再走正常流程
                // 所以加上isAdded进行判断，防止异常，该问题需要时间验证
                if (topFragment.isAdded()) {
                    topFragment.onResume();
                    FragmentManager childManager = topFragment.getChildFragmentManager();
                    List<Fragment> listFragment = childManager.getFragments();
                    if (listFragment != null && listFragment.size() > 0) {
                        for (Fragment f : listFragment) {
                            if (f != null && !f.equals(fragment)) {
                                try {
                                    if (f.isAdded()) {
                                        f.onResume();
                                    } else {
                                        continue;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } else {
                NavigationBarActivity activity = NavigationBarActivity.getMain();
                if (activity != null) {
                    // activity.refreshUi();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 重新加载Fragment
     * @param fragment
     * @param arg
     */
    public void reloadFragment(BaseFragment fragment, Bundle arg) {
        if (mFragmentManager == null || fragment == null) {
            return;
        }
        if (fragment instanceof NavigationSlidingFragment) {
            NavigationSlidingFragment hSlidingFragment = (NavigationSlidingFragment) fragment;
            hSlidingFragment.updateFragment(arg);
        }
    }

    /**
     * 获取在最上面的Fragment
     * @return
     */
    public BaseFragment getTopFragment() {
        if (mFragments != null && mFragments.size() > 0) {
            return mFragments.get(mFragments.size() - 1);
        }
        return null;
    }

    /**
     * 获取在最上面的Fragment
     * @return
     */
    public BaseFragment getSecondFragment() {
        if (mFragments != null && mFragments.size() > 1) {
            return mFragments.get(mFragments.size() - 2);
        }
        return null;
    }

}
