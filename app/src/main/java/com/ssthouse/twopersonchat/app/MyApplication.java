package com.ssthouse.twopersonchat.app;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.ssthouse.twopersonchat.lib.message.InviteMessage;
import com.ssthouse.twopersonchat.lib.util.ChatHelper;
import com.ssthouse.twopersonchat.lib.MsgHandler;
import com.ssthouse.twopersonchat.util.FileHelper;
import com.ssthouse.twopersonchat.util.LogHelper;


/**
 * 全局初始化
 * Created by ssthouse on 2015/8/4.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化自定义的数据类型
        AVIMMessageManager.registerAVIMMessageType(InviteMessage.class);

        //初始化app数据
        FileHelper.initAppDirectory(this);

        //初始化LeanCloud
        AVOSCloud.initialize(this, "axvboxrlkm8x0x5my5qsq7hrhkps6zkyu11pyash50earlgv",
                "srs1nwco56zeolg88r082bo97ctyaym7reqd4xjyv9yehofo");

        //初始时就接收消息
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MsgHandler(this));
        LogHelper.Log("Application", "我注册好了监听器=====");

        //建立连接
        ChatHelper.openConnection(this);
    }
}
