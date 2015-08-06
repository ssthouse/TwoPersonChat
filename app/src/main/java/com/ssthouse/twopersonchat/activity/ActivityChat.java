package com.ssthouse.twopersonchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.PreferenceHelper;
import com.ssthouse.twopersonchat.util.ViewHelper;

/**
 * Created by ssthouse on 2015/8/5.
 */
public class ActivityChat extends AppCompatActivity{

    public static void start(Activity activity){
        activity.startActivity(new Intent(activity, ActivityChat.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        initView();
    }

    private void initView(){
        ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, PreferenceHelper.getBindingName(this));
    }
}
