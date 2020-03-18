package com.example.imapp;

import com.example.common.common.app.Application;
import com.example.factory.Factory;
import com.igexin.sdk.PushManager;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 调用Factory进行初始化
        Factory.setup();
        // 推送进行初始化
        PushManager.getInstance().initialize(this,null);
    }
}
