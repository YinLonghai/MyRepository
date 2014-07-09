package com.guotion.sicilia.ui;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.guotion.common.media.AudioRecorder;
import com.guotion.common.utils.CacheUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ConversationInfo;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.ChatHistory;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.ChatServer;
import com.guotion.sicilia.im.util.ChatGroupManager;
import com.guotion.sicilia.im.util.ChatItemManager;
import com.guotion.sicilia.ui.adapter.ChatAdapter;
import com.guotion.sicilia.ui.listener.MessageFeedbackListener;
import com.guotion.sicilia.ui.listener.ReceiveP2PMessageListener;
import com.guotion.sicilia.ui.popupwindow.ChoosePhotoPopupwindow;
import com.guotion.sicilia.ui.view.PullDownView;
import com.guotion.sicilia.ui.view.PullDownView.OnPullDownListener;
import com.guotion.sicilia.util.AndroidFileUtils;
import com.guotion.sicilia.util.DisplayUtil;
import com.guotion.sicilia.util.LogUtil;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;
import com.uraroji.garage.android.mp3recvoice.RecMicToMp3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ChatActivity extends Activity {
	private RelativeLayout rootView;
	
	private EditText editText;
	private TextView tvSwitch;
	private PullDownView pullDownView;
	private ListView chatListView;
	private ImageView chatFriendinfo;
	private TextView back;
	private RelativeLayout top;
	private TextView addTextView;
	private TextView toUserName;
	ImageView ivBack;
	
	private LinkedList<ChatItem> chatList;
	private ChatAdapter chatAdapter;
	private User user;
	private ChatGroup chatGroup;
	
	private int theme;
	private Gson gson = new Gson();	
	private ReceiveP2PMessageListenerImpl receiveP2PMessageListenerImpl;
	private MessageFeedbackListenerImpl messageFeedbackListenerImpl;
	
	private ChoosePhotoPopupwindow choosePhotoPopupwindow = null;
	
	//下面是与录音相关的控件或工具类
	private Button btnRecord;
	
	private RelativeLayout rlRecordState;
	
	private ImageView ivRecordVolume;
	
	private AudioRecorder audioRecorder = null;
	private RecMicToMp3 recMicToMp3;
	
	/**
	 * 录制状态
	 */
	private boolean isRecord = false;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				chatAdapter.notifyDataSetChanged();
				chatListView.setSelection(chatList.size()-1);
				break;
			case 2:
				Toast.makeText(ChatActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(ChatActivity.this, "语音发送失败", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(ChatActivity.this, "图片发送失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initData();
		initView();
		initListener();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		AppData.imMessageToUIListener.cancleReceiveP2PMessageListeners(receiveP2PMessageListenerImpl);
		AppData.imMessageToUIListener.cancleMessageFeedbackListener(messageFeedbackListenerImpl);
		super.onDestroy();
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.w("ChatActivity onActivityResult");
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case AndroidRequestCode.REQ_CODE_CAMERA:
				//TODO 图片路径 choosePhotoPopupwindow.getImagePath();	
				sendImg(choosePhotoPopupwindow.getImagePath());
				break;

			case AndroidRequestCode.REQ_CODE_PICTURES:
				//TODO choosePhotoPopupwindow
				String imgPath = AndroidFileUtils.uriToFilePath(ChatActivity.this, data.getData());
				LogUtil.i("image path=" + imgPath);
				sendImg(imgPath);
//				AndroidFileUtils.uriToFile(ChatActivity.this, data.getData());
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initData() {
		user = AppData.tempuser;
		if(user == null){
			finish();
		}
		chatList = new LinkedList<ChatItem>();
		List<ChatItem> list = AppData.chatMap.get(user._id);
		if(list != null){
			chatList.addAll(list);
		}
		getP2PGroup();
		chatAdapter = new ChatAdapter(ChatActivity.this, chatList, null);
		receiveP2PMessageListenerImpl = new ReceiveP2PMessageListenerImpl();
		AppData.imMessageToUIListener.registReceiveP2PMessageListeners(receiveP2PMessageListenerImpl);
		messageFeedbackListenerImpl = new MessageFeedbackListenerImpl();
		AppData.imMessageToUIListener.registMessageFeedbackListener(messageFeedbackListenerImpl);
		theme = new PreferencesHelper(ChatActivity.this).getInt(AppData.THEME);
	}
	
	private void initView() {
		rootView = (RelativeLayout)findViewById(R.id.activity_chat_root);
		editText = (EditText) findViewById(R.id.editText_chat);
		tvSwitch = (TextView) findViewById(R.id.textView_chat_switch);
		pullDownView = (PullDownView) findViewById(R.id.listView_chat_msg);
		chatListView = pullDownView.getListView();
		pullDownView.setTvFreshIsVisible(false);
		//显示并且可以使用头部刷新
		pullDownView.setShowHeader();
		chatListView.setAdapter(chatAdapter);
		chatListView.setSelection(chatAdapter.getCount());
		chatFriendinfo = (ImageView) findViewById(R.id.textView_chat_friendinfo);
		back = (TextView) findViewById(R.id.textView_chat_back);
		top = (RelativeLayout) findViewById(R.id.RelativeLayout_chat_title);
		addTextView = (TextView)findViewById(R.id.textView_chat_image);
		toUserName = (TextView)findViewById(R.id.textView_chat_title);
		toUserName.setText(user.userName);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		
		btnRecord = (Button)findViewById(R.id.button_hold_record);
		rlRecordState = (RelativeLayout)findViewById(R.id.RelativeLayout_record_state);
		ivRecordVolume = (ImageView)findViewById(R.id.imageView_record_volume);
		updateTheme();
	}
	private void updateTheme(){
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				back.setTextColor(getResources().getColor(AppData.getThemeColor(theme)));
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				back.setTextColor(getResources().getColor(AppData.getThemeColor(theme)));
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				break;
			}
			chatFriendinfo.setImageResource(AppData.getThemeImgResId(theme, "view1"));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		chatFriendinfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppData.OTHER_USER = user;
				UISkip.skip(false, ChatActivity.this, OtherInfoActivity.class);
			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		/**
		 * 发送图片
		 */
		addTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (choosePhotoPopupwindow == null) {
					choosePhotoPopupwindow = new ChoosePhotoPopupwindow(ChatActivity.this);
				}
				choosePhotoPopupwindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
			}
		});
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				sendText();
				//editText.setText("");
				return true;
			}
		});
		
		tvSwitch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isRecord) {
					isRecord = false;
					tvSwitch.setBackgroundResource(R.drawable.btn_chat_keyboard_selector);
					btnRecord.setVisibility(View.GONE);
					//TODO
				}else {
					isRecord = true;
					tvSwitch.setBackgroundResource(R.drawable.btn_chat_microphone_selector);
					btnRecord.setVisibility(View.VISIBLE);
					//TODO
				}
			}
		});
		btnRecord.setOnTouchListener(new OnRecordTouchListener());
		pullDownView.setOnPullDownListener(new OnPullDownListener() {
			
			@Override
			public void onRefresh() {
				new Thread(new Runnable() {

					@Override
					public void run() {
//						try {
//							Thread.sleep(2000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						if(chatGroup != null && chatList.size() > 0){
							try {
								ChatHistory chatHistory = gson.fromJson(chatGroup.chatHistory+"", ChatHistory.class);
								List<ChatItem> list = chatItemManager.getMsgBeforeOneMsg(chatHistory._id, chatList.getLast()._id);
								chatList.addAll(0, list);
								handler.sendEmptyMessage(1);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								handler.sendEmptyMessage(2);
								e.printStackTrace();
							}
						}
						
						/** 关闭 刷新完毕 ***/
						pullDownView.RefreshComplete();//这个事线程安全的 可看源代码
					}
				}).start();
			}
		});
	}
	
	private void sendText(){
		final ChatItem chatItem = new ChatItem();
		chatItem.msg = editText.getText().toString();
		chatItem.userInfo = AppData.getUser(ChatActivity.this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {//System.out.println(AppData.USER._id+"---"+user._id);
					User u = AppData.getUser(ChatActivity.this);
					ChatServer.getInstance().getChat().sendP2PMessage(u._id, u.userName, user._id, chatItem.msg);
					chatList.add(chatItem);
					handleChatItem(user,chatItem);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		editText.setText("");
		//chatAdapter.notifyDataSetChanged();
	}
	ChatItemManager chatItemManager = new ChatItemManager();
	private void sendImg(final String path){
		Toast.makeText(ChatActivity.this, "正在发送图片，请稍等", Toast.LENGTH_LONG).show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String mediaType = path.substring(path.lastIndexOf(".")+1);
				ChatItem chatItem;
				try {
					User u = AppData.getUser(ChatActivity.this);
					chatItem = chatItemManager.sendP2PMediaFile(u._id, user._id, mediaType, new File(path),  "");
					ChatServer.getInstance().getChat().sendP2PPostChat(chatItem._id, u._id, u.userName, user._id);
					chatItem.userInfo = AppData.getUser(ChatActivity.this);
					chatItem.msg = "[图片]";
					String filenmae = chatItem.mediaUrl.substring(chatItem.mediaUrl.lastIndexOf("/"));
					filenmae = filenmae.replace(":", "_");
					AndroidFileUtils.saveToSDCard(path, CacheUtil.chatImageCachePath+filenmae, 500, 800);
					chatList.add(chatItem);
					handleChatItem(user,chatItem);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					handler.sendEmptyMessage(4);
					System.out.println("图片发送失败。。。");
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	private void sendAudio(final String path){
		Toast.makeText(ChatActivity.this, "正在发送语音，请稍等", Toast.LENGTH_LONG).show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String mediaType = path.substring(path.lastIndexOf(".")+1);
				ChatItem chatItem;
				try {
					User u = AppData.getUser(ChatActivity.this);
					chatItem = chatItemManager.sendP2PMediaFile(u._id, user._id, mediaType, new File(path),  "");
					ChatServer.getInstance().getChat().sendP2PPostChat(chatItem._id, u._id, u.userName, user._id);
					chatItem.userInfo = AppData.getUser(ChatActivity.this);
					chatItem.msg = "[语音]";
					chatList.add(chatItem);
					handleChatItem(user,chatItem);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("语音发送失败。。。");
					handler.sendEmptyMessage(3);
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	private final class ReceiveP2PMessageListenerImpl implements ReceiveP2PMessageListener{
		@Override
		public void receiveP2PMessage(ChatItem chatItem) {//System.out.println("ChatActivity..."+chatItem.mediaType);
			User u;
			if(chatItem.mediaType == null || chatItem.mediaType.equals("")){
				u = gson.fromJson(chatItem.user+"", User.class);
			}else{
				u = AppData.getUser(chatItem.user+"");
			}
			if(u._id.equals(user._id)){
				chatItem.userInfo = u;
				chatList.add(chatItem);
			}
			//handleChatItem(u,chatItem);
			
			handler.sendEmptyMessage(1);
		}
	}
	private void handleChatItem(User toUser,ChatItem chatItem){
		ConversationInfo conversationInfo = AppData.getConversation(toUser._id);
		if(conversationInfo == null){
			conversationInfo = new ConversationInfo();
			conversationInfo.accountId = toUser._id;
			conversationInfo.friendName = toUser.userName;
			conversationInfo.GroupPhoto = toUser.headPhoto;
			//conversationInfo.unread_num += 1;
			AppData.conversationList.add(0,conversationInfo);
		}
		conversationInfo.content = chatItem.msg;
		conversationInfo.contentType = chatItem.mediaType;
		List<ChatItem> list = AppData.chatMap.get(toUser.__v);
		if(list == null){
			list = new LinkedList<ChatItem>();
			AppData.chatMap.put(toUser._id, list);
		}
		list.add(chatItem);
	}
	
	private final class MessageFeedbackListenerImpl implements MessageFeedbackListener{
		@Override
		public void messageSendSuccess(ChatItem chatItem) {
			AppData.lastReadMap.put(chatGroup._id, chatItem._id);
			updateChatItem(chatItem);
			handler.sendEmptyMessage(1);
		}
		@Override
		public void messageReaded(ChatItem chatItem) {
			updateChatItem(chatItem);
			handler.sendEmptyMessage(1);
		}
	}
	private void updateChatItem(ChatItem chatItem){
		for(int i=0;i<chatList.size();i++){
			if(chatItem.msg.equals(chatList.get(i).msg) && chatItem.date.equals(chatList.get(i).date)){
				chatList.set(i, chatItem);
			}
		}
	}
	
	private void getP2PGroup(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					chatGroup = new ChatGroupManager().getP2PGroup(AppData.getUser(ChatActivity.this)._id, user._id);
					if(chatList.size()>0){
						AppData.lastReadMap.put(chatGroup._id, chatList.getFirst()._id);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private final class OnRecordTouchListener implements OnTouchListener {
		private float y;
		private boolean isCancel = false;
		private long time = 0;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//TODO 录音
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:				
					try {
						rlRecordState.setVisibility(View.VISIBLE);
						if (recMicToMp3 == null) {
							recMicToMp3 = new RecMicToMp3(null,8000);
						}
						String path = CacheUtil.chatAudioCachePath + "/" + System.currentTimeMillis()+".mp3";
						recMicToMp3.setmFilePath(path);
						recMicToMp3.start();
						y = event.getY();
						isCancel = false;
						time = System.currentTimeMillis();						
					
					} catch (Exception e) {
						recMicToMp3.stop();
						isCancel = true;
						rlRecordState.setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(), "录音开始失败", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					
					break;

				case MotionEvent.ACTION_UP:
					//TODO 发送语音
					rlRecordState.setVisibility(View.GONE);
					time = System.currentTimeMillis() - time;
					btnRecord.setBackgroundResource(R.drawable.hold_off_green);
					if (isCancel) {
						try {
							if(recMicToMp3 != null)
								recMicToMp3.stop();;
						} catch (Exception e) {
							e.printStackTrace();
						}
						return true;
					}
					if (time < 1500) {
						Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_LONG).show();
						try {
							if(recMicToMp3 != null)
								recMicToMp3.stop();
						} catch (Exception e) {
							e.printStackTrace();
						}
						return true;
					}
					try {	
						if(recMicToMp3 != null){
							//TODO 发送
							recMicToMp3.stop();
							String path = recMicToMp3.getmFilePath();//
							//addAudioInfoToDB("",CacheUtil.chatAudioCachePath+"/2014-05-22_12:31:58.mp3",time+"");
							sendAudio(path);
						}
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "录音停止失败", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
						
					
					break;
				case MotionEvent.ACTION_MOVE:
					if ((y - event.getY()) > DisplayUtil.dip2px(100)) {
						isCancel = true;
						btnRecord.setBackgroundResource(R.drawable.hold_off_green);
						//TODO 图片变
					}else {
						isCancel = false;
						btnRecord.setBackgroundResource(R.drawable.hold_on_green);
					}
					break;
				}
			return true;
		}		

	}
	
	private void addAudioInfoToDB(String fileName,String filePath,String duration){
		ContentValues values = new ContentValues();
		//values.put(MediaStore.Audio.Media.DISPLAY_NAME,fileName);
		values.put(MediaStore.Audio.Media.DATA,filePath);
		values.put(MediaStore.Audio.Media.DURATION,duration);
		ChatActivity.this.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
	}
}
