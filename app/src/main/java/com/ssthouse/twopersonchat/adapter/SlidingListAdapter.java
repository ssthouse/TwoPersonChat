package com.ssthouse.twopersonchat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.activity.ActivitySetting;
import com.ssthouse.twopersonchat.activity.ActivityUserInfo;

/**
 * TODO
 * 暂时----只有2项
 * Created by ssthouse on 2015/8/6.
 */
public class SlidingListAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    public SlidingListAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (position){
            case 0: {
                //个人信息
                LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.list_item_fragment_sliding, null);
                ImageView iv = (ImageView) ll.findViewById(R.id.id_iv);
                iv.setImageResource(R.drawable.icon_menu);
                TextView tv = (TextView) ll.findViewById(R.id.id_tv);
                tv.setText("个人信息");
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUserInfo.start((Activity) context);
                    }
                });
                convertView = ll;
                break;
            }
            case 1: {
                //设置
                LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.list_item_fragment_sliding, null);
                ImageView iv = (ImageView) ll.findViewById(R.id.id_iv);
                iv.setImageResource(R.drawable.icon_menu);
                TextView tv = (TextView) ll.findViewById(R.id.id_tv);
                tv.setText("设置");
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivitySetting.start((Activity) context);
                    }
                });
                convertView = ll;
                break;
            }
        }
        return convertView;
    }
}
