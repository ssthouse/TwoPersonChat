package com.ssthouse.twopersonchat.lib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.util.FileHelper;
import com.ssthouse.twopersonchat.util.LogHelper;
import com.ssthouse.twopersonchat.util.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天列表的适配器
 * 需要区分不同种类的消息----填充不同的数据
 * Created by ssthouse on 2015/8/9.
 */
public class ChatListAdapter extends BaseAdapter {

    private static final String TAG = "ChatListAdapter";

    private Context context;
    private LayoutInflater inflater;
    //ListView要展现的数据
    private List<AVIMTypedMessage> msgList;

    //缓存下来两个人的头像
    private Bitmap meAvatarBitmap, taAvatarBitmap;

    public ChatListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        msgList = new ArrayList<>();
        //填充本地数据
        meAvatarBitmap = BitmapFactory.decodeFile(FileHelper.AVATAR_PATH_AND_NAME);
        taAvatarBitmap = BitmapFactory.decodeFile(FileHelper.TA_AVATAR_PATH_AND_NAME);
        //TODO---获取最近的10条消息-----填充进来
    }

    /**
     * 添加新消息
     *
     * @param msg
     */
    public void addMessage(AVIMTypedMessage msg, ListView lv) {
        msgList.add(msg);
        notifyDataSetChanged();
        //滚动到最下方
        lv.smoothScrollToPosition(this.getCount() - 1);
        LogHelper.Log(TAG, "我刷新了listview");
    }

    @Override
    public int getCount() {
//        LogHelper.Log(TAG, "我现在有---" + msgList.size() + "个消息View");
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (msgList.get(position).getMessageType()) {
            //文本消息
            case -1:
                AVIMTextMessage msg = (AVIMTextMessage) msgList.get(position);
                //判断是不是自己发的
                if (msg.getFrom().equals(PreferenceHelper.getUserName(context))) {
                    //inflate出View
                    convertView = inflater.inflate(R.layout.chat_item_base_right, null);
                    //填充头像
                    ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.id_iv_avatar);
                    ivAvatar.setImageBitmap(meAvatarBitmap);
                    //填充文字内容
                    TextView tvContent = (TextView) inflater.inflate(R.layout.chat_item_text, null);
                    tvContent.setTextColor(0xffffffff);
                    tvContent.setText(msg.getText());
                    //文字内容放入View
                    LinearLayout llContainer = (LinearLayout) convertView.findViewById(R.id.id_ll_content);
                    llContainer.addView(tvContent);
                } else {
                    //inflate出View
                    convertView = inflater.inflate(R.layout.chat_item_base_left, null);
                    //填充头像
                    ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.id_iv_avatar);
                    ivAvatar.setImageBitmap(taAvatarBitmap);
                    //填充文字内容
                    TextView tvContent = (TextView) inflater.inflate(R.layout.chat_item_text, null);
                    tvContent.setText(msg.getText());
                    //文字内容放入View
                    LinearLayout llContainer = (LinearLayout) convertView.findViewById(R.id.id_ll_content);
                    llContainer.addView(tvContent);
                }
                return convertView;
            default:
                return convertView;
        }

    }
}
