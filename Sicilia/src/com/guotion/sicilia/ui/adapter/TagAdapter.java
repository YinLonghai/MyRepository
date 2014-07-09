package com.guotion.sicilia.ui.adapter;

import java.util.ArrayList;
import com.guotion.sicilia.ui.view.TagItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TagAdapter extends BaseAdapter{
	private ArrayList<String> list ;
	private Context context = null;
	
	public TagAdapter(ArrayList<String> list, Context context) {
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
		TagItemView view;
		if (convertView == null) {
			view = new TagItemView(context);
			convertView = view;
			convertView.setTag(view);
		}else{
			view = (TagItemView) convertView.getTag();
		}
		view.setData(list.get(position));
		return convertView;
	}

	public void setList(ArrayList<String> list){
		this.list = list;
		notifyDataSetInvalidated();
	}
}
