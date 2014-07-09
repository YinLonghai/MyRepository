package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.sicilia.bean.ActivityInfo;
import com.guotion.sicilia.bean.net.Activity;
import com.guotion.sicilia.ui.view.ActivityItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ActivityAdapter extends BaseAdapter{
	private ArrayList<Activity> list = new ArrayList<Activity>();
	private Context context = null;
	
	public ActivityAdapter(ArrayList<Activity> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ActivityItemView view;
		if (convertView == null) {
			view = new ActivityItemView(context);
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (ActivityItemView) convertView.getTag();
		}
		view.setData(list.get(position));
		return convertView;
	}

}
