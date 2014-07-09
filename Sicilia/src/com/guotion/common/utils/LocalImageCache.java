package com.guotion.common.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 主要用于好友头像缓存，如会话头像和好友列表头像
 * 退出程序后才摧毁
 * @author Qmy
 *
 */
public class LocalImageCache {

	private static LocalImageCache instance = null;
	
	private HashMap<String,SoftReference<Bitmap>> bitmapCaches = null;
	
	public LocalImageCache(){
		bitmapCaches = new HashMap<String, SoftReference<Bitmap>>();
	}
	
	public static LocalImageCache get(){
		if (instance == null) {
			instance = new LocalImageCache();
		}
		return instance;
	}
	
	/**
	 * 载入本地图片
	 * @param context 
	 * @param pathName 文件完整路径
	 * @param h 图片最大高度
	 * @param w 图片最大宽度
	 * @return
	 */
//	public Bitmap loadImageBitmap(Context context, String pathName, float h, float w){
//		if (bitmapCaches.get(pathName) == null || bitmapCaches.get(pathName).get() == null) {
//			Bitmap bitmap = AndroidFilesUtil.getBitmap(context, pathName, h, w);
//			SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);
//			bitmap = null;
//			bitmapCaches.put(pathName, softReference);
//		}
//		return bitmapCaches.get(pathName).get();		
//	}
	
	
	/**
	 * 载入本地图片
	 * @param context 
	 * @param pathName 文件完整路径
	 * @return
	 */
	public Bitmap loadImageBitmap( String pathName){
		if (bitmapCaches.get(pathName) == null || bitmapCaches.get(pathName).get() == null) {
			Bitmap bitmap = BitmapFactory.decodeFile(pathName);
			SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);
			bitmap = null;
			bitmapCaches.put(pathName, softReference);
		}
		return bitmapCaches.get(pathName).get();		
	}
	
	
	/**
	 * 销毁缓存的图片资源
	 */
	public void destroyCache(){
		Iterator<Entry<String, SoftReference<Bitmap>>> iter = bitmapCaches.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, SoftReference<Bitmap>> entry = iter.next();
			SoftReference<Bitmap> softReference = entry.getValue();
			if (softReference.get() != null) {
				softReference.get().recycle();
			}
			softReference = null;
		}
		bitmapCaches.clear();
		bitmapCaches = null;
		System.gc();
	}
	
	public void putCache(String key , Bitmap value){
		bitmapCaches.put(key, new SoftReference<Bitmap>(value));
	}
	public Bitmap getCache(String key) {
		return bitmapCaches.get(key).get();
	}
}
