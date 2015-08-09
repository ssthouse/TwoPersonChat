package com.ssthouse.twopersonchat.lib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.lib.util.EmotionHelper;
import com.ssthouse.twopersonchat.util.LogHelper;

import java.util.List;

/**
 * 每一页图标的Adapter
 * Created by ssthouse on 2015/8/9.
 */
public class EmotionGridAdapter extends BaseAdapter {
    private static final String TAG = "EmotionGridAdapter";

    private Context context;
    private LayoutInflater inflater;

    private List<String> strList;

    public EmotionGridAdapter(Context context, int position) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        strList = EmotionHelper.emojiGroups.get(position);
    }

    @Override
    public int getCount() {
//        LogHelper.Log(TAG, "我这一页的数量是---" + strList.size());
        return strList.size();
    }

    @Override
    public Object getItem(int position) {
        return strList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.chat_emotion_item, null);
            ImageView iv = (ImageView) ll.findViewById(R.id.id_iv_emotion_item);
            //算出emotion字符串
            String emotion = strList.get(position);
            emotion = emotion.substring(1, emotion.length() - 1);
            LogHelper.Log(TAG, emotion);
            iv.setImageBitmap(EmotionHelper.getEmojiDrawable(context, emotion));
            convertView = ll;
        } else {
            ImageView iv = (ImageView) convertView.findViewById(R.id.id_iv_emotion_item);
            //算出emotion字符串
            String emotion = strList.get(position);
            emotion = emotion.substring(1, emotion.length() - 1);
            iv.setImageBitmap(EmotionHelper.getEmojiDrawable(context, emotion));
        }
//        LogHelper.Log(TAG, "我设置了emotion 图标");
        return convertView;
    }
}
