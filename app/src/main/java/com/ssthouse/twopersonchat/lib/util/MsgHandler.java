package com.ssthouse.twopersonchat.lib.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.ssthouse.twopersonchat.util.LogHelper;

/**
 * 全局的一个单例Handler
 * Created by ssthouse on 2015/8/10.
 */
public class MsgHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {
    private static final String TAG = "MsgHandler";

    /**
     * 唯一的handler
     */
    private static AVIMTypedMessageHandler<AVIMTypedMessage> activityMessageHandler;

    private Context context;

    public MsgHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        Log.d(TAG, "消息已到达对方" + message.getContent());
    }

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        LogHelper.Log(TAG, "现在是appliaction中的handler");
        if (activityMessageHandler != null) {
            // 正在聊天时，分发消息，刷新界面
            activityMessageHandler.onMessage(message, conversation, client);
            LogHelper.Log(TAG, "我调用了InnerHelper中的Handler");
        } else {
            // 没有打开聊天界面，这里简单地 Toast 一下。实际中可以刷新最近消息页面，增加小红点
            if (message instanceof AVIMTextMessage) {
                AVIMTextMessage textMessage = (AVIMTextMessage) message;
                Toast.makeText(context, "新消息 " + message.getFrom() + " : "
                        + textMessage.getText(), Toast.LENGTH_SHORT).show();
            }
            LogHelper.Log(TAG, "InnerHandler是空的!!!");
        }
    }

    public static AVIMTypedMessageHandler<AVIMTypedMessage> getActivityMessageHandler() {
        return activityMessageHandler;
    }

    /**
     * 设置handler
     * @param activityMessageHandler
     */
    public static void setActivityMessageHandler(AVIMTypedMessageHandler<AVIMTypedMessage> activityMessageHandler) {
        MsgHandler.activityMessageHandler = activityMessageHandler;
    }
}
