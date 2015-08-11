package com.ssthouse.twopersonchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.lib.util.ChatHelper;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.PreferenceHelper;
import com.ssthouse.twopersonchat.util.ToastHelper;
import com.ssthouse.twopersonchat.util.ViewHelper;

/**
 * Created by ssthouse on 2015/8/4.
 */
public class ActivityLogin extends AppCompatActivity {
    private static final String TAG = "ActivityLogin";

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ActivityLogin.class));
//        LogHelper.Log(TAG, "我进入了LogInActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        initView();
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, "登陆");

        final EditText etUserName = (EditText) findViewById(R.id.id_et_username);
        final EditText etPassword = (EditText) findViewById(R.id.id_et_password);

        findViewById(R.id.id_btn_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etUserName.getText()) && !TextUtils.isEmpty(etPassword.getText())) {
                    //尝试登陆
                    final String userName = etUserName.getText().toString();
                    final String passWord = etPassword.getText().toString();
                    AVUser.logInInBackground(userName, passWord,
                            new LogInCallback<AVUser>() {
                                @Override
                                public void done(AVUser avUser, AVException e) {
                                    if (avUser != null) {
                                        // 登录成功
                                        PreferenceHelper.LogIn(ActivityLogin.this, userName, passWord);
                                        finish();
                                        ActivityMain.start(ActivityLogin.this);
                                        //TODO---登陆
                                        ChatHelper.openConnection(ActivityLogin.this);
                                    } else {
                                       // 登录失败
                                        ToastHelper.showToast(ActivityLogin.this, "登陆失败");
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_action_register:
                ActivityRegister.start(this);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
