package com.taobao.luaview.demo;

import android.app.Application;

import com.taobao.luaview.global.LuaViewManager;

/**
 * Created by frodo on 2016/6/13.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LuaViewManager.initBinders();
    }
}
