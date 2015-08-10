package com.ssthouse.twopersonchat.lib.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.ssthouse.twopersonchat.R;
import com.ssthouse.twopersonchat.lib.util.EmotionHelper;
import com.ssthouse.twopersonchat.lib.view.EmotionEditText;
import com.ssthouse.twopersonchat.lib.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 固定的是4页
 * 将输入框放进来---这样---每一页表情都在这里初始化---设置点击事件---减少ChatActivity中的代码量
 * Created by ssthouse on 2015/8/9.
 */
public class EmotionAdapter extends PagerAdapter {
    private static final String TAG = "EMotionAdpter";

    private Context context;

    private List<LinearLayout> llList;
    private EmotionEditText emotionEditText;

    public EmotionAdapter(Context context, EmotionEditText emotionEditText) {
        //初始化数据
        EmotionHelper.isEmojiDrawableAvailable(context);
        this.context = context;
        this.emotionEditText = emotionEditText;
        llList = new ArrayList<>();
        //初始化每一页的表情
        llList.add(getPage(0));
        llList.add(getPage(1));
        llList.add(getPage(2));
        //TODO----设置每一页表情的点击事件
    }

    /**
     * 获取每一页的表情
     *
     * @param position
     * @return
     */
    private LinearLayout getPage(int position) {
        LinearLayout ll = (LinearLayout) View.inflate(context, R.layout.chat_emotion_gridview, null);
        MyGridView gv = (MyGridView) ll.findViewById(R.id.id_gv);
//        LogHelper.Log(TAG, "我添加了第   " + position + "  个Adapter");
        gv.setAdapter(new EmotionGridAdapter(context, position));
        //TODO-----非常重要---emoji点击事件
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String emotionText = (String) parent.getAdapter().getItem(position);
                int start = emotionEditText.getSelectionStart();
                StringBuffer sb = new StringBuffer(emotionEditText.getText());
                sb.replace(emotionEditText.getSelectionStart(), emotionEditText.getSelectionEnd(), emotionText);
                emotionEditText.setText(sb.toString());

                CharSequence info = emotionEditText.getText();
                if (info instanceof Spannable) {
                    Spannable spannable = (Spannable) info;
                    Selection.setSelection(spannable, start + emotionText.length());
                }
            }
        });
        return ll;
    }

    @Override
    public int getCount() {
//        LogHelper.Log(TAG, "我总共页数是----" + llList.size());
        return llList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(llList.get(position));

//        LogHelper.Log(TAG, "我添加了第------" + position + "页");
        return llList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(llList.get(position));
    }
}
