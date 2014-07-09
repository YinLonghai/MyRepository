package com.guotion.sicilia.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ChatItemInfo;
import com.guotion.sicilia.bean.ConversationInfo;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.ChatServer;
import com.guotion.sicilia.im.util.ChatItemManager;
import com.guotion.sicilia.ui.adapter.ChatAdapter;
import com.guotion.sicilia.ui.listener.ReceiveGroupMessageListener;
import com.guotion.sicilia.ui.popupwindow.ChoosePhotoPopupwindow;
import com.guotion.sicilia.ui.view.PullDownView;
import com.guotion.sicilia.ui.view.PullDownView.OnPullDownListener;
import com.guotion.sicilia.util.AndroidFileUtils;
import com.guotion.sicilia.util.LogUtil;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class GroupChatActivity extends Activity{
	private RelativeLayout rootView;
	
	private TextView addTextView;
	private EditText editText;
	private TextView send;
	private PullDownView pullDownView;
	private ListView chatListView;
	private ImageView chatGroupinfo;
	private TextView back;
	private TextView chatGroupName;
	RelativeLayout top;
	ImageView ivBack;
	
	private LinkedList<ChatItem> chatList;
	private ChatAdapter chatAdapter;
	
	private ReceiveGroupMessageListenerImpl receiveGroupMessageListenerImpl;
	private ChoosePhotoPopupwindow choosePhotoPopupwindow = null;
	private ChatGroup chatGroup;
	
	private Gson gson = new Gson();
	
	private String idStr;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				chatAdapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(GroupChatActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_chat);
		initData();
		initView();
		initListener();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		AppData.imMessageToUIListener.cancleReceiveGroupMessageListeners(receiveGroupMessageListenerImpl);
		super.onDestroy();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.w("ChatActivity onActivityResult");
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case AndroidRequestCode.REQ_CODE_CAMERA:
				//TODO 图片路径 choosePhotoPopupwindow.getImagePath();		
				LogUtil.i("image path=" + choosePhotoPopupwindow.getImagePath());
				sendImg(choosePhotoPopupwindow.getImagePath());
				break;

			case AndroidRequestCode.REQ_CODE_PICTURES:
				//TODO choosePhotoPopupwindow
				String imgPath = AndroidFileUtils.uriToFilePath(GroupChatActivity.this, data.getData());
				LogUtil.i("image path=" + imgPath);
				sendImg(imgPath);
//				AndroidFileUtils.uriToFile(ChatActivity.this, data.getData());
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void initData() {
		chatGroup = AppData.tempChatGroup;
		if(chatGroup == null){
			finish();
		}
		List<String> admins = gson.fromJson(chatGroup.admins+"",new TypeToken<List<String>>(){}.getType());
		List<String> members = gson.fromJson(chatGroup.members+"",new TypeToken<List<String>>(){}.getType());
		StringBuilder sb = new StringBuilder();
		for(String id : admins){
			sb.append(id+"|");
		}
		for(String id : members){
			sb.append(id+"|");
		}
		sb.append(chatGroup.creator+"");
		idStr = sb.toString();
		chatList = new LinkedList<ChatItem>();
		List<ChatItem> list = AppData.chatMap.get(chatGroup._id);//System.out.println("list size="+list.size());
		if(list != null){
			chatList.addAll(list);
		}
		if(chatList.size()>0){
			AppData.lastReadMap.put(chatGroup._id, chatList.getLast()._id);
		}
		chatAdapter = new ChatAdapter(GroupChatActivity.this, chatList, null);
		receiveGroupMessageListenerImpl = new ReceiveGroupMessageListenerImpl();
		AppData.imMessageToUIListener.registReceiveGroupMessageListeners(receiveGroupMessageListenerImpl);
	}
	
	private void initView() {
		rootView = (RelativeLayout) findViewById(R.id.activity_group_chat_root);
		addTextView = (TextView)findViewById(R.id.textView_chat_image);
		editText = (EditText) findViewById(R.id.editText_chat);
		send = (TextView) findViewById(R.id.textView_chat_send);
		pullDownView = (PullDownView) findViewById(R.id.listView_chat_msg);
		chatListView = pullDownView.getListView();
		pullDownView.setTvFreshIsVisible(false);
		//显示并且可以使用头部刷新
		pullDownView.setShowHeader();
		chatListView.setAdapter(chatAdapter);
		chatListView.setSelection(chatAdapter.getCount());
		chatGroupinfo = (ImageView) findViewById(R.id.textView_chat_groupinfo);
		back = (TextView) findViewById(R.id.textView_chat_back);
		chatGroupName = (TextView) findViewById(R.id.textView_chat_title);
		chatGroupName.setText(chatGroup.GroupName);
		top =  (RelativeLayout) findViewById(R.id.RelativeLayout_chat_title);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(GroupChatActivity.this).getInt(AppData.THEME);
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
			chatGroupinfo.setImageResource(AppData.getThemeImgResId(theme, "view2"));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		chatGroupinfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skip(false, GroupChatActivity.this, ActivityGroupInfo.class);
			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		addTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (choosePhotoPopupwindow == null) {
					choosePhotoPopupwindow = new ChoosePhotoPopupwindow(GroupChatActivity.this);
				}				
				choosePhotoPopupwindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
			}
		});
		
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
								List<ChatItem> list = chatItemManager.getMsgBeforeOneMsg(chatGroup.chatHistory+"", chatList.getLast()._id);
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
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				sendText();
				
				return false;
			}
		});
	}
	
	private void sendText(){
		final ChatItem chatItem = new ChatItem();
		chatItem.msg = editText.getText().toString();
		chatItem.userInfo = AppData.getUser(GroupChatActivity.this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ChatServer.getInstance().getChat().sendGroupMessage(chatGroup._id, AppData.getUser(GroupChatActivity.this).userName, idStr, AppData.getUser(GroupChatActivity.this)._id, chatItem.msg);
					chatList.add(chatItem);
					handleChatItem(chatGroup,chatItem);
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				String mediaType = path.substring(path.lastIndexOf(".")+1);
				ChatItem chatItem;
				try {
					User u = AppData.getUser(GroupChatActivity.this);
					chatItem = chatItemManager.sendGroupMediaFile(u._id, chatGroup._id, mediaType, new File(path),  "");
					List<String> members = gson.fromJson(chatGroup.members+"", new TypeToken<List<String>>(){}.getType());
					StringBuilder receiversId = new StringBuilder();
					for(String id : members){
						receiversId.append(id+"|");
					}
					receiversId.deleteCharAt(receiversId.length()-1);
					ChatServer.getInstance().getChat().sendGroupPostChat(chatItem._id, chatGroup._id, AppData.getUser(GroupChatActivity.this).userName, receiversId.toString(), u._id);
					chatItem.userInfo = u;
					chatItem.msg = "[图片]";
					chatList.add(chatItem);
					handleChatItem(chatGroup,chatItem);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("图片发送失败。。。");
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	private final class ReceiveGroupMessageListenerImpl implements ReceiveGroupMessageListener{
		@Override
		public void receiveGroupMessage(ChatItem chatItem) {
			chatItem.userInfo = gson.fromJson(chatItem.user+"", User.class);
			chatList.add(chatItem);
			//handleChatItem(chatGroup,chatItem);
			handler.sendEmptyMessage(1);
		}
	}
	private void handleChatItem(ChatGroup chatGroup,ChatItem chatItem){
		ConversationInfo conversationInfo = AppData.getConversation(chatGroup._id);
		if(conversationInfo == null){
			conversationInfo = new ConversationInfo();
			conversationInfo.groupId = chatGroup._id;
			conversationInfo.friendName = chatGroup.GroupName;
			conversationInfo.GroupPhoto = chatGroup.GroupPhoto;
			//conversationInfo.unread_num += 1;
			AppData.conversationList.add(0,conversationInfo);
		}
		conversationInfo.content = chatItem.msg;
		conversationInfo.contentType = chatItem.mediaType;
		List<ChatItem> list = AppData.chatMap.get(chatGroup._id);
		if(list == null){
			list = new LinkedList<ChatItem>();
			AppData.chatMap.put(chatGroup._id,list);
		}
		list.add(chatItem);
	}
}
