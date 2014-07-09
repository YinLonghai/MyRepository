package com.guotion.sicilia.ui.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.Activity;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.ActivityManager;
import com.guotion.sicilia.ui.ChatActivity;
import com.guotion.sicilia.ui.adapter.CommonPeopleAdapter;
import com.guotion.sicilia.ui.dialog.DateChooseDialog.OnChooseDateListener;
import com.guotion.sicilia.ui.dialog.InviteMemberDialog.InviteMemberListener;
import com.guotion.sicilia.ui.popupwindow.CreateActivityPopupwindow;
import com.guotion.sicilia.ui.popupwindow.CreateActivityPopupwindow.OnJoinListener;
import com.guotion.sicilia.ui.view.MeasureGridView;
import com.guotion.sicilia.util.PreferencesHelper;

public class CreatActivityDialog extends Dialog implements OnClickListener{
	
	private Context context;

	private RelativeLayout rootView;
	private LinearLayout llBack;
	private EditText etname;
	private EditText etlocation;
	private EditText etdesc;
	private TextView tvStartTime;
	private TextView tvEndTime;
	private TextView tvRemindTime;
	private ImageView ivStartTime;
	private ImageView ivEndTime;
	private ImageView ivRemindTime;
	private ImageView ivInvite;
	private TextView back;
	private TextView ok;
	private TextView textView3;
	private TextView textView4;
	private RelativeLayout top;
	ImageView ivBack;
	private MeasureGridView gvMember;
	private CommonPeopleAdapter commonPeopleAdapter = null;
	private ArrayList<User> userInfos;
	private DateChooseDialog dateChooseDialog = null;
	private Activity activity;
	private Gson gson = new Gson();
	private int state = 0;
	
	private boolean isJoin = false;
	private CreateActivityPopupwindow createActivityPopupwindow = null;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if(createActivityPopupwindow == null){
					createActivityPopupwindow = new CreateActivityPopupwindow(
							getContext());
					createActivityPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
						
						@Override
						public void onDismiss() {
							if (createActivityPopupwindow.isCancel()) {
								dismiss();
							}
						}
					});
					
					createActivityPopupwindow.setOnJoinListener(new OnJoinListener() {
						@Override
						public void notJoinActivity() {
						}
						
						@Override
						public void joinActivity() {
						}
					});
				}
				 
				createActivityPopupwindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
			}else if(msg.what == 2){
				dismiss();
			}else if(msg.what == 3){
				showToast("网络异常");
			}

			super.handleMessage(msg);
		}
	};

	public CreatActivityDialog(Context context) {
		super(context,R.style.dialog_full_screen);
		setContentView(R.layout.activity_create_activity);
		this.context = context;
		initData();
		initView(context);
		initListener();
	}
	
	private void initPopupWindow() {
		new Thread() {
			@Override
			public void run() {
				handler.sendEmptyMessageDelayed(1, 100);
				super.run();
			}
		}.start();
	}

	private void initData() {
		activity = AppData.tempActivity;
		AppData.tempActivity = null;
		if(activity == null){
			activity = new Activity();
			initPopupWindow();
			state = 1;
		}
		userInfos = new ArrayList<User>();
	}

	private void initView(Context context) {
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		rootView = (RelativeLayout) findViewById(R.id.activity_create_activity_root);
		etname = (EditText) findViewById(R.id.editText_name);
		etname.setText(activity.name);
		etlocation = (EditText) findViewById(R.id.editText_location);
		etlocation.setText(activity.location);
		etdesc = (EditText) findViewById(R.id.editText_desc);
		etdesc.setText(activity.content);
		tvStartTime = (TextView) findViewById(R.id.textView_start_time);
		tvStartTime.setText(activity.date);
		tvEndTime = (TextView) findViewById(R.id.textView_end_time);
		tvRemindTime = (TextView) findViewById(R.id.textView_remind_time);
		tvEndTime.setText(activity.endDate);
		tvRemindTime.setText(activity.remind);
		back = (TextView) findViewById(R.id.textView_back);
		ivStartTime = (ImageView) findViewById(R.id.imageView_start_time);
		ivEndTime = (ImageView) findViewById(R.id.imageView_end_time);
		ivRemindTime = (ImageView) findViewById(R.id.imageView_remind_time);
		ivInvite = (ImageView) findViewById(R.id.imageView_invite_member);
		ok = (TextView) findViewById(R.id.textView1);
		textView3 = (TextView) findViewById(R.id.textView3);
		textView4 = (TextView) findViewById(R.id.textView4);
		top = (RelativeLayout) findViewById(R.id.relativeLayout1);
		gvMember = (MeasureGridView)findViewById(R.id.GridView_member);
		commonPeopleAdapter = new CommonPeopleAdapter(context, userInfos);
		gvMember.setAdapter(commonPeopleAdapter);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}

	private void updateTheme() {
		int theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
		int color = 0;
		try {
			switch (theme) {
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getContext().getResources().getColor(R.color.white));
				ok.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				ok.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				ok.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getContext().getResources().getColor(R.color.white));
				ok.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			}
			if (color == 0) {
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
			}
			textView3.setTextColor(color);
			textView4.setTextColor(color);
			ivStartTime.setImageResource(AppData.getThemeImgResId(theme, "start"));
			ivEndTime.setImageResource(AppData.getThemeImgResId(theme, "end"));
			ivRemindTime.setImageResource(AppData.getThemeImgResId(theme, "remind"));
			ivInvite.setImageResource(AppData.getThemeImgResId(theme, "invites_member"));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initListener() {
		llBack.setOnClickListener(this);
		ok.setOnClickListener(this);
		ivInvite.setOnClickListener(this);
		ivStartTime.setOnClickListener(this);
		ivEndTime.setOnClickListener(this);
		ivRemindTime.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (llBack == v) {
			dismiss();
		}else if (v == ivInvite) {
			InviteMemberDialog inviteMemberDialog = new InviteMemberDialog(getContext());
			inviteMemberDialog.setInviteMemberListener(new InviteMemberListener() {
				@Override
				public void getMembers(List<User> list) {
					userInfos.addAll(list);
					commonPeopleAdapter.notifyDataSetChanged();
				}
			});
			inviteMemberDialog.show();
		}else if (v == ivStartTime) {
			updateStartDate();
		}else if (v == ivEndTime) {
			updateEndDate();
		}else if (v == ivRemindTime) {
			updateRemindDate();
		}else if(v == ok){
			if(state == 1){
				createActivity();
			}
			else{
				updateActivity();
			}
		}
	}
	
	private void updateActivity(){
		final String name = etname.getText().toString();
		if(name == null || name.equals("")){
			showToast("请输入活动名称");
			return ;
		}
		final String location = etlocation.getText().toString();
		if(location == null || location.equals("")){
			showToast("请输入活动地点");
			return ;
		}
		final String desc = etdesc.getText().toString();
		if(desc == null || desc.equals("")){
			showToast("请输入活动描述");
			return ;
		}
		final String startTime = tvStartTime.getText().toString();
		if(startTime.length()<1){
			return ;
		}
		final String endTime = tvEndTime.getText().toString();
		if(endTime.length()<1){
			return ;
		}
		final String remindTime = tvRemindTime.getText().toString();
		if(remindTime.length()<1){
			return ;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					StringBuilder members = new StringBuilder();
					//StringBuilder dict = new StringBuilder();
					StringBuilder needPushIDs = new StringBuilder();
					HashMap<String,User> memberMap = new HashMap<String, User>();
					List<User> memberList = gson.fromJson(activity.members+"",new TypeToken<List<User>>(){}.getType());
					for(User user : memberList){
						memberMap.put(user._id, user);
					}
					for(User user : userInfos){
						User u = memberMap.get(user._id);
						if(u == null){
							activity.dict += ","+user._id+":0";
							memberMap.put(user._id, user);
						}
					}
					for(Entry<String,User> entry : memberMap.entrySet()){
						User user = entry.getValue();
						members.append(user._id+"|");
						needPushIDs.append(user._id+"-");
					}
					members.deleteCharAt(members.length()-1);
					needPushIDs.deleteCharAt(needPushIDs.length()-1);
					Activity newActivity = new ActivityManager().updateActivity(activity._id, name, desc, startTime, activity.dict, location, remindTime, members.toString(), needPushIDs.toString());
					if(updateActivityListener != null){
						updateActivityListener.getActivity(newActivity);
					}
					handler.sendEmptyMessage(2);
				} catch (Exception e) {
					handler.sendEmptyMessage(3);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void createActivity(){
		final String name = etname.getText().toString();
		if(name == null || name.equals("")){
			showToast("请输入活动名称");
			return ;
		}
		final String location = etlocation.getText().toString();
		if(location == null || location.equals("")){
			showToast("请输入活动地点");
			return ;
		}
		final String desc = etdesc.getText().toString();
		if(desc == null || desc.equals("")){
			showToast("请输入活动描述");
			return ;
		}
		final String startTime = tvStartTime.getText().toString();
		if(startTime.length()<1){
			return ;
		}
		final String endTime = tvEndTime.getText().toString();
		if(endTime.length()<1){
			return ;
		}
		final String remindTime = tvRemindTime.getText().toString();
		if(remindTime.length()<1){
			return ;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					User mine = AppData.getUser(context);
					StringBuilder members = new StringBuilder();
					StringBuilder dict = new StringBuilder();
					for(User uer : userInfos){
						members.append(uer._id+"|");
						dict.append(uer._id+":0,");
					}
					members.append(mine._id);
					//members.deleteCharAt(members.length()-1);
					if(isJoin){
						dict.append(mine._id+":1");
					}else{
						dict.append(mine._id+":0");
					}
					//dict.deleteCharAt(dict.length()-1);
					Activity activity = new ActivityManager().createActivity(name, desc, startTime, members.toString(), mine._id, remindTime, location, dict.toString(), endTime);
					if(creatActivityListener != null){
						creatActivityListener.getActivity(activity);
					}
					handler.sendEmptyMessage(2);
				} catch (Exception e) {
					handler.sendEmptyMessage(3);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void showToast(String string){
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}
	
	private void updateStartDate(){
		if (dateChooseDialog == null) {
			dateChooseDialog = new DateChooseDialog(getContext());
		}
		dateChooseDialog.setTitle("开始时间");
		dateChooseDialog.setOnChooseDateListener(new OnChooseDateListener() {

			@Override
			public void onChooseDate(int year, int month, int day, int hour,
					int minute) {
				tvStartTime.setText(year+"-"+
						String.format("%02d", month)+"-"+
						String.format("%02d", day)+"  "+
						String.format("%02d", hour)+":"+
						String.format("%02d", minute));
			}
			
		});
		dateChooseDialog.show();
	}
	
	private void updateEndDate(){
		if (dateChooseDialog == null) {
			dateChooseDialog = new DateChooseDialog(getContext());
		}
		dateChooseDialog.setTitle("开始时间");
		dateChooseDialog.setOnChooseDateListener(new OnChooseDateListener() {

			@Override
			public void onChooseDate(int year, int month, int day, int hour,
					int minute) {
				tvEndTime.setText(year+"-"+
						String.format("%02d", month)+"-"+
						String.format("%02d", day)+"  "+
						String.format("%02d", hour)+":"+
						String.format("%02d", minute));
			}
			
		});
		dateChooseDialog.show();
	}
	
	private void updateRemindDate(){
		if (dateChooseDialog == null) {
			dateChooseDialog = new DateChooseDialog(getContext());
		}
		dateChooseDialog.setTitle("开始时间");
		dateChooseDialog.setOnChooseDateListener(new OnChooseDateListener() {

			@Override
			public void onChooseDate(int year, int month, int day, int hour,
					int minute) {
				tvRemindTime.setText(year+"-"+
					String.format("%02d", month)+"-"+
					String.format("%02d", day)+"  "+
					String.format("%02d", hour)+":"+
					String.format("%02d", minute));
			}
			
		});
		dateChooseDialog.show();
	}
	private CreatActivityListener  creatActivityListener;
	
	public void setCreatActivityListener(CreatActivityListener creatActivityListener) {
		this.creatActivityListener = creatActivityListener;
	}

	public interface CreatActivityListener{
		public void getActivity(Activity activity);
	}
	
	private UpdateActivityListener updateActivityListener;
	public void setUpdateActivityListener(UpdateActivityListener updateActivityListener){
		this.updateActivityListener = updateActivityListener;
	}
	
	public interface UpdateActivityListener{
		public void getActivity(Activity activity);
	}
}
