package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.sicilia.bean.CloudItemInfo;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.ui.view.CloudListItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CloudListAdapter extends BaseAdapter{
	private ArrayList<CloudEvent> list ;
	private Context context = null;
	
	public CloudListAdapter(ArrayList<CloudEvent> list, Context context) {
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
		CloudListItemView view;
		if (convertView == null) {
			view = new CloudListItemView(context);
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (CloudListItemView) convertView.getTag();
		}
		view.setData(list.get(position));
		return convertView;
	}

}
