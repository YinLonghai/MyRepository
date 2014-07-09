package com.guotion.common.download;

public interface DownloadListener {
	public void onPrepared(int filesize);
	public void onDownloadSize(int size);
	public void finished(String filepath);
	public void onException(Exception e);
	//public void onError();
}
