package com.guotion.common.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtil {

	public static byte[] getHtmlByteArrayByGet(final String url) throws Exception {
		URL htmlUrl = null;
		InputStream inStream = null;
		
		htmlUrl = new URL(url);
		// URLConnection connection = htmlUrl.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection) htmlUrl
				.openConnection();
		int responseCode = httpConnection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			inStream = httpConnection.getInputStream();
			byte[] datas = inputStreamToByte(inStream);
			return datas;
		}

		return null;
	}
	public static byte[] getHtmlByteArrayByPost(final String url,Map<String,String> params) throws Exception {
		URL htmlUrl = null;
		InputStream inStream = null;
		StringBuilder data = new StringBuilder();
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, String> entry : params.entrySet()){
				data.append(entry.getKey()).append("=");
				//data.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				data.append(entry.getValue());
				data.append("&");
			}
			data.deleteCharAt(data.length()-1);
		}
		byte[] entity = data.toString().getBytes();
		
		htmlUrl = new URL(url);
		// URLConnection connection = htmlUrl.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection) htmlUrl
				.openConnection();
		httpConnection.setRequestMethod("POST");
		httpConnection.setDoOutput(true);
		httpConnection.setRequestProperty("Charset", "UTF-8");  //设置编码
		httpConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		httpConnection.setRequestProperty("Content-Length",
				String.valueOf(entity.length));
		OutputStream os = httpConnection.getOutputStream();
		os.write(entity);
		int responseCode = httpConnection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			inStream = httpConnection.getInputStream();
		}
		
		byte[] datas = inputStreamToByte(inStream);

		return datas;
	}
    public static byte[] inputStreamToByte(InputStream is) {
		try {
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
