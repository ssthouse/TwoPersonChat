package com.ssthouse.twopersonchat.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ssthouse on 2015/8/6.
 */
public class PictureHelper {
    private static final String TAG = "PictureHelper";

    /**
     * 开启照相机获取照片
     *
     *@paramactivity
     */
    public static void getPictureFromCamera(Activity activity,int requestCode, String pathAndName) {
        LogHelper.Log(TAG, "from camera");
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(pathAndName);
            Uri u = Uri.fromFile(file);
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
            activity.startActivityForResult(intent, requestCode);
        }else{
            Toast.makeText(activity, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开启图库获取照片
     *
     * @param activity
     */
    public static void getPictureFromAlbum(Activity activity, int requestCode) {
        LogHelper.Log(TAG, "from album");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 将图片保存到指定的目录
     *
     * @param photo
     * @param savePath
     * @return
     */
    public static boolean saveImage(Bitmap photo, String savePath) {
        try {
            File file = new File(savePath);
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(savePath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.Log(TAG, "something is wrong");
            return false;
        }
        return true;
    }

    /**
     * 将图片从指定文件夹---复制到目标文件夹
     *
     * @param srcPath
     * @param targetPath
     * @return
     */
    public static boolean saveImage(String srcPath, String targetPath) {
//        Log.e(TAG, "我是源文件" + srcPath);
//        Log.e(TAG, "我是目标文件" + targetPath);
        //判断路径是否为空
        if (srcPath == null || targetPath == null) {
            return false;
        }
        try {
            //判断源文件是否存在
            File srcFile = new File(srcPath);
            if (srcFile.exists() == false) {
                return false;
            }
            //如果目标文件不存在---创建
            File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            //设置目标文件权限
            targetFile.setReadable(true);
            targetFile.setWritable(true);
            targetFile.setExecutable(true);
            //复制文件
            FileInputStream fosFrom = new java.io.FileInputStream(srcFile);
            FileOutputStream fosTo = new FileOutputStream(targetFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosFrom.read(bt)) > 0) {
                fosTo.write(bt, 0, c); //将内容写到新文件当中
            }
            fosFrom.close();
            fosTo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 获取压缩后的bitmap
     *
     * @param imagePath
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getSmallBitmap(String imagePath, int width, int height) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

}
