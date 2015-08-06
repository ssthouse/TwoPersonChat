package com.ssthouse.twopersonchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ssthouse.twopersonchat.R;

/**
 * Created by ssthouse on 2015/8/6.
 */
public class ActivitySetting extends AppCompatActivity {
    private static final String TAG = "ActivitySetting";

    public static void start(Activity activity){
        activity.startActivity(new Intent(activity, ActivitySetting.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }
}
