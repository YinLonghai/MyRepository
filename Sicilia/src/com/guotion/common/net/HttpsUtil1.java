package com.guotion.common.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsUtil1 {
	private static HttpsUtil1 ins = new HttpsUtil1();
	
	TrustManager[] xtmArray = new MytmArray[] { new MytmArray() };   
	
	private HttpsUtil1(){}
	public HttpsUtil1 getInstance(){
		return ins;
	}
	
	/**  
     * 信任所有主机-对于任何证书都不做检查  
     */  
    private void trustAllHosts() {   
        // Create a trust manager that does not validate certificate chains   
        // Android 采用X509的证书信息机制   
        // Install the all-trusting trust manager   
        try {   
            SSLContext sc = SSLContext.getInstance("TLS");   
            sc.init(null, xtmArray, new java.security.SecureRandom());   
            HttpsURLConnection   
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());   
            // HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);//   
            // 不进行主机名确认   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
    }   
  
    HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {   
        @Override  
        public boolean verify(String hostname, SSLSession session) {   
            // TODO Auto-generated method stub   
            // System.out.println("Warning: URL Host: " + hostname + " vs. "   
            // + session.getPeerHost());   
            return true;   
        }
    };  
    
    public byte[] getHtmlByteArrayByGet(final String url) throws Exception {
		URL htmlUrl = null;
		InputStream inStream = null;
		
		htmlUrl = new URL(url);
		// URLConnection connection = htmlUrl.openConnection();
		HttpURLConnection httpConnection = null;
		if (htmlUrl.getProtocol().toLowerCase().equals("https")) {   
            trustAllHosts();   
            httpConnection = (HttpsURLConnection) htmlUrl.openConnection();   
            ((HttpsURLConnection) httpConnection).setHostnameVerifier(DO_NOT_VERIFY);// 不进行主机名确认   

        } else {   
        	httpConnection = (HttpURLConnection) htmlUrl.openConnection();   
        }  
		int responseCode = httpConnection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			inStream = httpConnection.getInputStream();
			byte[] datas = inputStreamToByte(inStream);
			return datas;
		}

		return null;
	}
	public byte[] getHtmlByteArrayByPost(final String url,Map<String,String> params) throws Exception {
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
		HttpURLConnection httpConnection = null;
		if (htmlUrl.getProtocol().toLowerCase().equals("https")) {   
            trustAllHosts();   
            httpConnection = (HttpsURLConnection) htmlUrl.openConnection();   
            ((HttpsURLConnection) httpConnection).setHostnameVerifier(DO_NOT_VERIFY);// 不进行主机名确认   

        } else {   
        	httpConnection = (HttpURLConnection) htmlUrl.openConnection();   
        } 
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

	/**
	* 信任所有主机-对于任何证书都不做检查   
	 */   
	class MytmArray implements X509TrustManager {   
	    public X509Certificate[] getAcceptedIssuers() {   
	        // return null;   
	        return new X509Certificate[] {};   
	    }   

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}   
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
