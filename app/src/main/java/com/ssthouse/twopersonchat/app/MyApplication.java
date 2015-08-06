package com.ssthouse.twopersonchat.app;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;


/**
 * 全局初始化
 * Created by ssthouse on 2015/8/4.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化LeanCloud
        AVOSCloud.initialize(this, "axvboxrlkm8x0x5my5qsq7hrhkps6zkyu11pyash50earlgv",
                "srs1nwco56zeolg88r082bo97ctyaym7reqd4xjyv9yehofo");
    }
}
