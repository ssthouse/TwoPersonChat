package com.ssthouse.twopersonchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.PreferenceHelper;
import com.ssthouse.twopersonchat.util.ToastHelper;
import com.ssthouse.twopersonchat.util.ViewHelper;

/**
 * Created by ssthouse on 2015/8/4.
 */
public class ActivityRegister extends AppCompatActivity {
    private static String TAG = "ActivityRegister";

    private AVUser user;

    private EditText etUserName;
    private EditText etPassword;
    private Button btnRegister;

    public static void start(Activity activity){
        activity.startActivity(new Intent(activity, ActivityRegister.class));
//        LogHelper.Log(TAG, "我进入了---registerActivity");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        user = new AVUser();
        initView();
    }

    private void initView(){
        ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, "注册");

        etUserName = (EditText) findViewById(R.id.id_et_username);
        etPassword = (EditText) findViewById(R.id.id_et_password);
        //注册按钮点击事件
        btnRegister = (Button) findViewById(R.id.id_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有网
                if (!NetworkHelper.isNetworkAvailable(ActivityRegister.this)) {
                    ToastHelper.showSnack(ActivityRegister.this, etUserName, "当前网络不可用");
                    return;
                }
                //判断输入是不是合法
                if (TextUtils.isEmpty(etUserName.getText()) || TextUtils.isEmpty(etPassword.getText())){
                    ToastHelper.showSnack(ActivityRegister.this, etUserName, "用户名或密码不合法");
                    return;
                }
                user.setUsername(etUserName.getText().toString());
                user.setPassword(etPassword.getText().toString());
                //尝试登陆
                user.signUpInBackground(new SignUpCallback() {
                    public void done(AVException e) {
                    if (e == null) {
                        // successfully
                        PreferenceHelper.setIsFistIn(ActivityRegister.this, false);
                        ToastHelper.showSnack(ActivityRegister.this,etUserName, "注册成功!");
                        //TODO---自动登陆---并跳转
                        final String userName = etUserName.getText().toString();
                        final String passWord = etPassword.getText().toString();
                        AVUser.logInInBackground(userName, passWord,
                            new LogInCallback<AVUser>() {
                                @Override
                                public void done(AVUser avUser, AVException e) {
                                    if (avUser != null) {
                                        // 登录成功
                                        PreferenceHelper.LogIn(ActivityRegister.this, userName, passWord);
                                        ActivityMain.start(ActivityRegister.this);
                                        finish();
                                    } else {
                                        // 登录失败
                                        ToastHelper.showToast(ActivityRegister.this, "登陆失败");
                                    }
                                }
                            });
                    } else {
                        // failed
                        ToastHelper.showSnack(ActivityRegister.this, etUserName, "抱歉,当前用户名已被注册!");
                    }
                    }
                });
             }
        });

        //登陆按钮---跳转到登陆界面
        findViewById(R.id.id_btn_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                PreferenceHelper.setIsFistIn(ActivityRegister.this, false);
                ActivityLogin.start(ActivityRegister.this);
            }
        });
    }
}
