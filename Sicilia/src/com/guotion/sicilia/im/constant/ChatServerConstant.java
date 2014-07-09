package com.guotion.sicilia.im.constant;

/**
 * Created with IntelliJ IDEA. User: lizhengbin Date: 14-4-14 Time: 下午10:37 To
 * change this template use File | Settings | File Templates.
 */
public interface ChatServerConstant {

	public interface URL {
		public final String SELF_WIFI_URL = "https://10.100.40.24:2014";
//		public final String SERVER_HOST = "https://192.168.1.107:2014";
//		public final String SERVER_HOST = "https://192.168.1.105:2014";
		public final String SERVER_HOST = "https://115.28.27.128:2014";
//		public final String SERVER_HOST = "https://192.168.1.101:2014";
	}

	public interface EVENT_NAME {
		public final String RECEIVER_CHAT_MSG_EVENT = "recieveChatMsg";
		public final String READ_CHAT_MSG_EVENT = "readChatMsg";
		public final String POST_CHAT_EVENT = "postChat";
		public final String CHAT_EVENT = "chat";
		public final String UNSUBSCRIBEMSG_EVENT = "unSubscribeMsg";
		public final String SUBSCRIBE_EVENT = "subscribe";
	}
}
