package com.guotion.common.media;

import java.io.File;

import android.media.MediaRecorder;

public class AudioRecorder {
	private String path = null;
	private MediaRecorder mediaRecorder;
	
	public AudioRecorder(){
		mediaRecorder = new MediaRecorder();
	}
	
	public void recordAudio() throws Exception{
//		File pathFile = new File(AppConfig.AUDIO_PATH);        	
//    	if (!pathFile.exists()) {
//    		pathFile.mkdirs();
//		}
    	//path = AppConfig.AUDIO_PATH + UUNameGenerator.createFileName("mp3");
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setOutputFile(file.getAbsolutePath());
		mediaRecorder.prepare();
		mediaRecorder.start();//开始录音
	}
	
	public void setAudioPath(String audioPath){
		this.path = audioPath;
	}
	
	public String stopRecordAudio() throws Exception{
		if(mediaRecorder != null){
			mediaRecorder.stop();		
		}
		return path;
	}
	public boolean cancelRecordAudio() throws Exception{
		this.stopRecordAudio();
		File file = new File(path);
		if(file.exists())
			return file.delete();
		return false;
	}
	
	public void release(){
		mediaRecorder.release();
		mediaRecorder = null;
	}
	
	public void reset() {
		mediaRecorder.reset();
	}
}
