package com.guotion.sicilia.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.CloudInfo;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.CloudManager;
import com.guotion.sicilia.ui.adapter.UserCloudAdapter;
import com.guotion.sicilia.ui.dialog.CloudDialog;
import com.guotion.sicilia.ui.listener.OnDeleteListener;
import com.guotion.sicilia.ui.listener.OnUpdateListener;
import com.guotion.sicilia.ui.view.PullDownView;
import com.guotion.sicilia.ui.view.PullDownView.OnPullDownListener;
import com.guotion.sicilia.ui.view.ScrollOverListView.ScrollListener;
import com.guotion.sicilia.ui.view.UserCloudItemView;
import com.guotion.sicilia.util.AndroidFileUtils;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserCloudsActivity extends Activity{
	
	private LinearLayout llBack;
	private TextView back;
	private TextView userName;
	private RelativeLayout top;
	private ImageView ivBack;
	private PullDownView pullDownView;
	private ListView cloudsListView;
	private UserCloudAdapter userCloudAdapter;
	private ArrayList<CloudEvent> cloudList;
	private User user;
	private OnPullDownListener pullDownListener = null;
	private CloudDialog cloudDialog;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				userCloudAdapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(UserCloudsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_clouds);
		initData();
		initView();
		initListener();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case AndroidRequestCode.REQ_CODE_UPDATE_FILE://修改云文件
				cloudDialog.addUpdateFile(AndroidFileUtils.uriToFilePath(UserCloudsActivity.this, data.getData()));
				break;
			}
		}
	}
	private void initData() {
		if(getIntent().hasExtra("user")){
			user = (User) getIntent().getSerializableExtra("user");
		}else{
			user = AppData.tempuser;
			if(user == null){
				finish();
			}
		}
		cloudList = new ArrayList<CloudEvent>();
		userCloudAdapter = new UserCloudAdapter(cloudList, UserCloudsActivity.this);
		if(AppData.cloudEventList.size() == 0){
			new Thread(new Runnable() {
				@Override
				public void run() {
					getCloudList();
				}
			}).start();
		}else{
			handleList(AppData.cloudEventList);
		}
		
	}
	private void initView() {
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		back =  (TextView) findViewById(R.id.textView_back);
		userName =  (TextView) findViewById(R.id.textView2);
		pullDownView = (PullDownView) findViewById(R.id.listView_user_clouds);
		pullDownView.setTvFreshIsVisible(true);
		//显示并且可以使用头部刷新
		pullDownView.setShowHeader();
		cloudsListView = pullDownView.getListView();
		cloudsListView.setAdapter(userCloudAdapter);
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		if(!AppData.getUser(UserCloudsActivity.this)._id.equals(user._id)){
			userName.setText(user.userName+"的云存储");
		}
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(UserCloudsActivity.this).getInt(AppData.THEME);
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
		cloudsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				AppData.tempCloudEvent = cloudList.get(arg2-1);
//				UISkip.skip(false, UserCloudsActivity.this, CloudActivity.class);
				showCloudDialog(arg2-1);
				//UISkip.skipToCloudActivity(UserCloudsActivity.this, cloudList.get(arg2));
			}
		});
		pullDownListener = new UserCloudsPullDownListener();
		pullDownView.setOnPullDownListener(pullDownListener);
		pullDownView.setScrollListener(new ScrollListener() {
			boolean isFirst = true;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof UserCloudItemView){
							UserCloudItemView userCloudItemView = (UserCloudItemView) childView;
							userCloudItemView.initNetImg();
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
						if(childView instanceof UserCloudItemView){
							UserCloudItemView userCloudItemView = (UserCloudItemView) childView;
							userCloudItemView.initNetImg();
							isFirst = false;
						}
					}
				}
			}
		});
	}
	
	private void showCloudDialog(final int arg2){
		cloudDialog = new CloudDialog(UserCloudsActivity.this, cloudList.get(arg2));		
		cloudDialog.setOnCloudUpdateListener(new OnUpdateListener<CloudEvent>(){

			@Override
			public void onUpdate(CloudEvent t) {
				cloudList.remove(arg2);
				cloudList.add(arg2, t);
				userCloudAdapter.notifyDataSetChanged();
			}
			
		});
		cloudDialog.setOnCloudDeleteListener(new OnDeleteListener<CloudEvent>(){
			@Override
			public void onDelete(CloudEvent t) {
				cloudList.remove(arg2);
				userCloudAdapter.notifyDataSetChanged();
			}
			
		});
		cloudDialog.show();
	}
	
	private void getCloudList(){
		try {
			List<CloudEvent> lsit = new CloudManager().getUserCloudFile(user._id);
			cloudList.clear();
			cloudList.addAll(lsit);
			handler.sendEmptyMessage(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			handler.sendEmptyMessage(2);
			e.printStackTrace();
		}
	}
	private void handleList(List<CloudEvent> lsit){
		Gson gson = new Gson();
		User u;
		for (CloudEvent cloudEvent : lsit) {
			u = gson.fromJson(cloudEvent.owner+"", User.class);
			if(user._id.equals(u._id)){
				cloudList.add(cloudEvent);
			}
		}
	}
	
	private final class UserCloudsPullDownListener implements OnPullDownListener{

		@Override
		public void onRefresh() {
			new Thread(new Runnable() {
				@Override
				public void run() {
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					getCloudList();
					
					/** 关闭 刷新完毕 ***/
					pullDownView.RefreshComplete();//这个事线程安全的 可看源代码
				}
			}).start();
		}
		
	}//end of UserCloudsPullDownListener
}
