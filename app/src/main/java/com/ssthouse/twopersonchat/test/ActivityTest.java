package com.ssthouse.twopersonchat.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.ssthouse.twopersonchat.util.FileHelper;

/**
 * Created by ssthouse on 2015/8/7.
 */
public class ActivityTest extends AppCompatActivity {
    private static final String TAG = "ActivityTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new LinearLayout(this));

        test();
    }

    private void test(){
        FileHelper.initAppDirectory(this);
    }

}
