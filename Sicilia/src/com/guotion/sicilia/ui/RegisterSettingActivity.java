package com.guotion.sicilia.ui;

import java.util.ArrayList;
import java.util.List;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.ui.RegisterManageActivity.RegisterManageListener;
import com.guotion.sicilia.ui.adapter.StatisticAdapter;
import com.guotion.sicilia.ui.view.MeasureListView;
import com.guotion.sicilia.ui.view.RefreshableView;
import com.guotion.sicilia.ui.view.RefreshableView.RefreshListener;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;
import com.guotion.sicilia.util.Utility;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @function 注册管理设置
 *
 * @author   Longhai.Yin  MailTo: 1195219040 @ qq.com
 *
 * @version  NO.01
 *
 * @create   2014-4-9 下午3:27:47
 *
 */
public class RegisterSettingActivity extends Activity {
	
	private LinearLayout llBack;
	/**
	 * 返回设置
	 */
	private TextView returnSetting;
	/**
	 * 冻结的账户 tv
	 */
	private TextView frozenAccount;
	/**
	 * 未通过的账户 tv
	 */
	private TextView nopassAccount;
	/**
	 * 通过的账户 tv
	 */
	private TextView waspassedAccount;
	/**
	 * 冻结的账户 lv
	 */
	private MeasureListView lvFrozen;
	/**
	 * 未通过的账户 lv
	 */
	private MeasureListView lvNopass;
	/**
	 * 通过的账户 lv
	 */
	private MeasureListView lvWaspass;
	
	RelativeLayout top;
	ImageView ivBack;
	
    private ArrayList<User> lvFrozenData ;
	
	private StatisticAdapter lvFrozenAdapter;

    private ArrayList<User> lvNopassData ;
	
	private StatisticAdapter lvNopassAdapter;

    private ArrayList<User> lvWaspassData ;
	
	private StatisticAdapter lvWaspassAdapter;
	private RefreshableView refreshableView;
	
	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch(message.what){
			case 1:
				refreshableView.finishRefresh();
				break;
			case 2:
				lvFrozenAdapter.notifyDataSetChanged();
				break;
			case 3:
				lvNopassAdapter.notifyDataSetChanged();
				break;
			case 4:
				lvWaspassAdapter.notifyDataSetChanged();
				break;
			case 5:
				lvFrozenAdapter.notifyDataSetChanged();
				lvNopassAdapter.notifyDataSetChanged();
				lvWaspassAdapter.notifyDataSetChanged();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		initView();
		initData();
		initListener();
	}

	private void initData() {
		lvFrozenData = new ArrayList<User>();
		lvNopassData = new ArrayList<User>();
		lvWaspassData = new ArrayList<User>();

		//frozen
		lvFrozenAdapter = new StatisticAdapter(this, lvFrozenData);
		lvFrozen.setAdapter(lvFrozenAdapter);
		//no pass
		lvNopassAdapter = new StatisticAdapter(this, lvNopassData);
		lvNopass.setAdapter(lvNopassAdapter);
		// was passed
		lvWaspassAdapter = new StatisticAdapter(this, lvWaspassData);
		lvWaspass.setAdapter(lvWaspassAdapter);
		if(AppData.userList.size() == 0){
			
		}else{
			handleList(AppData.userList);
		}

		Utility.setListViewHeightBasedOnChildren(lvFrozen);
		Utility.setListViewHeightBasedOnChildren(lvNopass);
		Utility.setListViewHeightBasedOnChildren(lvWaspass);
	}

	private void initView() {
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		returnSetting = (TextView) findViewById(R.id.to_settings_txt);
		frozenAccount = (TextView) findViewById(R.id.frozen_account);
		nopassAccount = (TextView) findViewById(R.id.not_pass_account);
		waspassedAccount = (TextView) findViewById(R.id.was_passed_account);
		lvFrozen = (MeasureListView) findViewById(R.id.lv_frozen);
		lvNopass = (MeasureListView) findViewById(R.id.lv_not_pass);
		lvWaspass = (MeasureListView) findViewById(R.id.lv_was_passed);
		top =  (RelativeLayout) findViewById(R.id.Relout_title_id);
		refreshableView = (RefreshableView) findViewById(R.id.LinearLayout_setting);
		refreshableView.setTvFreshIsVisible(true);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(RegisterSettingActivity.this).getInt(AppData.THEME);
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				returnSetting.setTextColor(getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				returnSetting.setTextColor(getResources().getColor(AppData.getThemeColor(theme)));
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				returnSetting.setTextColor(getResources().getColor(AppData.getThemeColor(theme)));
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				returnSetting.setTextColor(getResources().getColor(R.color.white));
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
		
		lvFrozen.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				User user = lvFrozenData.get(arg2);
				AppData.tempuser = user;
				//UISkip.skip(false, RegisterSettingActivity.this, RegisterManageActivity.class);
				RegisterManageActivity registerManageActivity = new RegisterManageActivity(RegisterSettingActivity.this);
				registerManageActivity.setRegisterManageListener(new RegisterManageListener() {
					@Override
					public void getUpdateUser(User user,String oldAccountState,String oldAuthorized) {
						handleUpdateUser(user,oldAccountState,oldAuthorized);
						handler.sendEmptyMessage(5);
					}
					@Override
					public void getDeleteUser(User user) {
						lvFrozenData.remove(user);
						handler.sendEmptyMessage(2);
					}
				});
				registerManageActivity.show();
				//UISkip.skipToRegisterManageActivity(RegisterSettingActivity.this,user._id, user.headPhoto, user.userName,user.passWord, user.birthday, user.level, user.authorized, user.accountState);
			}
		});
		lvNopass.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				User user = lvNopassData.get(arg2);
				AppData.tempuser = user;
				RegisterManageActivity registerManageActivity = new RegisterManageActivity(RegisterSettingActivity.this);
				registerManageActivity.setRegisterManageListener(new RegisterManageListener() {
					@Override
					public void getUpdateUser(User user,String oldAccountState,String oldAuthorized) {
						handleUpdateUser(user,oldAccountState,oldAuthorized);
						handler.sendEmptyMessage(5);
					}
					@Override
					public void getDeleteUser(User user) {
						lvNopassData.remove(user);
						handler.sendEmptyMessage(3);
					}
				});
				registerManageActivity.show();
				//UISkip.skip(false, RegisterSettingActivity.this, RegisterManageActivity.class);
				//UISkip.skipToRegisterManageActivity(RegisterSettingActivity.this, user._id,user.headPhoto, user.userName,user.passWord, user.birthday, user.level, user.authorized, user.accountState);
			}
		});
		lvWaspass.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				User user = lvWaspassData.get(arg2);
				AppData.tempuser = user;
				RegisterManageActivity registerManageActivity = new RegisterManageActivity(RegisterSettingActivity.this);
				registerManageActivity.setRegisterManageListener(new RegisterManageListener() {
					@Override
					public void getUpdateUser(User user,String oldAccountState,String oldAuthorized) {
						handleUpdateUser(user,oldAccountState,oldAuthorized);
						handler.sendEmptyMessage(5);
					}
					@Override
					public void getDeleteUser(User user) {
						lvWaspassData.remove(user);
						handler.sendEmptyMessage(4);
					}
				});
				registerManageActivity.show();
				//UISkip.skip(false, RegisterSettingActivity.this, RegisterManageActivity.class);
				//UISkip.skipToRegisterManageActivity(RegisterSettingActivity.this,user._id, user.headPhoto, user.userName,user.passWord, user.birthday, user.level, user.authorized, user.accountState);
			}
		});
		
		refreshableView.setRefreshListener(new RefreshListener() {
			
			@Override
			public void onRefresh(RefreshableView view) {
				handler.sendEmptyMessageDelayed(1, 2000);
			}
		});
	}
	private void handleUpdateUser(User user,String oldAccountState,String oldAuthorized){
		if(!user.accountState.equals(oldAccountState)){
			if(user.accountState.equals("1")){
				lvFrozenData.add(user);
			}else{
				for(int i=0;i<lvFrozenData.size();i++){
					if(lvFrozenData.get(i)._id.equals(user._id)){
						lvFrozenData.remove(i);
						break;
					}
				}
			}
		}
		if(!user.authorized.equals(oldAuthorized)){
			if(user.authorized.equals("1")){
				lvWaspassData.add(user);
				for(int i=0;i<lvNopassData.size();i++){
					if(lvNopassData.get(i)._id.equals(user._id)){
						lvNopassData.remove(i);
						break;
					}
				}
			}else{
				lvNopassData.add(user);
				for(int i=0;i<lvWaspassData.size();i++){
					if(lvWaspassData.get(i)._id.equals(user._id)){
						lvWaspassData.remove(i);
						break;
					}
				}
			}
		}
	}

	private void handleList(List<User> list){
		for (User user : list) {
			if(user.authorized.equals("1")){
				lvWaspassData.add(user);
			}else{
				lvNopassData.add(user);
			}
			if(user.accountState.equals("1")){
				lvFrozenData.add(user);
			}
		}
	}
}
