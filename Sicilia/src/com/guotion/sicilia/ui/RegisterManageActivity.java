package com.guotion.sicilia.ui;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.AccountManager;
import com.guotion.sicilia.ui.dialog.WarningDialog;
import com.guotion.sicilia.util.PreferencesHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @function 系统管理员注册管理
 * 
 * @author Longhai.Yin MailTo: 1195219040 @ qq.com
 * 
 * @version NO.01
 * 
 * @create 2014-4-8 下午4:35:30
 * 
 */
public class RegisterManageActivity extends Dialog {

	private LinearLayout llBack;

	/**
	 * 注册管理
	 */
	private TextView returnRegister;
	/**
	 * 登录的用户名称
	 */
	private TextView titleName;
	/**
	 * 注册用户头像
	 */
	private ImageView userHead;
	/**
	 * 注册用户姓名
	 */
	private TextView userName;
	/**
	 * 注册用户出身日期
	 */
	private TextView userBirthday;
	/**
	 * 管理员还是普通用户
	 */
	private RadioGroup userType;
	/**
	 * 未通过申请还是通过申请
	 */
	private RadioGroup passOrNot;
	/**
	 * 管理员
	 */
	private RadioButton userAdmin;
	/**
	 * 普通用户
	 */
	private RadioButton userOrdinary;
	/**
	 * 未通过申请
	 */
	private RadioButton userNoPass;
	/**
	 * 通过申请
	 */
	private RadioButton userIsPass;
	/**
	 * 提交修改
	 */
	private Button submitButton;
	/**
	 * 删除成员
	 */
	private Button delButton;
	/**
	 * 冻结成员
	 */
	private Button frozenButton;

	RelativeLayout top;
	int frozenOnImgResId;
	int frozenOffImgResId;
	int frozenState = 0;// 0-frozenOff ; 1-frozenOn
	
	private AccountManager accountManager;
//	private String id;
//	private String headPhoto;
//	private String name;
//	private String passWord;
//	private String birthday;
//	private String level;
//	private String authorized;
//	private String accountState;
	ImageView ivBack;
	
	private int theme;
	private User user;
	
	private boolean isManage = false;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 2){
				try {
					frozenButton.setBackgroundResource(AppData.getThemeImgResId(theme, "thaw_background_seletor"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(msg.what == 3){
				try {
					frozenButton.setBackgroundResource(AppData.getThemeImgResId(theme, "frozen_background_seletor"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{
				Toast.makeText(getContext(), msg.obj+"", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
	};

	public RegisterManageActivity(Context context) {
		super(context,R.style.dialog_full_screen);
		setContentView(R.layout.activity_register_manage);
		initData();
		initView();
		initListener();
	}

	private void initData() {
		accountManager = new AccountManager();
//		Intent intent = getIntent();
//		id = intent.getStringExtra("id");
//		headPhoto = intent.getStringExtra("headPhoto");
//		name = intent.getStringExtra("userName");
//		passWord = intent.getStringExtra("passWord");
//		birthday = intent.getStringExtra("birthday");
//		level = intent.getStringExtra("level");
//		authorized = intent.getStringExtra("authorized");
//		accountState = intent.getStringExtra("accountState");
		user = AppData.tempuser;
		if(user == null){
			dismiss();
		}
	}

	private void initView() {
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		returnRegister = (TextView) findViewById(R.id.to_manage_register_txt);
		titleName = (TextView) findViewById(R.id.register_manage_title);
		userHead = (ImageView) findViewById(R.id.user_head_img);
		userName = (TextView) findViewById(R.id.user_name_txt);
		userBirthday = (TextView) findViewById(R.id.user_birthday_txt);
		userType = (RadioGroup) findViewById(R.id.radio_group01);
		passOrNot = (RadioGroup) findViewById(R.id.radio_group02);
		userAdmin = (RadioButton) findViewById(R.id.btn_admin);
		userOrdinary = (RadioButton) findViewById(R.id.btn_user);
		userNoPass = (RadioButton) findViewById(R.id.btn_not_pass);
		userIsPass = (RadioButton) findViewById(R.id.btn_was_passed);
		submitButton = (Button) findViewById(R.id.btn_submit_edit);
		delButton = (Button) findViewById(R.id.btn_del_user);
		frozenButton = (Button) findViewById(R.id.btn_frozen_user);
		userName.setText(user.userName);
		userBirthday.setText(user.birthday);
		if(user.level.equals("0")){
			userAdmin.setChecked(true);
		}else{
			userOrdinary.setChecked(true);
		}
		if(user.authorized.equals("0")){
			userNoPass.setChecked(true);
		}else{
			userIsPass.setChecked(true);
		}
		String imgUrl = user.headPhoto;
		if(imgUrl != null && !imgUrl.equals("")){
			Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
			if(bitmap == null){
				AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+user.headPhoto, userHead, R.drawable.head_m, R.drawable.head_m);
			}else{
				userHead.setImageBitmap(bitmap);
			}
		}else{
			userHead.setImageResource(R.drawable.head_m);
		}
		
		top = (RelativeLayout) findViewById(R.id.Relout_title_id);
		
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}

	private void updateTheme() {
		theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
		int color;
		try {
//			frozenOnImgResId = AppData.getThemeImgResId(theme, "frozen_on");
//			frozenOffImgResId = AppData.getThemeImgResId(theme, "frozen_off");
			switch (theme) {
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				returnRegister.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				returnRegister.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				returnRegister.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				returnRegister.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			}
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
			
//			System.out.println(accountState);
			if(user.accountState.equals("0")){
				frozenButton.setBackgroundResource(AppData.getThemeImgResId(theme, "frozen_background_seletor"));
			}else{
				frozenButton.setBackgroundResource(AppData.getThemeImgResId(theme, "thaw_background_seletor"));
			}
			delButton.setBackgroundResource(AppData.getThemeImgResId(theme, "remove_members_background_seletor"));
			submitButton.setBackgroundResource(AppData.getThemeImgResId(theme, "submit_revision_background_seletor"));
			userAdmin.setBackgroundResource(AppData.getThemeImgResId(theme, "register_manage_seletor_one"));
			userOrdinary.setBackgroundResource(AppData.getThemeImgResId(theme, "register_manage_seletor_two"));
			userNoPass.setBackgroundResource(AppData.getThemeImgResId(theme, "register_manage_seletor_three"));
			userIsPass.setBackgroundResource(AppData.getThemeImgResId(theme, "register_manage_seletor_four"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initListener() {
		llBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isManage){
					isManage = true;
				}else{
					dismiss();
				}
				
			}
		});
		/**
		 * 删除成员
		 */
		delButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String warning = "要删除该成员吗？";
				String cancel = "取消";
				String sure = "删除";
				WarningDialog warningDialog = new WarningDialog(getContext(), warning, cancel, sure) {
					@Override
					public void clickCancel() {
					}
					@Override
					public void clickSure() {
						new Thread(new Runnable() {
							public void run() {
								try {
									boolean success = accountManager.deleteUser(user._id, AppData.getUser(getContext())._id);
									if(success){
										if(registerManageListener != null){
											registerManageListener.getDeleteUser(user);
										}
										dismiss();
									}else{
										Message message = new Message();
										message.what = 1;
										message.obj = "网络异常";
										handler.sendMessage(message);
									}
								} catch (Exception e) {
									e.printStackTrace();
									Message message = new Message();
									String string = e.getMessage().replace(":", "_");
									message.obj = getContext().getResources().getText(AppData.getStringResId(string));
									message.what = 1;
									handler.sendMessage(message);
								}
							}
						}).start();
						
					}
				};
				warningDialog.show();
			}
		});
		/**
		 * 冻结成员
		 */
		frozenButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (user.accountState.equals("0")) {
					String warning = "要冻结该账户吗？";
					String cancel = "取消";
					String sure = "冻结";
					WarningDialog warningDialog = new WarningDialog(getContext(), warning, cancel, sure) {
						@Override
						public void clickCancel() {
						}

						@Override
						public void clickSure() {
							new Thread(new Runnable() {
								public void run() {
									Message message = new Message();
									try {
										User u = accountManager.frozenAccount(user._id, AppData.getUser(getContext())._id);
										AppData.getUser(u._id).setAccountState(u.getAccountState());
										user.accountState = u.getAccountState();
										message.what = 2;
									} catch (Exception e) {
										e.printStackTrace();
										String string = e.getMessage().replace(":", "_");
										message.obj = getContext().getResources().getText(AppData.getStringResId(string));
										message.what = 1;
									}
									handler.sendMessage(message);
								}
							}).start();

						}
					};
					warningDialog.show();
				}
				//解冻
				else{
					String warning = "要解冻该账户吗？";
					String cancel = "取消";
					String sure = "解冻";
					WarningDialog warningDialog = new WarningDialog(getContext(), warning, cancel, sure) {
						@Override
						public void clickCancel() {
						}
						
						@Override
						public void clickSure() {
							final String newPassword = "123456";
							
							new Thread(new Runnable() {
								public void run() {
									Message message = new Message();
									try {
										User u = accountManager.unFrozenAccount(user._id, AppData.getUser(getContext())._id, newPassword);
										AppData.getUser(u._id).setAccountState(u.getAccountState());
										user.accountState = u.getAccountState();
										message.what = 3;
									} catch (Exception e) {
										e.printStackTrace();
										String string = e.getMessage().replace(":", "_");
										message.obj = getContext().getResources().getText(AppData.getStringResId(string));
										message.what = 1;
									}
									handler.sendMessage(message);
								}
							}).start();
						}
					};
					warningDialog.show();
				}
			}
		});
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							String level;
							if(userAdmin.isChecked()){
								level = "0";
							}else{
								level = "1";
							}
							String authorized;
							if(userIsPass.isChecked()){
								authorized = "1";
							}else{
								authorized = "0";
							}
							User u = accountManager.authorized(user.userName, user.passWord, level, authorized, AppData.getUser(getContext())._id);
							if(registerManageListener != null){
								registerManageListener.getUpdateUser(u,user.accountState,user.authorized);
							}
							dismiss();
						} catch (Exception e) {
							e.printStackTrace();
							Message message = new Message();
							String string = e.getMessage().replace(":", "_");
							message.obj = getContext().getResources().getText(AppData.getStringResId(string));
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}).start();
			}
		});
	}

	private RegisterManageListener registerManageListener;
	
	public void setRegisterManageListener(
			RegisterManageListener registerManageListener) {
		this.registerManageListener = registerManageListener;
	}

	public interface RegisterManageListener{
		public void getDeleteUser(User user);
		public void getUpdateUser(User user,String oldAccountState,String oldAuthorized);
	}
}
