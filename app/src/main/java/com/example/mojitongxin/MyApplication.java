package com.sunhaobin.imapp;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Application instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }
}
