package com.guotion.sicilia.im.util;

import java.net.URLEncoder;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import com.guotion.sicilia.bean.net.Tag;
import com.guotion.sicilia.im.constant.ChatServerConstant;

public class TagManager {
	private String CHARSET = "ISO-8859-1";
	private String cusException = "N:01";
	
	public Tag createTag(String type, String tags) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Tag/Create");
		url.append("?");
		url.append("type=" + type + "&");
		url.append("tags=" + EncodingUtils.getString(tags.getBytes(), CHARSET));
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (Tag) GsonTransferUtil.transferToObject(new String(result),
				new Tag());
	}

	public List<Tag> getTags(String type) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Tag/List");
		url.append("?");
		url.append("type=" + type);
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (List) GsonTransferUtil.transferToArray(new String(result),
				new Tag());
	}

	public Tag updateTag(String id, String tags) throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Tag/Update");
		url.append("?");
		url.append("id_=" + id + "&");
		url.append("tags=" + EncodingUtils.getString(tags.getBytes(), CHARSET));
		byte[] result = RequestSender.requestByGet(url.toString());
		if(result == null)
        	throw new Exception(cusException);
		return (Tag) GsonTransferUtil.transferToObject(new String(result),
				new Tag());
	}

	public void clearTag() throws Exception {
		StringBuilder url = new StringBuilder(
				ChatServerConstant.URL.SERVER_HOST + "/Tag/Clear");
		RequestSender.requestByGet(url.toString());
	}
}
