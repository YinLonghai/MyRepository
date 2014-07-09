package com.guotion.sicilia.bean.net;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:15
 * To change this template use File | Settings | File Templates.
 */
public class ChatHistory {
    public String _id="";
    /**
     * ChatGroup类型，或者String类型
     */
    public Object chatGroup;
    /**
     * ChatItem类型，或者String类型
     */
    public List<Object> chatItem;

    public ChatHistory() {}

    public ChatHistory(String _id, String chatGroup, List<Object> chatItem) {
        this._id = _id;
        this.chatGroup = chatGroup;
        this.chatItem = chatItem;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Object getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(Object chatGroup) {
        this.chatGroup = chatGroup;
    }

    public List<Object> getChatItem() {
        return chatItem;
    }

    public void setChatItem(List<Object> chatItem) {
        this.chatItem = chatItem;
    }
}
