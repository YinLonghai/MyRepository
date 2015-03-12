package com.guotion.sicilia.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.application.SiciliaApplication;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.ActivityManager;
import com.guotion.sicilia.ui.adapter.CommonPeopleAdapter;
import com.guotion.sicilia.ui.dialog.CreatActivityDialog;
import com.guotion.sicilia.ui.dialog.CreatActivityDialog.UpdateActivityListener;
import com.guotion.sicilia.ui.dialog.UploadCloudDialog;
import com.guotion.sicilia.ui.dialog.WarningDialog;
import com.guotion.sicilia.ui.dialog.UploadCloudDialog.UploadCloudListener;
import com.guotion.sicilia.ui.view.MeasureGridView;
import com.guotion.sicilia.ui.view.UserImageView;
import com.guotion.sicilia.util.DisplayUtil;
import com.guotion.sicilia.util.PreferencesHelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityInfoActivity extends Activity{
	private LinearLayout llBack;
	private TextView back;
	private TextView edit;
	private TextView startTime;
	private TextView endTime;
	private TextView remidTime;
	private TextView location;
	private TextView desc;
	private TextView userName;
	private TextView activityName;
	private TextView noCloud;
	private ImageView head;
	private ImageView join;
	private ImageView wait;
	private ImageView reject;
	private ImageView addCloud;

	private RelativeLayout top;
	private ImageView ivBack;
	private ArrayList<User> sharedPeople;


	private MeasureGridView gvPeople;
	private CommonPeopleAdapter commonPeopleAdapter = null;
	private LinearLayout llSharedPeople = null;	
	private ArrayList<User> joinList;
	private ArrayList<User> waitList;
	private ArrayList<User> rejectList;
	
	private int theme;
	private com.guotion.sicilia.bean.net.Activity activity;
	private String activityId;
	private int selected = 0;
	
	private SiciliaApplication siciliaApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_info);
		siciliaApplication = (SiciliaApplication) getApplication();
		siciliaApplication.addActivity(ActivityInfoActivity.this);
		initData();
		initView();
		initListener();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		siciliaApplication.removeActivity(ActivityInfoActivity.this);
		super.onDestroy();
	}
	private void initData() {
		if(getIntent().hasExtra("activityId")){
			activityId = (String) getIntent().getSerializableExtra("activityId");
			getSingleActivity();
		}else{
			finish();
		}
		
		joinList = new ArrayList<User>();
		waitList = new ArrayList<User>();
		rejectList = new ArrayList<User>();
		sharedPeople = new ArrayList<User>();
		
	}
	private void initView() {
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		back = (TextView) findViewById(R.id.textView_back);
		edit = (TextView) findViewById(R.id.textView_edit);
		edit.setVisibility(View.GONE);
		startTime = (TextView) findViewById(R.id.textView_start_time);
		//startTime.setText(activity.date);
		endTime = (TextView) findViewById(R.id.textView_end_time);
		//endTime.setText(activity.endDate);
		remidTime = (TextView) findViewById(R.id.textView_remind_time);
		//remidTime.setText(activity.remind);
		location = (TextView) findViewById(R.id.textView7);
		//location.setText(activity.location);
		userName = (TextView) findViewById(R.id.textView1_name);
		activityName = (TextView) findViewById(R.id.textView2);
		//System.out.println();
		
		desc = (TextView) findViewById(R.id.textView8);
		//desc.setText(activity.content);
		noCloud = (TextView) findViewById(R.id.textView9);
		addCloud = (ImageView) findViewById(R.id.ImageView_add);
		head = (ImageView) findViewById(R.id.imageView2);
		join = (ImageView) findViewById(R.id.imageView3);
		wait = (ImageView) findViewById(R.id.imageView4);
		reject = (ImageView) findViewById(R.id.imageView5);
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		gvPeople = (MeasureGridView)findViewById(R.id.GridView_people);
		commonPeopleAdapter = new CommonPeopleAdapter(getApplicationContext(), joinList);
		gvPeople.setAdapter(commonPeopleAdapter);
		llSharedPeople = (LinearLayout)findViewById(R.id.linearLayout_sharedPeople);
		if(sharedPeople.size() == 0){
			llSharedPeople.setVisibility(View.GONE);
			noCloud.setVisibility(View.VISIBLE);
		}else{
			for (User user:sharedPeople) {
				addUser(user);
			}
		}
		ivBack = (ImageView) findViewById(R.id.ImageView_back);

		updateTheme();
	}
	private void addUser(User user){
		UserImageView imageView = new UserImageView(ActivityInfoActivity.this);
		imageView.setUser(user);
		imageView.setMaxHeight(DisplayUtil.dip2px(50));
		imageView.setMaxWidth(DisplayUtil.dip2px(50));
		int padding = DisplayUtil.dip2px(2);
		imageView.setPadding(padding, padding, padding, padding);
		llSharedPeople.addView(imageView);
	}
	
	private void updateTheme(){
		theme = new PreferencesHelper(ActivityInfoActivity.this).getInt(AppData.THEME);
		int color;
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				edit.setTextColor(getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color  =getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				edit.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color  =getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				edit.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				edit.setTextColor(getResources().getColor(R.color.white));
				break;
			}
			join.setBackgroundResource(AppData.getThemeImgResId(theme, "participation_on"));
			wait.setBackgroundResource(AppData.getThemeImgResId(theme, "wait_off"));
			reject.setBackgroundResource(AppData.getThemeImgResId(theme, "reject_off"));
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
				AppData.tempActivity = activity;
				CreatActivityDialog creatActivityDialog = new CreatActivityDialog(ActivityInfoActivity.this);
				creatActivityDialog.setUpdateActivityListener(new UpdateActivityListener() {
					@Override
					public void getActivity(com.guotion.sicilia.bean.net.Activity activityInfo) {
						activity = activityInfo;
						handler.sendEmptyMessage(1);
					}
				});
				creatActivityDialog.show();
			}
		});
		addCloud.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UploadCloudDialog uploadCloudDialog = new UploadCloudDialog(ActivityInfoActivity.this);
				uploadCloudDialog.setUploadCloudListener(new UploadCloudListener() {
					@Override
					public void uploadCloud(CloudEvent cloudEvent) {
						User user = new Gson().fromJson(cloudEvent.owner+"", User.class);
						sharedPeople.add(user);	
						handler.sendEmptyMessage(2);
					}
				});
				uploadCloudDialog.show();
			}
		});
		join.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					selected = 0;
					join.setBackgroundResource(AppData.getThemeImgResId(theme, "participation_on"));
					wait.setBackgroundResource(AppData.getThemeImgResId(theme, "wait_off"));
					reject.setBackgroundResource(AppData.getThemeImgResId(theme, "reject_off"));
					commonPeopleAdapter.setList(joinList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		wait.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					selected = 1;
					join.setBackgroundResource(AppData.getThemeImgResId(theme, "participation_off"));
					wait.setBackgroundResource(AppData.getThemeImgResId(theme, "wait_on"));
					reject.setBackgroundResource(AppData.getThemeImgResId(theme, "reject_off"));
					commonPeopleAdapter.setList(waitList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		reject.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					selected = 2;
					join.setBackgroundResource(AppData.getThemeImgResId(theme, "participation_off"));
					wait.setBackgroundResource(AppData.getThemeImgResId(theme, "wait_off"));
					reject.setBackgroundResource(AppData.getThemeImgResId(theme, "reject_on"));
					commonPeopleAdapter.setList(rejectList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		gvPeople.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//				System.out.println("joinList="+joinList.size());
//				System.out.println("rejectList="+rejectList.size());
//				if(position > -1) return ;
				if(selected == 0 && joinList.get(position).get_id().equals(AppData.getUser(ActivityInfoActivity.this).get_id())){
					String warning = "您已参加该活动，要拒绝吗？";
					WarningDialog WarningDialog = new WarningDialog(ActivityInfoActivity.this, warning, "取消", "拒绝") {
						@Override
						public void clickSure() {
							rejectActivity(joinList.get(position));
						}
						@Override
						public void clickCancel() {
						}
					};
					WarningDialog.show();
				}
				if(selected == 2 && rejectList.get(position).get_id().equals(AppData.getUser(ActivityInfoActivity.this).get_id())){
					String warning = "您已拒绝该活动，要参加吗？";
					WarningDialog WarningDialog = new WarningDialog(ActivityInfoActivity.this, warning, "取消", "参加") {
						@Override
						public void clickSure() {
							joinActivity(rejectList.get(position));
						}
						@Override
						public void clickCancel() {
						}
					};
					WarningDialog.show();
				}
			}
		});
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				startTime.setText(activity.date);
				endTime.setText(activity.endDate);
				remidTime.setText(activity.remind);
				location.setText(activity.location);
				Gson gson = new Gson();
				User creator = gson.fromJson(activity.creator+"", User.class);
				if(creator._id.equals(AppData.getUser(ActivityInfoActivity.this)._id)){
					edit.setVisibility(View.VISIBLE);
				}
				String imgUrl = creator.headPhoto;
				if(imgUrl != null && !imgUrl.equals("")){
					Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
					if(bitmap == null){
						AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+creator.headPhoto, head, R.drawable.head_s, R.drawable.head_s);
					}else{
						head.setImageBitmap(bitmap);
					}
				}else{
					head.setImageResource(R.drawable.head_s);
				}
				
				userName.setText(creator.userName);
				activityName.setText(activity.name);
				desc.setText(activity.content);
				List<User> members = gson.fromJson(activity.members+"",new TypeToken<List<User>>(){}.getType());
				HashMap<String,User> userMap = new HashMap<String,User>();
				for(User user : members){
					userMap.put(user._id, user);
				}
				joinList.clear();
				waitList.clear();
				rejectList.clear();
				String[] dict = TextUtils.split(activity.dict, ",");//System.out.println(activity.dict);
				for(int i=0;i<dict.length;i++){
					String[] userDict = TextUtils.split(dict[i], ":");
					User user = userMap.get(userDict[0]);
					if(user != null){
						if(userDict[1].equals("1")){
							joinList.add(user);
						}else if(userDict[1].equals("0")){
							waitList.add(user);
						}else{
							rejectList.add(user);
						}
					}
				}
				commonPeopleAdapter.notifyDataSetChanged();
				break;
			case 2:
				noCloud.setVisibility(View.GONE);
				addUser(sharedPeople.get(sharedPeople.size()-1));
				break;
			case 3:
				commonPeopleAdapter.notifyDataSetChanged();
				break;
			case 4:
				Toast.makeText(ActivityInfoActivity.this, "更新参与情况失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	private void getSingleActivity(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {//System.out.println("activityId===="+activityId);
					activity = new ActivityManager().getSingleActivity(activityId);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void rejectActivity(final User user){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new ActivityManager().updateMembers(activityId, user._id+":-1");
					rejectList.add(user);
					joinList.remove(user);
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					// TODO Auto-generated catch block 
					handler.sendEmptyMessage(4);
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void joinActivity(final User user){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new ActivityManager().updateMembers(activityId, user._id+":1");
					joinList.add(user);
					rejectList.remove(user);
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					// TODO Auto-generated catch block 
					handler.sendEmptyMessage(4);
					e.printStackTrace();
				}
			}
		}).start();
	}
}
