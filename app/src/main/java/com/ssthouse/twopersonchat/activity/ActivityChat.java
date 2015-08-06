package com.ssthouse.twopersonchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.avos.avoscloud.AVUser;
import com.ssthouse.twopersonchat.lib.BaseChatActivity;
import com.ssthouse.twopersonchat.util.PreferenceHelper;
import com.ssthouse.twopersonchat.util.ViewHelper;

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


        initView();
    }

    private void initView(){
        ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, PreferenceHelper.getBindingName(this));
    }

    @Override
    public AVUser getMeUser() {
        //TODO
        return null;
    }

    @Override
    public AVUser getTaUser() {
        //TODO
        return null;
    }
}
