package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;

import com.guotion.sicilia.bean.CloudInfo;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.ui.view.CloudCheckItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CloudCheckFileAdapter extends BaseAdapter{
	private ArrayList<CloudEvent> list ;
	private Context context = null;
	
	public CloudCheckFileAdapter(ArrayList<CloudEvent> list, Context context) {
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
		CloudCheckItemView view;
		if(convertView == null){
			view = new CloudCheckItemView(context);
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (CloudCheckItemView) convertView.getTag();
		}
		view.setCloud(list.get(position));
		return convertView;
	}
}
