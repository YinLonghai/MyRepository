package com.guotion.sicilia.im.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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

public class HttpsUtil {
	private static final AllowAllHostnameVerifier HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
	private static X509TrustManager xtm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

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
	private static X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
	private static HttpsURLConnection conn = null;

	public static byte[] sendPOSTRequest(String path,
			Map<String, String> params, String encoding) throws Exception {
		HttpsURLConnection conn;
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
	
	public static byte[] sendPOSTRequest1(String path,
			Map<String, String> params, String encoding) throws Exception {
		HttpsURLConnection conn;
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
		conn.setConnectTimeout(15 * 1000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);// 允许输出数据
		conn.setRequestProperty("Content-Type",
				"multipart/form-data");
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
		HttpsURLConnection conn;
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

	// 上传代码，第一个参数，为要使用的URL，第二个参数，为表单内容，第三个参数为要上传的文件，可以上传多个文件，这根据需要页定
	public static byte[] post(String actionUrl, Map<String, String> params,
			Map<String, File> files) throws IOException,
			NoSuchAlgorithmException, KeyManagementException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		if (conn instanceof HttpsURLConnection) {
			// Trust all certificates
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(new KeyManager[0], xtmArray, new SecureRandom());
			SSLSocketFactory socketFactory = context.getSocketFactory();
			((HttpsURLConnection) conn).setSSLSocketFactory(socketFactory);
			((HttpsURLConnection) conn).setHostnameVerifier(HOSTNAME_VERIFIER);
		}
		conn.setReadTimeout(5 * 1000);
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false);
		conn.setRequestMethod("POST"); // Post方式
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);
		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if (files != null)
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"CloudFile\"; filename=\""
						+ file.getKey() + "\"" + LINEND);
				sb1.append("Content-Type: multipart/form-data; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}
		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		// 得到响应码
		// //boolean success = conn.getResponseCode()==200;
		InputStream in = conn.getInputStream();
		// InputStreamReader isReader = new InputStreamReader(in);
		// BufferedReader bufReader = new BufferedReader(isReader);
		// String line = null;
		// String data = new Sting()
		// while ((line = bufReader.readLine()) != null)
		// data += line;
		//
		// outStream.close();
		// conn.disconnect();
		// System.out.println(data);
		// //return success;
		return inputStreamToByte(in);
	}
}
