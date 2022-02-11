/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：App.java
 * 包名：com.jeremy.snapshot.App
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年10月16日 19:40:21
 */

package com.jeremy.snapshot;

import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;

public class App extends Application {
    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
        instance = getApplicationContext();
    }

    public static Context getContext()
    {
        return instance;
    }
}
