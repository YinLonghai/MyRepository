package com.guotion.sicilia.ui.dialog;

import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ANotification;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.NoteManager;
import com.guotion.sicilia.util.PreferencesHelper;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDialog extends Dialog{
	
	private EditText desc;
	private ImageView push;
	private ImageView pub;
	private TextView back;
	private TextView done;
	RelativeLayout top;
	ImageView ivBack;
	private LinearLayout llBack;
	private ANotification aNotification;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				dismiss();
				break;
			}
			super.handleMessage(msg);
		}		
	};
	
	public NoteDialog(Context context) {
		super(context,R.style.dialog_full_screen);
		setContentView(R.layout.activity_note);
		initData();
		initView();
		initListener();
	}
	
	private void initData() {
		aNotification = AppData.tempANotification;
		if(aNotification == null){
			aNotification = new ANotification();
		}
	}
	private void initView() {
		back = (TextView) findViewById(R.id.textView_back);
		done = (TextView) findViewById(R.id.textView_done);
		desc = (EditText) findViewById(R.id.editText_desc);
		desc.setText(aNotification.content);
		push = (ImageView) findViewById(R.id.imageView_push);
		pub = (ImageView) findViewById(R.id.imageView2);
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);		
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
		int color ;
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getContext().getResources().getColor(R.color.white));
				done.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				done.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				done.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getContext().getResources().getColor(R.color.white));
				done.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			}
			push.setBackgroundResource(AppData.getThemeImgResId(theme, "push_on"));
			pub.setBackgroundResource(AppData.getThemeImgResId(theme, "release_off"));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		llBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
//		done.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			}
//		});
		push.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String content = desc.getText().toString();
				if(content == null || content.equals("")){
					return ;
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							ANotification aNotification = new NoteManager().createANotification(content, AppData.getUser(getContext())._id, "1");
							if(createNoteListener != null){
								createNoteListener.getNote(aNotification);
							}
							handler.sendEmptyMessage(2);
						} catch (Exception e) {
							e.printStackTrace();
							Message message = new Message();
							message.what = 1;
							//message.obj = e.getMessage();
							handler.sendMessage(message);
						}
					}
				}).start();
			}
		});
		pub.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String content = desc.getText().toString();
				if(content == null || content.equals("")){
					return ;
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							ANotification aNotification = new NoteManager().createANotification(content, AppData.getUser(getContext())._id, "0");
							if(createNoteListener != null){
								createNoteListener.getNote(aNotification);
							}
							handler.sendEmptyMessage(2);
						} catch (Exception e) {
							e.printStackTrace();
							Message message = new Message();
							message.what = 1;
							//message.obj = e.getMessage();
							handler.sendMessage(message);
						}
					}
				}).start();
			}
		});
	}
	private CreateNoteListener createNoteListener;
	
	public void setCreateNoteListener(CreateNoteListener createNoteListener) {
		this.createNoteListener = createNoteListener;
	}

	public interface CreateNoteListener{
		public void getNote(ANotification aNotification);
	}
}
