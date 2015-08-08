package com.ssthouse.twopersonchat.model;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;

import java.util.Date;

/**
 * Created by ssthouse on 2015/8/4.
 * <p/>
 * bornDate---Date
 * <p/>
 * taUserName---String
 * <p/>
 * isBoy---boolean
 */
public class User extends AVUser {
    private static final String TAG = "User";

    //出生日期
    private static final String BORN_DATE = "bornData";
    private Date bornDate = new Date();

    //宣言--座右铭---motto
    private static final String MOTTO = "motto";
    private String motto = "";

    //是不是男的
    private static final String IS_BOY = "isBoy";
    private boolean isBoy = true;

    //Avatar
    public static final String AVATAR = "avatar";
    private AVFile avatar;


    //出生日期
    public void setBornDate(Date date) {
        this.put(BORN_DATE, date);
    }

    public Date getBornDate() {
        return (Date) get(BORN_DATE);
    }

    //Avatar
    public void setAvatar(AVFile avatar){
        this.put(AVATAR, avatar);
    }

    public AVFile getAvatar(){
        return this.getAVFile(AVATAR);
    }

    //性别
    public void setIsBoy(boolean isBoy) {
        this.put(IS_BOY, isBoy);
    }

    public boolean getIsBoy() {
        return this.getBoolean(IS_BOY);
    }

    //宣言
    public void setMotto(String motto){
        this.put(MOTTO, motto);
    }

    public String getMotto(){
        return this.getString(MOTTO);
    }
}
