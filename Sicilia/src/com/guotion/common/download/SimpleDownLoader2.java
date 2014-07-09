package com.guotion.common.download;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import com.guotion.common.utils.CacheUtil;
/**
 * 用于下载https地址的文件
 * @author Administrator
 *
 */
public class SimpleDownLoader2 {
	public void downLoad(final String url,final String savepath,final String fileName,final DownloadListener downloadListener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				FileOutputStream fos = null;
				InputStream is = null;
				try {
					URL myFileUrl = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
					if (conn instanceof HttpsURLConnection) {
						// Trust all certificates
						SSLContext context = SSLContext.getInstance("TLS");
						context.init(new KeyManager[0], xtmArray, new SecureRandom());
						SSLSocketFactory socketFactory = context.getSocketFactory();
						((HttpsURLConnection) conn).setSSLSocketFactory(socketFactory);
						((HttpsURLConnection) conn).setHostnameVerifier(HOSTNAME_VERIFIER);
					}
					conn.setDoInput(true);
					conn.connect();
					is = conn.getInputStream();
					if (is == null) { // 没有下载流
						//throw new Exception("无法获取文件");
						if(downloadListener != null)
							downloadListener.onException(new Exception("无法获取文件"));
						return ;
					}
					int fileSize = conn.getContentLength();// 根据响应获取文件大小
					if (fileSize <= 0) { // 获取内容长度为0
						//throw new Exception("无法获知文件大小 ");
						if(downloadListener != null)
							downloadListener.onException(new Exception("无法获知文件大小 "));
						return ;
					}else {
						if(downloadListener != null)
							downloadListener.onPrepared(fileSize);
					}
					fos = new FileOutputStream(savepath +"/"+ fileName); // 创建写入文件内存流，
					byte buf[] = new byte[1024];
					int downLoadFilePosition = 0;
					int numread = 0;
					while ((numread = is.read(buf)) != -1) {
//						try {
//							Thread.sleep(50);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						fos.write(buf, 0, numread);
						downLoadFilePosition += numread;
						if(downloadListener != null)
							downloadListener.onDownloadSize(downLoadFilePosition);		
					}
					if(downloadListener != null)
						downloadListener.finished(savepath +"/"+ fileName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					if(downloadListener != null)
						downloadListener.onException(e);
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					if(downloadListener != null)
						downloadListener.onException(e);
					e.printStackTrace();
				} catch (KeyManagementException e) {
					if(downloadListener != null)
						downloadListener.onException(e);
					e.printStackTrace();
				}finally{
					try {
						if(fos != null){
							fos.close();
						}
						if(is != null){
							is.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}
	
	public void downLoad(String url, String savepath, String fileName) {
		this.downLoad(url, savepath, fileName, null);
	}
	
	public void downLoad(String url, DownloadListener downloadListener) {
		String savepath = CacheUtil.cachePath;
		String fileName = System.currentTimeMillis()+"";
		this.downLoad(url, savepath, fileName, downloadListener);
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
