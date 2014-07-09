package com.guotion.sicilia.bean;

public class ChatItemInfo {
	public final static int SEND_SUCCESS = 0;
	public final static int RECEIVE = 1;
	public final static int SEND_FAIL = 2;
	public final static int SENDING = 3;
	
	public final static int TYPE_TEXT = 0;
	public final static int TYPE_IMG = 1;
	public final static int TYPE_AUDIO = 2;
	
	public String crc;
	public String date;
	public String id_;
	public int mediaType;
	public String mediaUrl;
	public String msg;
	public int state;
	public ChatGroupInfo chatGroup;
	public ChatHistoryInfo chatHistory;
	public CloudInfo cloud;
	public UserInfo user;
}
