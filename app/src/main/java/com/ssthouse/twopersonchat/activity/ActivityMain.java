package com.ssthouse.twopersonchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.fragment.FragmentChat;
import com.ssthouse.twopersonchat.fragment.FragmentRecord;
import com.ssthouse.twopersonchat.fragment.FragmentSliding;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.PreferenceHelper;
import com.ssthouse.twopersonchat.util.ToastHelper;
import com.ssthouse.twopersonchat.util.ViewHelper;

public class ActivityMain extends AppCompatActivity {
    private static final String TAG = "ActivityMain";

    private static final int SM_ID = Integer.MAX_VALUE -100;

    private static int REQUEST_RGISTER = 1001;
    private static int REQUEST_LOG_IN = 1002;
    private static int REQUEST_FIND_HER = 1003;

    /**
     * 没网的LinearLayout
     */
    private LinearLayout llNoInternet;

    private FragmentChat fragmentChat;
    private FragmentRecord fragmentRecord;

    private View drawerView;
    private DrawerLayout drawerLayout;
    private FrameLayout smContainer;
    private FragmentSliding fragmentSliding;

    private ViewPager viewPager;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ActivityMain.class));
//        LogHelper.Log(TAG, "我进入了MainActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        //判断是否是第一次
        if (PreferenceHelper.isFistIn(this)) {
            ActivityRegister.start(this);
            finish();
            return;
        }

        //判断是否已经登陆
        if (!PreferenceHelper.isLogIn(this)) {
            ActivityLogin.start(this);
            finish();
            return;
        }

        initView();

        //尝试登陆
        AVUser.logInInBackground(PreferenceHelper.getUserName(this),
                PreferenceHelper.getPassWord(this),
                new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (avUser == null) {
                            llNoInternet.setVisibility(View.VISIBLE);
                        } else {
                            ToastHelper.showSnack(ActivityMain.this, llNoInternet, "登陆成功");
                        }
                    }
                });
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, "MainActivity");
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);

        //有没有网
        llNoInternet = (LinearLayout) findViewById(R.id.id_ll_no_internet);

        //SlidingMenu
        initSlidingMenu();

        //主界面
        fragmentChat = new FragmentChat(this);
        fragmentRecord = new FragmentRecord(this);
        viewPager = (ViewPager) findViewById(R.id.id_view_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return fragmentChat;
                } else {
                    return fragmentRecord;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        //快捷聊天按钮
        findViewById(R.id.id_btn_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否已经绑定
                if (PreferenceHelper.isBinding(ActivityMain.this)) {
                    ActivityChat.start(ActivityMain.this);
                } else {
                    ActivityFindHer.start(ActivityMain.this);
                }
            }
        });
    }

    private void initSlidingMenu()
    {
        //找到drawer控件
        smContainer = (FrameLayout) findViewById(R.id.id_sm_container);
        //添加SlidingMenu
        fragmentSliding = new FragmentSliding();
        getSupportFragmentManager().beginTransaction().replace(R.id.id_sm_container, fragmentSliding)
                .commit();
        //找到DrawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.id_drawer_layout);
        //设置监听器
        android.support.v4.app.ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.mipmap.ic_launcher,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
            }
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerSlide(View inDrawerView, float slideOffset) {
                super.onDrawerSlide(inDrawerView, slideOffset);
//                drawerView.setRotation(slideOffset * 90);
//                drawerView.setScaleX(0.5f * slideOffset);
//                drawerView.setScaleY(0.5f * slideOffset);
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(drawerLayout.isDrawerOpen(smContainer)) {
                    drawerLayout.closeDrawer(smContainer);
                }else{
                    drawerLayout.openDrawer(smContainer);
                }
                break;
            case R.id.id_action_log_out:
                if (AVUser.getCurrentUser() != null) {
                    //注销登陆---跳转到LogIn的activity
                    PreferenceHelper.LogOut(ActivityMain.this);
                    AVUser.logOut();
                    ActivityLogin.start(this);
                    finish();
                } else {
                    ToastHelper.showSnack(this, llNoInternet, "你现在..本来就没登录");
                    ActivityLogin.start(this);
                    finish();
                }
                break;
            case R.id.id_action_find_her:
                //跳转FindHerActivity
                ActivityFindHer.start(this);
                break;
            case R.id.id_action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * FragmentFindHer的回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
