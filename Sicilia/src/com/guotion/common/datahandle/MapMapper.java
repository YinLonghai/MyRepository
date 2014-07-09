package com.guotion.common.datahandle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class MapMapper {
	
	public Map getMap(JSONObject obj){
		try {
			Iterator<?> it = obj.keys();
			HashMap<String, Object> map = new HashMap<String, Object>();
			while (it.hasNext()) {
				String key = (String) it.next();
				map.put(key, obj.get(key));
			}
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
