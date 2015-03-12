package com.guotion.sicilia.ui;

import java.util.ArrayList;
import java.util.List;

import com.guotion.sicilia.R;
import com.guotion.sicilia.application.SiciliaApplication;
import com.guotion.sicilia.bean.net.Tag;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.TagManager;
import com.guotion.sicilia.ui.adapter.TagAdapter;
import com.guotion.sicilia.ui.dialog.AddTagDialog;
import com.guotion.sicilia.ui.dialog.WarningDialog;
import com.guotion.sicilia.util.PreferencesHelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TagActivity extends Activity{
	private LinearLayout llBack;
	private TextView back;
	private ImageView add;
	private TextView family;
	private TextView cloud;
	private ListView tagListView;
	private TagAdapter tagAdapter;
	private LinearLayout choose;
	RelativeLayout top;
	ImageView ivBack;
	
	private String familyTagId;
	private String cloudTagId;
	private ArrayList<String> familyTagList;
	private ArrayList<String> cloudTagList;
	
	private int familyImgResId;
	private int cloudImgResId;
	
	private TagManager tagManager;
	private int tag = 0;//0-family ; 1-cloud
	
	private SiciliaApplication siciliaApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);
		siciliaApplication = (SiciliaApplication) getApplication();
		siciliaApplication.addActivity(TagActivity.this);
		initData();
		initView();
		initListener();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		siciliaApplication.removeActivity(TagActivity.this);
		AppData.tagList.clear();
		super.onDestroy();
	}
	private void initData() {
		tagManager = new TagManager();
		familyTagList = new ArrayList<String>();
//		for(int i=0;i<10;i++)
//			familyTagList.add("");
		cloudTagList = new ArrayList<String>();
//		for(int i=0;i<10;i++)
//			cloudTagList.add("");
		tagAdapter = new TagAdapter(familyTagList, TagActivity.this);
		if(AppData.tagList.size() == 0){
			getTags();
		}else{
			handleList(AppData.tagList);
		}
	}
	private void initView() {
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		back = (TextView) findViewById(R.id.textView_back);
		add = (ImageView) findViewById(R.id.imageView_add);
		family = (TextView) findViewById(R.id.button_family);
		cloud = (TextView) findViewById(R.id.button_cloud);
		tagListView = (ListView) findViewById(R.id.listView_tag);
		tagListView.setAdapter(tagAdapter);
		choose = (LinearLayout) findViewById(R.id.linearLayout_choose);
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(TagActivity.this).getInt(AppData.THEME);
		try{
			familyImgResId = AppData.getThemeImgResId(theme, "family");
			cloudImgResId = AppData.getThemeImgResId(theme, "cloud");
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
			choose.setBackgroundResource(familyImgResId);
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
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String title;
				final String type;
				final String tags;
				if(tag == 0){
					title = "为家庭添加标签";
					type = "F";
					tags = getTags(familyTagList);
				}else{
					title = "为云添加标签";
					type = "C";
					tags = getTags(cloudTagList);
				}
				AddTagDialog addTagDialog = new AddTagDialog(TagActivity.this, title) {
					
					@Override
					public void clickSure(final String name) {   //参数name是dialog中输入框中输入的值
						if(name == null || name.equals("")){
							return ;
						}
						new Thread(new Runnable() {
							@Override
							public void run() {
								String data;
								try {
									if(tags.equals("")){
										data = name;
									}else{
										data = tags+"|"+name;
									}
									Tag tag = tagManager.createTag(type, data);
									List<Tag> list = new ArrayList<Tag>();
									list.add(tag);
									handleList(list);
									AppData.tagList.addAll(list);
									handler.sendEmptyMessage(1);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									handler.sendEmptyMessage(2);
									e.printStackTrace();
								}
							}
						}).start();
						
					}
					
					@Override
					public void clickCancel() {
					}
				};
				addTagDialog.show();
			}
		});
		family.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tag = 0;
				tagAdapter.setList(familyTagList);
				choose.setBackgroundResource(familyImgResId);
			}
		});
		cloud.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tag = 1;
				tagAdapter.setList(cloudTagList);
				choose.setBackgroundResource(cloudImgResId);
			}
		});
		tagListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				String warning = "您确实要删除该标签吗？";
				String cancel = "取消";
				String sure = "删除";
				WarningDialog warningDialog = new WarningDialog(TagActivity.this, warning, cancel, sure) {
					@Override
					public void clickCancel() {
					}
					@Override
					public void clickSure() {
						Toast.makeText(TagActivity.this, "正在删除，请稍后", Toast.LENGTH_SHORT).show();
						new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									updateTag(arg2);
									AppData.tagList.clear();
									handler.sendEmptyMessage(1);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									handler.sendEmptyMessage(3);
									e.printStackTrace();
								}
							}
						}).start();
						
					}
				};
				warningDialog.show();
				return false;
			}
		});
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				tagAdapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(TagActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(TagActivity.this, "删除标签失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	private void getTags(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<Tag> list = tagManager.getTags("F");
					AppData.tagList.addAll(list);
					list = tagManager.getTags("C");
					AppData.tagList.addAll(list);
					handleList(AppData.tagList);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void handleList(List<Tag> list){
		for (Tag tag : list) {
			if(tag.type.equals("F")){
				familyTagList.clear();
				familyTagId = tag._id;
				familyTagList.addAll(tag.tags);
			}else if(tag.type.equals("C")){
				cloudTagList.clear();
				cloudTagId = tag._id;
				cloudTagList.addAll(tag.tags);
			}
		}
	}
	private void updateTag(int position) throws Exception{
		if(tag == 0){//System.out.println("delete tag = "+familyTagList.get(position));
			familyTagList.remove(position);
			String tags = getTags(familyTagList);
			tagManager.updateTag(familyTagId, tags);
		}else{//System.out.println("delete tag = "+cloudTagList.get(position));
			cloudTagList.remove(position);
			String tags = getTags(cloudTagList);
			tagManager.updateTag(cloudTagId, tags);
		}
	}
	private String getTags(List<String> list){
		if(list.size() == 0) return "";
		StringBuilder sb = new StringBuilder();
		for(String tag : list){
			sb.append(tag+"|");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}
