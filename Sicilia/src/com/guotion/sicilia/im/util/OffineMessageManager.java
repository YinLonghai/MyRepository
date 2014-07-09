package com.guotion.sicilia.im.util;

import com.google.gson.Gson;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.ChatHistory;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.bean.net.LastRead;

import org.json.JSONObject;

import java.util.*;

/**
 * Created with IntelliJ IDEA. User: lizhengbin Date: 14-4-27 Time: 下午7:48 To
 * change this template use File | Settings | File Templates.
 */
public class OffineMessageManager {
	private ChatItemManager chatItemManager = new ChatItemManager();
	private Gson gson = new Gson();
	/**
	 * 用户记录哪些组已经获取过离线
	 */
	private Map<String, String> havenGet = new HashMap<String, String>();

	public Map<String, String> getHavenGet() {
		return this.havenGet;
	}
	
	/**
	 * 获取有记录的组历史消息 把获取离线消息的方法放到一个线程中执行，方法可能会很耗时
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public Map<String,List<ChatItem>> getHaveLastReadOffineMessage(String userId) throws Exception {
		Map<String,List<ChatItem>> chatItems = new HashMap<String,List<ChatItem>>();
		LastRead lastRead = chatItemManager.getLastRead(userId);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(lastRead.getDict());
		} catch (Exception e) {
			e.printStackTrace();
			return chatItems; // 没有dict记录.
		}
		List<Object> groups = lastRead.getGroups(); // 有dict记录的组
		Map<String, String> lastReadMap = new HashMap<String, String>();
		for (Object object : groups) {
			ChatGroup chatGroup = gson.fromJson(object + "", ChatGroup.class);
			havenGet.put(chatGroup.get_id(), chatGroup.get_id());// 因为只是用于判断是否已经获取过了离线消息,所以保存的值是什么无需关心
			try {
				ChatHistory chatHistory = chatItemManager.getMsgRelateHistory(chatGroup.get_id());
				if (chatHistory != null) {
					String lastReadChatItemId = jsonObject.getString(chatGroup.get_id());
					List<ChatItem> oneGroupItem = new LinkedList<ChatItem>();
					List<ChatItem> items = chatItemManager.getMsgAfterOneMsg(chatHistory.get_id(),lastReadChatItemId);
					while(items.size() == 20) {
						oneGroupItem.addAll(items);
						items = chatItemManager.getMsgAfterOneMsg(chatHistory.get_id(),lastReadChatItemId);
					}
					if (oneGroupItem.size() > 0) {
						chatItems.put(chatGroup.get_id(),oneGroupItem);
						lastReadMap.put(chatGroup.get_id(),oneGroupItem.get(oneGroupItem.size() - 1).get_id());
						System.out.println(chatGroup.get_id()+"收到的消息条数:"+oneGroupItem.size());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				// ignore
			}
		}

		if (lastReadMap.size() > 0) {
			sendLastRead(lastReadMap, userId); // 同步服务器
		}
		return chatItems;
	}
	
	/**
	 * 获取有离线记录的，每个组的离线消息条数
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Integer> getUnReadMessageCount(String userId) throws Exception{
		Map<String,Integer> unReadMessageCount = new HashMap<String,Integer>();
		LastRead lastRead = chatItemManager.getLastRead(userId);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(lastRead.getDict());
		} catch (Exception e) {
			e.printStackTrace();
			return unReadMessageCount; // 没有dict记录.
		}
		List<Object> groups = lastRead.getGroups(); // 有dict记录的组
		Map<String, String> lastReadMap = new HashMap<String, String>();
		for (Object object : groups) {
			ChatGroup chatGroup = gson.fromJson(object + "", ChatGroup.class);
			havenGet.put(chatGroup.get_id(), chatGroup.get_id());// 因为只是用于判断是否已经获取过了离线消息,所以保存的值是什么无需关心
			ChatHistory chatHistory = chatItemManager.getMsgRelateHistory(chatGroup.get_id());
			String lastReadChatItemId = jsonObject.getString(chatGroup.get_id());
			List<ChatItem> oneGroupItem = chatItemManager.getMsgAfterOneMsg(chatHistory.get_id(),lastReadChatItemId);
			int oneGroupMessageCount = oneGroupItem.size();
			while(oneGroupItem.size() == 20){
				//if count==20,the unread message may have more.
				String lastReadItemId = oneGroupItem.get(oneGroupItem.size() - 1).get_id();
				oneGroupItem = chatItemManager.getMsgAfterOneMsg(chatHistory.get_id(),lastReadItemId);
				oneGroupMessageCount += oneGroupItem.size();
			}
			if(oneGroupMessageCount>0){
				lastReadMap.put(chatGroup.get_id(),oneGroupItem.get(oneGroupItem.size() - 1).get_id());
			}
			unReadMessageCount.put(chatGroup.get_id(),oneGroupMessageCount);
		}
		if (lastReadMap.size() > 0) {
			sendLastRead(lastReadMap, userId); // 同步服务器
		}
		return unReadMessageCount;
	}

	/**
	 * 同步最后阅读消息到服务器
	 * 
	 * @param lastReadMap
	 *            存放groupId和对应该组最后一条阅读消息的id
	 * @param userId
	 *            用户id
	 * @return LastRead instance
	 * @throws Exception
	 */
	public LastRead sendLastRead(Map<String, String> lastReadMap, String userId) throws Exception {
		StringBuilder groupDict = new StringBuilder();
		Set<Map.Entry<String, String>> set = lastReadMap.entrySet();
		for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			String groupId = entry.getKey();
			String lastReadChatItemId = entry.getValue();
			groupDict.append(groupId + ":" + lastReadChatItemId + "|");
		}
		if (groupDict.length() > 0) {
			String dict = groupDict.deleteCharAt(groupDict.length() - 1).toString();
			System.out.println("上传的dict=" + dict);
			return chatItemManager.updateLastRead(userId, dict);
		}
		return null;
	}
}
