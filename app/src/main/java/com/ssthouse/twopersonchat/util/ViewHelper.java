package com.ssthouse.twopersonchat.util;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ssthouse.twopersonchat.R;

/**
 * Created by ssthouse on 2015/8/5.
 */
public class ViewHelper {

    public static void initActionBar(AppCompatActivity activity, ActionBar actionBar, String title){
        actionBar.setBackgroundDrawable(activity.getResources().getDrawable(R.color.color_primary));
        actionBar.setTitle(title);
    }

    public static void hideKeyMap(Context context, View view){
        //1.得到InputMethodManager对象
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //获取状态信息
        boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if(isOpen){
            //2.调用hideSoftInputFromWindow方法隐藏软键盘
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
    }
}
