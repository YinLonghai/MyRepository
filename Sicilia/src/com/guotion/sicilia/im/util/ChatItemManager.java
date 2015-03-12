package com.guotion.sicilia.im.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.ChatHistory;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.bean.net.LastRead;
import com.guotion.sicilia.im.constant.ChatServerConstant;

public class ChatItemManager {
	private ChatItem chatItemObject = new ChatItem();
	private ChatGroupManager chatGroupManager = new ChatGroupManager();
	private String cusException = "N:01";

	/**
	 * 发送私密信息多媒体文件
	 * 
	 * @param userId
	 *            发送者id
	 * @param receierId
	 *            接收者Id
	 * @param mediaType
	 * @param media
	 * @param msg
	 *            可不传
	 * @return
	 * @throws Exception
	 */
	public ChatItem sendP2PMediaFile(String userId, String receierId,
			String mediaType, File media, String msg) throws Exception {
		ChatGroup chatGroup = chatGroupManager.getP2PGroup(userId, receierId);
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Chat/Msg");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> fileMap = new HashMap<String, File>();
		params.put("mediaType", mediaType);
		params.put("channel", chatGroup.get_id());
		params.put("user", userId);
		params.put("crc",
				new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
		if (msg != null && !msg.equals(""))
			params.put("msg", msg);
		fileMap.put("media", media);
		byte[] result = RequestSender.requestByPostWithFile(url.toString(),
				params, fileMap);
		if(result == null)
        	throw new Exception(cusException);
		return (ChatItem) GsonTransferUtil.transferToObject(new String(result),
				chatItemObject);
	}

	/**
	 * 发送群多媒体文件
	 * 
	 * @param userId
	 * @param groupId
	 * @param mediaType
	 * @param media
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public ChatItem sendGroupMediaFile(String userId, String groupId,
			String mediaType, File media, String msg) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Chat/Msg");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> fileMap = new HashMap<String, File>();
		params.put("mediaType", mediaType);
		params.put("channel", groupId);
		params.put("user", userId);
		params.put("crc",
				new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
		if (msg != null && !msg.equals(""))
			params.put("msg", msg);
		fileMap.put("media", media);
		byte[] result = RequestSender.requestByPostWithFile(url.toString(),
				params, fileMap);
		if(result == null)
        	throw new Exception(cusException);
		return (ChatItem) GsonTransferUtil.transferToObject(new String(result),
				chatItemObject);
	}

	public boolean deleteOneMsg(String chatItemId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Chat/Msg/Delete");
		url.append("?");
		url.append("id_=" + chatItemId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return GsonTransferUtil.transferToBoolean(new String(result));
	}

	/**
	 * 获取某个某个群的消息列表
	 * 
	 * @param chatHistory
	 *            chatHistory的id
	 * @return
	 * @throws Exception
	 */
	public List<ChatItem> getOneGroupMsgList(String chatHistory)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Chat/Msg/List");
		url.append("?");
		url.append("id_=" + chatHistory);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				chatItemObject);
	}

	/**
	 * 获取所有群消息列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ChatItem> getAllMsgList() throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Chat/Msg/List/All");
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				chatItemObject);
	}

	/**
	 * 通过群id获取消息列表(降序)
	 * 
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public List<ChatItem> getMsgListWithGroupId(String groupId)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Chat/Msg/List/Group");
		url.append("?");
		url.append("id_=" + groupId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return sortList((List) GsonTransferUtil.transferToArray(new String(result),
				chatItemObject));
	}

	/**
	 * 获取某条消息之后的消息 (获取最大20条，升序)
	 * 
	 * @param chatHistoryId
	 * @param chatItemId
	 * @return
	 * @throws Exception
	 */
	public List<ChatItem> getMsgAfterOneMsg(String chatHistoryId,
			String chatItemId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Chat/Msg/List/After");
		url.append("?");
		url.append("id_=" + chatHistoryId + "&");
		url.append("afterID=" + chatItemId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				chatItemObject);
	}

	/**
	 * 获取某条消息之前的消息(最大获取20条，降序)
	 * 
	 * @param chatHistoryId
	 * @param chatItemId
	 * @return
	 * @throws Exception
	 */
	public List<ChatItem> getMsgBeforeOneMsg(String chatHistoryId,
			String chatItemId) throws Exception {
		System.out.println("chatHistoryId="+chatHistoryId+",chatItemId="+chatItemId);
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Chat/Msg/List/Before");
		url.append("?");
		url.append("id_=" + chatHistoryId + "&");
		url.append("beforeID=" + chatItemId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return sortList((List) GsonTransferUtil.transferToArray(new String(result),
				chatItemObject));
	}

	/**
	 * 获取单条消息
	 * 
	 * @param chatItemId
	 * @return
	 * @throws Exception
	 */
	public ChatItem getSingleMsg(String chatItemId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Chat/SingleMsg");
		url.append("?");
		url.append("id_=" + chatItemId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (ChatItem) GsonTransferUtil.transferToObject(new String(result),
				chatItemObject);
	}

	/**
	 * 获取群对象的聊天历史类
	 * 
	 * @param chatGroupId
	 * @return
	 * @throws Exception
	 */
	public ChatHistory getMsgRelateHistory(String chatGroupId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Chat/History");
		url.append("?");
		url.append("id_=" + chatGroupId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (ChatHistory) GsonTransferUtil.transferToObject(new String(
				result), new ChatHistory());
	}

	/**
	 * 获取上次退出到
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public LastRead getLastRead(String userId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/LastRead/User");
		url.append("?");
		url.append("id_=" + userId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (LastRead) GsonTransferUtil.transferToObject(new String(result),
				new LastRead());
	}

	/**
	 * 更新最后阅读的消息
	 * 
	 * @param userId
	 *            用户id
	 * @param groupsDict
	 *            "groupId1:chatItemId1|groupId2:chatItemId2|"
	 * @return
	 * @throws Exception
	 */
	public LastRead updateLastRead(String userId, String groupsDict)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/LastRead/Update");
		url.append("?");
		url.append("id_=" + userId + "&");
		url.append("groups=" + groupsDict);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (LastRead) GsonTransferUtil.transferToObject(new String(result),
				new LastRead());
	}
	
	
	
	private List<ChatItem> sortList(List<ChatItem> list){
//		List<ChatItem> newList = new LinkedList<ChatItem>();
//		newList.add(list.get(0));
		for(int i=0;i<list.size()-1;i++){//System.out.println(list.get(i).msg+"  "+list.get(i).date);
			int index = i;
			for(int j=i+1;j<list.size();j++){
				if(list.get(index).date.compareTo(list.get(j).date)>0){
					index = j;
				}
			}
			if(index != i){
				ChatItem data1 = list.get(i);
				ChatItem data2 = list.get(index);
				list.set(i, data2);
				list.set(index, data1);
			}
		}
//		return newList;
		return list;
	}
}
























