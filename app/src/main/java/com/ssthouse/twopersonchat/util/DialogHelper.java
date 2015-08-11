package com.ssthouse.twopersonchat.util;

import android.content.Context;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ssthouse.twopersonchat.R;

/**
 * Created by ssthouse on 2015/8/8.
 */
public class DialogHelper {

    public static void showNoInternetDialog(Context context){
        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder
                .withTitle("请稍候")                                  //.withTitle(null)  no title
                .withMessage("当前网络不可用")
                .withTitleColor("#FFFFFF")                              //def
                .withDividerColor(0x00ffffff)                              //def
                .withDialogColor(context.getResources().getColor(R.color.btn_pressed))                               //def  | withDialogColor(int resid)
                .withEffect(Effectstype.Fadein)                           //def Effectstype.Slidetop
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .show();
    }

    public static void showInviteDialog(final Context context, final AVIMTypedMessage msg){
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder
                .withTitle(msg.getFrom()+"邀请你加入他的二人世界")                                  //.withTitle(null)  no title
                .withMessage("你接受吗?")
                .withTitleColor("#FFFFFF")                              //def
                .withDividerColor(0x00ffffff)                              //def
                .withDialogColor(context.getResources().getColor(R.color.btn_pressed))                               //def  | withDialogColor(int resid)
                .withEffect(Effectstype.Fadein)                           //def Effectstype.Slidetop
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withButton1Text("接受")
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //将消息中的conversation的id保存到本地
                        PreferenceHelper.setConversationId(context, ((AVIMTextMessage)msg).getText());
                        dialogBuilder.dismiss();
                    }
                })
                .withButton1Text("拒绝")
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .show();
    }

}
