package com.guotion.common.PictureBrowser;

/**
 * 封装一些关于UI的工具方法
 * @author 邱明月
 * @version 1.0
 */
public class UiUtil {
	/**
	 * dp大小转换为像素大小
	 * @param context：上下文
	 * @param dpValue:dp值
	 * @return : 像素大小
	 */
	public static int dip2px(float dpValue) {
		return (int) (dpValue * 1.5f + 0.5f);
	}
	
}
