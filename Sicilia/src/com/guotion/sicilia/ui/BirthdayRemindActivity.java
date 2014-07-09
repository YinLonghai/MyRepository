package com.guotion.sicilia.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ActivityInfo;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.ActivityManager;
import com.guotion.sicilia.ui.adapter.BirthdayRemindAdapter;
import com.guotion.sicilia.ui.view.BirthdayRemindItemView;
import com.guotion.sicilia.ui.view.PullDownView;
import com.guotion.sicilia.ui.view.PullDownView.OnPullDownListener;
import com.guotion.sicilia.ui.view.ScrollOverListView.ScrollListener;
import com.guotion.sicilia.util.DateUtil;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.app.Activity;
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

public class BirthdayRemindActivity extends Activity{
	private TextView back;
	private PullDownView pullDownView;
	private ListView birthdayRemindListView;
	private BirthdayRemindAdapter birthdayRemindAdapter;
	RelativeLayout top;
	ImageView ivBack;
	private LinearLayout llBack;
	
	private ArrayList<com.guotion.sicilia.bean.net.Activity> birthdayReminList;
	private OnPullDownListener pullDownListener = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_birthday_remind);
		initData();
		initView();
		initListener();
	}
	private void initData() {
		birthdayReminList = new ArrayList<com.guotion.sicilia.bean.net.Activity>();
		if(AppData.activityList.size() == 0){
			//getActivityList();
		}else{
			handleList(AppData.activityList);
		}
		birthdayRemindAdapter = new BirthdayRemindAdapter(birthdayReminList, BirthdayRemindActivity.this);
	}
	private void initView() {
		back = (TextView) findViewById(R.id.textView_back);
		pullDownView = (PullDownView) findViewById(R.id.listView_birthday_remind);
		pullDownView.setTvFreshIsVisible(true);
		//显示并且可以使用头部刷新
		pullDownView.setShowHeader();
		birthdayRemindListView = pullDownView.getListView();
		birthdayRemindListView.setAdapter(birthdayRemindAdapter);
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(BirthdayRemindActivity.this).getInt(AppData.THEME);
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
		birthdayRemindListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				com.guotion.sicilia.bean.net.Activity birthdayRemid = birthdayReminList.get(arg2-1);
				UISkip.skipToBirthdayRemindItemActivity(BirthdayRemindActivity.this, new Gson().fromJson(birthdayRemid.creator+"", User.class),birthdayRemid._id);
			}
		});
		
		pullDownListener = new BirthdayRemindPullDownListener();
		pullDownView.setOnPullDownListener(pullDownListener);
		pullDownView.setScrollListener(new ScrollListener() {
			boolean isFirst = true;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof BirthdayRemindItemView){
							BirthdayRemindItemView birthdayRemindItemView = (BirthdayRemindItemView) childView;
							birthdayRemindItemView.initNetImg();
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
						if(childView instanceof BirthdayRemindItemView){
							BirthdayRemindItemView birthdayRemindItemView = (BirthdayRemindItemView) childView;
							birthdayRemindItemView.initNetImg();
							isFirst = false;
						}
					}
				}
			}
		});
	}
	
	private final class BirthdayRemindPullDownListener implements OnPullDownListener{

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
		
	}//end of BirthdayRemindPullDownListener
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				birthdayRemindAdapter.notifyDataSetChanged();
				break;
			}
		}
	};
	
	private void getActivityList(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				try {
					List<com.guotion.sicilia.bean.net.Activity> list = new ActivityManager().getActivities();
					AppData.activityList.addAll(list);
					handleList(list);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block 
					handler.sendEmptyMessage(4);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void handleList(List<com.guotion.sicilia.bean.net.Activity> list){
		for (com.guotion.sicilia.bean.net.Activity activity : list) {
			if(activity.type.equals("B")){
				birthdayReminList.add(activity);
			}
		}
		System.out.println("birthdayReminList size="+birthdayReminList.size());
	}
}
