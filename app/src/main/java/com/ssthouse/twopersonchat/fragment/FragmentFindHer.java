package com.ssthouse.twopersonchat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.util.ToastHelper;

import java.util.List;

/**
 * Created by ssthouse on 2015/8/4.
 */
public class FragmentFindHer extends Fragment {
    private static final String TAG = "FragmentFindHer";

    public interface OnFindHerListener {
        void showFind(AVUser avUser);
    }

    private OnFindHerListener onFindHerListener;

    private Context context;

    private EditText etUserName;

    public FragmentFindHer() {

    }

    private FragmentFindHer(Context context) {
        this.context = context;
        onFindHerListener = (OnFindHerListener) context;
    }

    public static FragmentFindHer getInstance(Context context){
        return new FragmentFindHer(context);
    }

    private void initView(View view) {
        etUserName = (EditText) view.findViewById(R.id.id_et_her_user_name);

        view.findViewById(R.id.id_btn_find_her).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etUserName.getText())) {
                    //TODO---尝试搜索ta的信息
                    AVQuery<AVUser> avQuery = AVUser.getQuery();
                    avQuery.whereEqualTo("username", etUserName.getText());
                    avQuery.findInBackground(new FindCallback<AVUser>() {
                        @Override
                        public void done(List<AVUser> list, AVException e) {
                            if (e == null && list != null && list.size() == 1) {
                                //回调Main
                                ToastHelper.showSnack(context, etUserName, "找到了");
//                                LogHelper.Log(TAG, "找到了");
                                onFindHerListener.showFind(list.get(0));
                            }
                        }
                    });
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_her, null);
        initView(view);
        return view;
    }
}
