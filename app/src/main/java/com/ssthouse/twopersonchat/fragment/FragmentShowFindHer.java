package com.ssthouse.twopersonchat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.util.PreferenceHelper;

/**
 * Created by ssthouse on 2015/8/5.
 */
public class FragmentShowFindHer extends Fragment {
    private static final String TAG = "FragmentShowFinder";

    private AVUser avUser;
    private Context context;

    public FragmentShowFindHer(Context context, AVUser avUser){
        this.avUser = avUser;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_find_her, null);
        initView(view);
        return view;
    }

    private void initView(View view){
        TextView tvUerName = (TextView) view.findViewById(R.id.id_tv_user_name);
        tvUerName.setText(avUser.getUsername());

        TextView tvPassWord = (TextView) view.findViewById(R.id.id_tv_pass_word);
        tvPassWord.setText(avUser.getObjectId());

        view.findViewById(R.id.id_btn_choose_her).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO---要在网络上--和本地--都绑定
                //本地
                PreferenceHelper.setBinding(context, avUser.getUsername());
                //网络
                //AVUser.getCurrentUser(User.class).setTaUserName(avUser.getUsername());
                //TODO---如果成功了--退回
                getActivity().finish();
            }
        });
    }
}
