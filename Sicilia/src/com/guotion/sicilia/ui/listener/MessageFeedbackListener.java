package com.guotion.sicilia.ui.listener;

import com.guotion.sicilia.bean.net.ChatItem;

public interface MessageFeedbackListener {

	public void messageSendSuccess(ChatItem chatItem);
	
	public void messageReaded(ChatItem chatItem);
}
