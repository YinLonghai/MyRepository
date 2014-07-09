package com.guotion.sicilia.ui.listener;

import java.util.LinkedList;

import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.im.listener.ConnectionListener;
import com.guotion.sicilia.im.listener.MessageListener;

public class ImMessageToUIListener implements MessageListener , ConnectionListener{
	
	private LinkedList<ReceiveP2PMessageListener> receiveP2PMessageListeners = new LinkedList<ReceiveP2PMessageListener>();
	private LinkedList<ReceiveGroupMessageListener> receiveGroupMessageListeners = new LinkedList<ReceiveGroupMessageListener>();
	private LinkedList<MessageFeedbackListener> messageFeedbackListeners = new LinkedList<MessageFeedbackListener>();
	
	private static ImMessageToUIListener ins = new ImMessageToUIListener();
	private ImMessageToUIListener(){}
	public static ImMessageToUIListener getInstance(){
		return ins;
	}
	
	@Override
	public void notifyConnectionEstablished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAnErrorOccur(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyConnectionClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyReceiveP2PMessage(ChatItem chatItem, String p2pGroupId,
			String senderId) {//System.out.println("ImMessageToUIListener...");
		for(ReceiveP2PMessageListener receiveP2PMessageListener : receiveP2PMessageListeners){
			receiveP2PMessageListener.receiveP2PMessage(chatItem);
		}
	}
	@Override
	public void notifyReceiveGroupMessage(ChatItem chatItem, String groupId,
			String senderId) {
		for(ReceiveGroupMessageListener receiveGroupMessageListener : receiveGroupMessageListeners){
			receiveGroupMessageListener.receiveGroupMessage(chatItem);
		}
	}
	@Override
	public void notifyMessageReaded(ChatItem chatItem) {
		for(MessageFeedbackListener messageFeedbackListener : messageFeedbackListeners){
			messageFeedbackListener.messageReaded(chatItem);
		}
	}

	@Override
	public void notifyMessageSendSuccess(ChatItem chatItem) {
		for(MessageFeedbackListener messageFeedbackListener : messageFeedbackListeners){
			messageFeedbackListener.messageSendSuccess(chatItem);
		}
	}

	@Override
	public void notifyReceiveSystemMessage() {
		// TODO Auto-generated method stub
		
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
	
	
	

}
