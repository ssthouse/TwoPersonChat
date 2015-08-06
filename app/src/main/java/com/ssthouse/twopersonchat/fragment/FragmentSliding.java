package com.ssthouse.twopersonchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssthouse.twopersonchat.R;

/**
 * 主Activity的侧滑Fragment
 * Created by ssthouse on 2015/8/5.
 */
public class FragmentSliding extends Fragment {

    private ImageView iv;
    private TextView tvUserName;
    private TextView tvMotto;

    /**
     * 填充用户数据
     */
    public void initUser(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_sliding, null);
       initView(view);
        return view;
    }

    private void initView(View view){
        iv = (ImageView) view.findViewById(R.id.id_iv_avater);
        tvUserName = (TextView) view.findViewById(R.id.id_tv_user_name);
        tvMotto = (TextView) view.findViewById(R.id.id_tv_motto);
    }
}
