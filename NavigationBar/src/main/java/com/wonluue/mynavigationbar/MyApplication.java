package com.wonluue.mynavigationbar;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alex on 2016/3/21 0021.
 */
public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
