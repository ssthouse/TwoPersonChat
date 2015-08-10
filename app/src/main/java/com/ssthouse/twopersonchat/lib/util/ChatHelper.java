package com.ssthouse.twopersonchat.lib.util;

import android.content.Context;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.ssthouse.twopersonchat.util.LogHelper;
import com.ssthouse.twopersonchat.util.PreferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ssthouse on 2015/8/10.
 */
public class ChatHelper {
    private static final String TAG = "ChatHelper";

    /**
     * 创建对话
     */
    public static void createConversation(final Context context) {
        //创建房间
        List<String> clientIds = new ArrayList<String>();
        clientIds.add(PreferenceHelper.getUserName(context));
        clientIds.add(PreferenceHelper.getBindingName(context));
        // 我们给对话增加一个自定义属性 type，表示单聊还是群聊
        // 常量定义：
        // int ConversationType_OneOne = 0; // 两个人之间的单聊
        // int ConversationType_Group = 1;  // 多人之间的群聊
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put("type", 0);
        AVIMClient.getInstance(PreferenceHelper.getUserName(context))
                .createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation conversation, AVIMException e) {
                        if (null != conversation) {
                            // 成功了，这时候可以显示对话的 Activity 页面（假定为 ChatActivity）了。
                            LogHelper.Log(TAG, "房间创建成功");
                            //TODO---将ID保存下来
                            PreferenceHelper.setConversationId(context, conversation.getConversationId());
                        } else {
                            LogHelper.Log(TAG, "wrong 1:    " + e.toString());
                        }
                    }
                });
    }

    /**
     * 获取我和Ta的对话
     * @param context
     * @return
     */
    public static AVIMConversation getConversation(Context context) {
        if (PreferenceHelper.getConversationId(context) == null) {
            return null;
        }
        AVIMClient client = AVIMClient.getInstance(PreferenceHelper.getUserName(context));
        return client.getConversation(PreferenceHelper.getConversationId(context));
    }

    /**
     * 建立连接
     * @param context
     */
    public static void openConnection(Context context) {
        AVIMClient avimClient = AVIMClient.getInstance(PreferenceHelper.getUserName(context));
        avimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    LogHelper.Log(TAG, "我登录成功");
                } else {
                    LogHelper.Log(TAG, "wrong 0");
                }
            }
        });
    }
}
