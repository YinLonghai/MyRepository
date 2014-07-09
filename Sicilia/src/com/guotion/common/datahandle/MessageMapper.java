package com.guotion.common.datahandle;

import org.json.JSONObject;

import com.guotion.common.netmessage.NetMessage;

/**
 * 把服务器返回的信息映射成网络消息类的一些方法
 * @author xdy
 * @version 1
 */

public interface MessageMapper {

	/**
	 * 封装服务器返回的信息
	 * @return
	 */
	public NetMessage getMessage(JSONObject obj);
}
