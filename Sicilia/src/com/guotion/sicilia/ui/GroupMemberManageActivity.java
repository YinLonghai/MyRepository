package com.guotion.sicilia.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guotion.sicilia.R;
import com.guotion.sicilia.application.SiciliaApplication;
import com.guotion.sicilia.bean.UserGroupInfo;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.ChatGroupManager;
import com.guotion.sicilia.ui.adapter.GroupMemberManageExAdapter;
import com.guotion.sicilia.ui.adapter.GroupMemberExAdapter;
import com.guotion.sicilia.ui.view.ActivityChildItemView;
import com.guotion.sicilia.ui.view.GroupMemberChildItemView;
import com.guotion.sicilia.ui.view.MemberManageChildItemView;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.bean.net.User;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	
	private ArrayList<UserGroupInfo> templist;
	private ArrayList<UserGroupInfo> list;
	private List<String> toNormal = new LinkedList<String>();
//	private StringBuffer toDelete = new StringBuffer();
	private List<String> toAdmin = new LinkedList<String>();
	
	private ChatGroup chatGroup ;
	
	private SiciliaApplication siciliaApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_member_manage);
		siciliaApplication = (SiciliaApplication) getApplication();
		siciliaApplication.addActivity(GroupMemberManageActivity.this);
		initData();
		initView();
		initListener();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		siciliaApplication.removeActivity(GroupMemberManageActivity.this);
		super.onDestroy();
	}
	private void initData() {System.out.println("ChatGroup._id"+AppData.tempChatGroup._id);
//		chatGroup = AppData.tempChatGroup;
		list = new ArrayList<UserGroupInfo>();
		templist = new ArrayList<UserGroupInfo>();
		
//		templist.addAll(getUserGroupList());
		groupMemberExAdapter = new GroupMemberExAdapter(list,GroupMemberManageActivity.this );
		groupMemberManageExAdapter = new GroupMemberManageExAdapter(list, GroupMemberManageActivity.this);
		
		getSingleChatGroup();
	}
	private void initView() {
		back = (TextView) findViewById(R.id.textView_back);
		cancle = (TextView) findViewById(R.id.textView_cancle);
		ok = (TextView) findViewById(R.id.textView_ok);
		manage = (TextView) findViewById(R.id.textView_manage);
		manage.setVisibility(View.GONE);
		
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
				memberExpandableListView.setOnChildClickListener(onChildClickListener);
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
				memberExpandableListView.setOnChildClickListener(null);
				memberExpandableListView.setAdapter(groupMemberExAdapter);
				list.clear();
				list.addAll(getUserGroupList1());
				groupMemberExAdapter.notifyDataSetChanged();
				toNormal.clear();
				toAdmin.clear();
				groupMemberManageExAdapter.clear();
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
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							String toNormaldata="";
							String toDeletedata="";
							String toAdmindata="";
							if(toNormal.size()>0){
								toNormaldata = getParamater(toNormal);
							}
							if(groupMemberManageExAdapter.getDeleteList().size()>0){
								toDeletedata = getParamater(groupMemberManageExAdapter.getDeleteList());
							}
							if(toAdmin.size()>0){
								toAdmindata = getParamater(toAdmin);
							}System.out.println("toNormal="+toNormal+" toAdmin="+toAdmin);
							ChatGroup tempchatGroup = new ChatGroupManager().memberUpdate(AppData.tempChatGroup._id,toNormaldata ,toDeletedata , toAdmindata);
							for(int i=0;i<AppData.chatGroupList.size();i++){
								if(AppData.tempChatGroup._id.equals(AppData.chatGroupList.get(i)._id)){
									AppData.chatGroupList.set(i, tempchatGroup);System.out.println("替换chatGroup");
									break;
								}
							}
//							chatGroup = tempchatGroup;
							getSingleChatGroup();
							AppData.tempChatGroup = tempchatGroup;
							toNormal.clear();
							toAdmin.clear();
							groupMemberManageExAdapter.clear();
//							list.clear();
//							list.addAll(getUserGroupList1());
//							templist.clear();
//							templist.addAll(getUserGroupList());
							handler.sendEmptyMessage(1);
						} catch (Exception e) {
							handler.sendEmptyMessage(2);
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
		memberExpandableListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof GroupMemberChildItemView){
							GroupMemberChildItemView groupMemberChildItemView = (GroupMemberChildItemView) childView;
							groupMemberChildItemView.initNetImg();
						}else if(childView instanceof MemberManageChildItemView){
							MemberManageChildItemView memberManageChildItemView = (MemberManageChildItemView) childView;
							memberManageChildItemView.initNetImg();
						}
					}
				}
			}
			boolean isFirst = true;
			boolean isManageChildItemViewFirst = true;
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(!isFirst && !isManageChildItemViewFirst) return;
				if(view.getChildCount()>0){
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof GroupMemberChildItemView){
							GroupMemberChildItemView groupMemberChildItemView = (GroupMemberChildItemView) childView;
							groupMemberChildItemView.initNetImg();
							isFirst = false;
						}else if(childView instanceof MemberManageChildItemView){
							MemberManageChildItemView memberManageChildItemView = (MemberManageChildItemView) childView;
							memberManageChildItemView.initNetImg();
							isManageChildItemViewFirst = false;
						}
					}
				}
			}
		});
	}
	private void isCanManage(){
		String userId = AppData.getUser(GroupMemberManageActivity.this)._id;
		for(User user : list.get(2).list){
			if(userId.equals(user._id)){
				manage.setVisibility(View.GONE);
				break;
			}
		}
	}
	private String getParamater(List<String> list){
		StringBuilder sb = new StringBuilder();
		for(String id : list){
			sb.append(id+"|");
		}
		return sb.toString();
	}
//	private List<UserGroupInfo> getUserGroupList(){
//		Gson gson = new Gson();
//		List<UserGroupInfo> list = new LinkedList<UserGroupInfo>();
//		UserGroupInfo userGroupInfo;
//		userGroupInfo = new UserGroupInfo();
//		userGroupInfo.name = "创建者";
//		userGroupInfo.list = new ArrayList<User>();
//		User creator = AppData.getUser(chatGroup.creator+"");
//		if(creator != null){
//			userGroupInfo.list.add(creator);
//		}
//		list.add(userGroupInfo);
//		userGroupInfo = new UserGroupInfo();
//		userGroupInfo.name = "管理员";
//		userGroupInfo.list = new ArrayList<User>();	
//		User admin;
//		List<String> adminlist = gson.fromJson(chatGroup.admins+"",new TypeToken<List<String>>(){}.getType());
//		for(String adminId : adminlist){
//			admin = AppData.getUser(adminId);
//			if(admin != null){
//				userGroupInfo.list.add(admin);
//			}
//		}
//		
//		list.add(userGroupInfo);
//		userGroupInfo = new UserGroupInfo();
//		userGroupInfo.name = "普通成员";
//		userGroupInfo.list = new ArrayList<User>();
//		User member;
//		List<String> memberlist = gson.fromJson(chatGroup.members+"",new TypeToken<List<String>>(){}.getType());
//		for(String memberId : memberlist){
//			member = AppData.getUser(memberId);
//			if(member != null){
//				userGroupInfo.list.add(member);
//			}
//		}
//		
//		list.add(userGroupInfo);
//		return list;
//	}
	private OnChildClickListener onChildClickListener = new OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			User user;
			if(groupPosition == 1){
				user = list.get(1).list.get(childPosition);
				boolean b = true;
				for(int i=0;i<toAdmin.size();i++){
					if(user._id.equals(toAdmin.get(i))){
						b = false;
						toAdmin.remove(i);
					}
				}
				if(b){
					toNormal.add(user._id);
				}
				list.get(2).list.add(user);
				list.get(1).list.remove(childPosition);
				groupMemberManageExAdapter.notifyDataSetChanged();
			}
			else if(groupPosition == 2){
				user = list.get(2).list.get(childPosition);
				boolean b = true;
				for(int i=0;i<toNormal.size();i++){
					if(user._id.equals(toNormal.get(i))){
						b = false;
						toNormal.remove(i);
					}
				}
				if(b){
					toAdmin.add(user._id);
				}
				list.get(1).list.add(user);
				list.get(2).list.remove(childPosition);
				groupMemberManageExAdapter.notifyDataSetChanged();
			}
			return true;
		}
	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				Toast.makeText(GroupMemberManageActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
				manage.setVisibility(View.VISIBLE);
				cancle.setVisibility(View.GONE);
				ok.setVisibility(View.GONE);
				memberExpandableListView.setOnChildClickListener(null);
				memberExpandableListView.setAdapter(groupMemberExAdapter);
				groupMemberExAdapter.notifyDataSetChanged();
				groupMemberManageExAdapter.notifyDataSetChanged();
				//将所有项设置成默认展开
				int groupCount = groupMemberExAdapter.getGroupCount();
				for (int i=0; i<groupCount; i++) {
					memberExpandableListView.expandGroup(i);
				}
				isCanManage();
				break;
			case 2:
				Toast.makeText(GroupMemberManageActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	private void getSingleChatGroup(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					chatGroup = new ChatGroupManager().getSingleChatGroup(AppData.tempChatGroup._id);
//					handleChatGroup(chatGroup);
					list.clear();
					list.addAll(getUserGroupList1());
					handler.sendEmptyMessage(1);
					//AppData.tempChatGroup = chatGroup;
				} catch (Exception e) {
					handler.sendEmptyMessage(2);
					e.printStackTrace();
				}
			}
		}).start();
	}
	private List<UserGroupInfo> getUserGroupList1(){
		Gson gson = new Gson();
		List<UserGroupInfo> list = new LinkedList<UserGroupInfo>();
		UserGroupInfo userGroupInfo;
		userGroupInfo = new UserGroupInfo();
		userGroupInfo.name = "创建者";
		userGroupInfo.list = new ArrayList<User>();
		User creator = gson.fromJson(chatGroup.creator+"", User.class);
		if(creator != null){
			userGroupInfo.list.add(creator);
		}
		list.add(userGroupInfo);
		userGroupInfo = new UserGroupInfo();
		userGroupInfo.name = "管理员";
		userGroupInfo.list = new ArrayList<User>();
		List<User> adminlist = gson.fromJson(chatGroup.admins+"",new TypeToken<List<User>>(){}.getType());//System.out.println("adminlist="+adminlist);
		if(adminlist != null){
			userGroupInfo.list.addAll(adminlist);
		}
		list.add(userGroupInfo);
		userGroupInfo = new UserGroupInfo();
		userGroupInfo.name = "普通成员";
		userGroupInfo.list = new ArrayList<User>();
		List<User> members = gson.fromJson(chatGroup.members+"",new TypeToken<List<User>>(){}.getType());//System.out.println("members="+members);
		if(members != null){
			userGroupInfo.list.addAll(members);
		}
		list.add(userGroupInfo);
		return list;
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
