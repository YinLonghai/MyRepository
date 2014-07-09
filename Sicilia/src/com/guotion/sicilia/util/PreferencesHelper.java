package com.guotion.sicilia.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Preferences储存读取帮助类
 * @author 邱明月
 * @version 1.0
 */
public class PreferencesHelper {
	
	private SharedPreferences sharedPreferences = null;
		
	

	public PreferencesHelper(Context context) {
		sharedPreferences = context.getSharedPreferences("config", Context.MODE_APPEND);
	}
	
	
	/**
	 * 保存键为key的值为vlaue
	 * @param key
	 * @param vlaue
	 */
	public void put(String key,int vlaue){		
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, vlaue);
		editor.commit();
	}
	
	/**
	 * 保存键为key的值为vlaue
	 * @param key
	 * @param vlaue
	 */
	public void put(String key,String vlaue){		
		Editor editor = sharedPreferences.edit();
		editor.putString(key, vlaue);
		editor.commit();
	}
	
	
	/**
	 * 保存键为key的值为vlaue
	 * @param key
	 * @param vlaue
	 */
	public void put(String key,boolean vlaue){		
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, vlaue);
		editor.commit();
	}
	
	/**
	 * 保存键为key的值为vlaue
	 * @param key
	 * @param vlaue
	 */
	public void put(String key,long vlaue){		
		Editor editor = sharedPreferences.edit();
		editor.putLong(key, vlaue);
		editor.commit();
	}
	
	
	/**
	 * 保存键为key的值为vlaue
	 * @param key
	 * @param vlaue
	 */
	public void put(String key,float vlaue){		
		Editor editor = sharedPreferences.edit();
		editor.putFloat(key, vlaue);
		editor.commit();
	}
	/**
	 * key对应的整型值叠加1
	 * @param key
	 */
	public void superposition(String key){
		int vlaue = sharedPreferences.getInt(key, 0);
		Editor editor = sharedPreferences.edit();
		vlaue ++;
		editor.putFloat(key, vlaue);
		editor.commit();
	}
	
	public int getInt(String key, int defult){
		return sharedPreferences.getInt(key, defult);
	}
	
	public int getInt(String key){
		return sharedPreferences.getInt(key, 0);
	}
	
	public boolean getBoolean(String key){
		return sharedPreferences.getBoolean(key, true);
	}
	
	public boolean getBoolean(String key, boolean isTrue){
		return sharedPreferences.getBoolean(key, isTrue);
	}

	public String getString(String key) {
		return sharedPreferences.getString(key, null);
	}

	public String getString(String key,String defult) {
		return sharedPreferences.getString(key, defult);
	}

	public long getLong(String key, long defult) {
		return sharedPreferences.getLong(key, defult);
	}
	
	public long getLong(String key) {
		return sharedPreferences.getLong(key, 0);
	}
}
