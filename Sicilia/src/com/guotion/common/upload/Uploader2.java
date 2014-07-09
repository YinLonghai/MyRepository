package com.guotion.common.upload;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

import com.guotion.common.net.HttpsUtil;

import android.util.Log;
/**
 * 用于处理https地址的上传器
 * @author xdy
 *
 */
public class Uploader2 {
	
	// 上传代码，第一个参数，为要使用的URL，第二个参数，为表单内容，第三个参数为要上传的文件，可以上传多个文件，这根据需要页定  
	/**
	 * 上传文件
	 * @param actionUrl 上传的地址
	 * @param params 表单参数
	 * @param files 文件集合
	 * @param name 服务器端 name参数对应的参数值
	 * @return
	 */
    public static void upload(String actionUrl,Map<String, String> params,Map<String, File> files,String name,UploaderListener uploaderListener)  {  	
        String BOUNDARY = java.util.UUID.randomUUID().toString();  
        String PREFIX = "--", LINEND = "\r\n";  
        String MULTIPART_FROM_DATA = "multipart/form-data";  
        String CHARSET = "ISO-8859-1";  
        long fileSize = 0;
    	for (Map.Entry<String, File> file : files.entrySet()) {
    		fileSize += file.getValue().length();
    	}
    	if(uploaderListener != null)
        	uploaderListener.onPrepared((int) fileSize);
        URL uri;
		try {
			uri = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection(); 
	        if (conn instanceof HttpsURLConnection) {
				// Trust all certificates
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(new KeyManager[0], xtmArray, new SecureRandom());
				SSLSocketFactory socketFactory = context.getSocketFactory();
				((HttpsURLConnection) conn).setSSLSocketFactory(socketFactory);
				((HttpsURLConnection) conn).setHostnameVerifier(HOSTNAME_VERIFIER);
			}
	        conn.setReadTimeout(60 * 1000);  
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
	        if (files != null){
	        	for (Map.Entry<String, File> file : files.entrySet()) {  
	                StringBuilder sb1 = new StringBuilder();  
	                sb1.append(PREFIX);  
	                sb1.append(BOUNDARY);  
	                sb1.append(LINEND);  
	                sb1.append("Content-Disposition: form-data; name=\""+name+"\"; filename=\""  
	                        + file.getKey() + "\"" + LINEND);  
	                sb1.append("Content-Type: multipart/form-data; charset="  
	                        + CHARSET + LINEND);  
	                sb1.append(LINEND);  
	                outStream.write(sb1.toString().getBytes());  
	                InputStream is = new FileInputStream(file.getValue());  
	                byte[] buffer = new byte[1024];  
	                int len = 0;  
	                int size = 0;
	                while ((len = is.read(buffer)) != -1) {  
	                    outStream.write(buffer, 0, len);  
	                    size += len;
	                    if(uploaderListener != null)
	                    	uploaderListener.onUploading(size);
	                }  
	                is.close();  
	                outStream.write(LINEND.getBytes());  
	            }  
	        }
	        // 请求结束标志  
	        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();  
	        outStream.write(end_data);  
	        outStream.flush();  
	        
	        InputStream in = conn.getInputStream();  
	        byte[] data = HttpsUtil.inputStreamToByte(in);
	        System.out.println(data);
	        // 得到响应码  
	        if(conn.getResponseCode()==200) {
	        	if(uploaderListener != null ){
                	uploaderListener.finished(data);
	        	}
	        }
	        else{
	        	if(uploaderListener != null)
                    uploaderListener.onError();
	        }
	        in.close();
	        outStream.close();  
	        conn.disconnect();
		} catch (MalformedURLException e) {
			if(uploaderListener != null)
            	uploaderListener.onException(e);
			e.printStackTrace();
		} catch (IOException e) {
			if(uploaderListener != null)
            	uploaderListener.onException(e);
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			if(uploaderListener != null)
            	uploaderListener.onException(e);
			e.printStackTrace();
		} catch (KeyManagementException e) {
			if(uploaderListener != null)
            	uploaderListener.onException(e);
			e.printStackTrace();
		}  
          
    }  
    public static void uploadCloud(String actionUrl,Map<String, String> params,Map<String, File> files,UploaderListener uploaderListener){
    	upload(actionUrl, params, files, "CloudFile", uploaderListener);
    }
    
    public static String uploadFile(String actionUrl,Map<String, String> params,Map<String, File> files){
    	final StringBuilder response = new StringBuilder();
    	upload(actionUrl, params, files, "media", new UploaderListener() {
			@Override
			public void onUploading(int size) {
			}
			@Override
			public void onPrepared(int fileSize) {
			}
			@Override
			public void onException(Exception e) {
			}
			@Override
			public void onError() {
			}
			@Override
			public void finished(byte[] data) {
				response.append(new String(data));
			}
		});
    	return response.toString();
    }
    
	//上传方法二； (没有测试过)
    final String ENCORDING="UTF-8";
	public boolean upload(String filepath) throws Exception {
		String boundary = "---------------------------7db1c523809b2";//+java.util.UUID.randomUUID().toString();//
		// 分割线
		File file = new File(filepath);
		
		String fileName=new String("哈哈嗨".getBytes(),"ISO-8859-1");
		// 用来解析主机名和端口
		URL url = new URL("http://192.168.1.120/dev/index.php/Device/UploadFile?filename="+fileName+"&filetype=IMAGE");
		// 用来开启连接 
		StringBuilder sb = new StringBuilder();
		// 用来拼装请求

		/*// username字段
    	sb.append("--" + boundary + "\r\n");
    	sb.append("Content-Disposition: form-data; name=\"username\"" + "\r\n");
    	sb.append("\r\n");
    	sb.append(username + "\r\n");

    	// password字段
    	sb.append("--" + boundary + "\r\n");
    	sb.append("Content-Disposition: form-data; name=\"password\"" + "\r\n");
    	sb.append("\r\n");
    	sb.append(password + "\r\n");*/
    	
		// 文件部分
		sb.append("--" + boundary + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + filepath + "\"" + "\r\n");
		sb.append("Content-Type: application/octet-stream" + "\r\n");
		sb.append("\r\n");

		// 将开头和结尾部分转为字节数组，因为设置Content-Type时长度是字节长度
		byte[] before = sb.toString().getBytes(ENCORDING);
		byte[] after = ("\r\n--" + boundary + "--\r\n").getBytes(ENCORDING);

		// 打开连接, 设置请求头
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			// Trust all certificates
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(new KeyManager[0], xtmArray, new SecureRandom());
			SSLSocketFactory socketFactory = context.getSocketFactory();
			((HttpsURLConnection) conn).setSSLSocketFactory(socketFactory);
			((HttpsURLConnection) conn).setHostnameVerifier(HOSTNAME_VERIFIER);
		}
		conn.setConnectTimeout(10000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		conn.setRequestProperty("Content-Length", before.length + file.length() + after.length + "");
	
		conn.setDoOutput(true);
		conn.setDoInput(true);

		// 获取输入输出流
		OutputStream out = conn.getOutputStream();
		FileInputStream fis = new FileInputStream(file);
		// 将开头部分写出
		out.write(before);

		// 写出文件数据
		byte[] buf = new byte[1024*5];
		int len;
		while ((len = fis.read(buf)) != -1)
		out.write(buf, 0, len);

		// 将结尾部分写出
		out.write(after);
		
		 InputStream in = conn.getInputStream();  
		 InputStreamReader isReader = new InputStreamReader(in);  
		 BufferedReader bufReader = new BufferedReader(isReader);  
	        String line = null;  
	        String data = "getResult=";  
	        while ((line = bufReader.readLine()) != null)  
	            data += line;     
	        Log.e("fromServer", "result="+data);
	        boolean sucess=conn.getResponseCode() == 200;
	        in.close();
			fis.close();
			out.close();
			conn.disconnect();
			
			return sucess;  
		}
	
	public static byte[] post(String actionUrl, Map<String, String> params) throws Exception {
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "-----------------------------", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "ISO-8859-1";
		URL uri;
		
			uri = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
			if (conn instanceof HttpsURLConnection) {
				// Trust all certificates
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(new KeyManager[0], xtmArray, new SecureRandom());
				SSLSocketFactory socketFactory = context.getSocketFactory();
				((HttpsURLConnection) conn).setSSLSocketFactory(socketFactory);
				((HttpsURLConnection) conn)
						.setHostnameVerifier(HOSTNAME_VERIFIER);
			}
			conn.setReadTimeout(60 * 1000);
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false);
			conn.setRequestMethod("POST"); // Post方式
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", CHARSET);
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" +"---------------------------"+BOUNDARY);
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}
			System.out.println(sb.toString());
			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			outStream.write(sb.toString().getBytes());
			
			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + "--" + LINEND).getBytes();
			outStream.write(end_data);System.out.println(new String(end_data));
			outStream.flush();

			InputStream in = conn.getInputStream();
			byte[] data = HttpsUtil.inputStreamToByte(in);
			System.out.println(data);
			// 得到响应码
//			if (conn.getResponseCode() == 200) {
//				
//			} else {
//				
//			}
			in.close();
			outStream.close();
			conn.disconnect();
		return data;
	}
	
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
}
