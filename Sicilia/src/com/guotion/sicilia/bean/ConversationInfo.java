package com.guotion.sicilia.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="conversationInfo")
public class ConversationInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4571191420454287544L;
	@DatabaseField(generatedId=true)
	public int id;
//	@DatabaseField(foreign=true)
//	public FriendInfo friend;
	@DatabaseField
	public String accountId;
	public String groupId;
	@DatabaseField
	public int num;
	@DatabaseField
	public long lost_date;
	@DatabaseField
	public int unread_num = 0;
	@DatabaseField
	public String content;
	
	public String contentType = "";
	
	public String friendName;
	public String GroupPhoto = "";
	public ConversationInfo(int num,
			long lost_date, int unread_num, String content,String friendName) {
		super();
		//this.friend = friend;
		this.num = num;
		this.lost_date = lost_date;
		this.unread_num = unread_num;
		this.content = content;
		this.friendName = friendName;
	}
	public ConversationInfo(){}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
//	public FriendInfo getFriend() {
//		return friend;
//	}
//	public void setFriend(FriendInfo friend) {
//		this.friend = friend;
//	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public long getLost_date() {
		return lost_date;
	}
	public void setLost_date(long lost_date) {
		this.lost_date = lost_date;
	}
	public int getUnread_num() {
		return unread_num;
	}
	public void setUnread_num(int unread_num) {
		this.unread_num = unread_num;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	
}
