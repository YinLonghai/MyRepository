package com.guotion.sicilia.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.types.Resource;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.db.AccountDBHelper;
import com.guotion.sicilia.db.UserDao;
import com.guotion.sicilia.ui.adapter.AccountAdapter;
import com.guotion.sicilia.ui.adapter.AccountManageAdapter;
import com.guotion.sicilia.util.PreferencesHelper;

public class AccountManageDialog extends Dialog implements View.OnClickListener{
	
	private LinearLayout llBack;
	private TextView settingTextView;
	private Button manageButton;
	private ListView accountListView;
	private ListView accountManageListView;
	private Button loginoutButton;
	RelativeLayout top;
	ImageView ivBack;//android:id="@+id/ImageView_back"

	private ArrayList<User> lists;
	private AccountManageAdapter accountManageAdapter;
	private AccountAdapter accountAdapter;
	private int a = 0;
	private UserDao userDao;
	private Resources resource = null;
	
	private OnLogoutListerner logoutListerner = null;
	public AccountManageDialog(Context context) {
		super(context, R.style.dialog_full_screen);
		setContentView(R.layout.activity_account_manage);
		initData();
		initView();		
		initListener();
	}
	
	private void initView() {
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		settingTextView = (TextView) findViewById(R.id.tv_account_manage_setting);
		manageButton = (Button) findViewById(R.id.button_account_manage_manage);
		accountListView = (ListView) findViewById(R.id.listview_account);
		accountManageListView = (ListView) findViewById(R.id.listview_account_manage);
		loginoutButton = (Button) findViewById(R.id.button_account_manage_loginout);
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		accountAdapter = new AccountAdapter(getContext(), lists);
		accountAdapter.notifyDataSetChanged();
		accountManageAdapter = new AccountManageAdapter(getContext(), lists);
		accountListView.setAdapter(accountAdapter);
		updateTheme();
	}
	
	
	@Override
	public void show() {
		updateTheme();
		super.show();
	}

	private void updateTheme(){
		int theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
		int color;
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				settingTextView.setTextColor(resource.getColor(R.color.white));
				manageButton.setTextColor(resource.getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = resource.getColor(AppData.getThemeColor(theme));
				settingTextView.setTextColor(color);
				manageButton.setTextColor(color);
				break;
			case AppData.THEME_BLUE:System.out.println("THEME_BLUE");
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = resource.getColor(AppData.getThemeColor(theme));
				settingTextView.setTextColor(color);
				manageButton.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				settingTextView.setTextColor(resource.getColor(R.color.white));
				manageButton.setTextColor(resource.getColor(R.color.white));
				break;
			}
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initData() {
		lists = new ArrayList<User>();		
		userDao = new UserDao(new AccountDBHelper(getContext()));
		resource = getContext().getResources();
		List<User> list = userDao.getAll();
		//System.out.println(list==null);
		lists.addAll(list);
		
	}

	private void initListener() {
		llBack.setOnClickListener(this);
		settingTextView.setOnClickListener(this);
		manageButton.setOnClickListener(this);
		loginoutButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == llBack) {
			dismiss();
		}else if (v == settingTextView) {
			dismiss();
		}else if (v == manageButton) {
			if (a == 0) {
				accountManageListView.setVisibility(View.VISIBLE);
				accountListView.setVisibility(View.GONE);
				accountManageListView.setAdapter(accountManageAdapter);
				manageButton.setText("完成");
				a = 1;
			}
			else if (a == 1) {
				accountManageListView.setVisibility(View.GONE);
				accountListView.setVisibility(View.VISIBLE);
				accountListView.setAdapter(accountAdapter);
				manageButton.setText("管理");
				a = 0;
				for(Integer integer : accountManageAdapter.getChoose()){
					userDao.deleteUser(integer);
				}
			}
		}else if (v == loginoutButton) {
			if (logoutListerner != null) {
				dismiss();
				logoutListerner.onLogout();				
			}
			
		}
	}
	
	public void setOnLogoutListerner(OnLogoutListerner l){
		this.logoutListerner = l;
	}
	
	public interface OnLogoutListerner {
		public void onLogout();
	}
}
