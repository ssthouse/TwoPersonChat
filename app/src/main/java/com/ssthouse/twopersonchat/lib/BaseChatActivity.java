package com.ssthouse.twopersonchat.lib;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVUser;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.style.TransparentStyle;

/**
 * Created by ssthouse on 2015/8/6.
 */
public abstract class BaseChatActivity extends AppCompatActivity {

    private AVUser meUser;
    private AVUser taUser;

    public abstract AVUser getMeUser();

    public abstract AVUser getTaUser();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_chat);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        //初始化用户数据
        meUser = getMeUser();
        taUser = getTaUser();
    }
}
