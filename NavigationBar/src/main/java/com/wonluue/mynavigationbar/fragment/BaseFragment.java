package com.wonluue.mynavigationbar.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.utils.StatusBarUtil;

import java.lang.reflect.Field;

/**
 * 代理Fragment,用来将统一管理
 * Created by EchoWang on 2016/7/28.
 */
public class BaseFragment extends Fragment {

    protected View mRootView = null;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        }catch (NoSuchFieldException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置状态栏沉浸
     * 仅针对4.4及以上版本
     */
    protected void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        if (mRootView == null) {
            return;
        }
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
        View titleBarView = mRootView.findViewById(R.id.toolbar);
        if (titleBarView == null) {
            return;
        }
        ViewGroup.LayoutParams layoutP = titleBarView.getLayoutParams();
        float titleBarHeight = getResources().getDimension(R.dimen.toolbar_height_size);
        layoutP.height = (int) titleBarHeight + statusBarHeight;
        titleBarView.setLayoutParams(layoutP);
        View statusBarView = titleBarView.findViewById(R.id.status_bar_view);
        if (statusBarView == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = statusBarView.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            layoutParams.height = statusBarHeight;
            statusBarView.setLayoutParams(layoutParams);
            statusBarView.setVisibility(View.VISIBLE);
        } else {
            statusBarView.setVisibility(View.GONE);
        }
    }

}
