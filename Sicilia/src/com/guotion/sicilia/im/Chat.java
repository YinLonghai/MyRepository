package com.guotion.sicilia.im;

import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.ChatGroupManager;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.SocketIO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: lizhengbin
 * Date: 14-4-14
 * Time: 下午10:19
 * To change this template use File | Settings | File Templates.
 */
public class Chat {
    private SocketIO socketIO = null;

    /**
     * @param socketIO 连接到了服务器的SocketIO对象，可以通过ChatServer.getInstance().login()之后，
     *                 调用getSocketIO()方法获得，但是要在
     * @param userId   用户的id
     */
    public Chat(SocketIO socketIO, String userId) {
        if (socketIO != null) {
            this.socketIO = socketIO;
            //订阅自己帐号频道
            try {
                sendSubscribe(userId);
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    }

    /**
     * 发送收到消息反馈
     *
     * @param msgId
     * @throws Exception
     */
    public void sendRecieveChatMsgReceipt(String msgId){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("channel", msgId);
        } catch (JSONException e) {
           e.printStackTrace();
           //ignore
        }
        socketIO.emit(ChatServerConstant.EVENT_NAME.RECEIVER_CHAT_MSG_EVENT, jsonObject);

    }

    /**
     * 发送已经读了信息的反馈
     *
     * @param msgId
     * @throws Exception
     */
    public void sendReadChatMsgReceipt(String msgId){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("channel", msgId);
        } catch (JSONException e) {
            e.printStackTrace();
            //ignore
        }
        socketIO.emit(ChatServerConstant.EVENT_NAME.READ_CHAT_MSG_EVENT, jsonObject);

    }
    
    /**
     * 发送私密图片成功以后，调用此方法来通知接收者
     * @param chatItemId
     * @param senderId
     * @param senderName
     * @param receiverId
     * @throws Exception 
     */
    public void sendP2PPostChat(String chatItemId,String senderId,String senderName,String receiverId) throws Exception{
    	JSONObject jsonObject = new JSONObject();
    	ChatGroup p2pGroup = new ChatGroupManager().getP2PGroup(senderId, receiverId);
    	try{
    		jsonObject.put("chatItem_id",chatItemId);
    		jsonObject.put("channel", p2pGroup.get_id());
            jsonObject.put("name", senderName);
            jsonObject.put("toUsers", receiverId);
            jsonObject.put("user", senderId);
            jsonObject.put("isP2P", "1");
            jsonObject.put("crc", new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
            //jsonObject.put("msg", msg);
            socketIO.emit(ChatServerConstant.EVENT_NAME.POST_CHAT_EVENT, jsonObject);
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
  		  //ignore
		}
    }
    
    /**
     * 发送群图片成功以后应该调用此方法来通知所有接收者
     * @param chatItemId
     * @param chatGroupId
     * @param senderName
     * @param receiversId
     * @param senderId
     */
    public void sendGroupPostChat(String chatItemId,String chatGroupId, String senderName, String receiversId, String senderId){
    	JSONObject jsonObject = new JSONObject();
    	try{
    		jsonObject.put("chatItem_id",chatItemId);
    		jsonObject.put("channel", chatGroupId);
            jsonObject.put("name", senderName);
            jsonObject.put("toUsers", receiversId);
            jsonObject.put("user", senderId);
            jsonObject.put("isP2P", "0");
            jsonObject.put("crc", new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
            //jsonObject.put("msg", msg);
            socketIO.emit(ChatServerConstant.EVENT_NAME.POST_CHAT_EVENT, jsonObject);
    	}catch (Exception e) {
			// TODO: handle exception
    		  e.printStackTrace();
    		  //ignore
		}
    }
    

    /**
     * 发送群消息
     * @param chatGroupId
     * @param senderName
     * @param receiversId 以"|"分隔 ,如"xxxx|xxxx"
     * @param senderId
     * @param msg         消息内容
     * @throws Exception
     */
    public void sendGroupMessage(String chatGroupId, String senderName, String receiversId, String senderId,String msg) {
    	System.out.println("toUsers="+receiversId);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("channel", chatGroupId);
            jsonObject.put("name", senderName);
            jsonObject.put("toUsers", receiversId);
            jsonObject.put("user", senderId);
            jsonObject.put("isP2P", "0");
            jsonObject.put("crc", new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
            jsonObject.put("msg", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketIO.emit(ChatServerConstant.EVENT_NAME.CHAT_EVENT, jsonObject);
    }

    /**
     * 发送私密消息
     * @param senderId
     * @param senderName
     * @param receiverId
     * @param msg
     * @throws Exception
     */
    public void sendP2PMessage(String senderId,String senderName,String receiverId,String msg) throws Exception {
        ChatGroup p2pGroup = new ChatGroupManager().getP2PGroup(senderId, receiverId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("channel", p2pGroup.get_id());
        jsonObject.put("name", senderName);
        jsonObject.put("toUsers", receiverId);
        jsonObject.put("user", senderId);
        jsonObject.put("isP2P", "1");
        jsonObject.put("crc", new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
        jsonObject.put("msg", msg);
        socketIO.emit(ChatServerConstant.EVENT_NAME.CHAT_EVENT, jsonObject);
    }



    /**
     * 订阅自己的频道
     *
     * @param userId
     * @throws Exception
     */
    public void sendSubscribe(String userId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("channel", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketIO.emit(ChatServerConstant.EVENT_NAME.SUBSCRIBE_EVENT, jsonObject);
    }
    
    public void unSubscribeMsg(String groupId){
    	JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("channel", groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketIO.emit(ChatServerConstant.EVENT_NAME.UNSUBSCRIBEMSG_EVENT, jsonObject);
    }
    
    


}
