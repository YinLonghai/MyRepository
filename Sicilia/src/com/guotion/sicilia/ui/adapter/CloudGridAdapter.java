package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.sicilia.bean.CloudItemInfo;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.ui.view.CloudGridItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CloudGridAdapter extends BaseAdapter{
	private ArrayList<CloudEvent> list ;
	private Context context = null;
	
	public CloudGridAdapter(ArrayList<CloudEvent> list, Context context) {
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
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CloudGridItemView view;
		if (convertView == null) {
			view = new CloudGridItemView(context);
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (CloudGridItemView) convertView.getTag();
		}
		view.setData(list.get(position));
		return convertView;
	}

}
