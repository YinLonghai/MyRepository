package com.guotion.sicilia.ui;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.AccountManager;
import com.guotion.sicilia.ui.dialog.CommonPresentationDialog;
import com.guotion.sicilia.util.PreferencesHelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPasswordActivity extends Activity {
	CommonPresentationDialog commonPresentationDialog;
	/**
	 * 返回按钮
	 */
	private ImageView backImageView;
	/**
	 * 家云
	 */
	private TextView familyCloudTextView;
	/**
	 * 提交按钮
	 */
	private Button submitButton;
	/**
	 * 登录名输入框
	 */
	private EditText nameEditText;
	/**
	 * 显示用户的密码
	 */
	private TextView passwordTextView;
	LinearLayout top;
	
	private User user;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0)
				passwordTextView.setText("密码:"+user.getPassWord());
			else
				showToast(msg.obj + "");
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpassword);
		initView();
		initListener();
	}

	private void initView() {
		backImageView = (ImageView)findViewById(R.id.img_activity_forgetPassword_back);
		familyCloudTextView = (TextView)findViewById(R.id.tv_activity_forgetPassword_familyCloud);
		submitButton = (Button) findViewById(R.id.activity_forgetpassword_submit);
		nameEditText = (EditText) findViewById(R.id.activity_forgetpassword_loginName);
		passwordTextView = (TextView) findViewById(R.id.activity_forgetpassword_password);
		top =  (LinearLayout) findViewById(R.id.LinearLayout1);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(ForgetPasswordActivity.this).getInt(AppData.THEME);
		int color;
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				familyCloudTextView.setTextColor(getResources().getColor(R.color.white));
				submitButton.setTextColor(getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				familyCloudTextView.setTextColor(color);
				submitButton.setTextColor(color);
				break;
			case AppData.THEME_BLUE:System.out.println("THEME_BLUE");
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				familyCloudTextView.setTextColor(color);
				submitButton.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				familyCloudTextView.setTextColor(getResources().getColor(R.color.white));
				submitButton.setTextColor(getResources().getColor(R.color.white));
				break;
			}
			backImageView.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String name = nameEditText.getText().toString().trim();
				if (name == null || name.equals("")){
					commonPresentationDialog = new CommonPresentationDialog(ForgetPasswordActivity.this, "登录名不能为空");
					commonPresentationDialog.show();
				}
				else {
					if (name.equals("Admin")) {
						commonPresentationDialog = new CommonPresentationDialog(ForgetPasswordActivity.this, "此用户不能在获取");
						commonPresentationDialog.show();
					} else {
						new Thread(new Runnable() {
							public void run() {
								Message message = new Message();
								try {
									user = new AccountManager().getUser(name);
									message.what = 0;
								} catch (Exception e) {
									message.what = 1;
									String string = e.getMessage().replace(":", "_");
									message.obj = getResources().getText(AppData.getStringResId(string));
									e.printStackTrace();
								}
								handler.sendMessage(message);
							}
						}).start();
					}
				}
			}
		});

		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		familyCloudTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void showToast(String string) {
		Toast.makeText(ForgetPasswordActivity.this, string, Toast.LENGTH_SHORT).show();
	}
}
