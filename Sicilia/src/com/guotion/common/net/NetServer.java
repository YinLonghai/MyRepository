package com.guotion.common.net;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.guotion.common.datahandle.MapMessageMapper;
import com.guotion.common.datahandle.MessageMapper;
import com.guotion.common.netmessage.MapMessage;
import com.guotion.common.netmessage.NetMessage;
import com.guotion.sicilia.constants.MessageConstant;


/**
 * 网络服务抽象类
 * @author xdy
 * @version 3
 */
public abstract class NetServer {
	
	/**
	 * 处理POST请求
	 * @param url
	 * @param params
	 * @param mapper
	 * @param netListener
	 * @return
	 */
	protected NetMessage handlePOST(String url,Map<String,String> params,MessageMapper mapper,NetListener netListener){
		MapMessage message = null;
		String result = null;// 返回结果
		try {
			byte[] data = HttpUtil.getHtmlByteArrayByPost(url, params);
			if(data != null)
				result = new String(data);
		} catch (Exception e) {
			message = new MapMessage();
			message.setState(MessageConstant.ResultValue.RESULT_NETWORK_ERROR);
			if(netListener != null)
				netListener.onException(e);
			e.printStackTrace();
		}
		System.out.println("result:"+result);
		if(result == null){
			message = new MapMessage();
			message.setState(MessageConstant.ResultValue.RESULT_NETWORK_ERROR);
		}else{
			if(mapper == null){
				message = new MapMessage();
				//message.putData(NetMessageConstant.KEY_RESULT, result);
			}else{
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					message = (MapMessage) mapper.getMessage(obj);
				} catch (JSONException e) {
					message = new MapMessage();
					message.setState(MessageConstant.ResultValue.RESULT_NETWORK_ERROR);
					e.printStackTrace();
				}
			}
		}
		return message;
	}
	/**
	 * 处理POST请求
	 * @param url
	 * @param params
	 * @param mapper
	 * @return
	 */
	protected NetMessage handlePOST(String url,Map<String,String> params,MessageMapper mapper){
		return this.handlePOST(url, params, mapper,null);
	}
	/**
	 * 处理POST请求
	 * @param url
	 * @param params
	 * @return
	 */
	protected NetMessage handlePOST(String url,Map<String,String> params){
		return this.handlePOST(url, params,new MapMessageMapper());
	}
	/**
	 * 处理GET请求
	 * @param url
	 * @param mapper
	 * @param netListener
	 * @return
	 */
	protected NetMessage handleGET(String url,MessageMapper mapper,NetListener netListener){
		MapMessage message = null;
		String result = null;// 返回结果
		try {
			byte[] data = HttpUtil.getHtmlByteArrayByGet(url);
			if(data != null)
				result = new String(data);
		} catch (Exception e) {
			message = new MapMessage();
			message.setState(MessageConstant.ResultValue.RESULT_NETWORK_ERROR);
			if(netListener != null)
				netListener.onException(e);
			e.printStackTrace();
		}
		if(result == null){
			message = new MapMessage();
			message.setState(MessageConstant.ResultValue.RESULT_NETWORK_ERROR);
		}else{
			if(mapper == null){
				message = new MapMessage();
				//message.putData(NetMessageConstant.KEY_RESULT, result);
			}else{
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					message = (MapMessage) mapper.getMessage(obj);
				} catch (JSONException e) {
					message = new MapMessage();
					message.setState(MessageConstant.ResultValue.RESULT_NETWORK_ERROR);
					e.printStackTrace();
				}
			}
		}
		return message;
	}
	/**
	 * 处理GET请求
	 * @param url
	 * @param mapper
	 * @return
	 */
	protected NetMessage handleGET(String url,MessageMapper mapper){
		return this.handleGET(url, mapper, null);
	}
	/**
	 * 处理GET请求
	 * @param url
	 * @return
	 */
	protected NetMessage handleGET(String url){
		return this.handleGET(url, new MapMessageMapper());
	}
}
