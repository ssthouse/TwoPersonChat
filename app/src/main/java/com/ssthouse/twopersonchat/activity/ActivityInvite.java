package com.ssthouse.twopersonchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.lib.message.InviteMessage;
import com.ssthouse.twopersonchat.util.LogHelper;
import com.ssthouse.twopersonchat.util.PreferenceHelper;
import com.ssthouse.twopersonchat.util.ToastHelper;

/**
 * Created by ssthouse on 2015/8/11.
 */
public class ActivityInvite extends AppCompatActivity {
    private static final String TAG = "ActivityInvite";

    private InviteMessage msg;

    public static void start(Context context, AVIMTypedMessage msg) {
        Intent intent = new Intent(context, ActivityInvite.class);
        intent.putExtra("msg", msg);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        msg = (InviteMessage) getIntent().getParcelableExtra("msg");

        initView();
    }

    private void initView() {
        findViewById(R.id.id_btn_accept_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg == null) {
                    ToastHelper.showToast(ActivityInvite.this, "我得到的msg时空的!!!");
                    LogHelper.Log(TAG, "我得到的msg时空的");
                }
                //将消息中的conversation的id保存到本地
                PreferenceHelper.setConversationId(ActivityInvite.this, msg.getText());
                LogHelper.Log(TAG, "新的ConversationId保存成功");
                LogHelper.Log(TAG, "是   " + msg.getText());
                finish();
            }
        });
    }
}
