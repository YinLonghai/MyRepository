package com.guotion.sicilia.ui.view;

import java.io.File;
import com.guotion.common.PictureBrowser.PictureBrowseView;
import com.guotion.common.download.DownloadListener;
import com.guotion.common.download.SimpleDownLoader2;
import com.guotion.common.media.AudioPlayer;
import com.guotion.common.media.AudioPlayerListener;
import com.guotion.common.media.SongInfoUtils;
import com.guotion.common.upload.UploaderListener;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.common.utils.MyUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.Chat;
import com.guotion.sicilia.im.ChatServer;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.ui.listener.MessageFeedbackListener;
import com.guotion.sicilia.util.ExpressionUtil;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MsgRightView extends RelativeLayout {
	private final int IMG_PREPARED = 0;
	private final int IMG_UPLOADING = 1;
	private final int IMG_FINISHED = 2;
	private final int IMG_FAILED = 3;
	
	private final int AUDIO_UPLOAD_PREPARED = 4;
	private final int AUDIO_UPLOADING = 5;
	private final int AUDIO_UPLOAD_FINISHED = 6;
	private final int AUDIO_UPLOAD_FAILED = 7;
	
	private final int AUDIO_PLAY_PREPARED = 8;
	private final int AUDIO_PLAYING = 9;
	private final int AUDIO_PLAY_FINISHED = 10;
	private final int AUDIO_PLAY_FAILED = 11;
	
	private final int AUDIO_DOWNLOAD_FINISHED = 12;
	
	private boolean isPlaying = false;
	
	private ImageView rightHead;
	private TextView rightContent;
	private ImageView playVoice;
	private TextView rightAudiotime;
	private TextView sign;
	RelativeLayout right;
	
	private String headPhotoPath;
	private Context context;
	private AudioPlayer audioPlayer;
	private ChatItem chatItemInfo;
	private AnimationDrawable anim;
	
	private MessageFeedbackListenerImpl messageFeedbackListenerImpl;
	private Chat chat = ChatServer.getInstance().getChat();
	@SuppressLint("NewApi")
	private MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			//处理发送图片消息
			case IMG_UPLOADING:
				break;
			case IMG_PREPARED:
				break;
			case IMG_FINISHED:
				break;
			case IMG_FAILED:
				break;
			//处理发送音频消息
			case AUDIO_UPLOAD_PREPARED:
				break;
			case AUDIO_UPLOADING:
				break;
			case AUDIO_UPLOAD_FINISHED:
				break;
			case AUDIO_UPLOAD_FAILED:
				
				break;
				
			case AUDIO_DOWNLOAD_FINISHED:
				String filepath = msg.getData().getString("filepath");System.out.println("filepath="+filepath);
				rightAudiotime.setText(new SongInfoUtils(context).getFileInfo(filepath)[0]+"`");
				break;
			//处理播放音频消息
			case AUDIO_PLAY_PREPARED:
				isPlaying = true;
				playVoice.setBackgroundResource(R.drawable.playvoice_animation);
				anim = (AnimationDrawable) playVoice.getBackground();
				anim.start();
				break;
			case AUDIO_PLAYING:
				int position = msg.getData().getInt("position");
				
				break;
			case AUDIO_PLAY_FINISHED:
				isPlaying = false;
				if (anim != null) {
					anim.stop();
				}
				playVoice.setBackgroundResource(R.drawable.playingvoice);
				break;
			case AUDIO_PLAY_FAILED:
				isPlaying = false;
				if (anim != null) {
					anim.stop();
				}
				playVoice.setBackgroundResource(R.drawable.playingvoice);
				break;
			}
		}
	};
	
	public MsgRightView(Context context){
		super(context);
		this.context = context;
		initView();
		initListener();
	}
	public MsgRightView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
		initListener();
	}
	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_msg_right, this);
		rightHead = (ImageView) findViewById(R.id.imageView_msg_right_head);
		rightContent = (TextView) findViewById(R.id.textView_msg_right_content);
		playVoice = (ImageView) findViewById(R.id.imageView_msg_right_play);
		rightAudiotime = (TextView) findViewById(R.id.textView_msg_right_audiotime);
		sign = (TextView) findViewById(R.id.textView_msg_right_sign);
		right = (RelativeLayout) findViewById(R.id.relativeLayout_msg_right);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(context).getInt(AppData.THEME);
		try{
			right.setBackgroundResource(AppData.getThemeImgResId(theme, "bubble_outgoing"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@SuppressLint("NewApi")
	private void initData(){
		String imgUrl = chatItemInfo.userInfo.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+imgUrl,rightHead,R.drawable.head_orang,R.drawable.head_orang);
			}else{
				rightHead.setImageBitmap(bitmap);
			}
		}else{
			rightHead.setImageResource(R.drawable.head_orang);
		}
		
		String type = chatItemInfo.mediaType;System.out.println("type="+type);
		rightContent.setVisibility(View.VISIBLE);
		playVoice.setVisibility(View.GONE);
		rightAudiotime.setVisibility(View.GONE);
		if(type == null || type.equals("")){
			rightContent.setText(chatItemInfo.msg);
		}else if(MyUtil.isAudioFile(type)){System.out.println("AudioFile");
			rightContent.setVisibility(View.GONE);
			playVoice.setVisibility(View.VISIBLE);
			rightAudiotime.setVisibility(View.VISIBLE);
			String fileName = chatItemInfo.mediaUrl.substring(chatItemInfo.mediaUrl.lastIndexOf("/"));
			fileName = fileName.replace(":", "_");//System.out.println("fileName="+fileName);
			String audioPath = CacheUtil.chatAudioCachePath+fileName;//System.out.println("audioPath="+audioPath);
			if (CacheUtil.getInstence().iSCacheFileExists(audioPath)) {
				//rightAudiotime.setText(new SongInfoUtils(context).getFileInfo(audioPath)[0]+"`");
				mediaMetadataRetriever.setDataSource(audioPath);
				rightAudiotime.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
			}else{
				SimpleDownLoader2 simpleDownLoader2 = new SimpleDownLoader2();
				simpleDownLoader2.downLoad(ChatServerConstant.URL.SERVER_HOST+chatItemInfo.mediaUrl, CacheUtil.chatAudioCachePath, fileName.substring(1),new DownloadListener() {
					@Override
					public void onPrepared(int filesize) {
					}
					@Override
					public void onException(Exception e) {
					}
					@Override
					public void onDownloadSize(int size) {
					}
					@Override
					public void finished(String filepath) {
//						Message msg = new Message();
//						msg.what = AUDIO_DOWNLOAD_FINISHED;
//						msg.getData().putString("filepath", filepath);
//						handler.sendMessage(msg);
						System.out.println("语音下载成功=="+filepath);
					}
				});
			}
		}else if(MyUtil.isImgFile(type)){System.out.println("ImgFile");
			String path = chatItemInfo.mediaUrl.substring(chatItemInfo.mediaUrl.lastIndexOf("/"));
			path = path.replace(":", "_");//System.out.println("path="+CacheUtil.chatImageCachePath+path);
			rightContent.setText(ExpressionUtil.dealImg(getContext(), chatItemInfo.msg, null, CacheUtil.chatImageCachePath+path));
		}
		
		String state = chatItemInfo.state;
		if(state.equals("0")){
			sign.setText("已发送");
		}else if(state.equals("1")){
			sign.setText("已送达");
		}else if(state.equals("2")){
			sign.setText("已读");
		}
	}
	public void setChatItemInfo(ChatItem chatItemInfo) {
		this.chatItemInfo = chatItemInfo;
		initData();
	}
	private void initListener(){
		rightContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tvContentOnClick();
			}
		});
		playVoice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tvContentOnClick();
				System.out.println("playVoice OnClickListener");
			}
		});
		rightHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skipToUserInfoActivity((Activity)context);
			}
		});
	}
	private void tvContentOnClick() {
		//String filePath = CacheUtil.cachePath+"/sg.jpg";
				if(MyUtil.isAudioFile(chatItemInfo.mediaType)){
					if (isPlaying) {
						return ;
					}
					String path = chatItemInfo.mediaUrl.substring(chatItemInfo.mediaUrl.lastIndexOf("/"));
					path = path.replace(":", "_");
					String audioPath = CacheUtil.chatAudioCachePath+path;//System.out.println("audioPath="+audioPath);
					if (audioPath == null) {
						//
					} else {
						if (CacheUtil.getInstence().iSCacheFileExists(audioPath)) {
							playAudio(audioPath);
						} else {
							//
							Toast.makeText(context, "语音文件不存在", Toast.LENGTH_SHORT).show();
						}
					}
				}else if(MyUtil.isImgFile(chatItemInfo.mediaType)){
					String path = chatItemInfo.mediaUrl.substring(chatItemInfo.mediaUrl.lastIndexOf("/"));
					path = path.replace(":", "_");
					String imgPath = CacheUtil.chatImageCachePath+path;
					Dialog dialog = new Dialog(context,R.style.dialog_download_full_screen);
					PictureBrowseView pictureBrowseView = new PictureBrowseView(context);
					String url = ChatServerConstant.URL.SERVER_HOST+chatItemInfo.mediaUrl;
					pictureBrowseView.setImageUrls(new String[]{url}, new String[]{imgPath}, null);
					dialog.setContentView(pictureBrowseView);
					dialog.show();

				}
	}
	private void showImage(String imagePath) {
		
	}
	
	private void playAudio(String filePath){System.out.println("play audio...");
	audioPlayer = new AudioPlayer(context);
	audioPlayer.play(filePath, new AudioPlayerListener() {
			@Override
			public void onPrepared(int duration) {
				Message msg = new Message();
				msg.what = AUDIO_PLAY_PREPARED;
				//msg.getData().putInt("duration", duration);
				handler.sendMessage(msg);
			}
			@Override
			public void onPlaying(int position) {
//				Message msg = new Message();
//				msg.what = AUDIO_PLAYING;
//				msg.getData().putInt("position", position);
//				handler.sendMessage(msg);
			}
			@Override
			public void onException(Exception e) {
				Message msg = new Message();
				msg.what = AUDIO_PLAY_FAILED;
				handler.sendMessage(msg);
			}
			@Override
			public void finished() {
				Message msg = new Message();
				msg.what = AUDIO_PLAY_FINISHED;
				handler.sendMessage(msg);
			}
			@Override
			public void onDestroy(){
//				Message msg = new Message();
//				msg.what = AUDIO_PLAY_FINISHED;
//				handler.sendMessage(msg);
			}
		});
	}
	private void uploadImg(){
		//SimpleUploader2 uploader = new SimpleUploader2();
		//File file = new File(chatMessagePartInfo.filePath);
		File file = new File(CacheUtil.cachePath+"/sg.jpg");
		if(file.exists()){
			//uploader.uploadFile(file, new ImgUploaderListener());
		}else{
			//ivSign.setImageResource(resId);
			Toast.makeText(context, "图片不存在", Toast.LENGTH_SHORT).show();
		}
	}
	private void uploadAudio(){
		//SimpleUploader2 uploader = new SimpleUploader2();
//		File file = new File(audioPart.filePath);
//		if(file.exists()){
//			//uploader.uploadFile(file, new AudioUploaderListener());
//		}else{
//			//ivSign.setImageResource(resId);
//			Toast.makeText(context, "图片不存在", Toast.LENGTH_SHORT).show();
//		}
	}
	
	private class ImgUploaderListener implements UploaderListener{
		@Override
		public void onPrepared(int fileSize) {
			Message msg = new Message();
			msg.what = IMG_PREPARED;
			msg.getData().putInt("fileSize", fileSize);
			handler.sendMessage(msg);
		}
		@Override
		public void onUploading(int size) {
			Message msg = new Message();
			msg.what = IMG_UPLOADING;
			msg.getData().putInt("size", size);
			handler.sendMessage(msg);
		}
		@Override
		public void finished(byte[] data) {
			Message msg = new Message();
			msg.what = IMG_FINISHED;
			handler.sendMessage(msg);
		}
		@Override
		public void onException(Exception e) {
			Message msg = new Message();
			msg.what = IMG_FAILED;
			handler.sendMessage(msg);
		}
		@Override
		public void onError() {
			Message msg = new Message();
			msg.what = IMG_FAILED;
			handler.sendMessage(msg);
		}
	}
	private class AudioUploaderListener implements UploaderListener{
		@Override
		public void onPrepared(int fileSize) {
			Message msg = new Message();
			msg.what = IMG_PREPARED;
			msg.getData().putInt("fileSize", fileSize);
			handler.sendMessage(msg);
		}
		@Override
		public void onUploading(int size) {
			Message msg = new Message();
			msg.what = IMG_UPLOADING;
			msg.getData().putInt("size", size);
			handler.sendMessage(msg);
		}
		@Override
		public void finished(byte[] data) {
			Message msg = new Message();
			msg.what = IMG_FINISHED;
			handler.sendMessage(msg);
		}
		@Override
		public void onException(Exception e) {System.out.println(e.getMessage());
			Message msg = new Message();
			msg.what = IMG_FAILED;
			handler.sendMessage(msg);
		}
		@Override
		public void onError() {
			Message msg = new Message();
			msg.what = IMG_FAILED;
			handler.sendMessage(msg);
		}
	}
	
	private final class MessageFeedbackListenerImpl implements MessageFeedbackListener{
		@Override
		public void messageSendSuccess(ChatItem chatItem) {
			// TODO Auto-generated method stub
		}

		@Override
		public void messageReaded(ChatItem chatItem) {
			// TODO Auto-generated method stub
			
		}
	}
}
