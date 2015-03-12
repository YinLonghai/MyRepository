package com.guotion.sicilia.im.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.im.constant.ChatServerConstant;

/**
 * Created with IntelliJ IDEA. User: lizhengbin Date: 14-4-18 Time: 下午3:31 To
 * change this template use File | Settings | File Templates.
 */
public class ChatGroupManager {
	private ChatGroup chatGroupObject = new ChatGroup();
	private final String CHARSET = "ISO-8859-1";
	private String cusException = "N:01";

	/**
	 * 创建群 (要上传群头像)
	 * 
	 * @param creator
	 * @param members
	 * @param groupName
	 * @param mediaType
	 * @param media
	 * @return 1.admins是对象 2.members是的对象 3.creator是对象
	 * @throws Exception
	 */
	public ChatGroup createChatGroup(String creator, String member, String groupName, String mediaType, File media)
			throws Exception {
		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/Create");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> fileMap = new HashMap<String, File>();
		params.put("creator", creator);
		params.put("members", member);
		params.put("GroupName", groupName);
		params.put("mediaType", mediaType);
		fileMap.put("media", media);
		byte[] result = RequestSender.requestByPostWithFile(url.toString(), params, fileMap);
		if (result == null)
			throw new Exception(cusException);
		ChatGroup chatGroup = (ChatGroup) GsonTransferUtil.transferToObject(new String(result), chatGroupObject);
		User creator1 = new Gson().fromJson(chatGroup.creator+"", User.class);
		chatGroup.creator = creator1._id;
		List<User> admins = new Gson().fromJson(chatGroup.admins+"",new TypeToken<List<User>>(){}.getType());
		List<User> members = new Gson().fromJson(chatGroup.members+"",new TypeToken<List<User>>(){}.getType());
		ArrayList<Object> adminsList = new ArrayList<Object>();
		ArrayList<Object> membersList = new ArrayList<Object>();
		for(User user:admins){
			adminsList.add(user._id);
		}
		for(User user:members){
			membersList.add(user._id);
		}
		chatGroup.admins = adminsList;
		chatGroup.members = membersList;
		return chatGroup;
	}

	/**
	 * 创建群 (不上传头像)
	 * 
	 * @param creator
	 * @param members
	 * @param groupName
	 * @return 1.admins是对象 2.members是的对象 3.creator是对象
	 * @throws Exception
	 */
//	public ChatGroup createChatGroup(String creator, String members, String groupName) throws Exception {
//		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/Create");
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("creator", creator);
//		params.put("members", members);
//		params.put("GroupName", groupName);
//		params.put("mediaType", "jpg");
//		byte[] result = RequestSender.requestByPost(url.toString(), params, CHARSET);
//		if (result == null)
//			throw new Exception(cusException);
//		return (ChatGroup) GsonTransferUtil.transferToObject(new String(result), chatGroupObject);
//	}

	/**
	 * 退出群
	 * 
	 * @param chatGroupId
	 *            群id
	 * @param userId
	 *            用户id
	 * @return 1.admins 是对象
	 * @throws Exception
	 */
	public ChatGroup quitChatGroup(String chatGroupId, String userId) throws Exception {
		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/Quit");
		url.append("?");
		url.append("id_=" + chatGroupId + "&");
		url.append("user_id=" + userId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if (result == null)
			throw new Exception(cusException);
		return (ChatGroup) GsonTransferUtil.transferToObject(new String(result), chatGroupObject);
	}

	/**
	 * 获取单个群的资料
	 * 
	 * @param chatGroupId
	 *            群id
	 * @return 1.admins是对象 2.members是对象 3.creator是对象 4.chatHistory是对象
	 * @throws Exception
	 */
	public ChatGroup getSingleChatGroup(String chatGroupId) throws Exception {
		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/Single");
		url.append("?");
		url.append("id_=" + chatGroupId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if (result == null)
			throw new Exception(cusException);
		return (ChatGroup) GsonTransferUtil.transferToObject(new String(result), chatGroupObject);

	}

	/**
	 * 更新群资料(要更新群头像)
	 * 
	 * @param chatGroupId
	 * @param groupName
	 * @param desc
	 * @param mediaType
	 * @param media
	 * @return
	 */
	public ChatGroup updateChatGroup(String chatGroupId, String groupName, String desc, String mediaType, File media)
			throws Exception {
		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/Update");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> fileMap = new HashMap<String, File>();
		params.put("id_", chatGroupId);
		params.put("name", groupName);
		params.put("desc", desc);
		params.put("mediaType", mediaType);
		fileMap.put("media", media);
		byte[] result = RequestSender.requestByPostWithFile(url.toString(), params, fileMap);
		if (result == null)
			throw new Exception(cusException);
		return (ChatGroup) GsonTransferUtil.transferToObject(new String(result), chatGroupObject);
	}

	/**
	 * 更新群资料(不更新群头像)
	 * 
	 * @param chatGroupId
	 * @param groupName
	 * @param desc
	 * @return
	 */
	public ChatGroup updateChatGroup(String chatGroupId, String groupName, String desc) throws Exception {
		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/Update");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id_", chatGroupId);
		params.put("name", groupName);
		params.put("desc", desc);
		byte[] result = RequestSender.requestByPostNoFile(url.toString(), params, CHARSET);
		if (result == null)
			throw new Exception(cusException);
		return (ChatGroup) GsonTransferUtil.transferToObject(new String(result), chatGroupObject);
	}

	/**
	 * 用户特定的群
	 * 
	 * @param chatGroupIds
	 *            群帐号，以"|"分隔，如"xxxx|xxxx"
	 * @return 1.admins是对象 2.members是对象 3.creator是对象 4.chatHistory是对象
	 * @throws Exception
	 */
	public List<ChatGroup> getGroupByList(String chatGroupIds) throws Exception {
		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/Group/By/List");
		url.append("?");
		url.append("list=" + chatGroupIds);
		byte[] result = RequestSender.requestByGet(url.toString());
		if (result == null)
			throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result), chatGroupObject);
	}

	/**
	 * 增加群成员
	 * 
	 * @param groupId
	 *            群id
	 * @param members
	 *            成员id，以"|"分隔，如"xxxx|xxxx"
	 * @return
	 * @throws Exception
	 */
	public ChatGroup addMembers(String groupId, String members) throws Exception {
		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/Members/Add");
		url.append("?");
		url.append("groupid=" + groupId + "&");
		url.append("members=" + members);
		byte[] result = RequestSender.requestByGet(url.toString());
		if (result == null)
			throw new Exception(cusException);
		return (ChatGroup) GsonTransferUtil.transferToObject(new String(result), chatGroupObject);
	}

	/**
	 * 更新群成员
	 * 
	 * @param groupId
	 * @param toNormal
	 * @param toDelete
	 * @param toAdmin
	 * @return
	 * @throws Exception
	 */
	public ChatGroup memberUpdate(String groupId, String toNormal, String toDelete, String toAdmin) throws Exception {
		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/Members/Update");
		url.append("?");
		url.append("groupid=" + groupId + "&");
		if (!TextUtils.isEmpty(toNormal))
			url.append("toNormal=" + toNormal + "&");
		if (!TextUtils.isEmpty(toDelete))
			url.append("toDelete=" + toDelete + "&");
		if (!TextUtils.isEmpty(toAdmin))
			url.append("toAdmin=" + toAdmin + "&");
		url.deleteCharAt(url.length() - 1);
		System.out.println(url.toString());
		byte[] result = RequestSender.requestByGet(url.toString());
		System.out.println("result==" + new String(result));
		if (result == null)
			throw new Exception(cusException);
		return (ChatGroup) GsonTransferUtil.transferToObject(new String(result), chatGroupObject);
	}

	/**
	 * 获取与用户相关的群组
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<ChatGroup> getUserRelateGroups(String userId) throws Exception {
		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/Group/List");
		url.append("?");
		url.append("id_=" + userId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if (result == null)
			throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result), chatGroupObject);
	}

	/**
	 * 删除组
	 * 
	 * @param groupId
	 *            聊天组的id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGroup(String groupId) throws Exception {
		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/Delete");
		url.append("?");
		url.append("id_=" + groupId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if (result == null)
			throw new Exception(cusException);
		return GsonTransferUtil.transferToBoolean(new String(result));
	}

	/**
	 * 获取单对单聊天组
	 * 
	 * @param userId
	 *            发送者id
	 * @param receierId
	 *            接收者id
	 * @return
	 * @throws Exception
	 */
	public ChatGroup getP2PGroup(String userId, String receierId) throws Exception {
		StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/Chat/P2P");
		url.append("?");
		url.append("id_1=" + userId + "&");
		url.append("id_2=" + receierId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if (result == null)
			throw new Exception(cusException);
		return (ChatGroup) GsonTransferUtil.transferToObject(new String(result), chatGroupObject);
	}

}
