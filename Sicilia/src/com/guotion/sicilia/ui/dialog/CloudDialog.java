package com.guotion.sicilia.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.ChatItem;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.bean.net.CloudItem;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.CloudManager;
import com.guotion.sicilia.ui.adapter.CloudGridViewAdapter;
import com.guotion.sicilia.ui.adapter.CommentAdapter;
import com.guotion.sicilia.ui.dialog.InputSingleDialog;
import com.guotion.sicilia.ui.listener.OnDeleteListener;
import com.guotion.sicilia.ui.listener.OnUpdateListener;
import com.guotion.sicilia.util.LogUtil;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CloudDialog extends Dialog implements View.OnClickListener{
	
	private LinearLayout llBack;
	private TextView back;
	private ImageView avatar;
	private TextView name;
	private TextView date;
	private TextView desc;
	private ImageView delete;
	private ListView commentListView;
	private CommentAdapter commentAdapter;
	private TextView tvCloudName;
	private RelativeLayout top;
	private GridView gvPicture;
	private CloudGridViewAdapter cloudGridAdapter;
	private List<String> picture;
	private ImageView ivBack;
	private TextView tvEdit;
	
	private CloudEvent cloudInfo;
	private User owner;
	private String cloudId;
	private List<ChatItem> commentList;
	
	private CloudManager cloudManager;
	private ImageView ivComment;
	private InputSingleDialog inputSingleDialog = null;
	private Resources resources = null;
	
	private OnUpdateListener<CloudEvent> updateListener = null;
	private OnDeleteListener<CloudEvent> deleteListener = null;
	private final int LOAD_COMMENT_SUCCESS = 1;
	private final int LOAD_CLOUD_SUCCESS = 2;
	private final int DELETE_CLOUD_SUCCESS = 3;
	private final int DELETE_CLOUD_FAILED = 4;
	private UpdateCloudDialog updateCloudDialog  = null;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case LOAD_COMMENT_SUCCESS:
				commentAdapter.notifyDataSetChanged();
				break;
			case LOAD_CLOUD_SUCCESS:
				Gson gson = new Gson();
				User user = gson.fromJson(cloudInfo.owner+"", User.class);
				if(user._id.equals(AppData.getUser(context)._id)){
					tvEdit.setVisibility(View.VISIBLE);
				}
				//List<CloudItem> cloudItemList = gson.fromJson(cloudInfo.files+"",new TypeToken<List<CloudItem>>(){}.getType());
				String imgUrl = user.headPhoto;
				if(imgUrl != null && !imgUrl.equals("")){
					Bitmap bitmap = LocalImageCache.get().loadImageBitmap(CacheUtil.avatarCachePath+imgUrl.substring(imgUrl.lastIndexOf("/")));
					if(bitmap == null){
						AppData.volleyUtil.loadImageByVolley(ChatServerConstant.URL.SERVER_HOST + user.headPhoto, avatar, R.drawable.head_s, R.drawable.head_s);
					}else{
						avatar.setImageBitmap(bitmap);
					}
				}else{
					avatar.setImageResource(R.drawable.head_s);
				}
				name.setText(user.userName);
				tvCloudName.setText(cloudInfo.name);
				date.setText(cloudInfo.date);
				desc.setText(cloudInfo.desc);
				break;
			case DELETE_CLOUD_SUCCESS:
				if (deleteListener != null) {
					deleteListener.onDelete(cloudInfo);
				}
				dismiss();
				break;
			case DELETE_CLOUD_FAILED:
				Toast.makeText(getContext(), "云文件删除失败！", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	private Context context =null;
	
	public CloudDialog(Context context, CloudEvent cloudEvent){
		super(context, R.style.dialog_full_screen);
		setContentView(R.layout.activity_cloud);
		this.cloudInfo = cloudEvent;
		this.context  = context;
		initData();
		initView();
		initListener();
	}

	private void initData() {
		resources = getContext().getResources();
		cloudManager = new CloudManager();		
		cloudId = cloudInfo._id;

	}
	private void initView() {
		tvEdit = (TextView)findViewById(R.id.textView_edit);
		tvEdit.setVisibility(View.GONE);
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		back = (TextView) findViewById(R.id.textView_back);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		avatar = (ImageView) findViewById(R.id.imageView_avatar);
		name = (TextView) findViewById(R.id.textView_name);
		tvCloudName = (TextView) findViewById(R.id.textView2);
		
		date = (TextView) findViewById(R.id.textView_date);
		
		desc = (TextView) findViewById(R.id.textView_desc);
		
		delete = (ImageView) findViewById(R.id.imageView_delete);
		commentListView = (ListView) findViewById(R.id.listView_comment);
		commentList = new ArrayList<ChatItem>();		
		commentAdapter = new CommentAdapter(commentList, context);
		commentListView.setAdapter(commentAdapter);
		initComments();
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		gvPicture = (GridView)findViewById(R.id.imageView_file);
		picture = new ArrayList<String>();
		Gson gson = new Gson();
		owner = gson.fromJson(cloudInfo.owner+"", User.class);
		LogUtil.i("cloud files" + cloudInfo.files + "");
		List<CloudItem> list = new Gson().fromJson(cloudInfo.files+"",new TypeToken<List<CloudItem>>(){}.getType());
		for(CloudItem cloudItem : list){
			picture.add(cloudItem.url);
		}
		name.setText(owner.userName);
		tvCloudName.setText(cloudInfo.name);
		date.setText(cloudInfo.date);
		desc.setText(cloudInfo.desc);
		cloudGridAdapter = new CloudGridViewAdapter(getContext(), picture);
		gvPicture.setAdapter(cloudGridAdapter);
		initCloud();
		Utility.setListViewHeightBasedOnChildren(commentListView);
		Utility.setGridViewHeightBasedOnChildren(gvPicture);
		updateTheme();
		ivComment = (ImageView)findViewById(R.id.imageView_comment);
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(resources.getColor(R.color.white));
				tvEdit.setTextColor(resources.getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				back.setTextColor(resources.getColor(AppData.getThemeColor(theme)));
				tvEdit.setTextColor(resources.getColor(AppData.getThemeColor(theme)));
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				back.setTextColor(resources.getColor(AppData.getThemeColor(theme)));
				tvEdit.setTextColor(resources.getColor(AppData.getThemeColor(theme)));
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(resources.getColor(R.color.white));
				tvEdit.setTextColor(resources.getColor(R.color.white));
				break;
			}
			delete.setBackgroundResource(AppData.getThemeImgResId(theme, "delete_cloud"));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		llBack.setOnClickListener(this);
		delete.setOnClickListener(this);
		ivComment.setOnClickListener(this);
		tvEdit.setOnClickListener(this);
	}
	
	
	
	private void commentCloud(final String comment){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ChatItem chatItem = cloudManager.commentCloudFile(cloudId, AppData.getUser(context)._id, comment, owner._id);
					commentList.add(chatItem);
					handler.sendEmptyMessage(LOAD_COMMENT_SUCCESS);
				} catch (Exception e) {
					// TODO Auto-generated catch block  DELETE_CLOUD_FAILED
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	private void deleteCloud(){
		Toast.makeText(getContext(), "云文件正在删除中，请稍等", Toast.LENGTH_SHORT).show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					cloudManager.deleteCloudFile(cloudId);
					handler.sendEmptyMessage(DELETE_CLOUD_SUCCESS);
				} catch (Exception e) {
					handler.sendEmptyMessage(DELETE_CLOUD_FAILED);
				}
			}
		}).start();
	}
	
	private void initCloud(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					cloudInfo = cloudManager.getOneCloudFile(cloudId);
					handler.sendEmptyMessage(LOAD_CLOUD_SUCCESS);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void initComments(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<ChatItem> list = cloudManager.getCloudFileComments(cloudId);
					commentList.addAll(list);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		if (v == llBack) {
			dismiss();
		} else if (v == delete) {
			deleteCloud();
		} else if (v == ivComment) {
			if (inputSingleDialog == null) {
				inputSingleDialog = new InputSingleDialog(getContext()) {
					@Override
					public void clickSure(String comment) {
						commentCloud(comment);
					}
				};					
			}
			inputSingleDialog.show();
		}else if (v == tvEdit) {
			updateCloudDialog = new UpdateCloudDialog((Activity)context, cloudInfo);
			updateCloudDialog.setOnUpdateListener(new OnUpdateListener<CloudEvent>() {
				
				@Override
				public void onUpdate(CloudEvent cloudEvent) {
					if (updateListener != null) {
						updateListener.onUpdate(cloudEvent);
					}
					cloudInfo = cloudEvent;
					tvCloudName.setText(cloudInfo.name);
					date.setText(cloudInfo.date);
					desc.setText(cloudInfo.desc);
					List<CloudItem> list = new Gson().fromJson(cloudInfo.files+"",new TypeToken<List<CloudItem>>(){}.getType());
					picture.clear();
					for(CloudItem cloudItem : list){
						picture.add(cloudItem.url);
					}
					cloudGridAdapter.notifyDataSetChanged();
				}
			});
			updateCloudDialog.show();
		}
	}


	public void setOnCloudUpdateListener(OnUpdateListener<CloudEvent> l) {
		this.updateListener = l;
	}

	public void setOnCloudDeleteListener(OnDeleteListener<CloudEvent> l) {
		this.deleteListener = l;
	}

	public void addUpdateFile(String filePath) {
		if (updateCloudDialog != null) {
			updateCloudDialog.addUpdateFile(filePath);
		}
	}
}
