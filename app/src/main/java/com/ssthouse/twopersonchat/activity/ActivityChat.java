package com.ssthouse.twopersonchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.lib.BaseChatActivity;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.PreferenceHelper;

/**
 * Created by ssthouse on 2015/8/5.
 */
public class ActivityChat extends BaseChatActivity {

    public static void start(Activity activity){
        activity.startActivity(new Intent(activity, ActivityChat.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        initView();
    }

    private void initView(){

    }

    @Override
    public String getMyName() {
        return PreferenceHelper.getUserName(this);
    }

    @Override
    public String getTaName() {
        return PreferenceHelper.getBindingName(this);
    }

    @Override
    public void onAddLocationButtonClicked(View v) {

    }

    @Override
    public void onLocationMessageViewClicked(AVIMLocationMessage locationMessage) {

    }

    @Override
    public void onImageMessageViewClicked(AVIMImageMessage imageMessage, String localImagePath) {

    }
}
