package com.wonluue.mynavigationbar;

import android.app.Application;

/**
 * Created by Alex on 2016/3/21 0021.
 */
public class MyApplication extends Application {
    public static MyApplication context;

    public MyApplication() {
        context = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
