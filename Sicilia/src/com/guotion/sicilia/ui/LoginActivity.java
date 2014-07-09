package com.guotion.sicilia.ui;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.db.AccountDBHelper;
import com.guotion.sicilia.db.UserDao;
import com.guotion.sicilia.im.ChatServer;
import com.guotion.sicilia.im.listener.DefaultIOCallback;
import com.guotion.sicilia.im.util.AccountManager;
import com.guotion.sicilia.im.util.AccountManager.OnGetUserListener;
import com.guotion.sicilia.ui.dialog.CommonPresentationDialog;
import com.guotion.sicilia.ui.listener.ImMessageToUIListener;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	/**
	 * 输入帐号文本框
	 */
	private EditText accountEditText;
	/**
	 * 输入密码文本框
	 */
	private EditText passwordEditText;
	/**
	 * 登录按钮
	 */
	private Button loginButton;
	/**
	 * 注册按钮
	 */
	private Button registerButton;
	/**
	 * 忘记密码按钮
	 */
	private ImageButton forgetPasswordImageButton;
	/**
	 * 版本号
	 */
	private TextView versionTextView;
	
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "890136282627";//your sender id

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";

    //TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;

    String regid;
	
	private CommonPresentationDialog commonPresentationDialog;
	private ProgressDialog progressDialog = null;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {			
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			if(msg.what == 0)
				UISkip.skip(true, LoginActivity.this, MainActivity.class);
			else{
				showToast(msg.obj + "");
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			 moveTaskToBack(true);//true对任何Activity都适用
			 return true;
		}
		return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initData();
		initView();
		initListener();
	}

	private void initData() {
		// Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
//        if (checkPlayServices()) {
//            gcm = GoogleCloudMessaging.getInstance(this);
//            regid = getRegistrationId(getApplicationContext());
//            if (regid==null || regid.equals("")) {
//                registerInBackground();
//            }
//            //i add this code to test
//            else{
//            	Log.i(TAG,"regId="+regid);
//            }
//        } else {
//            Log.i(TAG, "No valid Google Play Services APK found.");
//        }
	}

	private String getVersionName() throws Exception {
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}

	private void initView() {
		accountEditText = (EditText) findViewById(R.id.editText_login_account);
		passwordEditText = (EditText) findViewById(R.id.editText_login_password);
		loginButton = (Button) findViewById(R.id.button_login_login);
		registerButton = (Button) findViewById(R.id.button_login_register);
		forgetPasswordImageButton = (ImageButton) findViewById(R.id.imageButton_login_forget);

		versionTextView = (TextView) findViewById(R.id.tv_activity_login_version);
		try {
			versionTextView.setText("Version:" + getVersionName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initListener() {
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				final String name = accountEditText.getText().toString().trim();
				final String password = passwordEditText.getText().toString().trim();
//				if((name == null || name.equals("")) && (password == null || password.equals(""))){
//				}
				if (name == null || name.equals("")){
					commonPresentationDialog = new CommonPresentationDialog(LoginActivity.this, "用户名不能为空");
					commonPresentationDialog.show();
				}
				else if (password == null || password.equals("")){
					commonPresentationDialog = new CommonPresentationDialog(LoginActivity.this, "密码不能为空");
					commonPresentationDialog.show();
				}
				else {
					progressDialog = ProgressDialog.show(LoginActivity.this, "登录", "登录中，请稍等……");
					new Thread(new Runnable() {
						public void run() {
							Message message = new Message();
							User user = null;
							try {
								user = new AccountManager().login(name, password, new OnGetUserListener(){

									@Override
									public void onGetUser(User user,String userJson) {
										AppData.setUser(getApplicationContext(), user, userJson);
									}
								});
								DefaultIOCallback defaultIOCallback = new DefaultIOCallback(AppData.getUser(LoginActivity.this)._id);
								AppData.imMessageToUIListener = ImMessageToUIListener.getInstance();
								defaultIOCallback.setMessageNotice(AppData.imMessageToUIListener);
								ChatServer.getInstance().login(AppData.getUser(LoginActivity.this)._id, defaultIOCallback);
								if(user.authorized.equals("0")){
									message.obj = "您还未通过审核";
									message.what = 1;
								}
								else{
									message.what = 0;
									new AccountManager().bindDeviceWithId(new PreferencesHelper(LoginActivity.this).getString(PROPERTY_REG_ID, ""), AppData.getUser(LoginActivity.this)._id);
								}
							} catch (Exception e) {
								e.printStackTrace();
								if(e.getMessage().contains(":")){
									String string = e.getMessage().replace(":", "_");
									message.obj = getResources().getText(AppData.getStringResId(string));
									message.what = 1;
								}else System.out.println("错误代码格式不对");
							}
							if(user != null){
								new UserDao(new AccountDBHelper(getApplicationContext())).createIfNotExists(user);
							}
							handler.sendMessage(message);
						}
					}).start();
				}
			}
		});

		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skip(true, LoginActivity.this, RegisterActivity.class);
			}
		});

		forgetPasswordImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skip(true, LoginActivity.this, ForgetPasswordActivity.class);
			}
		});
	}

	private void showToast(String string) {
		Toast.makeText(LoginActivity.this, string, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	        	//System.out.println();
	            finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId==null || registrationId.equals("")) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	
	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return getSharedPreferences(MainActivity.class.getSimpleName(),Context.MODE_PRIVATE);
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		 new AsyncTask() {
		        //@Override
		        protected void onPostExecute(String msg) {
		            //mDisplay.append(msg + "\n");
		        }

				@Override
				protected Object doInBackground(Object... params) {
					 String msg = "";
			            try {
			                if (gcm == null) {
			                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
			                }
			                regid = gcm.register(SENDER_ID);
			                msg = "Device registered, registration ID=" + regid;
			                Log.i(TAG,"regId="+regid);
			                // You should send the registration ID to your server over HTTP,
			                // so it can use GCM/HTTP or CCS to send messages to your app.
			                // The request to your server should be authenticated if your app
			                // is using accounts.
			                sendRegistrationIdToBackend();
			                // For this demo: we don't need to send it because the device
			                // will send upstream messages to a server that echo back the
			                // message using the 'from' address in the message.

			                // Persist the regID - no need to register again.
			                storeRegistrationId(getApplicationContext(), regid);
			            } catch (IOException ex) {
			                msg = "Error :" + ex.getMessage();
			                // If there is an error, don't just keep trying to register.
			                // Require the user to click a button again, or perform
			                // exponential back-off.
			                ex.printStackTrace();
			            }
			            return msg;
				}
		    }.execute(null, null, null);
	}
	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
	    // Your implementation here.send msgid to you implemented server.
	}
	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
}
