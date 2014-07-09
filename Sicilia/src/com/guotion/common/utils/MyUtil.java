package com.guotion.common.utils;

public class MyUtil {

	/**
	 * year:month:day:hour:minute:second
	 * 把一个整数转换成时间类型
	 * 如：70-->1:10
	 * @param t 大于等于0
	 */
	public static String toTimeType(int t){
		if(t<=0) return "0:0";
		String time ;
		int tmp = 0;
		
		//秒
		tmp = t / 60;
		if(tmp != 0){
			time = ":"+t % 60;
			t = tmp;
		}else {
			time = "0:"+t;
			return time;
		}
		//分
		tmp = t / 60;
		if(tmp != 0){
			time = ":"+t % 60 + time;
			t = tmp;
		}else {
			time = t+time;
			return time;
		}
		//计算小时
		tmp = t / 24;
		if(tmp != 0){
			time = ":"+t % 24 + time;
			t = tmp;
		}else {
			time = t+time;
			return time;
		}
		//计算日
		tmp = t / 30;
		if(tmp != 0){
			time = ":"+t % 30 + time;
			t = tmp;
		}else {
			time = t+time;
			return time;
		}
		//月
		tmp = t / 12;
		if(tmp != 0){
			time = ":"+t % 12 + time;
			t = tmp;
		}else {
			time = t+time;
			return time;
		}
		//年
		return t+time;
	}
	
	public static boolean isImgFile(String path){
		if(path.equalsIgnoreCase("png") || path.equalsIgnoreCase("jpg") || path.equalsIgnoreCase("jpeg") || path.equalsIgnoreCase("gif")){
			return true;
		}
		return false;
	}
	public static boolean isAudioFile(String path){
		if(path.equalsIgnoreCase("mp3") ||path.equalsIgnoreCase("caf") || path.equalsIgnoreCase("wav") || path.equalsIgnoreCase("wma") || path.equalsIgnoreCase("acc") || path.equalsIgnoreCase("ogg")){
			return true;
		}
		return false;
	}
}
