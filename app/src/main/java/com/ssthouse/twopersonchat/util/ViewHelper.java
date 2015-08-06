package com.ssthouse.twopersonchat.util;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.ssthouse.twopersonchat.R;

/**
 * Created by ssthouse on 2015/8/5.
 */
public class ViewHelper {

    public static void initActionBar(AppCompatActivity activity, ActionBar actionBar, String title){
        actionBar.setBackgroundDrawable(activity.getResources().getDrawable(R.color.color_primary));
        actionBar.setTitle(title);
    }
}
