package com.guotion.common.upload;

public interface UploaderListener {
	
	public void onPrepared(int fileSize);
	public void onUploading(int size);
	public void finished(byte[] data);
	public void onException(Exception e);
	public void onError();
}
