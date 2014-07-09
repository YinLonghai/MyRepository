package com.guotion.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class CacheUtil {
	public static final String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String avatarCachePath = cachePath + "/Sicilia/avatar";
	public static final String cloudImageCachePath = cachePath + "/Sicilia/cloud/images";
	public static final String chatImageCachePath = cachePath + "/Sicilia/chat/images";
	public static final String thumbnailCachePath = cachePath + "/Guoxin/Cache/thumbnails";
	public static final String chatAudioCachePath = cachePath + "/Sicilia/chat/audios";
	public static final String videoCachePath = cachePath + "/Guoxin/Cache/videos";
	public static final String cloudFileCachePath = cachePath + "/Sicilia/cloud/files";
	private static CacheUtil cacheUtil = new CacheUtil();
	private CacheUtil(){}
	
	public static CacheUtil getInstence(){
		return cacheUtil;
	}
	public static void createCachePath(){
		File file;
		file = new File(avatarCachePath);
		if(!file.isDirectory()){
			file.mkdirs();
		}
		file = new File(cloudImageCachePath);
		if(!file.isDirectory()){
			file.mkdirs();
		}
		file = new File(chatImageCachePath);
		if(!file.isDirectory()){
			file.mkdirs();
		}
		file = new File(chatAudioCachePath);
		if(!file.isDirectory()){
			file.mkdirs();
		}
	}
	public String cacheFile(InputStream inputStream,String savePath, String fileName){
		if(inputStream == null) return null;
		FileOutputStream fos = null;
		try {
			String filePath = savePath+"/"+ fileName;
			fos = new FileOutputStream(filePath);
			byte buf[] = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buf)) != -1) {
				fos.write(buf, 0, len);		
			}
			return filePath;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	public String cacheFile(InputStream inputStream, String fileName){
		return this.cacheFile(inputStream, cachePath, fileName);
	}
//	public String cacheImage(InputStream inputStream, String fileName){
//		return this.cacheImage(inputStream, imageCachePath, fileName);
//	}
	
	public String cacheImage(InputStream inputStream,String savePath, String fileName){
		return this.cacheFile(inputStream, savePath, fileName);
	}
	public String cacheImage(Bitmap bitmap,String savePath, String fileName) {
		if (bitmap != null) {
			String filePath = savePath+"/"+ fileName;
			File file = new File(filePath);
			OutputStream out = null;
			try {
				out = new FileOutputStream(file);
				bitmap.compress(CompressFormat.PNG, 100, out);
				return filePath;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.flush();
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
//	public String cacheImage(Bitmap bitmap, String fileName) {
//		return this.cacheImage(bitmap, imageCachePath, fileName);
//	}
	
	public String cacheThumbnail(InputStream inputStream, String fileName){
		return this.cacheImage(inputStream, thumbnailCachePath, fileName);
	}
	public String cacheThumbnail(InputStream inputStream,String savePath, String fileName){
		return this.cacheImage(inputStream, savePath, fileName);
	}
	public String cacheThumbnail(Bitmap bitmap,String savePath, String fileName) {
		return this.cacheImage(bitmap, savePath, fileName);
	}
	public String cacheThumbnail(Bitmap bitmap, String fileName) {
		return this.cacheImage(bitmap, thumbnailCachePath, fileName);
	}
	
	public String cacheAudio(InputStream inputStream, String fileName){
		return this.cacheFile(inputStream, chatAudioCachePath, fileName);
	}
	public String cacheAudio(InputStream inputStream,String savePath, String fileName){
		return this.cacheFile(inputStream, savePath, fileName);
	}
	
	public String cacheVideo(InputStream inputStream, String fileName){
		return this.cacheFile(inputStream, videoCachePath, fileName);
	}
	public String cacheVideo(InputStream inputStream,String savePath, String fileName){
		return this.cacheFile(inputStream, savePath, fileName);
	}
	
	public boolean isSDCardAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	//
	public boolean iSCacheFileExists(String filePath){
		File file = new File(filePath);
		return file.exists();
	}
	public boolean iSCachePathExists(String savePath){
		File file = new File(savePath);
		return file.exists();
	}
//	public boolean iSCacheImageExists(String savePath, String fileName){
//		return iSCacheFileExists(savePath,fileName);
//	}
//	public boolean iSCacheImageExists(String fileName){
//		return iSCacheFileExists(imageCachePath,fileName);
//	}
//	public boolean iSCacheThumbnailExists(String savePath, String fileName){
//		return iSCacheFileExists(savePath,fileName);
//	}
//	public boolean iSCacheThumbnailExists(String fileName){
//		return iSCacheFileExists(thumbnailCachePath,fileName);
//	}
//	public boolean iSCacheAudioExists(String savePath, String fileName){
//		return iSCacheFileExists(savePath,fileName);
//	}
//	public boolean iSCacheAudioExists(String fileName){
//		return iSCacheFileExists(audioCachePath,fileName);
//	}
//	public boolean iSCacheVideoExists(String savePath, String fileName){
//		return iSCacheFileExists(savePath,fileName);
//	}
//	public boolean iSCacheVideoExists(String fileName){
//		return iSCacheFileExists(videoCachePath,fileName);
//	}
}
