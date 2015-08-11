package com.ssthouse.twopersonchat.lib;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.lib.adapter.ChatListAdapter;
import com.ssthouse.twopersonchat.lib.adapter.EmotionAdapter;
import com.ssthouse.twopersonchat.lib.util.ChatHelper;
import com.ssthouse.twopersonchat.lib.view.EmotionEditText;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.LogHelper;
import com.ssthouse.twopersonchat.util.PreferenceHelper;
import com.ssthouse.twopersonchat.util.ViewHelper;

import java.util.List;

/**
 * 聊天的父类Activity
 * Created by ssthouse on 2015/8/6.
 */
public abstract class BaseChatActivity extends AppCompatActivity
        implements ChatActivityEventListener, View.OnClickListener {
    private static final String TAG = "BaseChatActivity";

    //初始时加载的消息数量
    private static final int PAGE_SIZE = 10;

    //消息处理相关
    private AVIMClient avimClient;
    private AVIMConversation conversation;
    private InnerHandler handler;

    //数据处理相关
    private ChatListAdapter adapter;

    //UI-------------------------------------------
    private SwipeRefreshLayout swipeRefreshLayout;
    //展示内容的ListView
    private ListView lv;
    //一直在的输入按钮
    private Button btnMore;
    private Button btnEmotion;
    //Emotion的pager
    private LinearLayout llEmotion;
    private ViewPager vpEmotion;
    //更多的----View
    private LinearLayout llMore;
    private TextView btnAddPicture, btnAddCamera, btnAddLocation;
    //文字输入
    private LinearLayout llInputText;
    private EmotionEditText et;
    private Button btnSendMsg;
    private Button btnTurn2Voice;
    //语音输入
    private LinearLayout llInputVoice;
    private Button btnRecord;
    private Button btnTurn2Text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        //如果没有建立对话创建Conversation
        if (PreferenceHelper.getConversationId(this) == null) {
            ChatHelper.createConversation(this);
        }

        //初始化数据
        avimClient = AVIMClient.getInstance(PreferenceHelper.getUserName(this));
        conversation = ChatHelper.getConversation(this);
        handler = new InnerHandler();

        //设置消息监听器
        MsgHandler.setActivityMessageHandler(handler);

        initView();

        //加载历史对话
        loadMessagesWhenInit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //加号按钮
            case R.id.id_btn_show_more:
                if (llMore.getVisibility() == View.VISIBLE) {
                    llMore.setVisibility(View.GONE);
                } else {
                    ViewHelper.hideKeyMap(this, swipeRefreshLayout);
                    llEmotion.setVisibility(View.GONE);
                    llMore.setVisibility(View.VISIBLE);
                }
                break;
            //Emoji按钮
            case R.id.id_btn_show_emotion:
                if (llEmotion.getVisibility() == View.VISIBLE) {
                    llEmotion.setVisibility(View.GONE);
                } else {
                    ViewHelper.hideKeyMap(this, swipeRefreshLayout);
                    llEmotion.setVisibility(View.VISIBLE);
                    llMore.setVisibility(View.GONE);
                    //自动切换为文字模式
                    llInputText.setVisibility(View.VISIBLE);
                    llInputVoice.setVisibility(View.GONE);
                }
                break;
            //转语音按钮
            case R.id.id_btn_turn_2_voice:
                ViewHelper.hideKeyMap(this, swipeRefreshLayout);
                llEmotion.setVisibility(View.GONE);
                llMore.setVisibility(View.GONE);
                llInputText.setVisibility(View.GONE);
                llInputVoice.setVisibility(View.VISIBLE);
                break;
            //转文字按钮
            case R.id.id_btn_turn_2_text:
                ViewHelper.hideKeyMap(this, swipeRefreshLayout);
                llEmotion.setVisibility(View.GONE);
                llMore.setVisibility(View.GONE);
                llInputText.setVisibility(View.VISIBLE);
                llInputVoice.setVisibility(View.GONE);
                break;
            //发送按钮
            case R.id.id_btn_send_msg:
                sendTextMsg();
                break;
        }
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, PreferenceHelper.getBindingName(this));

        //刷新View
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_refresh);
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
        adapter = new ChatListAdapter(this);
        lv.setAdapter(adapter);

        //一直在的输入按钮
        btnMore = (Button) findViewById(R.id.id_btn_show_more);
        btnMore.setOnClickListener(this);
        btnEmotion = (Button) findViewById(R.id.id_btn_show_emotion);
        btnEmotion.setOnClickListener(this);

        //文字输入----输入框
        llInputText = (LinearLayout) findViewById(R.id.id_ll_chat_input_text);
        btnSendMsg = (Button) findViewById(R.id.id_btn_send_msg);
        btnSendMsg.setOnClickListener(this);
        btnTurn2Voice = (Button) findViewById(R.id.id_btn_turn_2_voice);
        btnTurn2Voice.setOnClickListener(this);
        et = (EmotionEditText) findViewById(R.id.id_et_msg);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    btnTurn2Voice.setVisibility(View.VISIBLE);
                    btnSendMsg.setVisibility(View.GONE);
                } else {
                    btnTurn2Voice.setVisibility(View.GONE);
                    btnSendMsg.setVisibility(View.VISIBLE);
                }
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    llEmotion.setVisibility(View.GONE);
                    llMore.setVisibility(View.GONE);
                }
            }
        });

        //语音输入
        llInputVoice = (LinearLayout) findViewById(R.id.id_ll_chat_input_voice);
        btnRecord = (Button) findViewById(R.id.id_btn_record);
        btnTurn2Text = (Button) findViewById(R.id.id_btn_turn_2_text);
        btnTurn2Text.setOnClickListener(this);

        //Emotion的pager
        llEmotion = (LinearLayout) findViewById(R.id.id_ll_emotion);
        vpEmotion = (ViewPager) findViewById(R.id.id_vp_emotion);
        vpEmotion.setAdapter(new EmotionAdapter(this, et));

        //更多---其他信息
        llMore = (LinearLayout) findViewById(R.id.id_ll_more);
        btnAddPicture = (TextView) findViewById(R.id.id_btn_add_picture);
        btnAddCamera = (TextView) findViewById(R.id.id_btn_add_camera);
        btnAddLocation = (TextView) findViewById(R.id.id_btn_add_location);
        btnAddPicture.setOnClickListener(this);
        btnAddCamera.setOnClickListener(this);
        btnAddLocation.setOnClickListener(this);
    }

    private void loadMessagesWhenInit() {
        //防止conversation为空
        if (conversation == null) {
            conversation = avimClient.getConversation(PreferenceHelper.getConversationId(this));
            LogHelper.Log(TAG, "我更新了conversation");
        }
        conversation.queryMessages(PAGE_SIZE, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (e == null) {
                    adapter.addMessage(list, lv);
                    LogHelper.Log(TAG, "我把历史记录添加进去了adapter");
                } else {
                    LogHelper.Log(TAG, "历史记录查询失败....");
                }
            }
        });
    }

    /**
     * TODO
     * 发送文字消息
     */
    public void sendTextMsg() {
        if (TextUtils.isEmpty(et.getText())) {
            return;
        }
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(et.getText().toString());
        if (conversation == null) {
            conversation = avimClient.getConversation(PreferenceHelper.getConversationId(this));
            LogHelper.Log(TAG, "我更新了conversation");
        }
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
        //TODO---更新视图
        et.setText("");
        adapter.addMessage(message, lv);
    }


    /**
     * 对话内的Handler
     */
    public class InnerHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

        @Override
        public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
//            LogHelper.Log(TAG, message.getContent());
            //只用放进去就好了-------剩下的交给adapter处理
            adapter.addMessage(message, lv);
        }

        @Override
        public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
//            ToastHelper.showToast(BaseChatActivity.this, "对方已接收");
            LogHelper.Log(TAG, message.getContent() + "   对方已接收");
        }
    }


    //监听消息的Handler的生命周期------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        MsgHandler.setActivityMessageHandler(handler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MsgHandler.setActivityMessageHandler(null);
    }
}
