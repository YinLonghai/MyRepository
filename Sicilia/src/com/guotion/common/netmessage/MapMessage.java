package com.guotion.common.netmessage;

import java.util.HashMap;
import java.util.Map;

import com.guotion.sicilia.constants.MessageConstant;



public class MapMessage extends NetMessage{
	Map<String, Object> datas;
	public MapMessage(){
		datas = new HashMap<String,Object>();
	}
	@Override
	public void setState(int state) {
		datas.put(MessageConstant.ResultKey.KEY_STATE, state);
	}

	@Override
	public void putData(String key, Object data) {
		datas.put(key, data);
	}

	@Override
	public int getState() {
		return (Integer) datas.get(MessageConstant.ResultKey.KEY_STATE);
	}

	@Override
	public Map<?, ?> getDatas() {
		return datas;
	}

	@Override
	public void setDatas(Map<String, Object> datas) {
		this.datas = datas;
	}

	@Override
	public Object getData(String key) {
		return datas.get(key);
	}

}
