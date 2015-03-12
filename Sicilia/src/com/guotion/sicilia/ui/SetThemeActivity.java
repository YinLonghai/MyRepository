package com.guotion.sicilia.ui;

import com.guotion.sicilia.R;
import com.guotion.sicilia.application.SiciliaApplication;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.util.PreferencesHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetThemeActivity extends Activity{
	private TextView llBack;
	private TextView settingTextView;
	
	private ImageView maleImageView;
	private ImageView maleOffImageView;
	private ImageView redImageView;
	private ImageView redOffImageView;
	private ImageView blueImageView;
	private ImageView blueOffImageView;
	private ImageView femaleImageView;
	private ImageView femaleOnImageView;
	private RelativeLayout top;
	ImageView ivBack;
	
	private int theme;
	private PreferencesHelper preferencesHelper;
	
	private SiciliaApplication siciliaApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme);
		siciliaApplication = (SiciliaApplication) getApplication();
		siciliaApplication.addActivity(SetThemeActivity.this);
		initData();
		initView();
		initListener();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		siciliaApplication.removeActivity(SetThemeActivity.this);
		super.onDestroy();
	}
	private void initData(){
		preferencesHelper = new PreferencesHelper(SetThemeActivity.this);
		theme = preferencesHelper.getInt(AppData.THEME);
	}
	
	private void initView(){
		llBack = (TextView)findViewById(R.id.textView_theme_setting);
		top = (RelativeLayout) findViewById(R.id.RelativeLayout_top);
		settingTextView = (TextView)findViewById(R.id.textView_theme_setting);
		
		maleImageView = (ImageView)findViewById(R.id.img_acticity_theme_male);
		maleOffImageView = (ImageView)findViewById(R.id.img_acticity_theme_male_off);
		redImageView = (ImageView)findViewById(R.id.img_activity_theme_red);
		redOffImageView = (ImageView)findViewById(R.id.img_activity_theme_red_off);
		blueImageView = (ImageView)findViewById(R.id.img_activity_theme_blue);
		blueOffImageView = (ImageView)findViewById(R.id.img_activity_theme_blue_off);
		femaleImageView = (ImageView)findViewById(R.id.img_activity_theme_female);
		femaleOnImageView = (ImageView)findViewById(R.id.img_activity_theme_female_on);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		
		switch(theme){
		case AppData.THEME_MALE:
			toGreen();
			break;
		case AppData.THEME_FEMALE:
			toOrang();
			break;
		case AppData.THEME_RED:
			toRed();
			break;
		case AppData.THEME_BLUE:
			toBlue();
			break;
		}
		
	}
	
	private void initListener(){
		llBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		settingTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		maleImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(theme == AppData.THEME_MALE) return ;
				theme = AppData.THEME_MALE;
				preferencesHelper.put(AppData.THEME, theme);
				toGreen();
			}
		});
		
		redImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(theme == AppData.THEME_RED) return ;
				theme = AppData.THEME_RED;
				preferencesHelper.put(AppData.THEME, theme);
				toRed();
			}
		});
		
		blueImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(theme == AppData.THEME_BLUE) return ;
				theme = AppData.THEME_BLUE;
				preferencesHelper.put(AppData.THEME, theme);
				toBlue();
			}
		});
		
		femaleImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(theme == AppData.THEME_FEMALE) return ;
				theme = AppData.THEME_FEMALE;
				preferencesHelper.put(AppData.THEME, theme);
				toOrang();
			}
		});
	}
	private void toGreen(){
		maleOffImageView.setVisibility(View.VISIBLE);
		redOffImageView.setVisibility(View.GONE);
		blueOffImageView.setVisibility(View.GONE);
		femaleOnImageView.setVisibility(View.GONE);
		try {
			settingTextView.setTextColor(getResources().getColor(R.color.white));
			top.setBackgroundResource(AppData.getThemeColor(theme));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void toRed(){
		maleOffImageView.setVisibility(View.GONE);
		redOffImageView.setVisibility(View.VISIBLE);
		blueOffImageView.setVisibility(View.GONE);
		femaleOnImageView.setVisibility(View.GONE);
		try {
			settingTextView.setTextColor(getResources().getColor(AppData.getThemeColor(theme)));
			top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void toBlue(){
		maleOffImageView.setVisibility(View.GONE);
		redOffImageView.setVisibility(View.GONE);
		blueOffImageView.setVisibility(View.VISIBLE);
		femaleOnImageView.setVisibility(View.GONE);
		try {
			settingTextView.setTextColor(getResources().getColor(AppData.getThemeColor(theme)));
			top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void toOrang(){
		maleOffImageView.setVisibility(View.GONE);
		redOffImageView.setVisibility(View.GONE);
		blueOffImageView.setVisibility(View.GONE);
		femaleOnImageView.setVisibility(View.VISIBLE);
		try {
			settingTextView.setTextColor(getResources().getColor(R.color.white));
			top.setBackgroundResource(AppData.getThemeColor(theme));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}