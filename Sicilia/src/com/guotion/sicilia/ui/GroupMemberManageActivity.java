package com.guotion.sicilia.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.UserGroupInfo;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.ChatGroupManager;
import com.guotion.sicilia.ui.adapter.GroupMemberManageExAdapter;
import com.guotion.sicilia.ui.adapter.GroupMemberExAdapter;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.bean.net.User;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupMemberManageActivity extends Activity{
	private TextView back;
	private TextView manage;
	private TextView cancle;
	private TextView ok;
	RelativeLayout top;
	ImageView ivBack;
	
	private ExpandableListView memberExpandableListView;
	private GroupMemberExAdapter groupMemberExAdapter;
	private GroupMemberManageExAdapter groupMemberManageExAdapter;
	
	private ArrayList<UserGroupInfo> list;
	private String toNormal;
	private String toDelete;
	private String toAdmin;
	
	private ChatGroup chatGroup ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_member_manage);
		initData();
		initView();
		initListener();
	}
	private void initData() {
		chatGroup = AppData.tempChatGroup;
		list = new ArrayList<UserGroupInfo>();
		UserGroupInfo userGroupInfo;
		userGroupInfo = new UserGroupInfo();
		userGroupInfo.name = "创建者";
		userGroupInfo.list = new ArrayList<User>();
		list.add(userGroupInfo);
		userGroupInfo = new UserGroupInfo();
		userGroupInfo.name = "管理员";
		userGroupInfo.list = new ArrayList<User>();	
		
		list.add(userGroupInfo);
		userGroupInfo = new UserGroupInfo();
		userGroupInfo.name = "普通成员";
		userGroupInfo.list = new ArrayList<User>();
		
		list.add(userGroupInfo);
		groupMemberExAdapter = new GroupMemberExAdapter(list,GroupMemberManageActivity.this );
		groupMemberManageExAdapter = new GroupMemberManageExAdapter(list, GroupMemberManageActivity.this);
		if(chatGroup == null){
			//getSingleChatGroup();
		}
	}
	private void initView() {
		back = (TextView) findViewById(R.id.textView_back);
		cancle = (TextView) findViewById(R.id.textView_cancle);
		ok = (TextView) findViewById(R.id.textView_ok);
		manage = (TextView) findViewById(R.id.textView_manage);
		memberExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView_member);
		memberExpandableListView.setAdapter(groupMemberExAdapter);
		//将所有项设置成默认展开
		int groupCount = groupMemberExAdapter.getGroupCount();
		for (int i=0; i<groupCount; i++) {
			memberExpandableListView.expandGroup(i);
		}
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		ivBack = (ImageView) findViewById(R.id.imageView1);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(GroupMemberManageActivity.this).getInt(AppData.THEME);
		int color ;
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				manage.setTextColor(getResources().getColor(R.color.white));
				cancle.setTextColor(getResources().getColor(R.color.white));
				ok.setTextColor(getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				manage.setTextColor(color);
				cancle.setTextColor(color);
				ok.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				manage.setTextColor(color);
				cancle.setTextColor(color);
				ok.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				manage.setTextColor(getResources().getColor(R.color.white));
				cancle.setTextColor(getResources().getColor(R.color.white));
				ok.setTextColor(getResources().getColor(R.color.white));
				break;
			}
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		manage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				manage.setVisibility(View.GONE);
				cancle.setVisibility(View.VISIBLE);
				ok.setVisibility(View.VISIBLE);
				memberExpandableListView.setAdapter(groupMemberManageExAdapter);
				
				//将所有项设置成默认展开
				int groupCount = groupMemberExAdapter.getGroupCount();
				for (int i=0; i<groupCount; i++) {
					memberExpandableListView.expandGroup(i);
				}
			}
		});
		cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				manage.setVisibility(View.VISIBLE);
				cancle.setVisibility(View.GONE);
				ok.setVisibility(View.GONE);
				memberExpandableListView.setAdapter(groupMemberExAdapter);
				
				//将所有项设置成默认展开
				int groupCount = groupMemberExAdapter.getGroupCount();
				for (int i=0; i<groupCount; i++) {
					memberExpandableListView.expandGroup(i);
				}
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					ChatGroup tempchatGroup = new ChatGroupManager().memberUpdate(chatGroup._id, toNormal, toDelete, toAdmin);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		memberExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				User user;
				if(groupPosition == 1){
					user = list.get(1).list.get(childPosition);
					list.get(2).list.add(user);
					list.get(1).list.remove(childPosition);
					groupMemberManageExAdapter.notifyDataSetChanged();
				}
				else if(groupPosition == 2){
					user = list.get(2).list.get(childPosition);
					list.get(1).list.add(user);
					list.get(2).list.remove(childPosition);
					groupMemberManageExAdapter.notifyDataSetChanged();
				}
				return true;
			}
		});
		
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				groupMemberExAdapter.notifyDataSetChanged();
				groupMemberManageExAdapter.notifyDataSetChanged();
				break;
			}
		}
	};
	private void getSingleChatGroup(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					chatGroup = new ChatGroupManager().getSingleChatGroup(chatGroup._id);
					handleChatGroup(chatGroup);
					handler.sendEmptyMessage(1);
					//AppData.tempChatGroup = chatGroup;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	private void handleChatGroup(ChatGroup chatGroup){
		Gson gson = new Gson();
		User creator = gson.fromJson(chatGroup.creator+"", User.class);//System.out.println("creator="+creator);
		if(creator != null){
			list.get(0).list.add(creator);
		}
		List<User> adminlist = gson.fromJson(chatGroup.admins+"",new TypeToken<List<User>>(){}.getType());//System.out.println("adminlist="+adminlist);
		if(adminlist != null){
			list.get(1).list.addAll(adminlist);
		}
		List<User> members = gson.fromJson(chatGroup.members+"",new TypeToken<List<User>>(){}.getType());//System.out.println("members="+members);
		if(members != null){
			list.get(2).list.addAll(members);
		}
	}
}
