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
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.lib.message.InviteMessage;
import com.ssthouse.twopersonchat.lib.util.ChatHelper;
import com.ssthouse.twopersonchat.util.PreferenceHelper;
import com.ssthouse.twopersonchat.util.ToastHelper;

/**
 * Created by ssthouse on 2015/8/5.
 */
public class FragmentShowFindHer extends Fragment {
    private static final String TAG = "FragmentShowFinder";

    private AVUser avUser;
    private Context context;

    public FragmentShowFindHer(Context context, AVUser avUser) {
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

    private void initView(View view) {
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
                //TODO---发送一条消息过去---前提是已经创建好了--conversation
                //先创建--conversation
                ChatHelper.createConversation(context);
                //创建验证消息
                InviteMessage msg = new InviteMessage();
                msg.setText(PreferenceHelper.getConversationId(context));
                //发送验证消息
                AVIMConversation conversation = ChatHelper.getConversation(context);
                if (conversation != null) {
                    conversation.sendMessage(msg, new AVIMConversationCallback() {
                        @Override
                        public void done(AVIMException e) {
                            if (e == null) {
                                ToastHelper.showToast(context, "请求发送成功");
                            } else {

                                ToastHelper.showToast(context, "请求发送失败");
                            }
                        }
                    });
                }
            }
        });
    }
}
