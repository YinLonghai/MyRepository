package com.guotion.sicilia.im.util;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import com.guotion.sicilia.bean.net.Activity;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.im.constant.ChatServerConstant;

/**
 * Created with IntelliJ IDEA. User: lizhengbin Date: 14-4-21 Time: 上午9:21 To
 * change this template use File | Settings | File Templates.
 */
public class ActivityManager {
	private String CHARSET = "ISO-8859-1";
	private Activity activityObject = new Activity();
	private String cusException = "N:01";
	
	/**
	 * 创建活动
	 * 
	 * @param name
	 * @param content
	 * @param date
	 * @param members
	 * @param creator
	 * @param remind
	 * @param location
	 * @param dict
	 * @param endDate
	 * @return 1.members是User的数组 2.creator是User的对象 3.cloudFiles没有传递，所以是空数组
	 * @throws Exception
	 */
	public Activity createActivity(String name, String content, String date,
			String members, String creator, String remind, String location,
			String dict, String endDate) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Create");
		url.append("?");
		url.append("name=" + EncodingUtils.getString(name.getBytes(), CHARSET)
				+ "&");
		url.append("content="
				+ EncodingUtils.getString(content.getBytes(), CHARSET) + "&");
		url.append("date=" + URLEncoder.encode(date, CHARSET) + "&");
		url.append("members=" + members + "&");
		url.append("creator=" + creator + "&");
		url.append("location="
				+ EncodingUtils.getString(location.getBytes(), CHARSET) + "&");
		url.append("dict=" + dict + "&");
		url.append("remind=" + URLEncoder.encode(remind, CHARSET) + "&");
		url.append("endDate=" + URLEncoder.encode(endDate, CHARSET));
		System.out.println("url=" + url.toString());
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (Activity) GsonTransferUtil.transferToObject(new String(result),
				activityObject);
	}

	/**
	 * 删除活动
	 * 
	 * @param activityId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteActivity(String activityId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Note/Delete");
		url.append("?");
		url.append("id_=" + activityId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return GsonTransferUtil.transferToBoolean(new String(result));

	}

	/**
	 * 更新活动
	 * 
	 * @param activityId
	 * @param name
	 * @param content
	 * @param date
	 * @param dict
	 * @param location
	 * @param remind
	 * @param members
	 *            参加活动的人id，以"|"隔开,如"xxxx|xxxx"
	 * @param needPushIDs
	 *            需要推送到的好友id，以"-"隔开,如"xxxx-xxxx"
	 * @return
	 * @throws Exception
	 */
	public Activity updateActivity(String activityId, String name,
			String content, String date, String dict, String location,
			String remind, String members, String needPushIDs) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Update");
		url.append("?");
		url.append("id_=" + activityId + "&");
		url.append("name=" + EncodingUtils.getString(name.getBytes(), CHARSET)
				+ "&");
		url.append("content="
				+ EncodingUtils.getString(content.getBytes(), CHARSET) + "&");
		url.append("date=" + URLEncoder.encode(date, CHARSET) + "&");
		url.append("dict=" + dict + "&");
		url.append("location="
				+ EncodingUtils.getString(location.getBytes(), CHARSET) + "&");
		url.append("remind=" + URLEncoder.encode(remind, CHARSET) + "&");
		url.append("members=" + members + "&");
		url.append("needPushIDs=" + needPushIDs);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (Activity) GsonTransferUtil.transferToObject(new String(result),
				activityObject);
	}

	/**
	 * 更新单个成员参与情况
	 * 
	 * @param activityId
	 * @param condition
	 *            id:1,id:-1 参与情况{id:1,id:-1,id:0}，0-等待回应，1－参加，－1-拒绝
	 * @return 1.cloudFiles是对象
	 * @throws Exception
	 */

	public Activity updateMembers(String activityId, String condition)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Update/Member");
		url.append("?");
		url.append("id_=" + activityId + "&");
		url.append("condition=" + condition);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (Activity) GsonTransferUtil.transferToObject(new String(result),
				activityObject);
	}

	/**
	 * 增加云分享
	 * 
	 * @param activityId
	 * @param userId
	 * @param name
	 * @param desc
	 * @param isPrivate
	 *            是否为私有，1为私有，0为公有
	 * @param file
	 * @return 1.creator是String
	 *         2.cloudFiles是CloudEvent的数组，cloudFiles中的files是String
	 * @throws Exception
	 */
	public Activity addCloudFile(String activityId, String userId, String name,
			String desc, String isPrivate, File file) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/AddCloudFile");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> fileMap = new HashMap<String, File>();
		params.put("Aid", activityId);
		params.put("id_", userId);
		params.put("name", EncodingUtils.getString(name.getBytes(), CHARSET));
		params.put("desc", EncodingUtils.getString(desc.getBytes(), CHARSET));
		params.put("isPrivate", isPrivate);
		fileMap.put("CloudFile", file);
		byte[] result = RequestSender.requestByPostWithFile(url.toString(),
				params, fileMap);
		if(result == null)
        	throw new Exception(cusException);
		return (Activity) GsonTransferUtil.transferToObject(new String(result),
				activityObject);
	}

	/**
	 * 获取活动列表
	 * 
	 * @return 1.members是User的对象数组 2.creator是User对象
	 * @throws Exception
	 */
	public List<Activity> getActivities() throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/List");
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				new Activity());
	}

	/**
	 * 获取某个活动之后的活动
	 * 
	 * @param activityId
	 * @return
	 * @throws Exception
	 */
	public List<Activity> getActivitiesAfterActivity(String activityId)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/List/After");
		url.append("?");
		url.append("id_=" + activityId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				activityObject);
	}

	/**
	 * 获取某个活动之前的活动
	 * 
	 * @param activityId
	 * @return
	 * @throws Exception
	 */
	public List<Activity> getActivitiesBeforActivity(String activityId)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/List/Before");
		url.append("?");
		url.append("id_=" + activityId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				activityObject);
	}

	/**
	 * 获取某个活动的信息
	 * 
	 * @param activityId
	 * @return 1.creator是对象 2.members是对象数组
	 * @throws Exception
	 */
	public Activity getSingleActivity(String activityId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Single");
		url.append("?");
		url.append("id_=" + activityId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (Activity) GsonTransferUtil.transferToObject(new String(result),
				activityObject);
	}

	/**
	 * 评论活动
	 * 
	 * @param userId
	 * @param cloudId
	 * @param content
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public Activity commentActivity(String userId, String cloudId,
			String content, String date) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Comment/Reply");
		url.append("?");
		url.append("cid=" + cloudId + "&");
		url.append("msg=" + URLEncoder.encode(content, CHARSET) + "&");
		url.append("id_=" + userId + "&");
		url.append("date=" + URLEncoder.encode(date, CHARSET));
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (Activity) GsonTransferUtil.transferToObject(new String(result),
				activityObject);
	}

	/**
	 * 删除活动评论
	 * 
	 * @param chatItemId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteActivityComment(String chatItemId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Comment/Delete");
		url.append("?");
		url.append("id_=" + chatItemId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return GsonTransferUtil.transferToBoolean(new String(result));
	}

	/**
	 * 获取活动评论列表
	 * 
	 * @param activityId
	 *            活动id
	 * @return
	 * @throws Exception
	 */
	public List<ChatItem> getActivityCommentList(String activityId)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Comment/List");
		url.append("?");
		url.append("cid=" + activityId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				new ChatItem());
	}

	/**
	 * 更新生日状态
	 * 
	 * @param activityId
	 * @param name
	 * @param content
	 * @param date
	 * @param lunarDate
	 * @param remind
	 * @return
	 * @throws Exception
	 */
	public Activity updateBirthday(String activityId, String name,
			String content, String date, String lunarDate, String remind)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST
						+ "/Activity/Update/Birthday");
		url.append("?");
		url.append("id_=" + activityId + "&");
		url.append("name=" + EncodingUtils.getString(name.getBytes(), CHARSET)
				+ "&");
		url.append("content="
				+ EncodingUtils.getString(content.getBytes(), CHARSET) + "&");
		url.append("date=" + URLEncoder.encode(date, CHARSET) + "&");
		url.append("lunarDate=" + lunarDate + "&");
		url.append("remind=" + URLEncoder.encode(remind, CHARSET));
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (Activity) GsonTransferUtil.transferToObject(new String(result),
				activityObject);
	}

	/**
	 * 更新生日推送的情况
	 * 
	 * @param activityId
	 * @param remind
	 * @return
	 * @throws Exception
	 */
	public Activity updateBirthdayPushState(String activityId, String remind)
			throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST
						+ "/Activity/Update/Birthday/Push");
		url.append("?");
		url.append("id_=" + activityId + "&");
		url.append("remind=" + URLEncoder.encode(remind, CHARSET));
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (Activity) GsonTransferUtil.transferToObject(new String(result),
				activityObject);
	}

}
