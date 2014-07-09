package com.guotion.common.datahandle;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.guotion.common.netmessage.MapMessage;
import com.guotion.common.netmessage.NetMessage;

public class MapMessageMapper implements MessageMapper {

	@Override
	public NetMessage getMessage(JSONObject obj) {
		MapMessage message = new MapMessage();
		try {
			Iterator<?> it = obj.keys();
			HashMap<String, Object> map = new HashMap<String, Object>();
			while (it.hasNext()) {
				String key = (String) it.next();
				map.put(key, obj.get(key));
			}
			message.setDatas(map);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return message;
	}

}
