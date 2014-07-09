package com.guotion.sicilia.im.util;

import java.util.List;

import org.apache.http.util.EncodingUtils;

import com.guotion.sicilia.bean.net.ANotification;
import com.guotion.sicilia.im.constant.ChatServerConstant;

/**
 * Created with IntelliJ IDEA. User: lizhengbin Date: 14-4-21 Time: 上午10:03 To
 * change this template use File | Settings | File Templates.
 */
public class NoteManager {
	private String CHARSET = "ISO-8859-1";
	private ANotification noteObject = new ANotification();
	private String cusException = "N:01";
	/**
	 * 创建公告
	 * 
	 * @param content
	 * @param editBy
	 *            编辑者的Id
	 * @param push
	 *            是否需要推送，是传"1" ,不需要传"0"
	 * @return 1.editBy是User的对象
	 * @throws Exception
	 */
	public ANotification createANotification(String content, String editBy,
			String push) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Note");
		url.append("?");
		url.append("content="
				+ EncodingUtils.getString(content.getBytes(), CHARSET) + "&");
		url.append("editBy=" + editBy + "&");
		url.append("push=" + push);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (ANotification) GsonTransferUtil.transferToObject(new String(
				result), noteObject);
	}

	/**
	 * 获取公告列表
	 * 
	 * @return 1.editBy是User的对象
	 * @throws Exception
	 */
	public List<ANotification> getANotifications() throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Note/List");
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				noteObject);
	}

	/**
	 * 获取单个公告
	 * 
	 * @param noteId
	 * @return 1.editBy是User的对象
	 * @throws Exception
	 */
	public ANotification getSingleANotification(String noteId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Note/Single");
		url.append("?");
		url.append("id_=" + noteId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (ANotification) GsonTransferUtil.transferToObject(new String(
				result), noteObject);
	}

	/**
	 * 更新公告
	 * 
	 * @param noteId
	 * @param content
	 * @param editBy
	 *            编辑者的id
	 * @param push
	 * @return 1.editBy是String
	 * @throws Exception
	 */
	public ANotification updateNote(String noteId, String content,
			String editBy, String push) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Note/Update");
		url.append("?");
		url.append("id_=" + noteId + "&");
		url.append("content="
				+ EncodingUtils.getString(content.getBytes(), CHARSET) + "&");
		url.append("editBy=" + editBy + "&");
		url.append("push=" + push);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (ANotification) GsonTransferUtil.transferToObject(new String(
				result), noteObject);
	}

	/**
	 * 删除公告
	 * 
	 * @param noteId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteANotification(String noteId) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Activity/Note/Delete");
		url.append("?");
		url.append("id_=" + noteId);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return GsonTransferUtil.transferToBoolean(new String(result));
	}
}
