package com.guotion.sicilia.ui;

import com.guotion.sicilia.R;
import com.guotion.sicilia.application.SiciliaApplication;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.ActivityManager;
import com.guotion.sicilia.ui.view.TimeWheelView;
import com.guotion.sicilia.ui.view.TimeWheelView.OnTimeChangeListener;
import com.guotion.sicilia.util.PreferencesHelper;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BirthdayRemindTimeActivity extends Activity{
	private TimeWheelView timeWheelView = null;
	private TextView tvTime = null;
	private TextView tvDate = null;
	private TextView submit = null;
	private TextView back = null;
	private TextView llBack;
	
	private LinearLayout top;
	ImageView ivBack;
	
	private String activityId;
	private String birthday;
	
	private SiciliaApplication siciliaApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_birthdayremind_time);
		siciliaApplication = (SiciliaApplication) getApplication();
		siciliaApplication.addActivity(BirthdayRemindTimeActivity.this);
		initData();
		initView();
		initListener();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		siciliaApplication.removeActivity(BirthdayRemindTimeActivity.this);
		super.onDestroy();
	}
	private void initData(){
		activityId = getIntent().getStringExtra("activityId");
		birthday = getIntent().getStringExtra("birthday");
	}
	
	private void initView(){
		timeWheelView = (TimeWheelView)findViewById(R.id.timeWheelView);
		tvDate = (TextView)findViewById(R.id.tv_birthdayRemind_date);
		tvTime = (TextView)findViewById(R.id.tv_birthdayRemind_time);
		int hour = 12*timeWheelView.getHalfDay() + timeWheelView.getHour();
		tvTime.setText(hour+":"+timeWheelView.getMinute());
		back = (TextView)findViewById(R.id.textView_birthdayremind_setting);
		submit = (TextView)findViewById(R.id.textView_birthdayremind_submit);
		top = (LinearLayout) findViewById(R.id.LinearLayout1);
		llBack = (TextView)findViewById(R.id.textView_birthdayremind_setting);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	private void updateTheme() {
		int theme = new PreferencesHelper(BirthdayRemindTimeActivity.this).getInt(AppData.THEME);
		int color;
		try {
			switch (theme) {
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				submit.setTextColor(getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				submit.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				submit.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				submit.setTextColor(getResources().getColor(R.color.white));
				break;
			}
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void initListener(){
		timeWheelView.setOnTimeChangeListener(new OnTimeChangeListener() {
			
			@Override
			public void onChange(int type, int hour, int minute) {
				hour += 12*type;
				tvTime.setText(hour+":"+minute);
			}
		});
		llBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					new ActivityManager().updateBirthdayPushState(activityId, birthday+" "+tvTime.getText().toString()+":00");
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					handler.sendEmptyMessage(2);
					e.printStackTrace();
				}
			}
		});
	}
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				finish();
				break;
			case 2:
				Toast.makeText(BirthdayRemindTimeActivity.this,"网络异常", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
}