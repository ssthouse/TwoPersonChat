package com.ssthouse.twopersonchat.lib;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.LogHelper;
import com.ssthouse.twopersonchat.util.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ssthouse on 2015/8/6.
 */
public abstract class BaseChatActivity extends AppCompatActivity {
    private static final String TAG = "BaseChatActivity";

    private String myName;
    private String taName;

    public abstract String getMyName();

    public abstract String getTaName();

    private AVIMClient avimClient;
    private AVIMConversation conversation;

    private EditText etMsg;
    private Button btnSendMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        //初始化用户数据
        myName = getMyName();
        taName = getTaName();


        //建立连接
        avimClient = AVIMClient.getInstance(myName);
        avimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    LogHelper.Log(TAG, "我登录成功");
                    creatConversation();
                } else {
                    LogHelper.Log(TAG, "wrong 0");
                }
            }
        });


        //创建---消息接收器
        AVIMMessageManager.registerDefaultMessageHandler(new AVIMMessageHandler() {
            @Override
            public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
                //打印消息
                LogHelper.Log(TAG, "收到消息:   " + message.getContent());
                super.onMessage(message, conversation, client);
            }
        });

        initView();
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, taName);

        etMsg = (EditText) findViewById(R.id.id_et_msg);

        btnSendMsg = (Button) findViewById(R.id.id_btn_send_msg);
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送文字消息
                sendTextMsg();
            }
        });
    }

    public void sendTextMsg() {
        AVIMMessage message = new AVIMMessage();
        message.setContent("hello");
        conversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (null != e) {
                    // 出错了。。。
                    LogHelper.Log(TAG, "wrong 2");
                    e.printStackTrace();
                } else {
                    LogHelper.Log(TAG, "文字发送成功");
                }
            }
        });
    }

    public void creatConversation(){
        //创建房间
        List<String> clientIds = new ArrayList<String>();
        clientIds.add(myName);
        clientIds.add(taName);
        // 我们给对话增加一个自定义属性 type，表示单聊还是群聊
        // 常量定义：
        // int ConversationType_OneOne = 0; // 两个人之间的单聊
        // int ConversationType_Group = 1;  // 多人之间的群聊
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put("type", 0);
        avimClient.createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation conversation, AVIMException e) {
                if (null != conversation) {
                    // 成功了，这时候可以显示对话的 Activity 页面（假定为 ChatActivity）了。
                    BaseChatActivity.this.conversation = conversation;
                    LogHelper.Log(TAG, "房间创建成功");
                } else {
                    LogHelper.Log(TAG, "wrong 1:    " + e.toString());
                }
            }
        });
    }
}
