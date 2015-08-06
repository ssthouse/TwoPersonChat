package com.ssthouse.twopersonchat.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssthouse.twopersonchat.R;

/**
 * Created by ssthouse on 2015/8/5.
 */
public class FragmentChat extends Fragment {

    private Context context;

    public FragmentChat(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, null);
        initView(view);
        return view;
    }

    private void initView(View view){

    }
}
