package com.ssthouse.twopersonchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.model.AVFileConstant;
import com.ssthouse.twopersonchat.model.User;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.FileHelper;
import com.ssthouse.twopersonchat.util.LogHelper;
import com.ssthouse.twopersonchat.util.PreferenceHelper;
import com.ssthouse.twopersonchat.util.ToastHelper;
import com.ssthouse.twopersonchat.util.ViewHelper;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 注册Activity
 * Created by ssthouse on 2015/8/4.
 */
public class ActivityRegister extends AppCompatActivity {
    private static String TAG = "ActivityRegister";

    private User user;

    private EditText etUserName;
    private EditText etPassword;

    private NiftyDialogBuilder dialogBuilder;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ActivityRegister.class));
//        LogHelper.Log(TAG, "我进入了---registerActivity");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        user = new User();
        dialogBuilder  = NiftyDialogBuilder.getInstance(this);

        initView();
    }

    private void initView() {
        final ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, "注册");

        etUserName = (EditText) findViewById(R.id.id_et_username);
        etPassword = (EditText) findViewById(R.id.id_et_password);

        //注册按钮点击事件
        final Button btnRegister = (Button) findViewById(R.id.id_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有网
                if (!NetworkHelper.isNetworkAvailable(ActivityRegister.this)) {
                    ToastHelper.showSnack(ActivityRegister.this, etUserName, "当前网络不可用");
                    return;
                }
                //判断输入是不是合法
                if (TextUtils.isEmpty(etUserName.getText()) || TextUtils.isEmpty(etPassword.getText())) {
                    ToastHelper.showSnack(ActivityRegister.this, etUserName, "用户名或密码不合法");
                    return;
                }
                showWaitDialog();
                //开启上传任务
                new AsyncTask<Void, Void, AVFile>() {
                    @Override
                    protected AVFile doInBackground(Void... params) {
                        //上传头像文件
                        AVFile avatarFile = null;
                        try {
                            avatarFile = AVFile.withFile(AVFileConstant.avatarFileName,
                                    new File(FileHelper.AVATAR_PATH_AND_NAME));
                            avatarFile.save();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (AVException e1) {
                            e1.printStackTrace();
                        }
                        LogHelper.Log(TAG, "我完成了");
                        return avatarFile;
                    }

                    @Override
                    protected void onPostExecute(AVFile avFile) {
                        //填充User信息
                        user.setAvatar(avFile);
                        user.setUsername(etUserName.getText().toString());
                        user.setPassword(etPassword.getText().toString());
                        user.setIsBoy(true);
                        user.setMotto("我的宣言");
                        user.setBornDate(new Date());
                        //尝试注册
                        user.signUpInBackground(new SignUpCallback() {
                            public void done(AVException e) {
                                if (e == null) {
                                    // successfully
                                    btnRegister.setClickable(false);
                                    login();
                                } else {
                                    // failed
                                    ToastHelper.showSnack(ActivityRegister.this, etUserName, "抱歉,当前用户名已被注册!");
                                    btnRegister.setClickable(true);
                                }
                            }
                        });
                        LogHelper.Log(TAG, "我在主线程完成了");
                        dialogBuilder.dismiss();
                        super.onPostExecute(avFile);
                    }
                }.execute();
            }
        });

        //已有账号---直接登陆
        findViewById(R.id.id_btn_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ActivityLogin.start(ActivityRegister.this);
            }
        });
    }

    private void login() {
        PreferenceHelper.setIsFistIn(ActivityRegister.this, false);
        ToastHelper.showSnack(ActivityRegister.this, etUserName, "注册成功!");
        final String userName = etUserName.getText().toString();
        final String passWord = etPassword.getText().toString();
        User.logInInBackground(userName, passWord,
                new LogInCallback<User>() {
                    @Override
                    public void done(User avUser, AVException e) {
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
                }, User.class);
    }

    private void showWaitDialog(){
        LinearLayout ll = (LinearLayout) View.inflate(this, R.layout.dialog_progress_bar, null);
        dialogBuilder
                .withTitle("请稍候")                                  //.withTitle(null)  no title
                .withMessage(null)
                .withTitleColor("#FFFFFF")                              //def
                .withDividerColor(0x00ffffff)                              //def
                .withDialogColor(getResources().getColor(R.color.color_primary_light))                               //def  | withDialogColor(int resid)
                .withEffect(Effectstype.Fadein)                           //def Effectstype.Slidetop
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .setCustomView(ll, this)         //.setCustomView(View or ResId,context)
                .show();
    }
}
