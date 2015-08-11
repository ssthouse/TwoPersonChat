package com.ssthouse.twopersonchat.lib.message;

import com.avos.avoscloud.im.v2.AVIMMessageField;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

import java.util.Map;

/**
 * 请求加入的message---type为1
 * Created by ssthouse on 2015/8/11.
 */
@AVIMMessageType(type = 1)
public class InviteMessage extends AVIMTypedMessage {

    @AVIMMessageField(name = "_lctext")
    String text;

    @AVIMMessageField(name = "_lcattrs")
    Map<String, Object> attrs;

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getAttrs() {
        return this.attrs;
    }

    public void setAttrs(Map<String, Object> attr) {
        this.attrs = attr;
    }
}
