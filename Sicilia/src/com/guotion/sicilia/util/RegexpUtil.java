package com.guotion.sicilia.util;
/**
 * 正则表达式验证
 * @author 邱明月
 * @version 1.0
 */
public class RegexpUtil {
		
	/**
	 * 邮箱正则表达式
	 */
	public final static String REGEXP_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	
	/**
	 * 手机号码正则表达式
	 */
	public final static String REGEXP_PHONE = "^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
		
	/**
	 * 中文名，英译名正则表达式
	 */
	public final static String REGEXP_NAME_ZN = "[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*";
	
	/**
	 * 英文名正则表达式
	 */
	public final static String REGEXP_NAME_EN = "/\b([a-z]+(?:[a-z]+)*)\b";
	
	/**
	 * 强密码正则表达式
	 */
	public final static String REGEXP_PWD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
	
	/**
	 * 正则表达式验证
	 * @param regexp
	 * @param str
	 * @return
	 */
	public final static boolean match(String regexp,String str){
		if (regexp == null || str == null) {
			return false;
		}
		return str.matches(regexp);
	}
}
