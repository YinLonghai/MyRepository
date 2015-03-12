package com.guotion.sicilia.ui.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.CloudManager;
import com.guotion.sicilia.ui.CloudCheckActivity;
import com.guotion.sicilia.ui.adapter.CloudGridAdapter;
import com.guotion.sicilia.ui.adapter.CloudListAdapter;
import com.guotion.sicilia.ui.dialog.CloudDialog;
import com.guotion.sicilia.ui.dialog.UploadCloudDialog;
import com.guotion.sicilia.ui.dialog.UploadCloudDialog.UploadCloudListener;
import com.guotion.sicilia.ui.listener.OnDeleteListener;
import com.guotion.sicilia.ui.listener.OnUpdateListener;
import com.guotion.sicilia.ui.view.CloudGridItemView;
import com.guotion.sicilia.ui.view.CloudListItemView;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.UISkip;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CloudFragment extends Fragment {
	private View contentView = null;
	private TextView list;
	private TextView thumb;
	private TextView line;
	private ImageView add;
	private TextView check;
	private LinearLayout choose;
	private ListView cloudListView;
	private GridView cloudGridView;
	private RelativeLayout top;
	
	private CloudListAdapter cloudListAdapter;
	private CloudGridAdapter cloudGridAdapter;
	
	private ArrayList<CloudEvent> cloudList;
	private UploadCloudDialog uploadCloudDialog = null;
	private int theme;
	private int listImgResId;
	private int thumbImgResId;
	private CloudDialog cloudDialog = null;
	
	private boolean getCloudData = false;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				cloudListAdapter.notifyDataSetChanged();
				cloudGridAdapter.notifyDataSetChanged();
				getCloudData = false;
				break;
			}
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		contentView = inflater.inflate(R.layout.fragment_main_cloud, container,false);
		initData();
		initView(contentView);
		initListener();
		return contentView;
	}
	
	@Override
	public void onResume() {
		int theme = new PreferencesHelper(getActivity()).getInt(AppData.THEME);
		if(this.theme != theme){
			this.theme = theme;
			updateTheme();
		}
		super.onResume();
	}

	private void initData() {
		theme = new PreferencesHelper(getActivity()).getInt(AppData.THEME);
		cloudList = new ArrayList<CloudEvent>();
		
		cloudListAdapter = new CloudListAdapter(cloudList, getActivity());
		cloudGridAdapter = new CloudGridAdapter(cloudList, getActivity());
		if(AppData.cloudEventList.size() == 0){
			getCloudList();
		}else{
			cloudList.addAll(AppData.cloudEventList);
		}
		
	}
	private void initView(View contentView) {
		list = (TextView) contentView.findViewById(R.id.button_cloud_list);
		thumb = (TextView) contentView.findViewById(R.id.button_cloud_thumb);
		line = (TextView) contentView.findViewById(R.id.textView_line);
		add = (ImageView) contentView.findViewById(R.id.imageView_add);
		check = (TextView) contentView.findViewById(R.id.TextView_check);
		choose = (LinearLayout) contentView.findViewById(R.id.linearLayout_cloud_choose);
		top = (RelativeLayout) contentView.findViewById(R.id.relativeLayout1);
		cloudListView = (ListView) contentView.findViewById(R.id.listView_cloud);
		cloudListView.setAdapter(cloudListAdapter);
		cloudListView.setVisibility(View.VISIBLE);
		cloudGridView = (GridView) contentView.findViewById(R.id.gridView_cloud);
		cloudGridView.setAdapter(cloudGridAdapter);
		cloudGridView.setVisibility(View.GONE);
		updateTheme();
	}
	private void updateTheme(){
		try{
			switch(theme){
			case AppData.THEME_MALE:
				check.setTextColor(getResources().getColor(R.color.white));
				top.setBackgroundResource(AppData.getThemeColor(theme));
				break;
			case AppData.THEME_RED:
				check.setTextColor(getResources().getColor(AppData.getThemeColor(theme)));
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				break;
			case AppData.THEME_BLUE:
				check.setTextColor(getResources().getColor(AppData.getThemeColor(theme)));
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				break;
			case AppData.THEME_FEMALE:
				check.setTextColor(getResources().getColor(R.color.white));
				top.setBackgroundResource(AppData.getThemeColor(theme));
				break;
			}
			add.setImageResource(AppData.getThemeImgResId(theme, "add"));
			listImgResId = AppData.getThemeImgResId(theme, "list");
			thumbImgResId = AppData.getThemeImgResId(theme, "thumbnail");
			choose.setBackgroundResource(listImgResId);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cloudListView.setVisibility(View.VISIBLE);
				line.setVisibility(View.VISIBLE);
				cloudGridView.setVisibility(View.GONE);
				choose.setBackgroundResource(listImgResId);
			}
		});
		thumb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cloudListView.setVisibility(View.GONE);
				line.setVisibility(View.GONE);
				cloudGridView.setVisibility(View.VISIBLE);
				choose.setBackgroundResource(thumbImgResId);
			}
		});
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(uploadCloudDialog == null){
					uploadCloudDialog = new UploadCloudDialog(getActivity());
				}					
				uploadCloudDialog.setUploadCloudListener(new UploadCloudListener() {
					@Override
					public void uploadCloud(CloudEvent cloudEvent) {
						cloudList.add(0, cloudEvent);
						AppData.cloudEventList.add(cloudEvent);
						handler.sendEmptyMessage(1);
					}
				});
				uploadCloudDialog.show();
			}
		});
		check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UISkip.skip(false, getActivity(), CloudCheckActivity.class);
			}
		});
		cloudListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showCloudDialog(arg2);
//				AppData.tempCloudEvent = cloudList.get(arg2);
//				UISkip.skip(false, getActivity(), CloudActivity.class);				
				//UISkip.skipToCloudActivity(getActivity(), cloudList.get(arg2)._id);
			}
		});
		cloudGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showCloudDialog(arg2);
//				UISkip.skip(false, getActivity(), CloudActivity.class);
				//UISkip.skipToCloudActivity(getActivity(), cloudList.get(arg2)._id);
			}
		});
		cloudListView.setOnScrollListener(new OnScrollListener() {
			boolean isFirst = true;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for(int i=0;i<view.getChildCount();i++){
						CloudListItemView cloudListItemView = (CloudListItemView) view.getChildAt(i);
						cloudListItemView.initNetImg();
					}
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(!isFirst) return;
				if(view.getChildCount()>0){
					for(int i=0;i<view.getChildCount();i++){
						CloudListItemView cloudListItemView = (CloudListItemView) view.getChildAt(i);
						cloudListItemView.initNetImg();
					}
					isFirst = false;
				}
			}
		});
		cloudGridView.setOnScrollListener(new OnScrollListener() {
			boolean isFirst = true;
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					for(int i=0;i<view.getChildCount();i++){
						CloudGridItemView cloudGridItemView = (CloudGridItemView) view.getChildAt(i);
						cloudGridItemView.initNetImg();
					}
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(!isFirst) return;
				if(view.getChildCount()>0){
					for(int i=0;i<view.getChildCount();i++){
						CloudGridItemView cloudGridItemView = (CloudGridItemView) view.getChildAt(i);
						cloudGridItemView.initNetImg();
					}
					isFirst = false;
				}
			}
		});
	}//end of initListener
	
	private void showCloudDialog(final int arg2){
		cloudDialog = new CloudDialog(getActivity(), cloudList.get(arg2));		
		cloudDialog.setOnCloudUpdateListener(new OnUpdateListener<CloudEvent>(){

			@Override
			public void onUpdate(CloudEvent t) {
				cloudList.remove(arg2);
				cloudList.add(arg2, t);
				cloudGridAdapter.notifyDataSetChanged();
				cloudListAdapter.notifyDataSetChanged();
			}
			
		});
		cloudDialog.setOnCloudDeleteListener(new OnDeleteListener<CloudEvent>(){

			@Override
			public void onDelete(CloudEvent t) {
				cloudList.remove(arg2);
				cloudGridAdapter.notifyDataSetChanged();
				cloudListAdapter.notifyDataSetChanged();
			}
			
		});
		cloudDialog.show();
	}
	private void getCloudList(){
		if(getCloudData) return ;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					getCloudData = true;
					List<CloudEvent> list = new CloudManager().getCloudFiles();
					list = handleList(list);
					cloudList.clear();
					cloudList.addAll(list);
					AppData.cloudEventList.clear();
					AppData.cloudEventList.addAll(list);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					handler.sendEmptyMessage(2);
					e.printStackTrace();
				}
			}
		}).start();
	}
	private List<CloudEvent> handleList(List<CloudEvent> list){
		List<CloudEvent> cloudEventList = new LinkedList<CloudEvent>();
		String userId = AppData.getUser(getActivity())._id;
		Gson gson = new Gson();
		for(CloudEvent cloudEvent : list){
			if(cloudEvent.isPrivate.equals("0")){
				cloudEventList.add(cloudEvent);
			}else{
				User owner = gson.fromJson(cloudEvent.owner+"", User.class);
				if(owner._id.equals(userId)){
					cloudEventList.add(cloudEvent);
				}
			}
		}
		return cloudEventList;
	}
	
	public void addFile(String filePath){
		if (uploadCloudDialog != null) {
			uploadCloudDialog.addFile(filePath);
		}
	}

	public void addUpdateFile(String filePath) {
		if (cloudDialog != null) {
			cloudDialog.addUpdateFile(filePath);
		}
	}
}
