package com.ssthouse.twopersonchat.util;

import android.content.Context;

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

}
