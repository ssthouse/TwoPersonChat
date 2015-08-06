package com.ssthouse.twopersonchat.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 管理preferencer的工具类
 * Created by ssthouse on 2015/7/28.
 */
public class PreferenceHelper {
    private static final String TAG = "PreferenceHelper";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String PREFERENCE = "preference";

    //是否第一次
    private static final String IS_FIST_IN = "isFistIn";

    //是否已经本地登陆
    private static final String IS_LOG_IN = "isLogIn";
    private static final String USER_NAME = "userName";
    private static final String PASS_WORD = "passWord";

    //判断是否绑定Ta
    private static final String IS_BINDING = "isBinding";
    private static final String TA_USER_NAME = "taUserName";

    public static boolean isBinding(Context context){
        initSharedPreference(context);
        return sharedPreferences.getBoolean(IS_BINDING, false);
    }

    public static void setIsBinding(Context context, boolean isBinded){
        initSharedPreference(context);
        initEditor(context);
        editor.putBoolean(IS_BINDING, isBinded);
        editor.commit();
    }

    public static String getBindingName(Context context){
        initSharedPreference(context);
        return sharedPreferences.getString(TA_USER_NAME, "");
    }

    public static void setBinding(Context context, String taUserName){
        if(context == null || taUserName == null){
            return;
        }
        initSharedPreference(context);
        initEditor(context);
        setIsBinding(context, true);
        editor.putString(TA_USER_NAME, taUserName);
        editor.commit();
    }

    /**
     * 判断是否登陆
     *
     * @param context
     * @return
     */
    public static boolean isLogIn(Context context) {
        initSharedPreference(context);
        return sharedPreferences.getBoolean(IS_LOG_IN, false);
    }

    /**
     * 登陆
     *
     * @param context
     */
    public static void LogIn(Context context, String userName, String password) {
        initSharedPreference(context);
        initEditor(context);
        editor.putBoolean(IS_LOG_IN, true);
        editor.putString(USER_NAME, userName);
        editor.putString(PASS_WORD, password);
        editor.commit();
    }

    /**
     * 登出
     *
     * @param context
     */
    public static void LogOut(Context context) {
        initSharedPreference(context);
        initEditor(context);
        editor.putBoolean(IS_LOG_IN, false);
        editor.commit();
    }

    public static boolean isFistIn(Context context) {
        initSharedPreference(context);
        return sharedPreferences.getBoolean(IS_FIST_IN, true);
    }

    public static void setIsFistIn(Context context, boolean isFistIn) {
        initSharedPreference(context);
        initEditor(context);
        editor.putBoolean(IS_FIST_IN, isFistIn);
        editor.commit();
    }

    public static void setUserName(Context context, String userName) {
        initSharedPreference(context);
        initEditor(context);
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public static void setPassWord(Context context, String password) {
        initSharedPreference(context);
        initEditor(context);
        editor.putString(PASS_WORD, password);
        editor.commit();
    }

    public static String getUserName(Context context) {
        initSharedPreference(context);
        return sharedPreferences.getString(USER_NAME, "");
    }

    public static String getPassWord(Context context) {
        initSharedPreference(context);
        return sharedPreferences.getString(PASS_WORD, "");
    }

    //*************************************************************************
    private static void initSharedPreference(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        }
    }

    private static void initEditor(Context context) {
        if (editor == null) {
            editor = sharedPreferences.edit();
        }
    }

}
