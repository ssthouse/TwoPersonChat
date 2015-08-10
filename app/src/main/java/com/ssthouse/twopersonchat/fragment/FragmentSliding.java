package com.ssthouse.twopersonchat.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.activity.ActivityUserInfo;
import com.ssthouse.twopersonchat.adapter.SlidingListAdapter;
import com.ssthouse.twopersonchat.util.FileHelper;
import com.ssthouse.twopersonchat.util.PreferenceHelper;

/**
 * 主Activity的侧滑Fragment
 * Created by ssthouse on 2015/8/5.
 */
public class FragmentSliding extends Fragment {

    private ImageView iv;
    private TextView tvUserName;
    private TextView tvMotto;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sliding, null);
        initView(view);
        inflateView();
        return view;
    }


    private void initView(View view) {
        //初始化User信息
        iv = (ImageView) view.findViewById(R.id.id_iv_avatar);

        tvUserName = (TextView) view.findViewById(R.id.id_tv_user_name);

        tvMotto = (TextView) view.findViewById(R.id.id_tv_motto);

        //上方头像在的LinearLayout
        view.findViewById(R.id.id_ll_user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUserInfo.start(getActivity());
            }
        });

        //初始化listView
        ListView lv = (ListView) view.findViewById(R.id.id_lv);
        lv.setAdapter(new SlidingListAdapter(getActivity()));
    }

    public void inflateView() {
        iv.setImageBitmap(BitmapFactory.decodeFile(FileHelper.AVATAR_PATH_AND_NAME));

        tvUserName.setText(PreferenceHelper.getUserName(getActivity()));

        tvMotto.setText(PreferenceHelper.getMotto(getActivity()));
    }
}
