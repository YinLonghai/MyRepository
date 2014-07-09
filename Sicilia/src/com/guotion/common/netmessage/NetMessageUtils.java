package com.guotion.common.netmessage;

import com.google.gson.Gson;
import com.guotion.sicilia.R;
import com.guotion.sicilia.constants.MessageConstant;

import android.content.Context;


public class NetMessageUtils {
	
	/**
	 * 得到netMessage的附加消息
	 * @param netMessage
	 * @return
	 */
	public static String getAttachText(Context context, NetMessage netMessage){
		Object object = netMessage.getData(MessageConstant.ResultKey.ATTACH_TEXT_KEY);
		if (object != null) {
			return object + "";
		}
		//return context.getString(R.string.network_error);
		return "网络异常";
	};
	
	public static String getAttachText(NetMessage netMessage){
		Object object = netMessage.getData(MessageConstant.ResultKey.ATTACH_TEXT_KEY);
		if (object != null) {
			return object + "";
		}
		return null;
	};
	/**
	 * 得到netMessage返回的数据
	 * @param netMessage
	 * @param clazz 非集合类型的
	 * @return
	 */
	public static <T>T getResponseData(NetMessage netMessage, Class<T> clazz){
		Object object = netMessage.getData(MessageConstant.ResultKey.RESPONSE_DATA_KEY);
		if (object != null) {
			String json = object + "";
			return new Gson().fromJson(json, clazz);
		}
		return null;
	}
	
	
}
