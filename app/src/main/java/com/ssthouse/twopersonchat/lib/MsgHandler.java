package com.ssthouse.twopersonchat.lib;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.ssthouse.twopersonchat.util.LogHelper;
import com.ssthouse.twopersonchat.util.NotificationHelper;

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
        //TODO--如果消息是1--接收到的消息--一定是一邀请加入的消息
        if(message.getMessageType() == 1){
            //TODO---弹出对话框--选择接不接受邀请
//            ActivityInvite.start(context, message);
            LogHelper.Log(TAG, "我接受到了---请求消息---弹出了--notification");
            NotificationHelper.showInviteNotify(context, message);
            return;
        }
        if (activityMessageHandler != null) {
            // 正在聊天时，分发消息，刷新界面
            activityMessageHandler.onMessage(message, conversation, client);
//            LogHelper.Log(TAG, "我调用了InnerHelper中的Handler");
        } else {
            // 打开tNotification
            LogHelper.Log(TAG, "现在是application中的handler");
            NotificationHelper.showMsgNotify(context, message);
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
