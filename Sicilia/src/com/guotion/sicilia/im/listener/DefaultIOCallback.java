package com.guotion.sicilia.im.listener;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;
import android.util.Log;

import com.google.gson.Gson;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.im.Chat;
import com.guotion.sicilia.im.ChatServer;
import com.guotion.sicilia.im.util.GsonTransferUtil;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA. User: lizhengbin Date: 14-4-15 Time: 下午1:34 To
 * change this template use File | Settings | File Templates.
 */
public class DefaultIOCallback implements IOCallback {

	private String userId;
	private Gson gson = new Gson();
	
	public DefaultIOCallback(String userId) {
		this.userId = userId;
	}

	@Override
	public void onDisconnect() {
		System.out.println("连接断开了");
		if (connectionListener != null)
			connectionListener.notifyConnectionClosed();
	}

	@Override
	public void onConnect() {
		System.out.println("连接建立了");
		if (connectionListener != null)
			connectionListener.notifyConnectionEstablished();
	}

	@Override
	public void onMessage(String data, IOAcknowledge ack) {
		Log.i("------------>onMessage = ", data);
		if (messageListener == null)
			return;
		ChatItem chatItem = null;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(data);
			if (jsonObject.has("logout")) {
				if (connectionListener != null)
					connectionListener.notifyOtherDiviceLogin();
			} else if (jsonObject.has("recieve")) {// 收到“消息送达的反馈”消息
				chatItem = gson.fromJson(jsonObject.getString("msg"),ChatItem.class);
				messageListener.notifyMessageSendSuccess(chatItem);
			} else if (jsonObject.has("read")) { // 收到“消息已读的反馈”消息
				chatItem = gson.fromJson(jsonObject.getString("msg"),ChatItem.class);
				messageListener.notifyMessageReaded(chatItem);
			}else if (jsonObject.has("reSubscribe")) { // 收到“消息已读的反馈”消息
				ChatServer.getInstance().getChat().sendSubscribe(userId);//.relogin();
			} else {
				String userId;
				User user;
				chatItem = (ChatItem) GsonTransferUtil.transferToObject(data,new ChatItem());
				chatItem.setChatGroup(jsonObject.getString("chatGroup"));
				chatItem.setChatHistory(jsonObject.getString("chatHistory"));
				chatItem.setUser(jsonObject.getString("user"));
				String a = new Gson().toJson(chatItem);
				Log.i("------------>收到消息反馈chatItem = ", a);
				//收到发送消息后服务器返回的消息
				messageListener.notifyReceiveMineMessage(chatItem);
				
				try{
					user = gson.fromJson(jsonObject.getString("user"), User.class);
					userId = user.get_id();
				}catch(Exception e){
					//如果出现异常，说明这个json里的user是String
					userId=jsonObject.getString("user");
					//return;
				}
				//User user = gson.fromJson(chatItem.getUser() + "", User.class);

				ChatGroup chatGroup = gson.fromJson(chatItem.getChatGroup()+ "", ChatGroup.class);
				if (userId.equals(this.userId)){ // 收到的是自己发送的消息
					return;
				}
				if (chatGroup.getP2pid() != null && chatGroup.getP2pid().equals("")) {
					System.out.println("收到组消息:" + chatItem.getMsg());
					messageListener.notifyReceiveGroupMessage(chatItem,chatGroup.get_id(), userId); // 收到组消息
				} else {
					System.out.println("收到私密消息:" + chatItem.getMsg());
					messageListener.notifyReceiveP2PMessage(chatItem,chatGroup.get_id(), userId); // 收到私密消息
					
					
					Chat chat = ChatServer.getInstance().getChat();
					//发送收到消息的反馈
					chat.sendRecieveChatMsgReceipt(chatItem.get_id());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onMessage(JSONObject json, IOAcknowledge ack) {
		System.out.println("收到了JSONObject数据");

	}

	@Override
	public void on(String event, IOAcknowledge ack, Object... args) {
		System.out.println("触发了" + event + "事件");
		System.out.println("args.length=" + args.length);
		String result = args[0] + "";
		System.out.println("result=" + result);
		if (event.equalsIgnoreCase("message")) {
			onMessage(result, ack);
		}
	}

	@Override
	public void onError(SocketIOException socketIOException) {
		System.out.println("onError事件");
		socketIOException.printStackTrace();
		if (connectionListener != null && socketIOException != null)
			connectionListener.notifyAnErrorOccur(socketIOException);
		new Thread() {
			public void run() {
				try {
					ChatServer.getInstance().relogin();
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(5 * 1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
						// ignore
					}
					onError(null);
				}
			}
		}.start();

	}

	private MessageListener messageListener;
	private ConnectionListener connectionListener;

	public void setMessageNotice(MessageListener messageNotice) {
		removeMessageNotice();
		this.messageListener = messageNotice;
	}

	public void removeMessageNotice() {
		this.messageListener = null;
	}

	public void setConnectionListener(ConnectionListener connectionListener) {
		removeConnectionListener();
		this.connectionListener = connectionListener;
	}

	public void removeConnectionListener() {
		this.connectionListener = null;
	}
}
