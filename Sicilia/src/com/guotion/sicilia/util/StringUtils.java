package com.guotion.sicilia.util;

import java.util.LinkedList;
import java.util.List;

import android.text.TextUtils;

public class StringUtils {
	public static List<Integer> getIntegers(String text, String expression){	
		List<Integer> integers = new LinkedList<Integer>();
		if (text != null && expression != null) {
			String[] strings = TextUtils.split(text, expression);		
			for (String string:strings) {
				try {
					integers.add(Integer.valueOf(string));
				} catch (NumberFormatException e) {
					LogUtil.e("NumberFormatException:"+e.getMessage());
				}
				
			}
		}		
		return integers;
	}
	/**
	 * 得到文件后缀
	 * @param filePath 文件的完整路径
	 * @return
	 */
	public static String getFileSuffix(String filePath){
		if (filePath == null) {
			return null;
		}
		return filePath.substring(filePath.lastIndexOf(".")+1);  
	}
}
