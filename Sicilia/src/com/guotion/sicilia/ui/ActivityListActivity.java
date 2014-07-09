package com.guotion.sicilia.ui;

import java.util.ArrayList;
import java.util.List;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ActivityGroupInfo;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.ActivityManager;
import com.guotion.sicilia.ui.adapter.ActivityGroupAdapter;
import com.guotion.sicilia.ui.dialog.CreatActivityDialog;
import com.guotion.sicilia.ui.dialog.CreatActivityDialog.CreatActivityListener;
import com.guotion.sicilia.ui.view.ActivityChildItemView;
import com.guotion.sicilia.ui.view.ExpandablePullDownView;
import com.guotion.sicilia.ui.view.ExpandablePullDownView.OnExpandablePullDownListener;
import com.guotion.sicilia.ui.view.ScrollOverExpandableListView.ScrollListener;
import com.guotion.sicilia.util.DateUtil;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityListActivity extends Activity{
	private ExpandablePullDownView expandablePullDownView;
	private ExpandableListView activityExpandableListView;
	private ActivityGroupAdapter activityGroupAdapter;
	private TextView back;
	private ImageView add;
	RelativeLayout top;
	ImageView ivBack;
	private LinearLayout llBack;
	
	private ArrayList<ActivityGroupInfo> grouplist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_list);
		initData();
		initView();
		initListener();
	}
	private void initData() {
		grouplist = new ArrayList<ActivityGroupInfo>();
		ActivityGroupInfo activityGroupInfo;
		activityGroupInfo = new ActivityGroupInfo();
		activityGroupInfo.name = "进行中";
		activityGroupInfo.activityList = new ArrayList<com.guotion.sicilia.bean.net.Activity>();
		grouplist.add(activityGroupInfo);
		activityGroupInfo = new ActivityGroupInfo();
		activityGroupInfo.name = "即将开始";
		activityGroupInfo.activityList = new ArrayList<com.guotion.sicilia.bean.net.Activity>();
		grouplist.add(activityGroupInfo);
		activityGroupInfo = new ActivityGroupInfo();
		activityGroupInfo.name = "已结束";
		activityGroupInfo.activityList = new ArrayList<com.guotion.sicilia.bean.net.Activity>();
		grouplist.add(activityGroupInfo);
		activityGroupAdapter = new ActivityGroupAdapter(grouplist, ActivityListActivity.this);
		if(AppData.activityList.size()==0){
			//finish();
			//getActivityList();
		}else{
			handleList(AppData.activityList);
		}
		
	}
	private void initView() {
		back = (TextView) findViewById(R.id.textView_back);
		add = (ImageView) findViewById(R.id.imageView_add);
		expandablePullDownView = (ExpandablePullDownView) findViewById(R.id.expandableListView_activitys);
		activityExpandableListView = expandablePullDownView.getExpandableListView();
		expandablePullDownView.setTvFreshIsVisible(true);
		//显示并且可以使用头部刷新
		expandablePullDownView.setShowHeader();		
		activityExpandableListView.setAdapter(activityGroupAdapter);
		 //将所有项设置成默认展开
	     int groupCount = activityGroupAdapter.getGroupCount();
	     for (int i=0; i<groupCount; i++) {
	    	 activityExpandableListView.expandGroup(i);
	     }
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(ActivityListActivity.this).getInt(AppData.THEME);
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
			add.setImageResource(AppData.getThemeImgResId(theme, "add"));
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
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreatActivityDialog creatActivityDialog = new CreatActivityDialog(ActivityListActivity.this);
				creatActivityDialog.setCreatActivityListener(new CreatActivityListener() {
					@Override
					public void getActivity(com.guotion.sicilia.bean.net.Activity activity) {
						grouplist.get(1).activityList.add(activity);
						AppData.tempActivity = activity;
						handler.sendEmptyMessage(1);
					}
				});
				creatActivityDialog.show();
			}
		});
		activityExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				com.guotion.sicilia.bean.net.Activity activity = grouplist.get(groupPosition).activityList.get(childPosition);
				UISkip.skipToActivityInfoActivity(ActivityListActivity.this, activity._id);
				return false;
			}
		});
		
		expandablePullDownView.setOnExpandablePullDownListener(new OnExpandablePullDownListener() {
			
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
						getActivityList();
						/** 关闭 刷新完毕 ***/
						expandablePullDownView.RefreshComplete();//这个事线程安全的 可看源代码
					}
				}).start();
			}
		});
		expandablePullDownView.setScrollListener(new ScrollListener() {
			boolean isFirst = true;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for(int i=0;i<view.getChildCount();i++){
						View childView = view.getChildAt(i);
						if(childView instanceof ActivityChildItemView){
							ActivityChildItemView activityChildItemView = (ActivityChildItemView) childView;
							activityChildItemView.initNetImg();
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
						if(childView instanceof ActivityChildItemView){
							ActivityChildItemView activityChildItemView = (ActivityChildItemView) childView;
							activityChildItemView.initNetImg();
							isFirst = false;
						}
					}
				}
			}
		});
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				activityGroupAdapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(ActivityListActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	ActivityManager activityManager = new ActivityManager();
	private void getActivityList(){
		try {
			List<com.guotion.sicilia.bean.net.Activity> list = activityManager.getActivities();
			AppData.activityList.clear();
			AppData.activityList.addAll(list);
			grouplist.get(0).activityList.clear();
			grouplist.get(1).activityList.clear();
			grouplist.get(2).activityList.clear();
			handleList(list);
			handler.sendEmptyMessage(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block 
			handler.sendEmptyMessage(2);
			e.printStackTrace();
		}
	}
	
	private void handleList(List<com.guotion.sicilia.bean.net.Activity> list){
		String now = DateUtil.getLongDate();
		for (com.guotion.sicilia.bean.net.Activity activity : list) {
			if(activity.type.equals("N")){
				if(now.compareTo(activity.date)<0){
					grouplist.get(1).activityList.add(activity);
				}else if(now.compareTo(activity.endDate)>0){
					grouplist.get(2).activityList.add(activity);
				}else{
					grouplist.get(0).activityList.add(activity);
				}
			}
		}
		//System.out.println("进行中 ="+grouplist.get(0).activityList.size()+"即将开始="+grouplist.get(1).activityList.size()+"已结束="+grouplist.get(2).activityList.size());
	}
}
