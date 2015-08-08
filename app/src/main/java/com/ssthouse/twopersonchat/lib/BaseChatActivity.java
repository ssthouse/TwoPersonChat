package com.ssthouse.twopersonchat.lib;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import com.ssthouse.twopersonchat.lib.view.EmotionEditText;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.LogHelper;
import com.ssthouse.twopersonchat.util.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天的父类Activity
 * Created by ssthouse on 2015/8/6.
 */
public abstract class BaseChatActivity extends AppCompatActivity
        implements ChatActivityEventListener {
    private static final String TAG = "BaseChatActivity";

    private String myName;
    private String taName;
    public abstract String getMyName();
    public abstract String getTaName();

    private AVIMClient avimClient;
    private AVIMConversation conversation;

    //刷新View
    private SwipeRefreshLayout swipeRefreshLayout;
    //展示内容的ListView
    private ListView lv;
    //一直在的输入按钮
    private Button btnMore;
    private Button btnEmotion;
    //Emotion的pager
    private ViewPager vpEmotion;
    //一直在----add more
    private TextView btnAddPicture, btnAddCamera, btnAddLocation;
    //文字输入
    private EmotionEditText et;
    private Button btnSendMsg;
    private Button btnTurn2Voice;
    //语音输入
    private Button btnRecord;
    private Button btnTurn2Text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        //初始化用户数据
        myName = getMyName();
        taName = getTaName();

        //TODO---建立连接
        avimClient = AVIMClient.getInstance(myName);
        avimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    LogHelper.Log(TAG, "我登录成功");
                    createConversation();
                } else {
                    LogHelper.Log(TAG, "wrong 0");
                }
            }
        });

        //好像也要放在application中
        //TODO---创建---消息接收器
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

    //发送---其他消息的监听器
    private View.OnClickListener addMoreListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

        }
    };

    //emotion的监听器
    private View.OnClickListener emotionListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

        }
    };

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, taName);

        //刷新View
        swipeRefreshLayout.setColorSchemeResources(R.color.color_primary_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO
                swipeRefreshLayout.setRefreshing(true);
                LogHelper.Log(TAG, "i am refresh ing");
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
                LogHelper.Log(TAG, "我的监听器起作用了");
            }
        });

        //主ListView
        lv = (ListView) findViewById(R.id.id_lv);

        //一直在的输入按钮
        btnMore = (Button) findViewById(R.id.id_btn_show_more);
        btnEmotion = (Button) findViewById(R.id.id_btn_show_emotion);

        //Emotion的pager
        vpEmotion = (ViewPager) findViewById(R.id.id_vp_emotion);

        //一直在的表情---其他信息
        btnAddPicture = (TextView) findViewById(R.id.id_btn_add_picture);
        btnAddCamera = (TextView) findViewById(R.id.id_btn_add_camera);
        btnAddLocation = (TextView) findViewById(R.id.id_btn_add_location);
        btnAddPicture.setOnClickListener(addMoreListener);
        btnAddCamera.setOnClickListener(addMoreListener);
        btnAddLocation.setOnClickListener(addMoreListener);

        //文字输入
        et = (EmotionEditText) findViewById(R.id.id_et_msg);
        btnSendMsg = (Button) findViewById(R.id.id_btn_send_msg);
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送文字消息
                sendTextMsg();
            }
        });
        btnTurn2Voice = (Button) findViewById(R.id.id_btn_turn_2_voice);

        //语音输入
        btnRecord = (Button) findViewById(R.id.id_btn_record);
        btnTurn2Text = (Button) findViewById(R.id.id_btn_turn_2_text);
    }

    private void inflateEmotionPager(){

    }

    /**
     * TODO
     * 发送文字消息
     */
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

    /**
     * TODO---好像应该放到application中
     * 创建对话
     */
    public void createConversation() {
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
