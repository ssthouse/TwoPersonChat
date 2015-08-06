package com.ssthouse.twopersonchat.model;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.ssthouse.twopersonchat.util.ToastHelper;

import java.util.Date;

/**
 * Created by ssthouse on 2015/8/4.
 */
public class User extends AVUser{

    private Context context;

    //出生日期
    private static final String BORN_DATE = "bornData";
    private Date bornDate;

    //绑定的Ta
    private static final String TA_USER_NAME = "taUserName";
    private String taUserName;

    public User(){

    }

    public User(Context context){
        this.context = context;
    }


    public void setBornDate(Date date){
        this.put(BORN_DATE, date);
        if(!NetworkHelper.isNetworkAvailable(context)){
            ToastHelper.showToast(context, "当前无网络");
            return;
        }
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e ==  null){
                    ToastHelper.showToast(context, "保存失败");
                }else{
                    ToastHelper.showToast(context, "保存成功");
                }
            }
        });
    }

    public Date getBornDate(){
        return (Date) get(BORN_DATE);
    }

    public void setTaUserName(String taUserName){
        this.put(TA_USER_NAME, taUserName);
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e ==  null){
                    ToastHelper.showToast(context, "保存失败");
                }else{
                    ToastHelper.showToast(context, "保存成功");
                }
            }
        });
    }
}
