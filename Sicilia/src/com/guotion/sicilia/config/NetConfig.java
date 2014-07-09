package com.guotion.sicilia.config;

public class NetConfig {
	
	//public String HOST_URL = "http://192.168.137.199:8080";	
	public static String ACCOUNT_URL_HOST = "http://gcore.oicp.net:8080";	
	public static String UPDOAL_URL = "http://gcore.oicp.net:8080/upload";
	public static String IMG_UPDOAL_URL = UPDOAL_URL + "/imgs";
	public static String AUDIO_UPDOAL_URL = UPDOAL_URL + "/audios";
	public static String HEAD_DOWNLOAD_URL = ACCOUNT_URL_HOST + "/UploadServer/users/";	
	public static String HEAD_UPLOAD_URL = ACCOUNT_URL_HOST + "/UploadServer/servlet/UserInfoImageUpload";
	
	//聊天的原图的下载地址
	public static String getImgDownloadUrl(String accountId,String fileName){
		return ACCOUNT_URL_HOST+"UploadServer/users/"+accountId+"/messages/images/"+fileName;
	}
	
	//聊天的缩略图的下载地址
	public static String getThumbnailDownloadUrl(String accountId,String fileName){
		return ACCOUNT_URL_HOST+"UploadServer/users/"+accountId+"/messages/thumbnails/"+fileName;
	}
	
	//语音的下载地址
	public static String getAudioDownloadUrl(String accountId,String fileName){
		return ACCOUNT_URL_HOST+"UploadServer/users/"+accountId+"/audios/"+fileName;
	}
}
