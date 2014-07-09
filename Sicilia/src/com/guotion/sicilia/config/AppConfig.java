package com.guotion.sicilia.config;

import android.os.Environment;

/**
 * 文件夹路径：
 * 头像缩略图：sdcard/Guoxin/账号/userinfo/ thumbnails
 * 头像原图：sdcard/Guoxin/账号/userinfo/ heads
 * 消息缩略图：sdcard/Guoxin/账号/message/ thumbnails
 * 消息原图：sdcard/Guoxin/账号/message/images
 * 消息语音：sdcard/Guoxin/账号/message/ audios
 * 分享缩略图：sdcard/Guoxin/账号/share/ thumbnails
 * 分享原图：sdcard/Guoxin/账号/share/ images
 * 分享视频：sdcard/Guoxin/账号/share/ videos
 * 
 * 缩略图、原图接收呈现策略：
 * 发送策略：
 *  对方在线时：发送原图，并生成相应的缩略图，显示缩略图并显示正在上传的状态，上传成功或失败后修改状态。
 *  对方不在线时：上传原图，并生成相应的缩略图，显示缩略图并显示正在上传的状态，
 *  上传成功后将生成Url以文本消息发送给对方，并将Url保存到数据库，失败后修改状态。
 * 接收策略：
 *	在线图片：接收到文件后，保存原图，生成相应的缩略图，保存路径，缩略图替换默认图。
 *	离线图片：收到文本消息后，先显示默认图，下载缩略图下好后，保存缩略图路径，缩略图替换默认图。
 *
 * 呈现策略：
 *	呈现图片：获取缩略图路径，缩略图路径不存在就下载；
 *  路径存在、图片存在就显示，
 *  文件不存在，获取原图文件，存在显示缩小的原图，并生成相应的缩略图，不存在就显示图片已损坏。
 * @author Administrator
 *
 */
public interface AppConfig {
	
	/**
	 * 头像，sdcard/Guoxin/
	 */
	public String SOFT_CODE = "02";
	
	public String SOFT_VERSIONS = "1";

	

	public String PACKAGE_NAME = "com.guotion.sicilia";
	
	public String APP_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Sicilia/";
	
	/**
	 * 用户头像缩略图
	 */
	public String USERINFO_THUMBNAILS_PATH = APP_ROOT_PATH+"userinfo/thumbnails/";
	
	/**
	 * 用户头像原图
	 */
	public String USERINFO_HEADS_PATH = APP_ROOT_PATH+"userinfo/heads/";
	
	/**
	 * 消息缩略图
	 */
	public String MESSAGE_THUMBNAILS_PATH = APP_ROOT_PATH+"message/thumbnails/";
	
	/**
	 * 消息原图
	 */
	public String MESSAGE_IMAGES_PATH = APP_ROOT_PATH+"message/images/";
	
	/**
	 * 消息语音
	 */
	public String MESSAGE_AUDIO_PATH = APP_ROOT_PATH+"message/audios/";
		
	/**
	 * 分享原图
	 */
	public String SHARE_IMAGES_PATH = APP_ROOT_PATH+"share/images/";
	
	/**
	 * 分享缩略图
	 */
	public String SHARE_THUMBNAILS_PATH = APP_ROOT_PATH+"share/thumbnails/";
	
	/**
	 * 分享视频
	 */
	public String SHARE_VIDEOS_PATH = APP_ROOT_PATH+"share/videos/";
}
