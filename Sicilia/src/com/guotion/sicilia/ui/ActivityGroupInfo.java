package com.guotion.sicilia.ui;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.application.SiciliaApplication;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.ChatServer;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.ChatGroupManager;
import com.guotion.sicilia.ui.dialog.GroupInfoDialog;
import com.guotion.sicilia.ui.dialog.InviteMembersDialog;
import com.guotion.sicilia.ui.dialog.WarningDialog;
import com.guotion.sicilia.ui.listener.OnUpdateListener;
import com.guotion.sicilia.ui.dialog.InviteMembersDialog.InviteMembersListener;
import com.guotion.sicilia.util.LogUtil;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @function 群详细信息
 *
 * @author   Longhai.Yin  MailTo: 1195219040 @ qq.com
 *
 * @version  NO.01
 *
 * @create   2014-4-13 下午3:02:42
 *
 */
public class ActivityGroupInfo extends Activity {
	
	private LinearLayout llBack;
	/**
	 * 返回界面的名字
	 */
	private TextView returnGroup;
	/**
	 * 群头像
	 */
	private ImageView groupHead;
	/**
	 * 群名称
	 */
	private TextView groupName;
	/**
	 * 组成员
	 */
	private ImageButton btnGroup;
	/**
	 * 邀请新成员
	 */
	private ImageButton btnMember;
	
	private RelativeLayout top;
	private TextView line;
	private ImageView ivBack;
	
	boolean isCreator = false;
	Button button;//退群,解散群
	
	private ChatGroup chatGroup;
	
	private List<String> membersList;//群成员
	
	private GroupInfoDialog groupInfoDialog = null;
	
	private SiciliaApplication siciliaApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_info);
		siciliaApplication = (SiciliaApplication) getApplication();
		siciliaApplication.addActivity(ActivityGroupInfo.this);
		initView();
		initData();
		initListener();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		siciliaApplication.removeActivity(ActivityGroupInfo.this);
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initData();
		super.onResume();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.i("MainActivity onActivityResult!");
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case AndroidRequestCode.REQ_CODE_UPLOAD_GROUP_PICTURE:
				groupInfoDialog.notifyImageCreated();
				break;
			case AndroidRequestCode.REQ_CODE_UPLOAD_GROUP_CAMERA:
				groupInfoDialog.notifyImageCreated();
				break;
			
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void initData() {
		setChatGroup(AppData.tempChatGroup);		
	}

	private void initView() {
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		returnGroup = (TextView) findViewById(R.id.txt_return_group);
		
		groupHead = (ImageView) findViewById(R.id.group_head);
		groupName = (TextView) findViewById(R.id.group_name);
		
		btnGroup = (ImageButton) findViewById(R.id.img_group);
		btnMember = (ImageButton) findViewById(R.id.img_member);
		line = (TextView) findViewById(R.id.line);
		top =  (RelativeLayout) findViewById(R.id.relout_groupinfo_title);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		button = (Button) findViewById(R.id.Button_1);
		updateTheme();
	}
	private void updateTheme(){
		if(AppData.tempChatGroup!=null && AppData.tempChatGroup.creator.equals(AppData.getUser(ActivityGroupInfo.this)._id)){//判断是不是创建者
			isCreator = true;
		}else{
			isCreator = false;
		}
		int theme = new PreferencesHelper(ActivityGroupInfo.this).getInt(AppData.THEME);
		int color ;
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				returnGroup.setTextColor(getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				returnGroup.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				returnGroup.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				returnGroup.setTextColor(getResources().getColor(R.color.white));
				break;
			}
			btnGroup.setBackgroundResource(AppData.getThemeImgResId(theme, "member_2"));
			btnMember.setBackgroundResource(AppData.getThemeImgResId(theme, "member_1"));
			line.setBackgroundResource(AppData.getThemeColor(theme));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
			//System.out.println(AppData.tempChatGroup.creator+"  "+AppData.getUser(ActivityGroupInfo.this)._id+"  "+AppData.tempChatGroup.creator.equals(AppData.getUser(ActivityGroupInfo.this)._id));
			if(isCreator){//判断是不是创建者
				button.setBackgroundResource(AppData.getThemeImgResId(theme, "dismiss"));
			}else{
				button.setBackgroundResource(AppData.getThemeImgResId(theme, "quit"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String warning = "";
				String cancel = "取消";
				String sure = "确定";
				if(isCreator){
					warning = "确认要解散该聊天组吗？";
					new WarningDialog(ActivityGroupInfo.this, warning, cancel, sure){
						@Override
						public void clickCancel() {
						}
						@Override
						public void clickSure() {
							dismissChatGroup();
						}
					}.show();
				}else{
					warning = "确认要退出该聊天组吗？";
					new WarningDialog(ActivityGroupInfo.this, warning, cancel, sure){
						@Override
						public void clickCancel() {
						}
						@Override
						public void clickSure() {
							quit();
						}
					}.show();
				}
			}
		});
		llBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		groupHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				UISkip.skip(false, ActivityGroupInfo.this, ActivityGroupInfoEdit.class);
				if (groupInfoDialog == null) {
					groupInfoDialog = new GroupInfoDialog(ActivityGroupInfo.this, chatGroup);
				}
				groupInfoDialog.setOnUpdateListener(new OnUpdateListener<ChatGroup>(){
					@Override
					public void onUpdate(ChatGroup t) {
						setChatGroup(t);
					}
				});				
				groupInfoDialog.show();
			}
		});
		btnGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//AppData.tempChatGroup = 
				UISkip.skip(false, ActivityGroupInfo.this, GroupMemberManageActivity.class);
			}
		});
		btnMember.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppData.tempGroupMembers = membersList;
				InviteMembersDialog inviteMembersDialog = new InviteMembersDialog(ActivityGroupInfo.this);
				inviteMembersDialog.setInviteMembersListener(new InviteMembersListener() {
					@Override
					public void getMembers(List<User> list) {
						if(list.size() == 0) return ;
						final StringBuilder sb = new StringBuilder();
						for(int i=0;i<list.size();i++){
							sb.append(list.get(i)._id+"|");
						}
						sb.deleteCharAt(sb.length()-1);
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									AppData.tempChatGroup = new ChatGroupManager().addMembers(chatGroup._id, sb.toString());
									for(int i=0;i<AppData.chatGroupList.size();i++){
										if(chatGroup._id.equals(AppData.chatGroupList.get(i)._id)){
											AppData.chatGroupList.set(i, AppData.tempChatGroup);System.out.println("替换chatGroup");
											break;
										}
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}).start();
						
					}
				});
				inviteMembersDialog.show();
			}
		});
//		List<String> members = new Gson().fromJson(chatGroup.members+"",new TypeToken<List<String>>(){}.getType());
		String userId = AppData.getUser(ActivityGroupInfo.this)._id;//System.out.println("userId="+userId);System.out.println("aaaaaa--"+chatGroup.members);
		for(Object memberId : chatGroup.members){//System.out.println("memberId="+memberId);
			if(userId.equals(memberId)){//System.out.println("aaaaaa");
				btnMember.setOnClickListener(null);
				int theme = new PreferencesHelper(ActivityGroupInfo.this).getInt(AppData.THEME);
				try {
					btnMember.setBackgroundResource(AppData.getThemeImgResId(theme, "member_1_not_normal"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	}

	private void setChatGroup(ChatGroup chatGroup){
		this.chatGroup = chatGroup;//System.out.println(chatGroup.members);
		if(chatGroup == null){
			finish();
			//chatGroup = new ChatGroup();
			//getSingleChatGroup();
		}
		String imgUrl = chatGroup.GroupPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+chatGroup.GroupPhoto,groupHead,R.drawable.head_team,R.drawable.head_team);
			}else{
				groupHead.setImageBitmap(bitmap);
			}
		}else{
			groupHead.setImageResource(R.drawable.head_team);
		}
		returnGroup.setText(chatGroup.GroupName);
		groupName.setText(chatGroup.GroupName);
		membersList = getGroupMembers(chatGroup);
	}
	
	private List<String> getGroupMembers(ChatGroup chatGroup){
		Gson gson = new Gson();
		List<String> membersList = new LinkedList<String>();
		List<String> memberlist = gson.fromJson(chatGroup.members+"",new TypeToken<List<String>>(){}.getType());
		membersList.addAll(memberlist);
		List<String> adminlist = gson.fromJson(chatGroup.admins+"",new TypeToken<List<String>>(){}.getType());
		membersList.addAll(adminlist);
		membersList.add(chatGroup.creator+"");
		return membersList;
	}
	private void quit(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new ChatGroupManager().quitChatGroup(chatGroup._id ,AppData.getUser(ActivityGroupInfo.this)._id);
					ChatServer.getInstance().getChat().unSubscribeMsg(chatGroup._id);
					AppData.isQuitOrDismiss = true;
					deleteChatGroup();
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					handler.sendEmptyMessage(2);
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void dismissChatGroup(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if(new ChatGroupManager().deleteGroup(chatGroup._id)){
						AppData.isQuitOrDismiss = true;
						deleteChatGroup();
						handler.sendEmptyMessage(1);
					}else{
						handler.sendEmptyMessage(2);
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(2);
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void deleteChatGroup(){
		for(int i=0;i<AppData.chatGroupList.size();i++){
			if(chatGroup._id.equals(AppData.chatGroupList.get(i)._id)){
				AppData.chatGroupList.remove(i);System.out.println("删除chatGroup");
				break;
			}
		}
		//移除Conversation
		for(int i=0;i<AppData.conversationList.size();i++){
			if(chatGroup._id.equals(AppData.conversationList.get(i).groupId)){
				AppData.conversationList.remove(i);System.out.println("删除Conversation");
				break;
			}
		}
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				finish();
				break;
			case 2:
				Toast.makeText(ActivityGroupInfo.this, "网络异常", Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}
	};
	private void getSingleChatGroup(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					chatGroup = new ChatGroupManager().getSingleChatGroup(chatGroup._id);
					//AppData.tempChatGroup = chatGroup;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
