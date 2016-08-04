package com.wonluue.mynavigationbar.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络帮助类
 * Created by Jarlene on 2016/1/10.
 */
public class NetworkHelpers {

    private static final String TAG = "NetworkHelpers";

    public static boolean isNetworkConnected(Context context) {
        return NetworkHelpers.isNetworkAvailable(context);
    }

    /**
     * 判断网络状态
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null)
            return false;
        try {
            // 从系统服务中得到网络连接管理者
            ConnectivityManager connectivity =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                //LogUtil.d(TAG, "+++couldn't get connectivity manager");
            } else {
                // 得到所有的网络连接类型
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                // 如果不为空
                if (info != null) {
                    // 遍历
                    for (int i = 0; i < info.length; i++) {
                        // 如果有处于连接状态的就返回true
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            //LogUtil.d(TAG, "+++network is available");
                            return true;
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //LogUtil.d(TAG, "+++network is not available");
        // 返回false
        return false;
    }
}
