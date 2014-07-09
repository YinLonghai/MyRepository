package com.guotion.sicilia.ui;

import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.volley.VolleyUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatGroup;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.ui.fragment.ActivityFragment;
import com.guotion.sicilia.ui.fragment.CloudFragment;
import com.guotion.sicilia.ui.fragment.ConversationFragment;
import com.guotion.sicilia.ui.fragment.MemberFragment;
import com.guotion.sicilia.ui.fragment.SettingFragment;
import com.guotion.sicilia.ui.listener.MessageFeedbackListener;
import com.guotion.sicilia.ui.listener.ReceiveGroupMessageListener;
import com.guotion.sicilia.ui.listener.ReceiveP2PMessageListener;
import com.guotion.sicilia.ui.view.TabChooseView;
import com.guotion.sicilia.ui.view.TabChooseView.OnSelectChangeListener;
import com.guotion.sicilia.util.AndroidFileUtils;
import com.guotion.sicilia.util.LogUtil;
import com.guotion.sicilia.util.PreferencesHelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends FragmentActivity {
	private ViewPager viewPager = null;
	private TabChooseView tabChooseView = null;
	private int[] titles = null;
	private int[][] icons = null;
	private Fragment[] fragments = null;
	private SectionsPagerAdapter sectionsPagerAdapter = null;
	private ConversationFragment conversationFragment = null;
	private ActivityFragment activityFragment = null;
	private CloudFragment cloudFragment = null;
	private MemberFragment memberFragment = null;
	private SettingFragment settingFragment = null;
	
	private Gson gson = new Gson();
	private int theme;
	private int oldSelected = 0;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				titles[0] += 1;
				tabChooseView.setTitle(titles);
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		initView();
		initListener();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			 moveTaskToBack(true);//true对任何Activity都适用
			 return true;
		}
		return false;
	}
	@Override
	protected void onResume() {
		int theme = new PreferencesHelper(MainActivity.this).getInt(AppData.THEME);//System.out.println("old-"+this.theme+"new-"+theme);
		if(theme != this.theme){//System.out.println("change theme");
		this.theme = theme;
			try {
				icons = new int[][]{{AppData.getThemeImgResId(theme, "message_off"), AppData.getThemeImgResId(theme, "message_on")},
						{AppData.getThemeImgResId(theme, "member_off"),AppData.getThemeImgResId(theme, "member_on") },
						{AppData.getThemeImgResId(theme, "moment_off"), AppData.getThemeImgResId(theme, "moment_on")},
						{AppData.getThemeImgResId(theme, "cloud_off"), AppData.getThemeImgResId(theme, "cloud_on")},
						{AppData.getThemeImgResId(theme, "setting_off"), AppData.getThemeImgResId(theme, "setting_on")}};
				tabChooseView.setIcon(icons);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(conversationFragment != null){
			conversationFragment.onResume();
		}
		if(activityFragment != null){
			activityFragment.onResume();
		}
		if(cloudFragment != null){
			cloudFragment.onResume();
		}
		if(memberFragment != null){
			memberFragment.onResume();
		}
		if(settingFragment != null){
			settingFragment.onResume();
		}
		super.onResume();
	}


	private void initData() {
		CacheUtil.createCachePath();
		AppData.volleyUtil = new VolleyUtil(MainActivity.this);
		titles = new int[]{0,0,0,0,0};
		theme = new PreferencesHelper(MainActivity.this).getInt(AppData.THEME);//System.out.println(theme);
		try {
			icons = new int[][]{{AppData.getThemeImgResId(theme, "message_off"), AppData.getThemeImgResId(theme, "message_on")},
					{AppData.getThemeImgResId(theme, "member_off"),AppData.getThemeImgResId(theme, "member_on") },
					{AppData.getThemeImgResId(theme, "moment_off"), AppData.getThemeImgResId(theme, "moment_on")},
					{AppData.getThemeImgResId(theme, "cloud_off"), AppData.getThemeImgResId(theme, "cloud_on")},
					{AppData.getThemeImgResId(theme, "setting_off"), AppData.getThemeImgResId(theme, "setting_on")}};
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//icons = new int[][]{{R.drawable.message_off_orang, R.drawable.message_on_orang},{R.drawable.member_off_orang, R.drawable.member_on_orang},{R.drawable.moment_off_orang, R.drawable.moment_on_orang},{R.drawable.cloud_off_orang, R.drawable.cloud_on_orang},{R.drawable.setting_off_orang, R.drawable.setting_on_orang}};
	}

	private void initView() {
		tabChooseView = (TabChooseView)findViewById(R.id.tabChooseView_main);
		tabChooseView.setTabNum(5);
		tabChooseView.setTitle(titles);
		tabChooseView.setIcon(icons);
		viewPager = (ViewPager)findViewById(R.id.viewPager_main);
		sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(sectionsPagerAdapter);
	}

	private void initListener() {
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				tabChooseView.setSelected(arg0);	
				changeFragment(arg0);
				if(arg0 == 0){
					if(titles[0] != 0){
						titles[0] = 0;
						tabChooseView.setTitle(titles);
					}
					
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub	
			}
		});
		tabChooseView.setOnSelectChangeListener(new OnSelectChangeListener() {
			
			@Override
			public void selectChange(int selected) {
				viewPager.setCurrentItem(selected);
				changeFragment(selected);
			}
		});
		AppData.imMessageToUIListener.registReceiveP2PMessageListeners(new ReceiveP2PMessageListener() {
			@Override
			public void receiveP2PMessage(ChatItem chatItem) {
				ChatGroup chatGroup = gson.fromJson(chatItem.chatGroup+"", ChatGroup.class);
				AppData.lastReadMap.put(chatGroup._id, chatItem._id);
				if(conversationFragment == null){
					AppData.tempP2pChatList.add(chatItem);
				}
				if(viewPager.getCurrentItem() != 0){
					handler.sendEmptyMessage(1);
				}
			}
		});
		AppData.imMessageToUIListener.registReceiveGroupMessageListeners(new ReceiveGroupMessageListener() {
			@Override
			public void receiveGroupMessage(ChatItem chatItem) {
				ChatGroup chatGroup = gson.fromJson(chatItem.chatGroup+"", ChatGroup.class);
				AppData.lastReadMap.put(chatGroup._id, chatItem._id);
				if(conversationFragment == null){
					AppData.tempGroupChatList.add(chatItem);
				}
				if(viewPager.getCurrentItem() != 0){
					handler.sendEmptyMessage(1);
				}
			}
		});
		AppData.imMessageToUIListener.registMessageFeedbackListener(new MessageFeedbackListener() {
			@Override
			public void messageSendSuccess(ChatItem chatItem) {
				updateChatItem(chatItem);
			}
			@Override
			public void messageReaded(ChatItem chatItem) {
				updateChatItem(chatItem);
			}
		});
	}
	
	private void updateChatItem(ChatItem chatItem){
		for(Entry<String, List<ChatItem>> entry : AppData.chatMap.entrySet()){
			List<ChatItem> list = entry.getValue();
			for(int i=0;i<list.size();i++){
				if(chatItem.msg.equals(list.get(i).msg) && chatItem.date.equals(list.get(i).date)){
					list.set(i, chatItem);
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.i("MainActivity onActivityResult!");
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case AndroidRequestCode.REQ_CODE_UPLOAD_FILE://上传云文件
				//TODO 
//				data.getData().getPath();//TODO 文件路径
				LogUtil.i("fiel path:" + data.getData().getPath());
				if (cloudFragment != null) {
					cloudFragment.addFile(AndroidFileUtils.uriToFilePath(MainActivity.this, data.getData()));
				}
				break;
			case AndroidRequestCode.REQ_CODE_UPLOAD_GROUP_PICTURE:
				if (memberFragment != null) {
					memberFragment.notifyImageCreated();
				}
				break;
			case AndroidRequestCode.REQ_CODE_UPLOAD_GROUP_CAMERA:
				if (memberFragment != null) {
					memberFragment.notifyImageCreated();
				}
				break;
			case AndroidRequestCode.REQ_CODE_CAMERA:
				
				break;
			case AndroidRequestCode.REQ_CODE_UPDATE_FILE://修改云文件
				if (cloudFragment != null) {System.out.println("aaa");
					cloudFragment.addUpdateFile(AndroidFileUtils.uriToFilePath(MainActivity.this, data.getData()));
				}
				break;
			case AndroidRequestCode.REQ_CODE_EDIT_USER_INFO:
				if (data != null && data.getBooleanExtra("user_updated", false)) {
					if (settingFragment != null) {
						settingFragment.notifyUserChange();
					}
				};
				
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}



	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new Fragment[5];
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:		
				conversationFragment = new ConversationFragment();
				fragments[0] = conversationFragment;
				return conversationFragment;
			case 1:		
				memberFragment = new MemberFragment();
				fragments[1] = memberFragment;
				return memberFragment;
			case 2:	
				activityFragment = new ActivityFragment();
				fragments[2] = activityFragment;
				return activityFragment;
			case 3:
				cloudFragment = new CloudFragment();
				fragments[3] = cloudFragment;
				return cloudFragment;
			case 4:
				settingFragment = new SettingFragment();
				fragments[4] = settingFragment;
				return settingFragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 5;
		}
	}
	
	
	public void changeFragment(int selected){
		if (selected != oldSelected) {
			if(fragments[selected] != null){
				fragments[selected].onResume();
			}
			if(fragments[oldSelected] != null){
				fragments[oldSelected].onStop();
			}
			oldSelected = selected;
		}
	}
}
