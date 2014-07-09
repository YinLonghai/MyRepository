package com.guotion.common.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
/**
 * 用于请求https
 * @author Administrator
 *
 */
public class HttpsUtil {
	private static final AllowAllHostnameVerifier HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();   
	private static X509TrustManager xtm = new X509TrustManager() {
	public void checkClientTrusted(X509Certificate[] chain, String authType) {}
	    public void checkServerTrusted(X509Certificate[] chain, String authType) {
	    }
		@Override
		public void checkClientTrusted(
				java.security.cert.X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void checkServerTrusted(
				java.security.cert.X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}
	   }; 
	private static  X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
	private static HttpsURLConnection conn=null;

	public static byte[] sendPOSTRequest(String path,
			Map<String, String> params, String encoding) throws Exception {
		// 1> 组拼实体数据
		// method=save&name=liming&timelength=100
		StringBuilder entityBuilder = new StringBuilder("");
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				entityBuilder.append(entry.getKey()).append('=');
				entityBuilder.append(URLEncoder.encode(entry.getValue(),
						encoding));
				entityBuilder.append('&');
			}
			entityBuilder.deleteCharAt(entityBuilder.length() - 1);
		}
		byte[] entity = entityBuilder.toString().getBytes();
		URL url = new URL(path);
		conn = (HttpsURLConnection) url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			// Trust all certificates
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(new KeyManager[0], xtmArray, new SecureRandom());
			SSLSocketFactory socketFactory = context.getSocketFactory();
			((HttpsURLConnection) conn).setSSLSocketFactory(socketFactory);
			((HttpsURLConnection) conn).setHostnameVerifier(HOSTNAME_VERIFIER);
		}
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);// 允许输出数据
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(entity);
		outStream.flush();
		outStream.close();
		if (conn.getResponseCode() == 200) {
			return inputStreamToByte(conn.getInputStream());
		}
		return null;
	}
	public static byte[] sendGETRequest(String path) throws Exception {
		URL url = new URL(path);
		conn = (HttpsURLConnection) url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			// Trust all certificates
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(new KeyManager[0], xtmArray, new SecureRandom());
			SSLSocketFactory socketFactory = context.getSocketFactory();
			((HttpsURLConnection) conn).setSSLSocketFactory(socketFactory);
			((HttpsURLConnection) conn).setHostnameVerifier(HOSTNAME_VERIFIER);
		}
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);// 允许输出数据
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		
		if (conn.getResponseCode() == 200) {
			return inputStreamToByte(conn.getInputStream());
		}
		return null;
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
