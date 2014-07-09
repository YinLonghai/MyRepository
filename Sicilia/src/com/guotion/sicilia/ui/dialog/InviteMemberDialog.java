package com.guotion.sicilia.ui.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.UserGroupInfo;
import com.guotion.sicilia.bean.UserInfo;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.ui.adapter.AgeMemberAdapter;
import com.guotion.sicilia.ui.adapter.GenderMemberAdapter;
import com.guotion.sicilia.ui.view.ExpandablePullDownView;
import com.guotion.sicilia.ui.view.ExpandablePullDownView.OnExpandablePullDownListener;
import com.guotion.sicilia.ui.view.MemberChildItemView;
import com.guotion.sicilia.ui.view.PullDownView;
import com.guotion.sicilia.ui.view.PullDownView.OnPullDownListener;
import com.guotion.sicilia.ui.view.ScrollOverListView.ScrollListener;
import com.guotion.sicilia.util.PreferencesHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InviteMemberDialog extends Dialog{
	
	private LinearLayout llBack;
	private ExpandablePullDownView expandablePullDownView;
	private PullDownView pullDownView;
	private ExpandableListView memberExpandableListView;
	private GenderMemberAdapter genderMemberAdapter;
	private ListView memberListView;
	private AgeMemberAdapter ageMemberAdapter;
	private CheckBox select;
	private TextView gender;
	private TextView age;
	private TextView back;
	private TextView done;
	RelativeLayout top;
	LinearLayout choose;
	ImageView ivBack;
	
	private ArrayList<UserGroupInfo> genderList;
	private ArrayList<User> ageList;
	
	private boolean isSelectAll = false;
	int selectGenderImgResId;
	int selectAgeImgResId;

	public InviteMemberDialog(Context context) {
		super(context,R.style.dialog_full_screen);
		setContentView(R.layout.activity_invite_meber);
		initData();
		initView();
		initListener();
	}
	
	private void initData() {
		genderList = new ArrayList<UserGroupInfo>();
		UserGroupInfo userGroupInfo;
		userGroupInfo = new UserGroupInfo();
		userGroupInfo.name = "男士";
		userGroupInfo.list = new ArrayList<User>();
		genderList.add(userGroupInfo);
		userGroupInfo = new UserGroupInfo();
		userGroupInfo.name = "女士";
		userGroupInfo.list = new ArrayList<User>();
		genderList.add(userGroupInfo);
		genderMemberAdapter = new GenderMemberAdapter(getContext(), genderList);
		ageList = new ArrayList<User>();
		ageMemberAdapter = new AgeMemberAdapter(getContext(), ageList);
		if(AppData.userList.size() == 0){
			
		}else{
			handleList(AppData.userList);
		}
	}
	private void initView() {
		expandablePullDownView = (ExpandablePullDownView) findViewById(R.id.expandableListView_member_gender);
		expandablePullDownView.setTvFreshIsVisible(true);
		//显示并且可以使用头部刷新
		expandablePullDownView.setShowHeader();
		memberExpandableListView = expandablePullDownView.getExpandableListView();
		memberExpandableListView.setAdapter(genderMemberAdapter);
		//将所有项设置成默认展开
	     int groupCount = genderMemberAdapter.getGroupCount();
	     for (int i=0; i<groupCount; i++) {
	    	 memberExpandableListView.expandGroup(i);
	     }
		
		pullDownView = (PullDownView) findViewById(R.id.expandableListView_member_age);
		pullDownView.setTvFreshIsVisible(true);
		//显示并且可以使用头部刷新
		pullDownView.setShowHeader();
		memberListView = pullDownView.getListView();
		memberListView.setAdapter(ageMemberAdapter);
		select = (CheckBox) findViewById(R.id.imageView_select);
		gender = (TextView) findViewById(R.id.button_gender);
		age = (TextView) findViewById(R.id.button_age);
		back = (TextView) findViewById(R.id.textView_back);
		done = (TextView) findViewById(R.id.textView_cloud_add);
		choose = (LinearLayout) findViewById(R.id.linearLayout_cloud_choose);
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
		int color ;
		try{
			selectGenderImgResId = AppData.getThemeImgResId(theme, "activity_gender");
			selectAgeImgResId = AppData.getThemeImgResId(theme, "activity_age");
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getContext().getResources().getColor(R.color.white));
				done.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				done.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				done.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getContext().getResources().getColor(R.color.white));
				done.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			}
			select.setButtonDrawable(AppData.getThemeImgResId(theme, "checkbox_style"));
			choose.setBackgroundResource(selectGenderImgResId);
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		llBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		gender.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				expandablePullDownView.setVisibility(View.VISIBLE);
				pullDownView.setVisibility(View.GONE);
				choose.setBackgroundResource(selectGenderImgResId);
			}
		});
		age.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pullDownView.setVisibility(View.VISIBLE);
				expandablePullDownView.setVisibility(View.GONE);
				choose.setBackgroundResource(selectAgeImgResId);
			}
		});
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(inviteMemberListener != null){
					ArrayList<User> genderList = genderMemberAdapter.getChoose();
					ArrayList<User> ageList = ageMemberAdapter.getChoose();
					HashMap<String,User> map = new HashMap<String,User>();
					for(int i=0;i<genderList.size();i++){
						map.put(genderList.get(i)._id, genderList.get(i));
					}
					for(int i=0;i<ageList.size();i++){
						map.put(ageList.get(i)._id, ageList.get(i));
					}
					ArrayList<User> choose = new ArrayList<User>();
					for(Entry<String, User> entry : map.entrySet()){
						choose.add(entry.getValue());
					}
					inviteMemberListener.getMembers(choose);
				}
				dismiss();
			}
		});
		select.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if(cb.isChecked()){
					isSelectAll = true;
				}else{
					isSelectAll = false;
				}
				genderMemberAdapter.setSelectAll(isSelectAll);
				ageMemberAdapter.setSelectAll(isSelectAll);
			}
		});
		memberExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				MemberChildItemView memberChildItemView = (MemberChildItemView) v;
//				memberChildItemView.changeSelect();
				return false;
			}
		});
		memberListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				MemberChildItemView memberChildItemView = (MemberChildItemView) v;
//				memberChildItemView.changeSelect();
			}
		});
		
		expandablePullDownView.setOnExpandablePullDownListener(new OnExpandablePullDownListener() {
			
			@Override
			public void onRefresh() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						
						/** 关闭 刷新完毕 ***/
						expandablePullDownView.RefreshComplete();//这个事线程安全的 可看源代码
					}
				}).start();
				
			}
		});
		
		pullDownView.setOnPullDownListener(new OnPullDownListener() {
			
			@Override
			public void onRefresh() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						
						/** 关闭 刷新完毕 ***/
						pullDownView.RefreshComplete();//这个事线程安全的 可看源代码
					}
				}).start();
				
			}
		});
		expandablePullDownView.setScrollListener(new com.guotion.sicilia.ui.view.ScrollOverExpandableListView.ScrollListener() {
			boolean isFirst = true;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof MemberChildItemView){
							MemberChildItemView memberChildItemView = (MemberChildItemView) childView;
							memberChildItemView.initNetImg();
						}
					}
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisiableItem, int arg2,
					int arg3) {
				if(!isFirst) return;
				if(view.getChildCount()>0){
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof MemberChildItemView){
							MemberChildItemView memberChildItemView = (MemberChildItemView) childView;
							memberChildItemView.initNetImg();
							isFirst = false;
						}
					}
				}
			}
		});
		pullDownView.setScrollListener(new ScrollListener() {
			boolean isFirst = true;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof MemberChildItemView){
							MemberChildItemView memberChildItemView = (MemberChildItemView) childView;
							memberChildItemView.initNetImg();
						}
					}
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisiableItem, int arg2,
					int arg3) {
				if(!isFirst) return;
				if(view.getChildCount()>0){
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof MemberChildItemView){
							MemberChildItemView memberChildItemView = (MemberChildItemView) childView;
							memberChildItemView.initNetImg();
							isFirst = false;
						}
					}
				}
			}
		});
	}
	
	private void handleList(List<User> list){
		for (User user : list) {
			if(user.gender.equals("Male")){
				genderList.get(0).list.add(user);
			}else{
				genderList.get(1).list.add(user);
			}
			ageList.add(user);
		}
	}
	
	private InviteMemberListener inviteMemberListener; 
	
	public void setInviteMemberListener(InviteMemberListener inviteMemberListener) {
		this.inviteMemberListener = inviteMemberListener;
	}

	public interface InviteMemberListener{
		public void getMembers(List<User> list);
	}
}
