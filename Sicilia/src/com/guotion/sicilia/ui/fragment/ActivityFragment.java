package com.guotion.sicilia.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ANotification;
import com.guotion.sicilia.bean.net.Activity;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.ActivityManager;
import com.guotion.sicilia.im.util.NoteManager;
import com.guotion.sicilia.ui.ActivityListActivity;
import com.guotion.sicilia.ui.ActivityNotesActivity;
import com.guotion.sicilia.ui.BirthdayRemindActivity;
import com.guotion.sicilia.ui.adapter.ActivityAdapter;
import com.guotion.sicilia.ui.dialog.CreatActivityDialog;
import com.guotion.sicilia.ui.dialog.NoteDialog;
import com.guotion.sicilia.ui.dialog.CreatActivityDialog.CreatActivityListener;
import com.guotion.sicilia.ui.dialog.NoteDialog.CreateNoteListener;
import com.guotion.sicilia.ui.view.MeasureListView;
import com.guotion.sicilia.util.DateUtil;
import com.guotion.sicilia.util.DisplayUtil;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.TextBold;
import com.guotion.sicilia.util.UISkip;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityFragment extends Fragment {
	private View contentView;
	private TextView note;
	private TextView name;
	private TextView desc;
	private ImageView ivAdd;
	private ImageView ivNote;
	private ImageView ivEdit;
	private ImageView ivBirthdayRemind;
	private ImageView ivActivity;
	private ImageView ivHead;
	private ImageView ivRefresh;
	private RelativeLayout top;
	private MeasureListView acticitysListView;
	private ActivityAdapter activityAdapter;

	private ArrayList<Activity> activitylist;
	private List<ANotification> noteList;
	private Activity birthdayRemind;

	private int theme;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				handleNote();
				break;
			case 3:
				activityAdapter.notifyDataSetChanged();
				if (birthdayRemind.creator != null && !birthdayRemind.creator.equals("")) {
					User user = new Gson().fromJson(birthdayRemind.creator+"", User.class);
					if(user == null) break ;
					String imgUrl = user.headPhoto;
					if(imgUrl != null && !imgUrl.equals("")){
						Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
						if(bitmap == null){
							AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST+user.headPhoto, ivHead, R.drawable.head_s, R.drawable.head_s);
						}else{
							ivHead.setImageBitmap(bitmap);
						}
					}else{
						ivHead.setImageResource(R.drawable.head_s);
					}
					name.setText(user.userName);
					desc.setText(user.lunarBtd + " 就要生日啦");
				}
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_main_activity,
				container, false);
		initData();
		initView(contentView);
		initListener();
		return contentView;
	}

	@Override
	public void onResume() {
		int theme = new PreferencesHelper(getActivity()).getInt(AppData.THEME);
		if (this.theme != theme) {
			this.theme = theme;
			updateTheme();
		}
		if(AppData.tempANotification != null){
			noteList.add(AppData.tempANotification);
			AppData.aNotificationList.add(AppData.tempANotification);
			handleNote();
			AppData.tempANotification = null;
		}
		if(AppData.tempActivity != null){
			String now = DateUtil.getLongDate();
			if(now.compareTo(AppData.tempActivity.date)<0){
				activitylist.add(0, AppData.tempActivity);
				activityAdapter.notifyDataSetChanged();
			}
			AppData.activityList.add(AppData.tempActivity);
			AppData.tempActivity = null;
		}
		super.onResume();
	}

	private void initData() {
		theme = new PreferencesHelper(getActivity()).getInt(AppData.THEME);
		activitylist = new ArrayList<Activity>();
		activityAdapter = new ActivityAdapter(activitylist, getActivity());
		noteList = new ArrayList<ANotification>();
		if (AppData.aNotificationList.size() == 0) {
			getNoteList();
		} else {
			noteList.addAll(AppData.aNotificationList);
		}
		if (AppData.activityList.size() == 0) {
			getActivityList();
		} else {
			handleList(AppData.activityList);
		}
	}

	private void initView(View contentView) {
		note = (TextView) contentView.findViewById(R.id.textView_note);
		name = (TextView) contentView.findViewById(R.id.textView_name);
		desc = (TextView) contentView.findViewById(R.id.textView_desc);
		note.setPadding(DisplayUtil.dip2px(30), DisplayUtil.dip2px(30),
				DisplayUtil.dip2px(30), DisplayUtil.dip2px(30));
		ivAdd = (ImageView) contentView.findViewById(R.id.imageView_add);
		ivNote = (ImageView) contentView.findViewById(R.id.imageView_note);
		ivEdit = (ImageView) contentView.findViewById(R.id.imageView_edit);
		ivHead = (ImageView) contentView.findViewById(R.id.imageView_head);
		
		ivRefresh = (ImageView) contentView
				.findViewById(R.id.imageView_refresh);
		ivBirthdayRemind = (ImageView) contentView
				.findViewById(R.id.imageView_birthday_remind);
		ivActivity = (ImageView) contentView
				.findViewById(R.id.imageView_activity);
		top = (RelativeLayout) contentView.findViewById(R.id.relativeLayout1);
		acticitysListView = (MeasureListView) contentView
				.findViewById(R.id.listView_acticitys);
		acticitysListView.setAdapter(activityAdapter);
		TextBold.setTextBold(note);
		handleNote();
		updateTheme();
	}

	private void updateTheme() {
		try {
			switch (theme) {
			case AppData.THEME_MALE:
				top.setBackgroundResource(R.color.green);
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme,
						"bg_title"));
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme,
						"bg_title"));
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(R.color.orang);
				break;
			}
			ivAdd.setImageResource(AppData.getThemeImgResId(theme, "add"));
			ivNote.setImageResource(AppData.getThemeImgResId(theme,
					"notice_off"));
			ivBirthdayRemind.setImageResource(AppData.getThemeImgResId(theme,
					"birthday_reminder_off"));
			ivActivity.setImageResource(AppData.getThemeImgResId(theme,
					"activity_list_off"));
			ivRefresh.setImageResource(AppData.getThemeImgResId(theme,
					"activity_reflash"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initListener() {
		ivRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activitylist.clear();
				noteList.clear();
				AppData.activityList.clear();
				AppData.aNotificationList.clear();
				getNoteList();
				getActivityList();
			}
		});
		ivAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreatActivityDialog creatActivityDialog = new CreatActivityDialog(
						getActivity());
				creatActivityDialog
						.setCreatActivityListener(new CreatActivityListener() {
							@Override
							public void getActivity(Activity activity) {
								activitylist.add(0, activity);
								AppData.activityList.add(activity);
								handler.sendEmptyMessage(3);
							}
						});
				creatActivityDialog.show();
			}
		});
		ivNote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skip(false, getActivity(), ActivityNotesActivity.class);
			}
		});
		ivEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppData.tempANotification = null;
				NoteDialog noticeDialog = new NoteDialog(getActivity());
				noticeDialog.setCreateNoteListener(new CreateNoteListener() {
					@Override
					public void getNote(ANotification aNotification) {
						noteList.add(aNotification);
						AppData.aNotificationList.add(aNotification);
						handler.sendEmptyMessage(1);
					}
				});
				noticeDialog.show();
			}
		});
		ivBirthdayRemind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skip(false, getActivity(), BirthdayRemindActivity.class);
			}
		});
		ivActivity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skip(false, getActivity(), ActivityListActivity.class);
			}
		});
		ivHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skipToBirthdayRemindItemActivity(getActivity(), new Gson().fromJson(birthdayRemind.creator+"", User.class),birthdayRemind._id);
			}
		});
		acticitysListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				UISkip.skipToActivityInfoActivity(getActivity(),
						activitylist.get(arg2)._id);
			}
		});
	}

	private void getNoteList() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<ANotification> list = new NoteManager()
							.getANotifications();
					noteList.addAll(list);
					AppData.aNotificationList.addAll(list);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					handler.sendEmptyMessage(2);
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void getActivityList() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<Activity> list = new ActivityManager().getActivities();
					System.out.println("Activity size-" + list.size());
					AppData.activityList.addAll(list);
					handleList(list);
					if(birthdayRemind != null){
						handler.sendEmptyMessage(3);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					handler.sendEmptyMessage(4);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void handleList(List<Activity> list){
		String now = DateUtil.getLongDate();
		for (Activity activity : list) {
			if (activity.type.equals("B")) {
				birthdayRemind = activity;
			} else if(now.compareTo(activity.date)<0){
				activitylist.add(0, activity);
			}
		}
	}
	private void handleNote(){
		if (noteList.size() > 0) {
			note.setText(noteList.get(noteList.size() - 1).content);
		} else {
			note.setText("no note");
		}
	}
}
