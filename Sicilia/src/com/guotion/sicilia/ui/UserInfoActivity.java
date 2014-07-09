package com.guotion.sicilia.ui;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.ImgUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.SignatureHistory;
import com.guotion.sicilia.bean.net.Tag;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.AccountManager;
import com.guotion.sicilia.im.util.AccountManager.OnGetUserListener;
import com.guotion.sicilia.im.util.TagManager;
import com.guotion.sicilia.ui.listener.OnDateChooseListener;
import com.guotion.sicilia.ui.popupwindow.ChooseDatePopupWindow;
import com.guotion.sicilia.ui.popupwindow.ChooseGenderPopupwindow;
import com.guotion.sicilia.ui.popupwindow.ChoosePhotoPopupwindow;
import com.guotion.sicilia.ui.popupwindow.ChooseGenderPopupwindow.GetGenderListener;
import com.guotion.sicilia.ui.popupwindow.ChooseTextPopupwindow;
import com.guotion.sicilia.ui.popupwindow.ChooseTextPopupwindow.OnTextChangedListener;
import com.guotion.sicilia.util.AndroidFileUtils;
import com.guotion.sicilia.util.LogUtil;
import com.guotion.sicilia.util.LunarCalendar;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.StringUtils;

public class UserInfoActivity extends Activity  implements OnClickListener{
	private LinearLayout parentView;

	private ImageView backImgView;
	private TextView editTextView;
	private TextView settingTextView;

	private ImageView userHeadImageView;

	private EditText nickNameEditText;
	private Button btnFamily;
	private EditText etFamilyPosition;
	private TextView tvTel;
	private TextView tvEmail;
	private Button btnBirthday;
	private EditText sexEditText;
	private EditText preferenceEditText;
	private EditText signatureEditText;
	private EditText passwordEditText;

	private ProgressBar zoneProgressBar;
	
	private TextView zoneSize;

	private LinearLayout linearLayout;
	private Button cancelButton;
	private Button submitButton;

	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private TextView textView4;
	private TextView textView5;
	private TextView textView6;
	private TextView textView7;
	private TextView textView8;
	private TextView textView9;
	private TextView textView10;
	private TextView textView11;

	private RelativeLayout top;
	private User user;
	private ChoosePhotoPopupwindow choosePhotoPopupwindow = null;
	private ChooseDatePopupWindow chooseDatePopupWindow = null;
	private ChooseTextPopupwindow chooseTagPopupwindow = null;
	private String headImagePath = null;
	private List<String> tags = null;
	private boolean isEdit = false;
	private ProgressDialog progressDialog = null;
	private AccountManager accountManager = null;
	private boolean isUpdate = false;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			if(msg.what == 0){
				isUpdate = true;
				showUserInfo();
			}else if(msg.what == 1) {
				Toast.makeText(getApplicationContext(), msg.obj + "", Toast.LENGTH_LONG).show();;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		initData();
		initView();		
		initListener();
	}

	
	@Override
	protected void onDestroy() {
		Intent intent = new Intent();
		intent.putExtra("user_updated", isUpdate);
		setResult(RESULT_OK, intent);
		super.onDestroy();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case AndroidRequestCode.REQ_CODE_CROP_PICTURES:
				headImagePath = choosePhotoPopupwindow.getImagePath();
				userHeadImageView.setImageBitmap(ImgUtil.toRoundBitmap(AndroidFileUtils.getBitmap(headImagePath, 240, 240)));
				break;
			case  AndroidRequestCode.REQ_CODE_CAMERA:
				headImagePath = choosePhotoPopupwindow.getImagePath();
				userHeadImageView.setImageBitmap(ImgUtil.toRoundBitmap(AndroidFileUtils.getBitmap(headImagePath, 240, 240)));
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void initData() {
		isUpdate = false;
		user = AppData.getUser(UserInfoActivity.this);
		accountManager = new AccountManager();
	}
	private void initView() {
		parentView = (LinearLayout) findViewById(R.id.ll_activity_userInfo_parent);

		backImgView = (ImageView) findViewById(R.id.img_activity_userInfo_back);
		settingTextView = (TextView) findViewById(R.id.textView_activity_userInfo_setting);
		editTextView = (TextView) findViewById(R.id.textView_userInfo_top_edit);

		userHeadImageView = (ImageView) findViewById(R.id.img_userinfo_head);

		nickNameEditText = (EditText) findViewById(R.id.edt_userInfo_userName);
		btnFamily = (Button) findViewById(R.id.button_userInfo_family);
		etFamilyPosition = (EditText) findViewById(R.id.editText_userInfo_familyposition);
		tvTel = (TextView) findViewById(R.id.textView_userInfo_tel);
		tvEmail = (TextView) findViewById(R.id.textView_userInfo_email);
		btnBirthday = (Button) findViewById(R.id.button_userInfo_birthday);
		sexEditText = (EditText) findViewById(R.id.edt_userInfo_sex);
		preferenceEditText = (EditText) findViewById(R.id.edt_userInfo_preference);
		signatureEditText = (EditText) findViewById(R.id.edt_userInfo_signature);
		passwordEditText = (EditText) findViewById(R.id.edt_userInfo_password);

		zoneProgressBar = (ProgressBar) findViewById(R.id.progressbar_userInfo_zone);
		zoneSize = (TextView)findViewById(R.id.tv_activity_userInfo_zone_size);

		linearLayout = (LinearLayout) findViewById(R.id.ll_activity_userInfo);
		cancelButton = (Button) findViewById(R.id.button_activity_userInfo_cancel);
		submitButton = (Button) findViewById(R.id.button_activity_userInfo_submit);

		textView1 = (TextView) findViewById(R.id.tv_userInfo_userName);
		textView2 = (TextView) findViewById(R.id.tv_userInfo_family);
		textView3 = (TextView) findViewById(R.id.tv_userInfo_familyposition);
		textView4 = (TextView) findViewById(R.id.tv_userInfo_birthday);
		textView5 = (TextView) findViewById(R.id.tv_userInfo_sex);
		textView6 = (TextView) findViewById(R.id.tv_userInfo_tel);
		textView7 = (TextView) findViewById(R.id.tv_userInfo_email);
		textView8 = (TextView) findViewById(R.id.tv_userInfo_preference);
		textView9 = (TextView) findViewById(R.id.tv_userInfo_signature);
		textView10 = (TextView) findViewById(R.id.tv_userInfo_password);
		textView11 = (TextView) findViewById(R.id.tv_activity_userInfo_zone);
		top = (RelativeLayout) findViewById(R.id.relativeLayout_activity_userInfo_top);
		String imgUrl = AppData.getUser(UserInfoActivity.this).headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+ user.headPhoto, userHeadImageView, R.drawable.head_m, R.drawable.head_m);
			}else{
				userHeadImageView.setImageBitmap(bitmap);
			}
		}else{
			userHeadImageView.setImageResource(R.drawable.head_m);
		}
		updateTheme();
		
		setEditText();
		initFamilyTags();
	}

	
	
	private void initFamilyTags(){
		tags = new ArrayList<String>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<Tag> list = new TagManager().getTags("family");					
					if (list != null) {
						for (Tag tag : list) {
							tags.addAll(tag.tags);
						}						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void updateTheme() {
		int theme = new PreferencesHelper(UserInfoActivity.this).getInt(AppData.THEME);
		int color = 0;
		try {
			switch (theme) {
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				settingTextView.setTextColor(getResources().getColor(R.color.white));
				editTextView.setTextColor(getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				settingTextView.setTextColor(color);
				editTextView.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				settingTextView.setTextColor(color);
				editTextView.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				settingTextView.setTextColor(getResources().getColor(R.color.white));
				editTextView.setTextColor(getResources().getColor(R.color.white));
				break;
			}
			if (color == 0) {
				color = getResources().getColor(AppData.getThemeColor(theme));
			}
			backImgView.setImageResource(AppData.getThemeImgResId(theme, "back"));
			textView1.setTextColor(color);
			textView2.setTextColor(color);
			textView3.setTextColor(color);
			textView4.setTextColor(color);
			textView5.setTextColor(color);
			textView6.setTextColor(color);
			textView7.setTextColor(color);
			textView8.setTextColor(color);
			textView9.setTextColor(color);
			textView10.setTextColor(color);
			textView11.setTextColor(color);
			zoneProgressBar.setProgressDrawable(getResources().getDrawable(AppData.getThemeImgResId(theme, "progressbar")));
			cancelButton.setBackgroundResource(AppData.getThemeImgResId(theme, "cancel_background_seletor"));
			submitButton.setBackgroundResource(AppData.getThemeImgResId(theme, "submit_background_seletor"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initListener() {
		nickNameEditText.setKeyListener(null);
		sexEditText.setKeyListener(null);
		preferenceEditText.setKeyListener(null);
		signatureEditText.setKeyListener(null);
		passwordEditText.setKeyListener(null);
		etFamilyPosition.setKeyListener(null);
		
		editTextView.setOnClickListener(this);

		cancelButton.setOnClickListener(this);

		backImgView.setOnClickListener(this);

		settingTextView.setOnClickListener(this);

		submitButton.setOnClickListener(this);
		btnBirthday.setOnClickListener(this);
		btnFamily.setOnClickListener(this);
		userHeadImageView.setOnClickListener(this);
	}

	private void editListener(EditText editText) {
		editText.setBackgroundResource(R.drawable.edittext_bg_rectangle);
		editText.setGravity(Gravity.CENTER);
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
	}

	private void cancelListener(EditText editText) {
		editText.setGravity(Gravity.CENTER_VERTICAL);
		editText.setBackgroundResource(R.color.transparentwhite);
		editText.setKeyListener(null);
	}

	private void showUserInfo(){
		isEdit = false;
		editTextView.setVisibility(View.VISIBLE);
		textView11.setVisibility(View.VISIBLE);
		zoneProgressBar.setVisibility(View.VISIBLE);
		zoneSize.setVisibility(View.VISIBLE);
		linearLayout.setVisibility(View.GONE);

		cancelListener(nickNameEditText);
		
		cancelListener(etFamilyPosition);
//		cancelListener(btnFamily);
		btnFamily.setBackgroundResource(R.color.transparentwhite);
		
		//TODO
//		cancelListener(btnBirthday);
		btnBirthday.setBackgroundResource(R.color.transparentwhite);
		cancelListener(preferenceEditText);
		cancelListener(signatureEditText);
		cancelListener(passwordEditText);
		cancelListener(sexEditText);
		sexEditText.setOnClickListener(null);

		setEditText();
	}
	
	private void editUserInfo(){
		isEdit = true;
		editTextView.setVisibility(View.INVISIBLE);
		textView11.setVisibility(View.GONE);
		zoneProgressBar.setVisibility(View.GONE);
		zoneSize.setVisibility(View.GONE);
		linearLayout.setVisibility(View.VISIBLE);

		editListener(nickNameEditText);
		editListener(etFamilyPosition);
		//TODO
		btnFamily.setBackgroundResource(R.drawable.edittext_bg_rectangle);
//		editListener(btnFamily);
		//TODO
//		editListener(btnBirthday);
		btnBirthday.setBackgroundResource(R.drawable.edittext_bg_rectangle);
		editListener(preferenceEditText);
		editListener(signatureEditText);
		editListener(passwordEditText);

		editListener(sexEditText);
		sexEditText.setKeyListener(null);
		sexEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChooseGenderPopupwindow chooseGenderPopupwindow = new ChooseGenderPopupwindow(UserInfoActivity.this);
				chooseGenderPopupwindow.setGenderListener(new GetGenderListener() {
					@Override
					public void getGender(String string) {
						sexEditText.setText(string);
					}
				});
				chooseGenderPopupwindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
			}
		});
	}
	
	private void setEditText() {
			nickNameEditText.setText(user.getUserName());
			btnFamily.setText(user.getAttribution());
			etFamilyPosition.setText(user.getJob());
			tvTel.setText(user.getMobile());
			tvEmail.setText(user.getMail());
			btnBirthday.setText(user.getBirthday());
			sexEditText.setText(user.getGender());
			preferenceEditText.setText(user.getPersonalPreferences());
			Gson gson = new Gson();
			LogUtil.i("user signature=" + user.signature);
			SignatureHistory signatureHistory = gson.fromJson(user.signature + "", SignatureHistory.class);
			if (signatureHistory != null && signatureHistory.getContent() != null)
				signatureEditText.setText(signatureHistory.getContent());
			passwordEditText.setText(user.getPassWord());
			int capacity = 1073741824;
			try {
				//个人剩余空间大小
				capacity = Integer.parseInt(user.getCapacity());
			} catch (Exception e) {
				e.printStackTrace();
			}
			zoneProgressBar.setMax(1073741824);
			zoneProgressBar.setProgress(1073741824 - capacity);
			String size = String.format("%.2f", (1073741824 - capacity)*1.0/1024/1024);
			zoneSize.setText(size + "M");
	}
	
	@Override
	public void onClick(View v) {
		if (v == userHeadImageView && isEdit == true) {
			photoChoose();
		}else if (v == btnBirthday && isEdit == true) {
			dateChoose();
		}else if (v == btnFamily && isEdit == true) {
			familyChoose();
		}else if (v == submitButton) {
			submitUserInfo();
		}else if (v == settingTextView || v == backImgView) {
			finish();
		}else if (v == cancelButton) {
			showUserInfo();
		}else if (v == editTextView) {
			editUserInfo();
		}
	}
	
	/**
	 * 提交用户信息
	 */
	private void submitUserInfo() {
		final String userId = user._id;
		final String nickName = nickNameEditText.getText().toString();
		final String attribution = btnFamily.getText().toString();
		final String birthday = btnBirthday.getText().toString();
		final String gender = sexEditText.getText().toString();
		final String personalPreferences= preferenceEditText.getText().toString();
		final String signature = signatureEditText.getText().toString();
		final String passWord = passwordEditText.getText().toString();		
		final String job = etFamilyPosition.getText().toString();
		progressDialog = ProgressDialog.show(UserInfoActivity.this, "提交用户信息", "正在提交中，请稍等……");
		new Thread(new Runnable() {

			@Override
			public void run() {
				final Message message = new Message();
				try {
					String lunarBtd = "";
					if (!birthday.equals("")) {
						DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
						Date date = new Date();
						try {
							date = fmt.parse(birthday);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						LunarCalendar lunarCalendar = new LunarCalendar(date);
						StringBuilder builder = new StringBuilder();
						builder.append(lunarCalendar.getMonth() + "-"
								+ lunarCalendar.getDay());
						lunarBtd = builder.toString();
					}
					if (headImagePath != null && !headImagePath.equals("")) {
						/**
						 * 修改资料(修改头像)
						 * 
						 * user.getSignature()
						 * 的类型不确定是SignatureHistory还是String
						 */
						accountManager.updateUserInfo(userId, passWord, 
								birthday, lunarBtd, gender,
								nickName, signature, attribution,
								personalPreferences, user.vestingDepartment,
								job,
								StringUtils.getFileSuffix(headImagePath),
								new File(headImagePath),
								new OnGetUserListener() {
									@Override
									public void onGetUser(User newUser,
											String userJson) {
										if (newUser != null && userJson != null && !userJson.equals("")) {
											AppData.setUser(
													UserInfoActivity.this,
													newUser, userJson);
											message.what = 0;
											headImagePath = null;
											user = newUser;
										}else {
											message.what = 1;
											message.obj = "网络异常";
											
										}
									}
								});
					} else {
						accountManager.updateUserInfo(userId, passWord, 
								birthday, lunarBtd, gender,
								nickName, signature, attribution,
								personalPreferences, user.vestingDepartment,
								job,
								new OnGetUserListener() {
									@Override
									public void onGetUser(User newUser,
											String userJson) {
										if (newUser != null && userJson != null && !userJson.equals("")) {
											AppData.setUser(
													UserInfoActivity.this,
													newUser, userJson);
											message.what = 0;
											headImagePath = null;
											user = newUser;
										}else {
											message.what = 1;
											message.obj = "网络异常";
										}
									}
								});
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					
					message.what = 1;
					String string = e.getMessage().replace(":", "_");
					message.obj = getResources().getText(
							AppData.getStringResId(string));
				}
				handler.sendMessage(message);
			}
		}).start();
	}
	
	private void photoChoose(){
		if (choosePhotoPopupwindow == null) {
			choosePhotoPopupwindow = new ChoosePhotoPopupwindow(UserInfoActivity.this);
		}
		choosePhotoPopupwindow.setPictureRequestCode(AndroidRequestCode.REQ_CODE_CROP_PICTURES);
		choosePhotoPopupwindow.setCameraRequestCode(AndroidRequestCode.REQ_CODE_CAMERA);
		choosePhotoPopupwindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
	}
	private void familyChoose() {
		if (chooseTagPopupwindow == null) {
			chooseTagPopupwindow = new ChooseTextPopupwindow(UserInfoActivity.this);
		}
		if (tags != null && tags.size() > 0) {
			chooseTagPopupwindow.setTextList(tags);
			chooseTagPopupwindow.setOnTextChangedListener(new OnTextChangedListener() {
				
				@Override
				public void textChanged(int index, String text) {
					btnFamily.setText(text);
				}
			});
			chooseTagPopupwindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		}	
		
	}
	
	private void dateChoose(){
		if (chooseDatePopupWindow == null) {
			chooseDatePopupWindow = new ChooseDatePopupWindow(UserInfoActivity.this);
			chooseDatePopupWindow.setOnDateChooseListener(new OnDateChooseListener() {
				
				@Override
				public void onDateChoosed(int year, int month, int day) {
					btnBirthday.setGravity(Gravity.CENTER_HORIZONTAL);
					btnBirthday.setText(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
				}
			});
			chooseDatePopupWindow.setDate(AppData.getUser(UserInfoActivity.this).getBirthday());
		}
		chooseDatePopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		
	}
}