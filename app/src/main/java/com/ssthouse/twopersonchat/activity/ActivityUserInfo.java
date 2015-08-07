package com.ssthouse.twopersonchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.FileHelper;
import com.ssthouse.twopersonchat.util.LogHelper;
import com.ssthouse.twopersonchat.util.PictureHelper;
import com.ssthouse.twopersonchat.util.ViewHelper;

/**
 * 用户信息Activity
 * Created by ssthouse on 2015/8/6.
 */
public class ActivityUserInfo extends AppCompatActivity {
    private static final String TAG = "ActivityUserInfo";

    private static final int REQUEST_ALBUM = 1001;
    private static final int REQUEST_CAMERA = 1002;

    private ImageView ivAvatar;

    public static void start(Activity activity){
        activity.startActivity(new Intent(activity, ActivityUserInfo.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        initView();
    }

    private void initView(){
        ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, "我的资料");

        //头像设置
        ivAvatar = (ImageView) findViewById(R.id.id_iv_avatar);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlbumOrCameraDialog();
            }
        });

        findViewById(R.id.id_cv_user_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.Log(TAG, "我点到了CardView");
            }
        });
    }

    private void showAlbumOrCameraDialog(){
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        LinearLayout ll = (LinearLayout) View.inflate(this, R.layout.dialog_album_or_camera, null);
        ll.findViewById(R.id.id_tv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureHelper.getPictureFromCamera(ActivityUserInfo.this,
                        REQUEST_CAMERA, FileHelper.AVATAR_PATH_AND_NAME);
                dialogBuilder.dismiss();
            }
        });
        ll.findViewById(R.id.id_tv_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureHelper.getPictureFromAlbum(ActivityUserInfo.this, REQUEST_ALBUM);
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder
                .withTitle("选择头像")                                  //.withTitle(null)  no title
                .withMessage(null)
                .withTitleColor("#FFFFFF")                              //def
                .withDividerColor(0x00ffffff)                              //def
                .withDialogColor(getResources().getColor(R.color.color_primary_light))                               //def  | withDialogColor(int resid)
                .withEffect(Effectstype.Fadein)                           //def Effectstype.Slidetop
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .setCustomView(ll, this)         //.setCustomView(View or ResId,context)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //判断是图库---还是照相机
        switch (requestCode){
            case REQUEST_ALBUM:
                if (null != data) {
                    //根据uri获取图片路径
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String srcPath = cursor.getString(columnIndex);
                    cursor.close();
                    //将照片保存到指定文件夹
                    PictureHelper.saveImage(srcPath, FileHelper.AVATAR_PATH_AND_NAME);
                    //更新界面
                    ivAvatar.setImageBitmap(PictureHelper.getSmallBitmap(FileHelper.AVATAR_PATH_AND_NAME,
                            120, 120));
                }
                break;
            case REQUEST_CAMERA:
                //TODO---应该是可以直接填充上去的
//                Picasso.with(this).load(new File(FileHelper.AVATAR_PATH_AND_NAME))
//                        .
//                        .into(ivAvatar);
                ivAvatar.setImageBitmap(PictureHelper.getSmallBitmap(FileHelper.AVATAR_PATH_AND_NAME,
                        120, 120));
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
