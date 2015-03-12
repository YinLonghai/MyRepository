package com.guotion.sicilia.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.guotion.sicilia.R;
import com.guotion.sicilia.application.SiciliaApplication;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.CloudManager;
import com.guotion.sicilia.ui.adapter.CloudCheckFileAdapter;
import com.guotion.sicilia.ui.adapter.CloudCheckUserAdapter;
import com.guotion.sicilia.ui.dialog.CloudDialog;
import com.guotion.sicilia.ui.listener.OnDeleteListener;
import com.guotion.sicilia.ui.listener.OnUpdateListener;
import com.guotion.sicilia.ui.view.CloudCheckItemView;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CloudCheckActivity extends Activity{
	private LinearLayout llBack;
	private TextView back;
	private TextView user;
	private TextView file;
	private ListView userListView;
	private ListView fileListView;
	private LinearLayout choose;
	private CloudCheckUserAdapter cloudCheckUserAdapter;
	private CloudCheckFileAdapter cloudCheckFileAdapter;
	private RelativeLayout top;
	private ImageView ivBack;
	
	private ArrayList<User> userList;
	
	private ArrayList<CloudEvent> cloudList;

	private int userImgResId;
	private int fileImgResId;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				cloudCheckFileAdapter.notifyDataSetChanged();
				//cloudGridAdapter.notifyDataSetChanged();
				break;
			}
		}
	};
	
	private SiciliaApplication siciliaApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cloud_check);
		siciliaApplication = (SiciliaApplication) getApplication();
		siciliaApplication.addActivity(CloudCheckActivity.this);
		initData();
		initView();
		initListener();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		siciliaApplication.removeActivity(CloudCheckActivity.this);
		super.onDestroy();
	}
	private void initData() {
		userList = new ArrayList<User>();
		cloudList = new ArrayList<CloudEvent>();
		if(AppData.cloudEventList.size() == 0){
			//getCloudList();
		}else{
			cloudList.addAll(AppData.cloudEventList);
			handleList(AppData.cloudEventList);
		}
		cloudCheckUserAdapter = new CloudCheckUserAdapter(userList, CloudCheckActivity.this);
		cloudCheckFileAdapter = new CloudCheckFileAdapter(cloudList, CloudCheckActivity.this);
	}
	private void initView() {
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		back = (TextView) findViewById(R.id.textView_back);
		user = (TextView) findViewById(R.id.button_user);
		file = (TextView) findViewById(R.id.button_file);
		choose = (LinearLayout) findViewById(R.id.linearLayout_cloud_choose);
		userListView = (ListView) findViewById(R.id.listView_user);
		userListView.setAdapter(cloudCheckUserAdapter);
		fileListView = (ListView) findViewById(R.id.listView_file);
		fileListView.setAdapter(cloudCheckFileAdapter);
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(CloudCheckActivity.this).getInt(AppData.THEME);
		try{
			userImgResId = AppData.getThemeImgResId(theme, "user");
			fileImgResId = AppData.getThemeImgResId(theme, "file");
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
			choose.setBackgroundResource(userImgResId);
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
		user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				choose.setBackgroundResource(userImgResId);
				userListView.setVisibility(View.VISIBLE);
				fileListView.setVisibility(View.GONE);
			}
		});
		file.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				choose.setBackgroundResource(fileImgResId);
				userListView.setVisibility(View.GONE);
				fileListView.setVisibility(View.VISIBLE);
			}
		});
		userListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AppData.tempuser = userList.get(arg2);
				UISkip.skip(false, CloudCheckActivity.this, UserCloudsActivity.class);
			}
		});
		fileListView.setOnItemClickListener(new OnItemClickListener() {
			boolean isFirst = true;
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				AppData.tempCloudEvent = cloudList.get(arg2);
				showCloudDialog(arg2);
//				UISkip.skip(false, CloudCheckActivity.this, CloudActivity.class);
			}
		});
		OnScrollListenerImpl onScrollListenerImpl = new OnScrollListenerImpl();
		userListView.setOnScrollListener(onScrollListenerImpl);
		fileListView.setOnScrollListener(onScrollListenerImpl);
	}
	private class OnScrollListenerImpl implements OnScrollListener{
		boolean isFirst = true;
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				for(int i=0;i<view.getChildCount();i++){
					CloudCheckItemView cloudCheckItemView = (CloudCheckItemView) view.getChildAt(i);
					cloudCheckItemView.initNetImg();
				}
			}
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if(!isFirst) return;
			if(view.getChildCount()>0){
				for(int i=0;i<view.getChildCount();i++){
					CloudCheckItemView cloudCheckItemView = (CloudCheckItemView) view.getChildAt(i);
					cloudCheckItemView.initNetImg();
				}
				isFirst = false;
			}
		}
	}
	
	private void showCloudDialog(final int arg2){
		CloudDialog cloudDialog = new CloudDialog(CloudCheckActivity.this, cloudList.get(arg2));		
		cloudDialog.setOnCloudUpdateListener(new OnUpdateListener<CloudEvent>(){

			@Override
			public void onUpdate(CloudEvent t) {
				cloudList.remove(arg2);
				cloudList.add(arg2, t);
				cloudCheckFileAdapter.notifyDataSetChanged();
			}
			
		});
		cloudDialog.setOnCloudDeleteListener(new OnDeleteListener<CloudEvent>(){

			@Override
			public void onDelete(CloudEvent t) {
				cloudList.remove(arg2);
				cloudCheckFileAdapter.notifyDataSetChanged();
			}
			
		});
		cloudDialog.show();
	}
	
	
	private void getCloudList(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<CloudEvent> lsit = new CloudManager().getCloudFiles();
					handleList(lsit);
					cloudList.addAll(lsit);
					AppData.cloudEventList.addAll(lsit);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					handler.sendEmptyMessage(2);
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void handleList(List<CloudEvent> cloudList){
		HashMap<String,User> map = new HashMap<String, User>();
		User user;
		Gson gson = new Gson();
		for (CloudEvent cloudEvent : cloudList) {
			user = gson.fromJson(cloudEvent.owner+"", User.class);
			if(map.get(user._id) == null){
				map.put(user._id, user);
				userList.add(user);
			}
		}
	}
}
