package com.guotion.sicilia.im.util;

import com.google.gson.Gson;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.im.constant.ChatServerConstant;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: lizhengbin Date: 14-4-20 Time: 下午6:25 To
 * change this template use File | Settings | File Templates.
 */
public class CloudManager {
	private String CHARSET = "ISO-8859-1";
	private CloudEvent cloudEventObject = new CloudEvent();
	private String cusException = "N:01";
	/**
	 * 上传云文件
	 * 
	 * @param userId
	 *            上传者的id
	 * @param fileName
	 * @param fileDesc
	 *            文件描述
	 * @param isPrivate
	 *            文件是否私有，1为私有，0为公有
	 * @param activityId
	 *            活动的id
	 * @param file
	 * @return 1.chatGroups是String的数组, 2.files是CloudItem的数组, 3.activity是String,
	 *         4.owner是User的对象
	 * @throws Exception
	 */
	public CloudEvent uploadCloudFile(String userId, String fileName,
			String fileDesc, String isPrivate, String activityId, File file)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Cloud/Create");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> fileMap = new HashMap<String, File>();
		params.put("id_", userId);
		params.put("name", fileName);
		params.put("desc", fileDesc);
		params.put("isPrivate", isPrivate);
		params.put("Aid", activityId);
		fileMap.put("CloudFile", file);
		byte[] result = RequestSender.requestByPostWithCloudFile(url.toString(),params,fileMap);
		if(result == null)
        	throw new Exception(cusException);
		return (CloudEvent) GsonTransferUtil.transferToObject(
				new String(result), cloudEventObject);
	}

	/**
	 * 更新云文件 (要上传文件,不会删除以前的云文件,会保留)
	 * 
	 * @param cloudId
	 * @param name
	 * @param desc
	 * @param isPrivate
	 * @param file
	 *            要上传的文件
	 * @param cloudDeleteFile
	 *            要删除文件的id (非必须)
	 * @return 1.chatGroups是String的数组 2.owner是User的对象 3.activity是String,
	 *         4.files是CloudItem的数组,
	 * @throws Exception
	 */
	public CloudEvent updateCloudFile(String cloudId, String name, String desc,
			String isPrivate, File file, String cloudDeleteFile)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Cloud/Update");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> fileMap = new HashMap<String, File>();
		params.put("id_", cloudId);
		params.put("name", name);
		params.put("desc", desc);
		params.put("isPrivate", isPrivate);
		if (cloudDeleteFile != null)
			params.put("CloudDeleteFile", cloudDeleteFile);
		fileMap.put("CloudFile", file);
		byte[] result = RequestSender.requestByPostWithCloudFile(url.toString(),
				params, fileMap);
		if(result == null)
        	throw new Exception(cusException);
		return (CloudEvent) GsonTransferUtil.transferToObject(
				new String(result), cloudEventObject);
	}

	/**
	 * 更新云文件但不上传新的文件
	 * 
	 * @param cloudId
	 * @param name
	 * @param desc
	 * @param isPrivate
	 * @param cloudDeleteFile
	 *            要删除的文件的id,非必须
	 * @return 1.chatGroups是String的数组 2.owner是User的对象 3.activity是String,
	 *         4.files是CloudItem的数组,
	 * @throws Exception
	 */
	public CloudEvent updateCloudFile(String cloudId, String name, String desc,
			String isPrivate, String cloudDeleteFile) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Cloud/Update");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id_", cloudId);
		params.put("name", name);
		params.put("desc", desc);
		params.put("isPrivate", isPrivate);
		if (cloudDeleteFile != null)
			params.put("CloudDeleteFile", cloudDeleteFile);
		byte[] result = RequestSender.requestByPostNoFile(url.toString(),params,CHARSET);
		if(result == null)
        	throw new Exception(cusException);
		return (CloudEvent) GsonTransferUtil.transferToObject(
				new String(result), cloudEventObject);
	}

	/**
	 * 评论云文件
	 * 
	 * @param cloudId
	 * @param userId
	 * @param comment
	 * @param toUser
	 * @return 1.toUser是String 2.user是String 3.cloud是String
	 *         4.没有返回chatGroup和chatHistory
	 */
	public ChatItem commentCloudFile(String cloudId, String userId,
			String comment, String toUser) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Cloud/Comment");
		url.append("?");
		url.append("id_=" + cloudId + "&");
		url.append("user=" + userId + "&");
		url.append("comment="
				+ EncodingUtils.getString(comment.getBytes(), CHARSET) + "&");
		url.append("toUser=" + toUser);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (ChatItem) GsonTransferUtil.transferToObject(new String(result),
				new ChatItem());
	}

	/**
	 * 获取云文件的评论
	 * 
	 * @param cloudId
	 * @return 1.toUser是String 2.user是String 3.cloud是String
	 *         4.没有返回chatGroup和chatHistory
	 * @throws Exception
	 */
	public List<ChatItem> getCloudFileComments(String cloudId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Cloud/List/Comment");
		url.append("?");
		url.append("id_=" + cloudId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				new ChatItem());
	}

	/**
	 * 删除云文件
	 * 
	 * @param cloudId
	 * @return 当该文件为私有文件时，返回该用户，用户更新存储空间.否则返回删除结果，并且chatGroup是String的数组
	 * @throws Exception
	 */
	public Object deleteCloudFile(String cloudId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Cloud/Delete");
		url.append("?");
		url.append("id_=" + cloudId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		String res = new String(result);
		JSONObject jsonObject = new JSONObject(res);
		try {
			jsonObject.getString("response");
		} catch (Exception e) {
			return new Gson().fromJson(res, User.class);
		}
		return jsonObject.getString("response");
	}

	/**
	 * 获取所有云文件列表，限制20条
	 * 
	 * @return 1.chatGroups是String的数组 2.owner是User的对象 3.activity是Activity得到对象,
	 *         4.files是CloudItem的数组,
	 */
	public List<CloudEvent> getCloudFiles() throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Cloud/List");
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				cloudEventObject);
	}

	/**
	 * 获取某个云文件之后的最多20个云文件
	 * 
	 * @param cloudId
	 * @return 1.chatGroups是String的数组 2.files是CloudEvent的对象数组 3.owner是User对象
	 *         4.activity是对象
	 */
	public List<CloudEvent> getCloudFilesAfterCloudId(String cloudId)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Cloud/List/After");
		url.append("?");
		url.append("id_=" + cloudId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				cloudEventObject);
	}

	/**
	 * 获取某个云文件之前的最多20个云文件
	 * 
	 * @param cloudId
	 * @return 1.chatGroups是String的数组 2.files是CloudEvent的对象数组 3.owner是User对象
	 *         4.activity是对象
	 * @throws Exception
	 */
	public List<CloudEvent> getCloudFilesBeforeCloudId(String cloudId)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Cloud/List/Before");
		url.append("?");
		url.append("id_=" + cloudId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				cloudEventObject);
	}

	/**
	 * 获取某个特定的云文件
	 * 
	 * @param cloudId
	 * @return 1.chatGroups是String的数组 2.files是CloudEvent的对象数组 3.owner是User对象
	 *         4.activity是对象
	 * @throws Exception
	 */
	public CloudEvent getOneCloudFile(String cloudId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Cloud/Single");
		url.append("?");
		url.append("id_=" + cloudId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (CloudEvent) GsonTransferUtil.transferToObject(
				new String(result), cloudEventObject);
	}

	/**
	 * 获取某个用户的云文件
	 * 
	 * @param userId
	 * @return 1.chatGroups是String的数组 2.files是CloudEvent的对象数组 3.owner是User对象
	 *         4.activity是对象
	 * @throws Exception
	 */
	public List<CloudEvent> getUserCloudFile(String userId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Cloud/List/User");
		url.append("?");
		url.append("id_=" + userId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				cloudEventObject);
	}
}
