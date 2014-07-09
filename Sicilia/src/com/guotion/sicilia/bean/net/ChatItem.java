package com.guotion.sicilia.bean.net;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:16
 * To change this template use File | Settings | File Templates.
 */
public class ChatItem implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6450415146709980024L;
	public String _id="";
    public String mediaUrl = "";
    public String mediaType = "";
    /**
     * User类型或者 String类型
     */
    public Object user;
    public User userInfo;
    /**
     * User类型或者String类型
     */
    public Object toUser;
    public String date = "";
    public String state = "0";//0:发送成功,1:送达,2已读
    public String msg = "";
    public String crc = ""; //timestamp作为校验码
    /**
     * ChatGroup类型或者String类型
     */
    public Object chatGroup;
    /**
     *  ChatHistory类型或者String类型
     */
    public Object chatHistory;
    /**
     * CloudEvent类型或者String类型
     */
    public Object cloud;

    public ChatItem() {}

    public ChatItem(String _id, String mediaUrl, String mediaType, Object user, Object toUser, String date, String state,
                    String msg, String crc, Object chatGroup, Object chatHistory, Object cloud) {
        this._id = _id;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.user = user;
        this.toUser = toUser;
        this.date = date;
        this.state = state;
        this.msg = msg;
        this.crc = crc;
        this.chatGroup = chatGroup;
        this.chatHistory = chatHistory;
        this.cloud = cloud;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public Object getToUser() {
        return toUser;
    }

    public void setToUser(Object toUser) {
        this.toUser = toUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public Object getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(Object chatGroup) {
        this.chatGroup = chatGroup;
    }

    public Object getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(Object chatHistory) {
        this.chatHistory = chatHistory;
    }

    public Object getCloud() {
        return cloud;
    }

    public void setCloud(Object cloud) {
        this.cloud = cloud;
    }

    @Override
    public String toString() {
        return "ChatItem{" +
                "_id='" + _id + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", user=" + user +
                ", toUser=" + toUser +
                ", date='" + date + '\'' +
                ", state='" + state + '\'' +
                ", msg='" + msg + '\'' +
                ", crc='" + crc + '\'' +
                ", chatGroup=" + chatGroup +
                ", chatHistory=" + chatHistory +
                ", cloud=" + cloud +
                '}';
    }
}