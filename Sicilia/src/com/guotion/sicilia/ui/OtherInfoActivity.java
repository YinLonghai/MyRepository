package com.guotion.sicilia.ui;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
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
import android.widget.TextView;

public class OtherInfoActivity extends Activity{
	private ImageView backImageView;
	private TextView userNameTextView;
	private TextView cloudTextView;
	
	private ImageView headImage;
	private TextView nickNameTextView;
	private TextView familyTextView;
	private TextView familyPositionTextView;
	private TextView birthdayTextView;
	private TextView genderTextView;
	private TextView telTextView;
	private TextView emailTextView;
	private TextView preferenceTextView;
	private TextView signatureTextView;
	
	TextView textView1;
	TextView textView2;
	TextView textView3;
	TextView textView4;
	TextView textView5;
	TextView textView6;
	TextView textView7;
	TextView textView8;
	TextView textView9;
	
	LinearLayout top;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_others_info);
		
		initView();
		initData();
		initListener();
	}

	private void initView(){
		backImageView = (ImageView)findViewById(R.id.imageView_others_info_back);
		userNameTextView = (TextView)findViewById(R.id.textView_other_info_suerName);
		cloudTextView = (TextView)findViewById(R.id.textView_other_info_choud);
		
		headImage = (ImageView)findViewById(R.id.img_other_info_head);
		nickNameTextView = (TextView)findViewById(R.id.tv_other_info_userName);
		familyTextView = (TextView)findViewById(R.id.tv_other_info_family);
		familyPositionTextView = (TextView)findViewById(R.id.tv_other_info_familyposition);
		birthdayTextView = (TextView)findViewById(R.id.tv_other_info_birthday);
		genderTextView = (TextView)findViewById(R.id.tv_other_info_sex);
		telTextView = (TextView)findViewById(R.id.tv_other_info_tel);
		emailTextView = (TextView)findViewById(R.id.tv_other_info_email);
		preferenceTextView = (TextView)findViewById(R.id.tv_other_info_preference);
		signatureTextView = (TextView)findViewById(R.id.tv_other_info_signature);
		
		textView1 = (TextView)findViewById(R.id.tv_userInfo_userName);
		textView2 = (TextView)findViewById(R.id.tv_userInfo_family);
		textView3 = (TextView)findViewById(R.id.tv_userInfo_familyposition);
		textView4 = (TextView)findViewById(R.id.tv_userInfo_birthday);
		textView5 = (TextView)findViewById(R.id.tv_userInfo_sex);
		textView6 = (TextView)findViewById(R.id.tv_userInfo_tel);
		textView7 = (TextView)findViewById(R.id.tv_userInfo_email);
		textView8 = (TextView)findViewById(R.id.tv_userInfo_preference);
		textView9 = (TextView)findViewById(R.id.tv_userInfo_signature);
		top =  (LinearLayout) findViewById(R.id.LinearLayout1);
		
		updateTheme();
	}
	
	private void initData() {
		if(AppData.OTHER_USER != null){
			userNameTextView.setText(AppData.OTHER_USER.getUserName());
			nickNameTextView.setText(AppData.OTHER_USER.getNickName());
			familyTextView.setText(AppData.OTHER_USER.getAttribution());
			familyPositionTextView.setTag(AppData.OTHER_USER.getJob());
			birthdayTextView.setText(AppData.OTHER_USER.getBirthday());
			genderTextView.setText(AppData.OTHER_USER.getGender());
			telTextView.setText(AppData.OTHER_USER.getMobile());
			emailTextView.setText(AppData.OTHER_USER.getMail());
			preferenceTextView.setText(AppData.OTHER_USER.getPersonalPreferences());
			/**
			 * 不确定Signature类型是SignatureHistory，还是String
			 */
			signatureTextView.setText(AppData.OTHER_USER.getSignature()+"");
			String imgUrl = AppData.OTHER_USER.headPhoto;
			if(imgUrl != null && !imgUrl.equals("")){
				Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
				if(bitmap == null){
					AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+AppData.OTHER_USER.headPhoto, headImage, R.drawable.head_m, R.drawable.head_m);
				}else{
					headImage.setImageBitmap(bitmap);
				}
			}else{
				headImage.setImageResource(R.drawable.head_m);
			}
			
		}else{
			finish();
		}
	}
	
	private void updateTheme(){
		int theme = new PreferencesHelper(OtherInfoActivity.this).getInt(AppData.THEME);
		int color =0 ;
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				userNameTextView.setTextColor(getResources().getColor(R.color.white));
				cloudTextView.setTextColor(getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				userNameTextView.setTextColor(color);
				cloudTextView.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				userNameTextView.setTextColor(color);
				cloudTextView.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				userNameTextView.setTextColor(getResources().getColor(R.color.white));
				cloudTextView.setTextColor(getResources().getColor(R.color.white));
				break;
			}
			if(color == 0){
				color = getResources().getColor(AppData.getThemeColor(theme));
			}
			textView1.setTextColor(color);
			textView2.setTextColor(color);
			textView3.setTextColor(color);
			textView4.setTextColor(color);
			textView5.setTextColor(color);
			textView6.setTextColor(color);
			textView7.setTextColor(color);
			textView8.setTextColor(color);
			textView9.setTextColor(color);
			backImageView.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener(){
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		userNameTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		cloudTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skip(false, OtherInfoActivity.this, UserCloudsActivity.class);
			}
		});
	}
}