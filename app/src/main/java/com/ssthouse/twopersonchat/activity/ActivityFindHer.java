package com.ssthouse.twopersonchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.avos.avoscloud.AVUser;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.fragment.FragmentFindHer;
import com.ssthouse.twopersonchat.fragment.FragmentShowFindHer;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.ViewHelper;

/**
 * 为了安全---开启时---手动获取云端的用户信息
 * Created by ssthouse on 2015/8/4.
 */
public class ActivityFindHer extends AppCompatActivity implements
        FragmentFindHer.OnFindHerListener{
    private static final String TAG = "ActivityFindHer";

    private FrameLayout continer;

    private android.support.v4.app.FragmentManager fragmentManager;
    private FragmentShowFindHer fragmentShowFindHer;
    private FragmentFindHer fragmentFindHer;

    public static void start(Activity activity){
        activity.startActivity(new Intent(activity, ActivityFindHer.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_her);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        fragmentFindHer = new FragmentFindHer(this);
        fragmentManager = getSupportFragmentManager();

        initView();
    }

    private void initView(){
        ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, "Find Her");

        continer = (FrameLayout) findViewById(R.id.id_container);
        fragmentManager.beginTransaction().replace(R.id.id_container, fragmentFindHer).commit();
    }

    /**
     * 找到her的资料的时候的回调
     * @param avUser
     */
    @Override
    public void showFind(AVUser avUser) {
        fragmentShowFindHer = new FragmentShowFindHer(this, avUser);
        fragmentManager.beginTransaction().replace(R.id.id_container, fragmentShowFindHer).commit();
    }
}
