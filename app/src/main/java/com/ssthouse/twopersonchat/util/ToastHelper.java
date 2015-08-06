package com.ssthouse.twopersonchat.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ssthouse.twopersonchat.R;


/**
 * Created by ssthouse on 2015/7/15.
 */
public class ToastHelper {

    public static void showSnack(Context context, View view, String toastStr) {
        //隐藏键盘
        hideKeyMap(context, view);
        //show toast
        Snackbar snackbar = Snackbar.make(view, toastStr, Snackbar.LENGTH_SHORT);
        snackbar.getView().getLayoutParams().height = (int) context.getResources()
                .getDimension(R.dimen.snack_bar_height);
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.color_primary));
        TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        tv.setTextColor(context.getResources().getColor(R.color.white));
        tv.setTextSize(16);
        snackbar.show();
    }

    public static void showToast(Context context, String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
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
