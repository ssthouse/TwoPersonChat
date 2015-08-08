package com.ssthouse.twopersonchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.model.AVFileConstant;
import com.ssthouse.twopersonchat.model.User;
import com.ssthouse.twopersonchat.style.TransparentStyle;
import com.ssthouse.twopersonchat.util.DialogHelper;
import com.ssthouse.twopersonchat.util.FileHelper;
import com.ssthouse.twopersonchat.util.PictureHelper;
import com.ssthouse.twopersonchat.util.PreferenceHelper;
import com.ssthouse.twopersonchat.util.ToastHelper;
import com.ssthouse.twopersonchat.util.ViewHelper;

import java.io.File;
import java.io.IOException;

/**
 * 用户信息Activity
 * Created by ssthouse on 2015/8/6.
 */
public class ActivityUserInfo extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ActivityUserInfo";

    private static final int REQUEST_ALBUM = 1001;
    private static final int REQUEST_CAMERA = 1002;

    private ImageView ivAvatar;

    private TextView tvUserName;

    private TextView tvBoy, tvGirl;

    private TextView tvMotto;

    private TextView tvTa;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ActivityUserInfo.class));
    }

    public static void startForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ActivityUserInfo.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        TransparentStyle.setAppToTransparentStyle(this, getResources().getColor(R.color.color_primary_dark));

        initView();

        inflateView();
    }


    @Override
    public void onClick(View v) {
        if (!NetworkHelper.isNetworkAvailable(this)) {
            DialogHelper.showNoInternetDialog(this);
            return;
        }
        switch (v.getId()) {
            case R.id.id_iv_avatar:
                showAlbumOrCameraDialog();
                break;
            case R.id.id_tv_user_name:
                showUserNameDialog();
                break;
            case R.id.id_tv_boy: {
                User user = User.getCurrentUser(User.class);
                user.setIsBoy(true);
                user.saveInBackground(isBoyCallback);
                break;
            }
            case R.id.id_tv_girl: {
                User user = User.getCurrentUser(User.class);
                user.setIsBoy(false);
                user.saveInBackground(isNotBoyCallback);
                break;
            }
            case R.id.id_tv_motto:
                showMottoDialog();
                break;
            case R.id.id_tv_binding_user_name:
                ActivityFindHer.start(this);
                break;
        }
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        ViewHelper.initActionBar(this, actionBar, "我的资料");

        //头像设置
        ivAvatar = (ImageView) findViewById(R.id.id_iv_avatar);
        ivAvatar.setOnClickListener(this);

        //用户名
        tvUserName = (TextView) findViewById(R.id.id_tv_user_name);
        tvUserName.setOnClickListener(this);

        //性别
        tvBoy = (TextView) findViewById(R.id.id_tv_boy);
        tvBoy.setOnClickListener(this);
        tvGirl = (TextView) findViewById(R.id.id_tv_girl);
        tvGirl.setOnClickListener(this);

        //Motto---宣言
        tvMotto = (TextView) findViewById(R.id.id_tv_motto);
        tvMotto.setOnClickListener(this);

        //Ta
        tvTa = (TextView) findViewById(R.id.id_tv_binding_user_name);
        tvTa.setOnClickListener(this);
    }

    //填充数据
    private void inflateView() {
        ivAvatar.setImageBitmap(BitmapFactory.decodeFile(FileHelper.AVATAR_PATH_AND_NAME));

        tvUserName.setText(PreferenceHelper.getUserName(this));

        if (PreferenceHelper.isBoy(this)) {
            tvBoy.setBackgroundColor(getResources().getColor(R.color.btn_pressed));
        } else {
            tvGirl.setBackgroundColor(getResources().getColor(R.color.btn_pressed));
        }

        tvMotto.setText(PreferenceHelper.getMotto(this));

        tvTa.setText(PreferenceHelper.getBindingName(this));
    }

    private SaveCallback isBoyCallback = new SaveCallback() {
        @Override
        public void done(AVException e) {
            if (e == null) {
                PreferenceHelper.setIsBoy(ActivityUserInfo.this, true);
                ToastHelper.showSnack(ActivityUserInfo.this, ivAvatar, "保存成功");
                tvGirl.setBackgroundColor(0xffffff);
                tvBoy.setBackgroundColor(getResources().getColor(R.color.btn_pressed));
            } else {
                ToastHelper.showSnack(ActivityUserInfo.this, ivAvatar, "保存失败");
            }
        }
    };

    private SaveCallback isNotBoyCallback = new SaveCallback() {
        @Override
        public void done(AVException e) {
            if (e == null) {
                PreferenceHelper.setIsBoy(ActivityUserInfo.this, false);
                ToastHelper.showSnack(ActivityUserInfo.this, ivAvatar, "保存成功");
                tvBoy.setBackgroundColor(0xffffff);
                tvGirl.setBackgroundColor(getResources().getColor(R.color.btn_pressed));
            } else {
                ToastHelper.showSnack(ActivityUserInfo.this, ivAvatar, "保存失败");
            }
        }
    };

    private void showAlbumOrCameraDialog() {
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
                .withDialogColor(getResources().getColor(R.color.btn_pressed))                               //def  | withDialogColor(int resid)
                .withEffect(Effectstype.Fadein)                           //def Effectstype.Slidetop
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .setCustomView(ll, this)         //.setCustomView(View or ResId,context)
                .show();
    }

    private void showMottoDialog() {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        LinearLayout ll = (LinearLayout) View.inflate(this, R.layout.dialog_et_motto, null);
        final EditText et = (EditText) ll.findViewById(R.id.id_et);
        View.OnClickListener positiveListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et.getText())) {
                    User user = User.getCurrentUser(User.class);
                    user.setMotto(et.getText().toString());
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                dialogBuilder.dismiss();
                                ToastHelper.showSnack(ActivityUserInfo.this, ivAvatar, "保存成功");
                                PreferenceHelper.setMotto(ActivityUserInfo.this, et.getText().toString());
                                inflateView();
                            } else {
                                ToastHelper.showSnack(ActivityUserInfo.this, ivAvatar, "保存失败");
                            }
                        }
                    });
                }
            }
        };
        View.OnClickListener negativeListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        };
        dialogBuilder
                .withTitle("Motto")                                  //.withTitle(null)  no title
                .withMessage(null)
                .withTitleColor("#FFFFFF")                              //def
                .withDividerColor(0x00ffffff)                              //def
                .withDialogColor(getResources().getColor(R.color.btn_pressed))                               //def  | withDialogColor(int resid)
                .withEffect(Effectstype.Fadein)                           //def Effectstype.Slidetop
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .setCustomView(ll, this)         //.setCustomView(View or ResId,context)
                .withButton1Text("取消")
                .setButton1Click(negativeListener)
                .withButton2Text("确认")
                .setButton2Click(positiveListener)
                .show();
    }

    private void showUserNameDialog() {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        LinearLayout ll = (LinearLayout) View.inflate(this, R.layout.dialog_user_name, null);
        final EditText et = (EditText) ll.findViewById(R.id.id_et);
        View.OnClickListener positiveListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et.getText())) {
                    User user = User.getCurrentUser(User.class);
                    user.setUsername(et.getText().toString());
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                dialogBuilder.dismiss();
                                ToastHelper.showSnack(ActivityUserInfo.this, ivAvatar, "保存成功");
                                PreferenceHelper.setUserName(ActivityUserInfo.this, et.getText().toString());
                                inflateView();
                            } else {
                                ToastHelper.showSnack(ActivityUserInfo.this, ivAvatar, "保存失败");
                            }
                        }
                    });
                }
            }
        };
        View.OnClickListener negativeListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        };
        dialogBuilder
                .withTitle("用户名")                                  //.withTitle(null)  no title
                .withMessage(null)
                .withTitleColor("#FFFFFF")                              //def
                .withDividerColor(0x00ffffff)                              //def
                .withDialogColor(getResources().getColor(R.color.btn_pressed))                               //def  | withDialogColor(int resid)
                .withEffect(Effectstype.Fadein)                           //def Effectstype.Slidetop
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .setCustomView(ll, this)         //.setCustomView(View or ResId,context)
                .withButton1Text("取消")
                .setButton1Click(negativeListener)
                .withButton2Text("确认")
                .setButton2Click(positiveListener)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //判断是图库---还是照相机
        switch (requestCode) {
            case REQUEST_ALBUM:
                if (null != data && resultCode == RESULT_OK) {
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
                    //将大图片换成小图片
                    Bitmap bitmap = PictureHelper.getSmallBitmap(FileHelper.AVATAR_PATH_AND_NAME, 200, 200);
                    PictureHelper.saveImage(bitmap, FileHelper.AVATAR_PATH_AND_NAME);
                    //更新界面
                    ivAvatar.setImageBitmap(BitmapFactory.decodeFile(FileHelper.AVATAR_PATH_AND_NAME));
                    //上传照片
                    final User user = User.getCurrentUser(User.class);
                    //开启上传任务
                    new AsyncTask<Void, Void, Exception>() {
                        @Override
                        protected Exception doInBackground(Void... params) {
                            //上传头像文件
                            AVFile avatarFile = null;
                            try {
                                avatarFile = AVFile.withFile(AVFileConstant.avatarFileName,
                                        new File(FileHelper.AVATAR_PATH_AND_NAME));
                                avatarFile.save();
                                user.setAvatar(avatarFile);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                return e1;
                            } catch (AVException e1) {
                                e1.printStackTrace();
                                return e1;
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Exception e) {
                            if (e == null) {
                                ToastHelper.showToast(ActivityUserInfo.this, "上传完成");
                            } else {
                                ToastHelper.showToast(ActivityUserInfo.this, "上传失败");
                            }
                            super.onPostExecute(e);
                        }
                    }.execute();
                }
                break;
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    //TODO---应该是可以直接填充上去的
                    //将大图片换成小图片
                    Bitmap bitmap = PictureHelper.getSmallBitmap(FileHelper.AVATAR_PATH_AND_NAME, 200, 200);
                    PictureHelper.saveImage(bitmap, FileHelper.AVATAR_PATH_AND_NAME);
                    ivAvatar.setImageBitmap(BitmapFactory.decodeFile(FileHelper.AVATAR_PATH_AND_NAME));
                    final User user = User.getCurrentUser(User.class);
                    //开启上传任务
                    new AsyncTask<Void, Void, Exception>() {
                        @Override
                        protected Exception doInBackground(Void... params) {
                            //上传头像文件
                            AVFile avatarFile = null;
                            try {
                                avatarFile = AVFile.withFile(AVFileConstant.avatarFileName,
                                        new File(FileHelper.AVATAR_PATH_AND_NAME));
                                avatarFile.save();
                                user.setAvatar(avatarFile);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                return e1;
                            } catch (AVException e1) {
                                e1.printStackTrace();
                                return e1;
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Exception e) {
                            if (e == null) {
                                ToastHelper.showToast(ActivityUserInfo.this, "上传完成");
                            } else {
                                ToastHelper.showToast(ActivityUserInfo.this, "上传失败");
                            }
                            super.onPostExecute(e);
                        }
                    }.execute();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
//        super.onBackPressed();
    }
}
