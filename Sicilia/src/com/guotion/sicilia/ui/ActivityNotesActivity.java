package com.guotion.sicilia.ui;

import java.util.ArrayList;
import java.util.List;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.ActivityInfo;
import com.guotion.sicilia.bean.net.ANotification;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.NoteManager;
import com.guotion.sicilia.ui.adapter.ActivityNoteAdapter;
import com.guotion.sicilia.ui.dialog.NoteDialog;
import com.guotion.sicilia.ui.dialog.NoteDialog.CreateNoteListener;
import com.guotion.sicilia.ui.view.PullDownView;
import com.guotion.sicilia.ui.view.PullDownView.OnPullDownListener;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityNotesActivity extends Activity{
	private PullDownView pullDownView;
	private ListView activityNotesListView;
	private ActivityNoteAdapter activityNoteAdapter;
	private TextView back;
	private ImageView add;
	RelativeLayout top;
	ImageView ivBack;
	
	private ArrayList<ANotification> list;
	private OnPullDownListener pullDownListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_note);
		initData();
		initView();
		initListener();
	}
	private void initData() {
		list = new ArrayList<ANotification>();
		activityNoteAdapter = new ActivityNoteAdapter(list, ActivityNotesActivity.this);
		if(AppData.aNotificationList.size() == 0){
			//getNoteList();
		}else{
			list.addAll(AppData.aNotificationList);
		}
		
	}
	private void initView() {
		back = (TextView) findViewById(R.id.textView_back);
		add = (ImageView) findViewById(R.id.imageView_add);
		pullDownView = (PullDownView) findViewById(R.id.listView_activity_note);
		pullDownView.setTvFreshIsVisible(true);
		//显示并且可以使用头部刷新
		pullDownView.setShowHeader();
		activityNotesListView = pullDownView.getListView();
		activityNotesListView.setAdapter(activityNoteAdapter);
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(ActivityNotesActivity.this).getInt(AppData.THEME);
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				back.setTextColor(getResources().getColor(AppData.getThemeColor(theme)));
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				back.setTextColor(getResources().getColor(AppData.getThemeColor(theme)));
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getResources().getColor(R.color.white));
				break;
			}
			add.setImageResource(AppData.getThemeImgResId(theme, "add"));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppData.tempANotification = null;
				NoteDialog noteDialog = new NoteDialog(ActivityNotesActivity.this);
				noteDialog.setCreateNoteListener(new CreateNoteListener() {
					@Override
					public void getNote(ANotification aNotification) {
						list.add(0, aNotification);
						AppData.tempANotification = aNotification;
						handler.sendEmptyMessage(1);
					}
				});
				noteDialog.show();
			}
		});
		activityNotesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AppData.tempANotification = list.get(arg2-1);
				NoteDialog noteDialog = new NoteDialog(ActivityNotesActivity.this);
				noteDialog.show();
			}
		});
		
		pullDownListener = new ActivityNotesPullDownListener();
		pullDownView.setOnPullDownListener(pullDownListener);
	}
	
	private final class ActivityNotesPullDownListener implements OnPullDownListener{

		@Override
		public void onRefresh() {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
					/** 关闭 刷新完毕 ***/
					pullDownView.RefreshComplete();//这个事线程安全的 可看源代码
				}
			}).start();
		}
		
	}//end of ActivityNotesPullDownListener
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				activityNoteAdapter.notifyDataSetChanged();
				break;
			
			}
		}
	};
	private void getNoteList(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("获取Note...");
					List<ANotification> noteList = new NoteManager().getANotifications();
					list.addAll(noteList);
					AppData.aNotificationList.addAll(noteList);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					handler.sendEmptyMessage(2);
					e.printStackTrace();
			}
			}
		}).start();
	}	
}
