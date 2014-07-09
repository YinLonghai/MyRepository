package com.guotion.sicilia.ui;

import java.util.List;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.ChatGroupManager;
import com.guotion.sicilia.ui.dialog.GroupInfoDialog;
import com.guotion.sicilia.ui.dialog.InviteMembersDialog;
import com.guotion.sicilia.ui.listener.OnUpdateListener;
import com.guotion.sicilia.ui.dialog.InviteMembersDialog.InviteMembersListener;
import com.guotion.sicilia.util.LogUtil;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
	
	private ChatGroup chatGroup;
	
	private GroupInfoDialog groupInfoDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_info);
		initView();
		initData();
		initListener();
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
		updateTheme();
	}
	private void updateTheme(){
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
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
				// TODO Auto-generated method stub
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
									new ChatGroupManager().addMembers(chatGroup._id, sb.toString());
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
	}

	private void setChatGroup(ChatGroup chatGroup){
		this.chatGroup = chatGroup;
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
	}
	
	
	
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
