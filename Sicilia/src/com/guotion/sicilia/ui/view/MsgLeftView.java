package com.guotion.sicilia.ui.view;

import java.io.File;

import com.guotion.common.PictureBrowser.PictureBrowseView;
import com.guotion.common.download.DownloadListener;
import com.guotion.common.download.SimpleDownLoader2;
import com.guotion.common.media.AudioPlayer;
import com.guotion.common.media.AudioPlayerListener;
import com.guotion.common.media.SongInfoUtils;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.common.utils.MyUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.ChatServer;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.ui.OtherInfoActivity;
import com.guotion.sicilia.util.AndroidFileUtils;
import com.guotion.sicilia.util.ExpressionUtil;
import com.guotion.sicilia.util.UISkip;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MsgLeftView extends RelativeLayout {
	private final int IMG_PREPARED = 0;
	private final int IMG_DOWNLOADING = 1;
	private final int IMG_FINISHED = 2;
	private final int IMG_FAILED = 3;
	
	private final int AUDIO_PREPARED = 4;
	private final int AUDIO_PLAYING = 5;
	private final int AUDIO_FINISHED = 6;
	private final int AUDIO_FAILED = 7;
	private final int AUDIO_DOWNLOAD_FINISHED = 8;
	
	private boolean isDownloading = false;
	private boolean isPlaying = false;
	
	@SuppressLint("NewApi")
	private MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
	
	private ImageView leftHead;
	private TextView leftContent;
	private ImageView playVoice;
	private TextView leftAudiotime;
	
	private ChatItem chatItemInfo;
	
	private Context context;
	private AudioPlayer audioPlayer;
	private AnimationDrawable anim;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			//处理播放音频消息
			case AUDIO_PREPARED:
				isPlaying = true;
				playVoice.setBackgroundResource(R.drawable.playvoice_animation);
				anim = (AnimationDrawable) playVoice.getBackground();
				anim.start();
				break;
			case AUDIO_PLAYING:
				
				break;
			case AUDIO_FINISHED:
				isPlaying = false;
				if (anim != null) {
					anim.stop();
				}
				playVoice.setBackgroundResource(R.drawable.playingvoice);
				break;
			case AUDIO_FAILED:
				isPlaying = false;
				if (anim != null) {
					anim.stop();
				}
				playVoice.setBackgroundResource(R.drawable.playingvoice);
				break;
				
			case AUDIO_DOWNLOAD_FINISHED:
				String filepath = msg.getData().getString("filepath");
				leftAudiotime.setText(new SongInfoUtils(context).getFileInfo(filepath)[0]+"`");
				break;
				
			case IMG_FINISHED:
				leftContent.setText(ExpressionUtil.dealImg(getContext(), chatItemInfo.msg, null, msg.getData().getString("filepath")));
				break;
			}
		}
	};
	
	public MsgLeftView(Context context){
		super(context);
		this.context = context;
		initView();
		initListener();
	}
	public MsgLeftView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
		initListener();
	}
	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_msg_left, this);
		leftHead = (ImageView) findViewById(R.id.imageView_msg_left_head);
		leftContent = (TextView) findViewById(R.id.textView_msg_left_content);
		playVoice = (ImageView) findViewById(R.id.imageView_msg_left_play);
		leftAudiotime = (TextView) findViewById(R.id.textView_msg_left_audiotime);
	}
	
	@SuppressLint("NewApi")
	private void initData(){
		if(!chatItemInfo.state.equals("2")){
			new Thread(new Runnable() {
				@Override
				public void run() {
					ChatServer.getInstance().getChat().sendReadChatMsgReceipt(chatItemInfo._id);
				}
			}).start();
		}
		String imgUrl = chatItemInfo.userInfo.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+imgUrl,leftHead,R.drawable.head_orang,R.drawable.head_orang);
			}else{
				leftHead.setImageBitmap(bitmap);
			}
		}else{
			leftHead.setImageResource(R.drawable.head_orang);
		}
		String type = chatItemInfo.mediaType;//System.out.println("mediaType=="+type);
		if(type == null || type.equals("")){
			leftContent.setText(chatItemInfo.msg);
		}else if(MyUtil.isAudioFile(type)){
			if(chatItemInfo.mediaUrl.endsWith(".caf")){
				leftContent.setText("不支持的语音文件");
				return ;
			}
			leftContent.setVisibility(View.GONE);
			playVoice.setVisibility(View.VISIBLE);
			leftAudiotime.setVisibility(View.VISIBLE);
			String fileName = chatItemInfo.mediaUrl.substring(chatItemInfo.mediaUrl.lastIndexOf("/"));
			String audioPath = CacheUtil.chatAudioCachePath+fileName;
			if (CacheUtil.getInstence().iSCacheFileExists(audioPath)) {//System.out.println("audioPath="+audioPath);
				//leftAudiotime.setText(new SongInfoUtils(context).getFileInfo(audioPath)[0]+"`");
				mediaMetadataRetriever.setDataSource(audioPath);
				leftAudiotime.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
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
						System.out.println("语音下载成功");
					}
				});
			}
		}else if(MyUtil.isImgFile(type)){
			chatItemInfo.msg = "[图片]";//System.out.println("chatItemInfo.msg="+chatItemInfo.msg);
			String path = chatItemInfo.mediaUrl.substring(chatItemInfo.mediaUrl.lastIndexOf("/"));
			path = path.replace(":", "_");
//			SpannableString spannableString = new SpannableString(chatItemInfo.msg);
//			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.chatImageCachePath+path);
//			ImageSpan imageSpan = new ImageSpan(AndroidFileUtils.zoomIn(bitmap, 480, 480));
//			spannableString.setSpan(imageSpan, 0, 4,Spanned.SPAN_INCLUSIVE_EXCLUSIVE); // 将该图片替换字符串中规定的位置中
//			leftContent.setText(spannableString);
			leftContent.setText(ExpressionUtil.dealImg(getContext(), chatItemInfo.msg, null, CacheUtil.chatImageCachePath+path));
		}
	}
	public void setChatItemInfo(ChatItem chatItemInfo) {
		this.chatItemInfo = chatItemInfo;
		initData();
	}
	private void initListener(){
		leftContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tvContentOnClick();
				System.out.println("leftContent OnClickListener");
			}
		});
		playVoice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tvContentOnClick();
				System.out.println("playVoice OnClickListener");
			}
		});
		leftHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//System.out.println("leftHead OnClickListener");
				UISkip.skip(false, getContext(), OtherInfoActivity.class);
			}
		});
	}
	private void tvContentOnClick() {
		//String filePath = CacheUtil.cachePath+"/sg.jpg";
		
		if(MyUtil.isAudioFile(chatItemInfo.mediaType)){
			if(chatItemInfo.mediaUrl.endsWith(".caf")) return ;
			if (isPlaying) return ;
			String path = chatItemInfo.mediaUrl.substring(chatItemInfo.mediaUrl.lastIndexOf("/"));
			String audioPath = CacheUtil.chatAudioCachePath+path;//System.out.println("audioPath="+audioPath);
			if (audioPath == null) {
				//
			} else {
				if (CacheUtil.getInstence().iSCacheFileExists(audioPath)) {
					playAudio(audioPath);
				} else {
					//
					//System.out.println("语音文件不存在");
					Toast.makeText(context, "语音文件不存在", Toast.LENGTH_SHORT).show();
				}
			}
		}else if(MyUtil.isImgFile(chatItemInfo.mediaType)){
			String path = chatItemInfo.mediaUrl.substring(chatItemInfo.mediaUrl.lastIndexOf("/"));
			path = path.replace(":", "_");
			String imgPath = CacheUtil.chatImageCachePath+path;
			Dialog dialog = new Dialog(context,R.style.dialog_download_full_screen);
			PictureBrowseView pictureBrowseView = new PictureBrowseView(context,new com.guotion.common.PictureBrowser.DownloadListener() {
				@Override
				public void finished(String filepath) {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = IMG_FINISHED;
					msg.getData().putString("filepath", filepath);
					handler.sendMessage(msg);
					System.out.println("图片下载成功");
				}
			});
			String url = ChatServerConstant.URL.SERVER_HOST+chatItemInfo.mediaUrl;
			pictureBrowseView.setImageUrls(new String[]{url}, new String[]{imgPath}, null);
			dialog.setContentView(pictureBrowseView);
			dialog.show();
		}
	}
	private void showImage(String imagePath) {
		
	}
	
	private void playAudio(String filePath){System.out.println("play audio...");
	if(audioPlayer == null){
		audioPlayer = new AudioPlayer(null);
	}
	audioPlayer.play(filePath, new AudioPlayerListener() {
			@Override
			public void onPrepared(int duration) {System.out.println(duration);
				Message msg = new Message();
				msg.what = AUDIO_PREPARED;
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
				msg.what = AUDIO_FAILED;
				handler.sendMessage(msg);
			}
			@Override
			public void finished() {
				Message msg = new Message();
				msg.what = AUDIO_FINISHED;
				handler.sendMessage(msg);
			}
			@Override
			public void onDestroy(){
//				Message msg = new Message();
//				msg.what = AUDIO_FINISHED;
//				handler.sendMessage(msg);
			}
		});
	}
	private void downloadImg(){
		//if(chatMessagePartInfo.url == null) return ;
		SimpleDownLoader2 simpleDownLoader2 = new SimpleDownLoader2();
		//String url = chatMessagePartInfo.url;
		String url = "http://192.168.137.199:8080/UploadServer/files/sg.jpg";
		simpleDownLoader2.downLoad(url,CacheUtil.cachePath,System.currentTimeMillis()+"", new DownloadListener() {
			@Override
			public void onPrepared(int filesize) {
				Message msg = new Message();
				msg.what = IMG_PREPARED;
				msg.getData().putInt("filesize", filesize);
				handler.sendMessage(msg);
			}
			@Override
			public void onException(Exception e) {
				Message msg = new Message();
				msg.what = IMG_FAILED;
				msg.getData().putString("exception", e.getMessage());
				handler.sendMessage(msg);
				//System.out.println("图片下载失败");
			}
			@Override
			public void onDownloadSize(int size) {
				Message msg = new Message();
				msg.what = IMG_DOWNLOADING;
				msg.getData().putInt("size", size);
				handler.sendMessage(msg);
			}
			@Override
			public void finished(String filepath) {
				Message msg = new Message();
				msg.what = IMG_FINISHED;
				msg.getData().putString("filepath", filepath);
				handler.sendMessage(msg);
				
				System.out.println("图片下载成功");
			}
		});
	}
}
