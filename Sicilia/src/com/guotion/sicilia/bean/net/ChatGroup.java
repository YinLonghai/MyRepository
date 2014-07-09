package com.guotion.sicilia.bean.net;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-18
 * Time: 下午12:15
 * To change this template use File | Settings | File Templates.
 */
public class ChatGroup implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5230044560629189924L;
	public String _id="";
    public String GroupName = "";
    public String GroupProfile = "";
    public String GroupPhoto = "";
    public String __v = "0";
    //activities:[{type:Schema.Types.ObjectId, ref:'Activity',default:null}], //[]means to many
    public List<Object> members;//成员.数组
    public List<Object> admins;//管理员.数组
    public Object creator; //群主,userName
    public Object chatHistory;//聊天记录
    public String p2pid = "";

    public ChatGroup() {}

    public ChatGroup(String _id, String groupName, String groupProfile, String groupPhoto, String __v,
                     List<Object> members, List<Object> admins, Object creator, Object chatHistory, String p2pid) {
        this._id = _id;
        GroupName = groupName;
        GroupProfile = groupProfile;
        GroupPhoto = groupPhoto;
        this.__v = __v;
        this.members = members;
        this.admins = admins;
        this.creator = creator;
        this.chatHistory = chatHistory;
        this.p2pid = p2pid;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupProfile() {
        return GroupProfile;
    }

    public void setGroupProfile(String groupProfile) {
        GroupProfile = groupProfile;
    }

    public String getGroupPhoto() {
        return GroupPhoto;
    }

    public void setGroupPhoto(String groupPhoto) {
        GroupPhoto = groupPhoto;
    }

    public List<Object> getMembers() {
        return members;
    }

    public void setMembers(List<Object> members) {
        this.members = members;
    }

    public List<Object> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Object> admins) {
        this.admins = admins;
    }

    public Object getCreator() {
        return creator;
    }

    public void setCreator(Object creator) {
        this.creator = creator;
    }

    public Object getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(Object chatHistory) {
        this.chatHistory = chatHistory;
    }

    public String getP2pid() {
        return p2pid;
    }

    public void setP2pid(String p2pid) {
        this.p2pid = p2pid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "ChatGroup{" +
                "_id='" + _id + '\'' +
                ", GroupName='" + GroupName + '\'' +
                ", GroupProfile='" + GroupProfile + '\'' +
                ", GroupPhoto='" + GroupPhoto + '\'' +
                ", __v='" + __v + '\'' +
                ", members=" + members +
                ", admins=" + admins +
                ", creator='" + creator + '\'' +
                ", chatHistory=" + chatHistory +
                ", p2pid='" + p2pid + '\'' +
                '}';
    }
}
