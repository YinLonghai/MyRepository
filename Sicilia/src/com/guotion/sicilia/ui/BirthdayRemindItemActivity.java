package com.guotion.sicilia.ui;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.application.SiciliaApplication;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BirthdayRemindItemActivity extends Activity{
	private TextView back;
	private TextView edit;
	private TextView date;
	private TextView date2;
	private TextView name;
	private TextView username;
	private ImageView head;
	RelativeLayout top;
	ImageView ivBack;
	ImageView imageView2;
	ImageView imageView3;
	private LinearLayout llBack;
	
	private User user;
	private String activityId;

	private SiciliaApplication siciliaApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_birthday_remind_item);
		siciliaApplication = (SiciliaApplication) getApplication();
		siciliaApplication.addActivity(BirthdayRemindItemActivity.this);
		initData();
		initView();
		initListener();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		siciliaApplication.removeActivity(BirthdayRemindItemActivity.this);
		super.onDestroy();
	}
	private void initData() {
		if(getIntent().hasExtra("user")){
			user = (User) getIntent().getSerializableExtra("user");
			if(user == null){
				user = new User();
			}
			activityId = getIntent().getStringExtra("activityId");
		}else{
			finish();
			//user = new User();
		}
	}
	private void initView() {
		back = (TextView) findViewById(R.id.textView_back);
		edit = (TextView) findViewById(R.id.textView_edit);
		date = (TextView) findViewById(R.id.textView_date);
		date.setText(user.birthday);
		date2 = (TextView) findViewById(R.id.textView_date2);
		date2.setText(user.lunarBtd);
		name = (TextView) findViewById(R.id.textView_name);
		name.setText(user.userName);
		username = (TextView) findViewById(R.id.textView_userName);
		username.setText(user.userName);
		head = (ImageView) findViewById(R.id.imageView_avatar);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		imageView3 = (ImageView) findViewById(R.id.ImageView3);
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		String imgUrl = user.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+user.headPhoto, head, R.drawable.head_orang, R.drawable.head_orang);
			}else{
				head.setImageBitmap(bitmap);
			}
		}else{
			head.setImageResource(R.drawable.head_orang);
		}
		
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(BirthdayRemindItemActivity.this).getInt(AppData.THEME);
		int color = 0;
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				edit.setTextColor(getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				edit.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				edit.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				edit.setTextColor(getResources().getColor(R.color.white));
				break;
			}
			if(color == 0){
				color = getResources().getColor(AppData.getThemeColor(theme));
			}
			date.setTextColor(color);
			date2.setTextColor(color);
			imageView2.setImageResource(AppData.getThemeImgResId(theme, "cake"));
			imageView3.setImageResource(AppData.getThemeImgResId(theme, "happybday"));
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
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skipToBirthdayRemindTimeActivity(BirthdayRemindItemActivity.this, activityId,user.birthday);
			}
		});
	}
}
