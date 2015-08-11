package com.ssthouse.twopersonchat.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.activity.ActivityChat;
import com.ssthouse.twopersonchat.activity.ActivityInvite;

/**
 * Created by ssthouse on 2015/8/11.
 */
public class NotificationHelper {
    private static final int requestCode = 0;

    private static NotificationManager notificationManager;

    public static void showMsgNotify(Context context, AVIMTypedMessage msg) {
        if(notificationManager == null){
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, requestCode,
                new Intent(context, ActivityChat.class), 0);

        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.avatar_girl)
                .setTicker("Ta发来了新消息")
                .setContentTitle(PreferenceHelper.getBindingName(context))
                .setContentText("Ta发来了新消息")
                .setContentIntent(pendingIntent3);
        //如果是文字消息
        if(msg.getMessageType() == -1){
            builder.setContentText(((AVIMTextMessage) msg).getText());
        }
        // 设置notify的属性----正式弹出Notify
        Notification notify = builder.getNotification();
        notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        notificationManager.notify( Notification.FLAG_AUTO_CANCEL, notify);
    }

    public static void showInviteNotify(Context context, AVIMTypedMessage msg) {
        if(notificationManager == null){
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Intent intent = new Intent(context, ActivityInvite.class);
        intent.putExtra("msg", msg);
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 3,
                intent, 0);

        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.avatar_girl)
                .setTicker("Ta发来了新消息")
                .setContentTitle(PreferenceHelper.getBindingName(context))
                .setContentText("Ta发来了新消息")
                .setContentIntent(pendingIntent3);
        //如果是文字消息
//        if(msg.getMessageType() == -1){
//            builder.setContentText(((AVIMTextMessage) msg).getText());
//        }
        // 设置notify的属性----正式弹出Notify
        Notification notify = builder.getNotification();
        notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        notificationManager.notify( Notification.FLAG_AUTO_CANCEL, notify);
    }
}
