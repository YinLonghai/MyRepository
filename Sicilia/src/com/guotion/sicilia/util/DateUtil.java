package com.guotion.sicilia.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	public static String getDate(Long date){
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日  HH:mm");
		
		long nowDate = System.currentTimeMillis();
		String nowTime = format.format(nowDate);
		String time = format.format(date);
		if(nowTime.substring(0, 6).equals(time.substring(0, 6))){
			SimpleDateFormat nowFormat = new SimpleDateFormat("HH:mm");
			time = nowFormat.format(date);
		}
		return time;//10月03日 23:41
		
	}
	
	
	
	public static String getShortDate(Long date){
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日  HH:mm");
		
		long nowDate = System.currentTimeMillis();
		String nowTime = format.format(nowDate);
		String time = format.format(date);
		if(nowTime.substring(0, 6).equals(time.substring(0, 6))){
			SimpleDateFormat nowFormat = new SimpleDateFormat("HH:mm");
			time = nowFormat.format(date);
		}
		else {
			SimpleDateFormat nowFormat = new SimpleDateFormat("MM月dd日");
			time = nowFormat.format(date);
		}
		return time;//10月03日 23:41
		
	}

	public static String getLongDate(long date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d HH:mm");
		String time = format.format(date);
		return time;//10月03日 23:41
	}
		
	/**
	 * 得到的年月
	 * @return
	 */
	public static String getNowYearAndMonth(long currentTimeMillis) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String time = format.format(System.currentTimeMillis());
		return time;//10月03日 23:41
	}
	/**
	 * 得到当前的年月
	 * @return
	 */
	public static String getNowYearAndMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String time = format.format(System.currentTimeMillis());
		return time;//10月03日 23:41
	}
	
	
	public static String getLongDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = format.format(System.currentTimeMillis());
		return time;//10月03日 23:41
	}
	
	
	public static long getNowYearAndMonthTime() {
		long time = 0L;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM"); 
		String timeStr = sdf.format(System.currentTimeMillis());
		Date date;
		try {
			date = sdf.parse(timeStr);
			time = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	public static long getLongTime(String dateStr) {
		long time = 0L;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d HH:mm");
		Date date;
		try {
			date = format.parse(dateStr);
			time = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return time;
	}


	public static String getNowYYYYMMDD() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(System.currentTimeMillis());
		return time;
	}
}
