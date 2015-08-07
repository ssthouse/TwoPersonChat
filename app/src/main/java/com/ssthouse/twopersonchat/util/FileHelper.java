package com.ssthouse.twopersonchat.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 具体路径--看有道笔记的记录
 * Created by ssthouse on 2015/8/7.
 */
public class FileHelper {
    private static final String TAG = "FileHlper";

    public static final String ROOT_PATH = "/storage/sdcard0/ssthouse/";
    public static final String AVATAR_PATH = ROOT_PATH + "avatar/";
    public static final String AVATAR_PATH_AND_NAME = ROOT_PATH + "avatar/avatar.jpg";


    /**
     * 初始化app存储路径
     * @param context
     */
    public static void initAppDirectory(Context context){
        //用户头像Avatar
        File avatarFile = new File(AVATAR_PATH_AND_NAME);
        if(!avatarFile.exists()){
            avatarFile.getParentFile().mkdirs();
            try {
                avatarFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                LogHelper.Log(TAG, "something is wrong1");
            }
            //将文件替换为...drawable中的文件
            try {
                //复制文件到指定路径
                copyFile(context.getAssets().open("avatar.png"), AVATAR_PATH_AND_NAME);
            } catch (IOException e) {
                e.printStackTrace();
                LogHelper.Log(TAG, "something is wrong2");
            }
            LogHelper.Log(TAG, "Avatar生成完毕");
        }
        //TODO---还有什么文件路径??
    }

    /**
     * 复制单个文件
     *
     * @return boolean
     */
    public static void copyFile(InputStream is, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            FileOutputStream fos = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];
            while ((byteread = is.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                fos.write(buffer, 0, byteread);
            }
            is.close();
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    public static void copyFile(File file, String newPath) {
        try {
            FileInputStream fis = new FileInputStream(file);
            int byteread = 0;
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];
            while ((byteread = fis.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            fis.close();
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }
}
