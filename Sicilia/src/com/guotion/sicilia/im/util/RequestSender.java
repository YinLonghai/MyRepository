package com.guotion.sicilia.im.util;

import java.io.File;
import java.util.Map;

import com.guotion.common.upload.Uploader2;
import com.guotion.common.upload.UploaderListener;

public class RequestSender {

	public static byte[] requestByGet(String url) throws Exception {
		try {
			byte[] result = HttpsUtil.sendGETRequest(url);
			return dealByteArray(result);
		} catch (Exception e) {
			throw new Exception("N:01");
		}
	}

	public static byte[] requestByPost(String url, Map<String, String> params,
			String encoding) throws Exception {
		try {
			byte[] result = HttpsUtil.sendPOSTRequest(url, params, encoding);
			return dealByteArray(result);
		} catch (Exception e) {
			throw new Exception("N:01");
		}
	}
	
	public static byte[] requestByPostNoFile(String url, Map<String, String> params,
			String encoding) throws Exception {
		try{
			byte[] result = Uploader2.post(url,params);
			return dealByteArray(result);
		}catch(Exception e){
			throw new Exception("N:01");
		}
	}

	public static byte[] requestByPostWithCloudFile(String url,Map<String,String> params,
			Map<String,File> fileMap) throws Exception{
		final StringBuilder builder = new StringBuilder();
		Uploader2.uploadCloud(url,params,fileMap,new UploaderListener(){

			@Override
			public void onPrepared(int fileSize) {
			}

			@Override
			public void onUploading(int size) {
			}

			@Override
			public void finished(byte[] data) {
				builder.append(new String(data));
			}

			@Override
			public void onException(Exception e) {
			}

			@Override
			public void onError() {
			}
			
		});
		if(builder.length()==0)
			throw new Exception("N:01");
		return builder.toString().getBytes();
	}
	
	public static byte[] requestByPostWithFile(String url,
			Map<String, String> params, Map<String, File> fileMap)
			throws Exception {
		try {
			String result = Uploader2.uploadFile(url, params, fileMap);
			if(result.length()==0)
				throw new Exception("N:01");
			return result.getBytes();
		} catch (Exception e) {
			throw new Exception("N:01");
		}
	}

	private static byte[] dealByteArray(byte[] result) throws Exception {
		if (result == null)
			throw new Exception("N:01");
		else
			return result;
	}
	
	
}
