package com.guotion.common.media;

public interface AudioPlayerListener {

	public void onPrepared(int duration);
	public void onPlaying(int position);
	public void finished();
	public void onException(Exception e);
	public void onDestroy();
}
