package com.guotion.sicilia.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils{
	/**
	 * 写入数据到指定文件
	 * @param filePath 文件路径
	 * @param text 写入内容
	 * @param append 是否追加内容
	 */
	public static boolean write(String filePath, String text, boolean append){
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			FileOutputStream fw = new FileOutputStream(filePath, append);
			byte[] byt = text.getBytes();
			fw.write(byt);
			fw.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			return false;
		} 
	}
	
	/**
	 * 　　* 读取文件内容 　　* @param filePath 　　* @return 文件内容 　　
	 */
	public static String readFile(String filePath){
		String str = null;
		try
		{
			File readFile = new File(filePath);
			if(!readFile.exists())
			{
				return null;
			}
			FileInputStream inStream = new FileInputStream(readFile);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1)
			{
				stream.write(buffer, 0, length);
			}
			str = stream.toString();
			stream.close();
			inStream.close();
			return str;
		}catch (FileNotFoundException e){
			e.printStackTrace();
			return null;
		}catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}
}
