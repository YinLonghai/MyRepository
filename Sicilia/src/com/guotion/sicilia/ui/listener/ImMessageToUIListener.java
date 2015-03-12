package com.guotion.sicilia.ui.listener;

import java.util.LinkedList;

import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.im.listener.ConnectionListener;
import com.guotion.sicilia.im.listener.MessageListener;

public class ImMessageToUIListener implements MessageListener, ConnectionListener {

	private LinkedList<ReceiveP2PMessageListener> receiveP2PMessageListeners = new LinkedList<ReceiveP2PMessageListener>();
	private LinkedList<ReceiveGroupMessageListener> receiveGroupMessageListeners = new LinkedList<ReceiveGroupMessageListener>();
	private LinkedList<MessageFeedbackListener> messageFeedbackListeners = new LinkedList<MessageFeedbackListener>();
	private LinkedList<OtherDiviceLoginListener> otherDiviceLoginListeners = new LinkedList<OtherDiviceLoginListener>();

	private ReceiveMineMessageListener receiveMineMessageListener = null;

	private static ImMessageToUIListener ins = new ImMessageToUIListener();

	private ImMessageToUIListener() {
	}

	public static ImMessageToUIListener getInstance() {
		return ins;
	}

	@Override
	public void notifyConnectionEstablished() {
	}

	@Override
	public void notifyAnErrorOccur(Exception e) {
	}

	@Override
	public void notifyConnectionClosed() {
	}

	@Override
	public void notifyOtherDiviceLogin() {
		for (OtherDiviceLoginListener otherDiviceLoginListener : otherDiviceLoginListeners) {
			otherDiviceLoginListener.otherDiviceLogin();
		}
	}

	@Override
	public void notifyReceiveP2PMessage(ChatItem chatItem, String p2pGroupId, String senderId) { 
		System.out.println("notifyReceiveP2PMessage in ImMessageToUIListener..."+receiveP2PMessageListeners.size());
		for (ReceiveP2PMessageListener receiveP2PMessageListener : receiveP2PMessageListeners) {
			receiveP2PMessageListener.receiveP2PMessage(chatItem);System.out.println("notifyReceiveP2PMessage in ImMessageToUIListener..."+receiveP2PMessageListener);
		}
	}

	@Override
	public void notifyReceiveGroupMessage(ChatItem chatItem, String groupId, String senderId) {System.out.println("notifyReceiveGroupMessage in ImMessageToUIListener...");
		for (ReceiveGroupMessageListener receiveGroupMessageListener : receiveGroupMessageListeners) {
			receiveGroupMessageListener.receiveGroupMessage(chatItem);
		}
	}

	@Override
	public void notifyMessageReaded(ChatItem chatItem) {
		for (MessageFeedbackListener messageFeedbackListener : messageFeedbackListeners) {
			messageFeedbackListener.messageReaded(chatItem);
		}
	}

	@Override
	public void notifyMessageSendSuccess(ChatItem chatItem) {
		for (MessageFeedbackListener messageFeedbackListener : messageFeedbackListeners) {
			messageFeedbackListener.messageSendSuccess(chatItem);
		}
	}

	@Override
	public void notifyReceiveSystemMessage() {
	}

	public void cancleReceiveP2PMessageListeners(ReceiveP2PMessageListener receiveP2PMessageListener) {
		receiveP2PMessageListeners.remove(receiveP2PMessageListener);
	}

	public void registReceiveP2PMessageListeners(ReceiveP2PMessageListener receiveP2PMessageListener) {
		receiveP2PMessageListeners.add(receiveP2PMessageListener);
	}

	public void cancleReceiveGroupMessageListeners(ReceiveGroupMessageListener receiveGroupMessageListener) {
		receiveGroupMessageListeners.remove(receiveGroupMessageListener);
	}

	public void registReceiveGroupMessageListeners(ReceiveGroupMessageListener receiveGroupMessageListener) {
		receiveGroupMessageListeners.add(receiveGroupMessageListener);
	}

	public void cancleMessageFeedbackListener(MessageFeedbackListener messageFeedbackListener) {
		messageFeedbackListeners.remove(messageFeedbackListener);
	}

	public void registMessageFeedbackListener(MessageFeedbackListener messageFeedbackListener) {
		messageFeedbackListeners.add(messageFeedbackListener);
	}

	public void cancleOtherDiviceLoginListener(OtherDiviceLoginListener otherDiviceLoginListener) {
		otherDiviceLoginListeners.remove(otherDiviceLoginListener);
	}

	public void registOtherDiviceLoginListener(OtherDiviceLoginListener otherDiviceLoginListener) {
		otherDiviceLoginListeners.add(otherDiviceLoginListener);
	}
	
	public void setReceiveMineMessageListener(ReceiveMineMessageListener listener){
		this.receiveMineMessageListener = listener;
	}

	@Override
	public void notifyReceiveMineMessage(ChatItem item) {
		receiveMineMessageListener.receiveMineMessage(item);
	}

}