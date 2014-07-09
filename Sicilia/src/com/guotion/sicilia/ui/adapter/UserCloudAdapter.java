package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.ui.view.UserCloudItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class UserCloudAdapter extends BaseAdapter{

	private ArrayList<CloudEvent> list;
	private Context context = null;
	
	public UserCloudAdapter(ArrayList<CloudEvent> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserCloudItemView view;
		if (convertView == null) {
			view = new UserCloudItemView(context);
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (UserCloudItemView) convertView.getTag();
		}
		view.setCloudInfo(list.get(position));
		return convertView;
	}

}
