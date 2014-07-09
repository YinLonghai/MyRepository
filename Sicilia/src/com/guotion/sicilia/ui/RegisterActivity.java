package com.guotion.sicilia.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.SharedPreferences;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.Tag;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.AccountManager;
import com.guotion.sicilia.im.util.TagManager;
import com.guotion.sicilia.ui.dialog.CommonPresentationDialog;
import com.guotion.sicilia.ui.listener.OnDateChooseListener;
import com.guotion.sicilia.ui.popupwindow.ChooseDatePopupWindow;
import com.guotion.sicilia.ui.popupwindow.ChooseTextPopupwindow;
import com.guotion.sicilia.ui.popupwindow.ChooseTextPopupwindow.OnTextChangedListener;
import com.guotion.sicilia.util.AndroidSystemUtils;
import com.guotion.sicilia.util.LunarCalendar;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.RegexpUtil;

public class RegisterActivity extends Activity implements OnClickListener{
	private View parentView = null;
	private ImageView backImageView;
	/**
	 * 返回按钮家云
	 */
	private Button familyCloudButton;
	private Button submitButton;

	private EditText loginNameEditText;
	private EditText nickNameEditText;
	private EditText passwordEditText;
	private EditText surePasswordEditText;
	private EditText telEditText;
	private EditText emailEditText;
	/**
	 * 家庭
	 */
	private Button btnFamily;
	private Button btnBirthday;
	
	private ChooseDatePopupWindow chooseDatePopupWindow = null;
	private ChooseTextPopupwindow chooseTagPopupwindow = null;
	
	private String gender = "male";
	private String lundarBtd;

	private RadioGroup radioGroup;
	
	private CommonPresentationDialog presentationDialog = null;

	private List<String> tags = null;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			showToast(msg.obj + "");
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initData();
		initView();
		initListener();
	}

	private void initData() {
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void initView() {
		parentView = findViewById(R.id.activity_regisetr_root);
		backImageView = (ImageView) findViewById(R.id.activity_register_img_back);
		familyCloudButton = (Button) findViewById(R.id.activity_register_button_back);
		submitButton = (Button) findViewById(R.id.activity_register_submit);

		loginNameEditText = (EditText) findViewById(R.id.activity_regisetr_loginName);
		nickNameEditText = (EditText) findViewById(R.id.activity_regisetr_nickname);
		passwordEditText = (EditText) findViewById(R.id.activity_regisetr_password);
		surePasswordEditText = (EditText) findViewById(R.id.activity_regisetr_surePassword);
		telEditText = (EditText) findViewById(R.id.activity_regisetr_tel);
		emailEditText = (EditText) findViewById(R.id.activity_regisetr_email);
		btnFamily = (Button) findViewById(R.id.activity_regisetr_family);
		btnBirthday = (Button) findViewById(R.id.activity_regisetr_birthday);

		radioGroup = (RadioGroup) findViewById(R.id.activity_register_radiogroup);
	}

	private void initListener() {
		backImageView.setOnClickListener(this);
		familyCloudButton.setOnClickListener(this);
		submitButton.setOnClickListener(this);
		btnFamily.setOnClickListener(this);
		btnBirthday.setOnClickListener(this);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (group.getCheckedRadioButtonId()) {
				case R.id.activity_register_male:
					gender = "male";
					break;
				case R.id.activity_register_female:
					gender = "female";
					break;
				default:
					break;
				}
			}
		});
	}

	private void showToast(String string) {
		Toast.makeText(RegisterActivity.this, string, Toast.LENGTH_SHORT).show();
	}
	
	private void familyChoose() {
		if (chooseTagPopupwindow == null) {
			chooseTagPopupwindow = new ChooseTextPopupwindow(RegisterActivity.this);
		}
		if (tags != null && tags.size() > 0) {
			btnFamily.setText(tags.get(0));
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
			chooseDatePopupWindow = new ChooseDatePopupWindow(RegisterActivity.this);
			chooseDatePopupWindow.setOnDateChooseListener(new OnDateChooseListener() {
				
				@Override
				public void onDateChoosed(int year, int month, int day) {
					btnBirthday.setText(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
				}
			});
		}
		chooseDatePopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == submitButton) {
			submitUserInfo();			
		}else if (v == familyCloudButton) {
			finish();
		}else if (v == backImageView) {
			finish();
		}else if (v == btnFamily) {			
			familyChoose();
		}else if (v == btnBirthday) {
			dateChoose();
		}
	}
	
	private void submitUserInfo(){
		final String userName = loginNameEditText.getText().toString().replace(" ", "");
		final String password = passwordEditText.getText().toString();
		final String birthday = btnBirthday.getText().toString();
		final String nickName = nickNameEditText.getText().toString().replace(" ", "");

		String surePassword = surePasswordEditText.getText().toString();
		final String tel = telEditText.getText().toString();
		final String email = emailEditText.getText().toString();
		final String family = btnFamily.getText().toString().replace(" ", "");
		if (userName == null || userName.equals("")){
			presentationDialog = new CommonPresentationDialog(RegisterActivity.this, "请填写登录名");
			presentationDialog.show();
		}
		else if (password == null || password.equals("")){
			presentationDialog = new CommonPresentationDialog(RegisterActivity.this, "请填写密码");
			presentationDialog.show();
		}
		else if (surePassword == null || surePassword.equals("")){
			presentationDialog = new CommonPresentationDialog(RegisterActivity.this, "请确认密码");
			presentationDialog.show();
		}
		else if (birthday == null || birthday.equals("")){
			presentationDialog = new CommonPresentationDialog(RegisterActivity.this, "请选择出生日期");
			presentationDialog.show();
		}
		else if (!password.equals(surePassword)){
			presentationDialog = new CommonPresentationDialog(RegisterActivity.this, "两次输入的密码不一致");
			presentationDialog.show();
		}
		else if (family == null || family.equals("")){
			presentationDialog = new CommonPresentationDialog(RegisterActivity.this, "请选择家庭");
			presentationDialog.show();
		}
		else if (!RegexpUtil.match(RegexpUtil.REGEXP_EMAIL, email)){
			presentationDialog = new CommonPresentationDialog(RegisterActivity.this, "请填写邮箱");
			presentationDialog.show();
		}
		else if (!RegexpUtil.match(RegexpUtil.REGEXP_PHONE, tel)){
			presentationDialog = new CommonPresentationDialog(RegisterActivity.this, "请填写手机号码");
			presentationDialog.show();
		}
		else {
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			try {
				date = fmt.parse(birthday);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			LunarCalendar lunarCalendar = new LunarCalendar(date);
			StringBuilder builder = new StringBuilder();
			builder.append(lunarCalendar.getMonth() + "-" + lunarCalendar.getDay());
			lundarBtd = builder.toString();
			new Thread(new Runnable() {
				public void run() {
					Message message = new Message();
					try {
						User user = new AccountManager().createAccount(userName, password, birthday, lundarBtd, gender, nickName, email,
								family, tel);
						message.obj = "注册成功";
						new AccountManager().bindDeviceWithUsername(new PreferencesHelper(RegisterActivity.this).getString("registration_id", ""), userName);
					} catch (Exception e) {
						e.printStackTrace();
						String string = e.getMessage().replace(":", "_");
						message.obj = getResources().getText(AppData.getStringResId(string));
					}
					handler.sendMessage(message);
				}
			}).start();
		}
	}
}
