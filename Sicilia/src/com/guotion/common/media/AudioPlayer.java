package com.guotion.common.media;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
/**
 * 一个简单的音频播放工具
 * @author xdy
 *
 */
public class AudioPlayer {

	private String path;//音频文件路径
    private MediaPlayer mediaPlayer;
    private int position;
    private int duration;
    AudioPlayerListener audioplayerListener;
    public AudioPlayer(Context comtext){
    	if(comtext != null){
    		TelephonyManager telephonyManager = (TelephonyManager) comtext.getSystemService(Context.TELEPHONY_SERVICE);
    		telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
    	}
    	mediaPlayer = new MediaPlayer();
    }
    
    public int getDuration(String path) {
    	try {
    		mediaPlayer.reset();//把各项参数恢复到初始状态
			mediaPlayer.setDataSource(path);
			return mediaPlayer.getDuration();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
    public int getDuration() {
		return duration;
	}

	public String getPath() {
		return path;
	}
    /**
     * 设置音频文件路径
     * @param path
     */
	public void setPath(String path) {
		this.path = path;
	}
    
    public void setAudioplayerListener(AudioPlayerListener audioplayerListener) {
		this.audioplayerListener = audioplayerListener;
	}

	private final class MyPhoneListener extends PhoneStateListener{
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://来电
				if(mediaPlayer.isPlaying()) {
					position = mediaPlayer.getCurrentPosition();
					mediaPlayer.stop();
				}
				break;

			case TelephonyManager.CALL_STATE_IDLE:
				if(position>0 && path!=null){
					play(position);
					position = 0;
				}
				break;
			}
		}
    }
    /**
     * 开始播放
     * @param position 播放的位置
     */
    public void play(int position) {
    	File audio = new File(path);
    	if(!audio.exists())
    		return;
		try {
			//System.out.print("start play..........");
			mediaPlayer.reset();//把各项参数恢复到初始状态
			mediaPlayer.setDataSource(path);
			mediaPlayer.setLooping(false);
			mediaPlayer.prepare();//进行缓冲
			mediaPlayer.setOnPreparedListener(new PrepareListener(position));
			mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

				@Override
				public void onCompletion(MediaPlayer mp) {
					//onDestroy();
					if(audioplayerListener != null)
						audioplayerListener.finished();
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
			if(audioplayerListener != null)
				audioplayerListener.onException(e);
		}
	}
    public void play(String filePath,AudioPlayerListener audioplayerListener){
    	this.audioplayerListener = audioplayerListener;
    	play(filePath,0);
    }
    /**
     * 开始播放
     * @param filePath 音频文件路径
     * @param position 播放的位置
     */
    public void play(String filePath,int position){
    	this.path = filePath;
    	this.play(position);
    }
    
	private final class PrepareListener implements OnPreparedListener{
		private int position;
		public PrepareListener(int position) {
			this.position = position;
		}

		public void onPrepared(MediaPlayer mp) {
			duration = mediaPlayer.getDuration();
			
			if(position>0) mediaPlayer.seekTo(position);
			if(audioplayerListener != null){
				audioplayerListener.onPrepared(duration);
				new Thread(new Runnable() {
					@Override
					public void run() {
						int currentPosition = 0;
						while(true){
							if(!mediaPlayer.isPlaying()){
								onDestroy();
								return;
							}
							currentPosition = mediaPlayer.getCurrentPosition();
							audioplayerListener.onPlaying(currentPosition);
						}
					}
				}).start();
			}
			mediaPlayer.start();//开始播放
		}
	}
	/**
	 * 继续播放
	 */
	public void start(){
		mediaPlayer.start();
	}
	/**
	 * 暂停播放
	 */
	public void pause(){
		mediaPlayer.pause();
	}
	/**
	 * 停止播放
	 */
	public void stop(){
		mediaPlayer.stop();
	}
	public void isPlaying(){
		mediaPlayer.isPlaying();
	}
	public void onDestroy() {
		stop();
    	mediaPlayer.release();
    	mediaPlayer = null;
    	path = null;
    	audioplayerListener.onDestroy();
	}
}
